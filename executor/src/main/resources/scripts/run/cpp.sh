#!/bin/bash

# Check if the required box_id and filepath are provided
if [ $# -ne 2 ]; then
    echo "{\"error\": \"Usage: $0 <box_id> <file_path>\"}" >&2
    exit 1
fi

# Assign box_id and file_path to variables
box_id="$1"
file_path="$2"
time_limit=2
memory_limit=256000

# Check if the file exists
if [ ! -f "$file_path" ]; then
    echo "{\"error\": \"File not found\"}" >&2
    exit 1
fi

# Read the code string from the file
code_string=$(<"$file_path")

# Check if the code string is empty
if [ -z "$code_string" ]; then
    echo "{\"error\": \"File is empty\"}" >&2
    exit 1
fi

# Write code string to a temporary file
echo "$code_string" >"/tmp/$box_id.cpp"

# Compile the code
if ! g++ "/tmp/$box_id.cpp" -o "/tmp/$box_id.out"; then
    echo "{\"error\": \"Compilation error\"}" >&2
    exit 1
fi

# Initialize isolate sandbox
if ! isolate --init -b "$box_id"; then
    echo "{\"error\": \"Failed to initialize isolate sandbox\"}" >&2
    exit 1
fi

# Copy the executable to the sandbox directory
if ! cp "/tmp/$box_id.out" "/var/local/lib/isolate/$box_id/box/$box_id"; then
    echo "{\"error\": \"Failed to copy executable to sandbox\"}" >&2
    exit 1
fi

# Execute the program within the sandbox
if ! isolate -b $box_id -t $time_limit -m $memory_limit -M "/var/local/lib/isolate/$box_id/box/meta.txt" -o "output.txt" -r "error.txt" --run -- $box_id; then
    echo "{\"error\": \"Execution failed\"}" >&2
    exit 1
fi

# Return output and error in JSON format
output=$(cat "/var/local/lib/isolate/$box_id/box/output.txt")
error=$(cat "/var/local/lib/isolate/$box_id/box/error.txt")
metadata=$(cat "/var/local/lib/isolate/$box_id/box/meta.txt")

# Cleanup the sandbox
if ! isolate --cleanup -b "$box_id"; then
    echo "{\"error\": \"Failed to cleanup isolate sandbox\"}" >&2
    exit 1
fi

echo "{\"output\": \"$output\", \"error\": \"$error\", \"metadata\": \"$metadata\"}"

# docker exec -it judger /scripts/judge/cpp.sh "/scripts/test.cpp" 1 128000 2