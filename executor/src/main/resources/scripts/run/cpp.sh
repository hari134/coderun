#!/bin/bash

# Check if all required arguments are provided
if [ $# -lt 4 ] || [ $# -gt 6 ]; then
    echo "{\"error\": \"Usage: $0 <path_to_cpp_file> <time_limit> <memory_limit> <box_id> [path_to_stdin_file] [expected_output_file]\", \"output\": \"\", \"error\": \"\"}" >&2
    exit 1
fi

# Assign arguments to variables
path_to_cpp_file="$1"
time_limit="$2"
memory_limit="$3"
box_id="$4"
stdin_path="${5:-}"  # Optional stdin path
expected_output_file="${6:-}"  # Optional expected output file

# Compile the code
if ! g++ "$path_to_cpp_file" -o "/tmp/$box_id.out"; then
    echo "{\"error\": \"Compilation error\", \"output\": \"\", \"metadata\": {}}" >&2
    isolate --cleanup -b "$box_id"  # Cleanup before exit
    exit 1
fi

# Initialize isolate sandbox
if ! isolate --init -b "$box_id"; then
    echo "{\"error\": \"Failed to initialize isolate sandbox\", \"output\": \"\", \"metadata\": {}}" >&2
    isolate --cleanup -b "$box_id"  # Cleanup before exit
    exit 1
fi

# Copy the executable to the sandbox directory
if ! cp "/tmp/$box_id.out" "/var/local/lib/isolate/$box_id/box/$box_id"; then
    echo "{\"error\": \"Failed to copy executable to sandbox\", \"output\": \"\", \"metadata\": {}}" >&2
    isolate --cleanup -b "$box_id"  # Cleanup before exit
    exit 1
fi

# Set up the stdin redirection if a stdin path is provided
stdin_option=""
if [ -n "$stdin_path" ]; then
    stdin_option="-i $stdin_path"
fi

# Execute the program within the sandbox
if ! isolate -b $box_id -t $time_limit -m $memory_limit -M "/var/local/lib/isolate/$box_id/box/meta.txt" -o "output.txt" -r "error.txt" $stdin_option --run -- "/box/$box_id"; then
    echo "{\"error\": \"Execution failed\", \"output\": \"\", \"metadata\": {}}" >&2
    isolate --cleanup -b "$box_id"  # Cleanup before exit
    exit 1
fi

# Read output and error files, ensure files are read safely
output=$(cat "/var/local/lib/isolate/$box_id/box/output.txt" 2>/dev/null)
error=$(cat "/var/local/lib/isolate/$box_id/box/error.txt" 2>/dev/null)
metadata=$(cat "/var/local/lib/isolate/$box_id/box/meta.txt" 2>/dev/null)

# Parse metadata for output
isTLE=$(grep -q 'status:TO' <<< "$metadata" && echo "true" || echo "false")
isMLE=$(grep -q 'status:RE' <<< "$metadata" && grep -q 'exitsig:9' <<< "$metadata" && echo "true" || echo "false")
cpu_time=$(grep 'time:' <<< "$metadata" | cut -d':' -f2)
memory_used=$(grep 'max-rss:' <<< "$metadata" | cut -d':' -f2)

# Compare output to expected output if provided
output_match="not applicable"
if [ -n "$expected_output_file" ]; then
    expected_output=$(cat "$expected_output_file" 2>/dev/null)
    if [ "$output" == "$expected_output" ]; then
        output_match="true"
    else
        output_match="false"
    fi
fi

# Cleanup the sandbox before exiting successfully
if ! isolate --cleanup -b "$box_id"; then
    echo "{\"error\": \"Failed to cleanup isolate sandbox\", \"output\": \"$output\", \"error\": \"$error\", \"metadata\": {\"isTLE\": $isTLE, \"isMLE\": $isMLE, \"cpu_time\": \"$cpu_time\", \"memory_used\": \"$memory_used\", \"output_match\": \"$output_match\"}}" >&2
    exit 1
fi

echo "{\"output\": \"$output\", \"error\": \"$error\", \"metadata\": {\"isTLE\": $isTLE, \"isMLE\": $isMLE, \"cpu_time\": \"$cpu_time\", \"memory_used\": \"$memory_used\", \"output_match\": \"$output_match\"}}"


# docker exec -it judger /scripts/judge/cpp.sh "/scripts/test.cpp" 1 128000 2