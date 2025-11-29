#!/bin/bash

echo "========================================"
echo "Starting Online Banking System Backend"
echo "========================================"
echo ""

if [ ! -d "bin" ]; then
    echo "Error: bin directory not found. Please run build.sh first."
    exit 1
fi

java -cp bin Main

