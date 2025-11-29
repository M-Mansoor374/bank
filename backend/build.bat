@echo off
echo ========================================
echo Building Online Banking System Backend
echo ========================================

if not exist bin mkdir bin

echo Compiling Java files...
javac -d bin -encoding UTF-8 src/models/*.java src/services/*.java src/controllers/*.java src/utils/*.java src/Main.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Build successful!
    echo ========================================
    echo.
    echo To run the server, use:
    echo   java -cp bin Main
    echo.
) else (
    echo.
    echo ========================================
    echo Build failed!
    echo ========================================
    exit /b 1
)

