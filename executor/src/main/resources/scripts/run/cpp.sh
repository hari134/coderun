#!/bin/bash

# Assign arguments to variables
path_to_cpp_file="$1"
time_limit="$2"
memory_limit="$3"
box_id="$4"
wall_time="$5"
stdin_path="${6:-}"  # Optional stdin path
expected_output_file="${7:-}"  # Optional expected output file

# Compile the code
if ! g++ "$path_to_cpp_file" -o "/tmp/$box_id.out"; then
    echo "{\"status\": \"Compilation error\", \"error\": \"\", \"output\": \"\", \"isTLE\": false, \"isMLE\": false, \"cpu_time\": \"\", \"memory_used\": \"\", \"output_match\": \"not applicable\", \"wall_time\": \"\", \"exit_code\": \"\"}"
    isolate --cleanup -b "$box_id"  # Cleanup before exit
    exit 1
fi

# Initialize isolate sandbox
if ! isolate --cg --init -b "$box_id" >/dev/null; then
    echo "{\"status\": \"Failed to initialize isolate sandbox\", \"error\": \"\", \"output\": \"\", \"isTLE\": false, \"isMLE\": false, \"cpu_time\": \"\", \"memory_used\": \"\", \"output_match\": \"not applicable\", \"wall_time\": \"\", \"exit_code\": \"\"}"
    isolate --cleanup -b "$box_id"  # Cleanup before exit
    exit 1
fi

# Copy the executable to the sandbox directory
if ! cp "/tmp/$box_id.out" "/var/local/lib/isolate/$box_id/box/$box_id"; then
    echo "{\"status\": \"Failed to copy executable to sandbox\", \"error\": \"\", \"output\": \"\", \"isTLE\": false, \"isMLE\": false, \"cpu_time\": \"\", \"memory_used\": \"\", \"output_match\": \"not applicable\", \"wall_time\": \"\", \"exit_code\": \"\"}"
    isolate --cg --cleanup -b "$box_id"  # Cleanup before exit
    exit 1
fi

# Set up the stdin redirection if a stdin path is provided
stdin_option=""
if [ -n "$stdin_path" ]; then
    stdin_option="-i $stdin_path"
fi

# Execute the program within the sandbox
if ! isolate --cg -b $box_id -t $time_limit --cg-mem $memory_limit -w $wall_time -M "/var/local/lib/isolate/$box_id/box/meta.txt" -o "output.txt" --stderr "error.txt" $stdin_option --run -- "/box/$box_id"; then
    metadata=$(cat "/var/local/lib/isolate/$box_id/box/meta.txt")
    error_data=$(cat "/var/local/lib/isolate/$box_id/box/error.txt" 2>/dev/null || echo "")
    error_data="${error_data:-""}"  # Ensure error is an empty string if null
    output_data=$(cat "/var/local/lib/isolate/$box_id/box/output.txt" 2>/dev/null || echo "")
    output_data="${output:-""}"  # Ensure error is an empty string if null

    isTLE=$(grep -q 'status:TO' <<< "$metadata" && echo "true" || echo "false")
    memory_used=$(grep 'max-rss:' <<< "$metadata" | cut -d':' -f2)
    isMLE=$(grep -q 'status:RE' <<< "$metadata" && grep -q 'exitsig:9' <<< "$metadata" && echo "true" || echo "false")

    # Check if memory used exceeds limit
    if [ "$memory_used" -gt "$memory_limit" ]; then
        isMLE="true"
    fi

    cpu_time=$(grep 'time:' <<< "$metadata" | cut -d':' -f2)
    wall_time=$(grep 'time-wall:' <<< "$metadata" | cut -d':' -f2)
    exit_code=$(grep 'exitcode:' <<< "$metadata" | cut -d':' -f2)

    if [ "$isTLE" == "true" ]; then
        status="Time Limit Exceeded"
    elif [ "$isMLE" == "true" ]; then
        status="Memory Limit Exceeded"
    else
        status="Runtime Error"
    fi

    # Escape newlines and double quotes for JSON
    metadata_json=$(echo "$metadata" | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/"/\\"/g')
    error_data_json=$(echo "$error_data" | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/"/\\"/g')
    output_data_json=$(echo "$output_data" | sed ':a;N;$!ba;s/\n/\\n/g' | sed 's/"/\\"/g')

    echo "{\"status\": \"$status\", \"error\": \"$error_data_json\", \"output\": \"$output_data_json\", \"isTLE\": $isTLE, \"isMLE\": $isMLE, \"cpu_time\": \"$cpu_time\", \"memory_used\": \"$memory_used\", \"wall_time\": \"$wall_time\", \"exit_code\": \"$exit_code\", \"metadata\": \"$metadata_json\"}"
    isolate --cg --cleanup -b "$box_id"  # Cleanup before exit
    exit 1
fi

# Read output and error files, ensure files are read safely
output=$(cat "/var/local/lib/isolate/$box_id/box/output.txt" 2>/dev/null || echo "")
error=$(cat "/var/local/lib/isolate/$box_id/box/error.txt" 2>/dev/null || echo "")
error="${error:-""}"  # Ensure error is an empty string if null
metadata=$(cat "/var/local/lib/isolate/$box_id/box/meta.txt" 2>/dev/null || echo "")

# Parse metadata for output
isTLE=$(grep -q 'status:TO' <<< "$metadata" && echo "true" || echo "false")
memory_used=$(grep 'max-rss:' <<< "$metadata" | cut -d':' -f2)
isMLE=$(grep -q 'status:RE' <<< "$metadata" && grep -q 'exitsig:9' <<< "$metadata" && echo "true" || echo "false")

# Check if memory used exceeds limit
if [ "$memory_used" -gt "$memory_limit" ]; then
    isMLE="true"
fi

cpu_time=$(grep 'time:' <<< "$metadata" | cut -d':' -f2)
wall_time=$(grep 'time-wall:' <<< "$metadata" | cut -d':' -f2)
exit_code=$(grep 'exitcode:' <<< "$metadata" | cut -d':' -f2)

# Set status based on conditions
status="Successfully Executed"
if [ "$isTLE" == "true" ]; then
    status="Time Limit Exceeded"
elif [ "$isMLE" == "true" ]; then
    status="Memory Limit Exceeded"
fi

# Compare output to expected output if provided
output_match="not applicable"
if [ -n "$expected_output_file" ]; then
    expected_output=$(cat "$expected_output_file" 2>/dev/null || echo "")
    if [ "$output" == "$expected_output" ]; then
        output_match="true"
    else
        output_match="false"
    fi
fi

# Cleanup the sandbox before exiting successfully
if ! isolate --cg --cleanup -b "$box_id"; then
    echo "{\"status\": \"Failed to cleanup isolate sandbox\", \"error\": \"\", \"output\": \"$output\", \"isTLE\": $isTLE, \"isMLE\": $isMLE, \"cpu_time\": \"$cpu_time\", \"memory_used\": \"$memory_used\", \"wall_time\": \"$wall_time\", \"exit_code\": \"$exit_code\", \"output_match\": \"$output_match\"}"
    exit 1
fi

echo "{\"status\": \"$status\", \"error\": \"$error\", \"output\": \"$output\", \"isTLE\": $isTLE, \"isMLE\": $isMLE, \"cpu_time\": \"$cpu_time\", \"memory_used\": \"$memory_used\", \"wall_time\": \"$wall_time\", \"exit_code\": \"$exit_code\", \"output_match\": \"$output_match\"}"
