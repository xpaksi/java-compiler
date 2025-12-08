#!/bin/bash

echo "Compiling the parser..."

./compile.sh

INPUT_DIR="./test/input"
OUTPUT_DIR="./test/output"

mkdir -p "$OUTPUT_DIR/function"
mkdir -p "$OUTPUT_DIR/procedure"

echo "Clearing previous outputs..."
rm -f "$OUTPUT_DIR/function/"**
rm -f "$OUTPUT_DIR/procedure/"**


echo "Running tests..."
for input_file in "$INPUT_DIR/function"/*.txt; do
    echo "Processing $input_file"
    filename=$(basename "$input_file" .txt)
    java parser < "$input_file" > "$OUTPUT_DIR/function/${filename}.machine.txt" 2>"$OUTPUT_DIR/function/${filename}.error.txt"
    java Machine "$OUTPUT_DIR/function/${filename}.machine.txt" > "$OUTPUT_DIR/function/${filename}.output.txt" 2>/dev/null
    echo "Finished processing $input_file"
done

for input_file in "$INPUT_DIR/procedure"/*.txt; do
    echo "Processing $input_file" 
    filename=$(basename "$input_file" .txt)
    java parser < "$input_file" > "$OUTPUT_DIR/procedure/${filename}.machine.txt" 2>"$OUTPUT_DIR/procedure/${filename}.error.txt"
    java Machine "$OUTPUT_DIR/procedure/${filename}.machine.txt" > "$OUTPUT_DIR/procedure/${filename}.output.txt" 2>/dev/null
    echo "Finished processing $input_file"
done

echo "Compilation and execution completed. Check the output directory for results."