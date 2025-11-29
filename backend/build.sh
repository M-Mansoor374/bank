#!/bin/bash

echo "========================================"
echo "Building Online Banking System Backend"
echo "========================================"

mkdir -p bin

echo "Compiling Java files..."
javac -d bin -encoding UTF-8 src/models/*.java src/services/*.java src/controllers/*.java src/utils/*.java src/Main.java

if [ $? -eq 0 ]; then
    echo ""
    echo "========================================"
    echo "Build successful!"
    echo "========================================"
    echo ""
    echo "To run the server, use:"
    echo "  java -cp bin Main"
    echo ""
else
    echo ""
    echo "========================================"
    echo "Build failed!"
    echo "========================================"
    exit 1
fi

