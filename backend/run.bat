@echo off
echo ========================================
echo Starting Online Banking System Backend
echo ========================================
echo.

if not exist bin (
    echo Error: bin directory not found. Please run build.bat first.
    pause
    exit /b 1
)

java -cp bin Main

