#!/bin/bash

# Usage: ./run-individual.sh <type> <number>
# Example: ./run-individual.sh function 1
# Example: ./run-individual.sh procedure 2

TYPE=$1
NUMBER=$2

INPUT_DIR="./test/input"
OUTPUT_DIR="./test/output"

# Validate arguments
if [ -z "$TYPE" ] || [ -z "$NUMBER" ]; then
    echo "Usage: ./run-individual.sh <type> <number>"
    echo "  type: function or procedure"
    echo "  number: test number (e.g., 1, 2, 3)"
    exit 1
fi

# Validate type
if [ "$TYPE" != "function" ] && [ "$TYPE" != "procedure" ]; then
    echo "Error: type must be 'function' or 'procedure'"
    exit 1
fi

# Check if input file exists
INPUT_FILE="$INPUT_DIR/$TYPE/$NUMBER.txt"
if [ ! -f "$INPUT_FILE" ]; then
    echo "Error: Input file not found: $INPUT_FILE"
    exit 1
fi

# Ensure output directory exists
mkdir -p "$OUTPUT_DIR/$TYPE"

echo "=========================================="
echo "Running test: $TYPE/$NUMBER"
echo "=========================================="

# Run parser
echo ""
echo "--- Running Parser ---"
java parser < "$INPUT_FILE" > "$OUTPUT_DIR/$TYPE/$NUMBER.machine.txt" 2>"$OUTPUT_DIR/$TYPE/$NUMBER.error.txt"

# Show parser errors if any
if [ -s "$OUTPUT_DIR/$TYPE/$NUMBER.error.txt" ]; then
    echo "Parser Errors:"
    cat "$OUTPUT_DIR/$TYPE/$NUMBER.error.txt"
else
    echo "No parser errors."
fi

# Run machine
echo ""
echo "--- Running Machine ---"
java Machine "$OUTPUT_DIR/$TYPE/$NUMBER.machine.txt" > "$OUTPUT_DIR/$TYPE/$NUMBER.output.txt" 2>&1

# Show machine output
echo "Machine Output:"
cat "$OUTPUT_DIR/$TYPE/$NUMBER.output.txt"
