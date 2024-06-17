#!/bin/bash

# Check if all required arguments are provided
if [ $# -ne 4 ]; then
    echo "{\"error\": \"Usage: $0 <path_to_cpp_file> <time_limit> <memory_limit> <box_id>\"}" >&2
    exit 1
fi

# Assign arguments to variables
path_to_cpp_file="$1"
time_limit="$2"
memory_limit="$3"
box_id="$4"

# Compile the code
if ! g++ "$path_to_cpp_file" -o "/tmp/$box_id.out"; then
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

echo "{\"output\": \"$output\", \"error\": \"$error\", \"metadata\": \"$metadata\"}"
