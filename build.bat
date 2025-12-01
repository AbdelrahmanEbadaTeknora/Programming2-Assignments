@echo off
REM Sudoku Validator - Build and Run Script for Windows
REM This script compiles, packages, and optionally runs the Sudoku validator

echo ==========================================
echo Sudoku Solution Verifier - Build Script
echo ==========================================
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/
    pause
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java JDK 11 or higher
    pause
    exit /b 1
)

echo [OK] Maven found
echo [OK] Java found
echo.

REM Clean previous builds
echo Cleaning previous builds...
call mvn clean >nul 2>nul
echo [OK] Clean complete
echo.

REM Compile the project
echo Compiling project...
call mvn compile
if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed
    pause
    exit /b 1
)
echo [OK] Compilation successful
echo.

REM Run tests
echo Running tests...
call mvn test
if %errorlevel% neq 0 (
    echo [WARNING] Some tests failed (continuing...)
) else (
    echo [OK] All tests passed
)
echo.

REM Package into JAR
echo Creating JAR file...
call mvn package -DskipTests
if %errorlevel% neq 0 (
    echo [ERROR] JAR creation failed
    pause
    exit /b 1
)
echo [OK] JAR created successfully
echo.

echo ==========================================
echo Build Complete!
echo ==========================================
echo.
echo To run the application:
echo   java -jar target\sudoku-validator-1.0.0.jar ^<csv-file^> ^<mode^>
echo.
echo Examples:
echo   java -jar target\sudoku-validator-1.0.0.jar board.csv 0
echo   java -jar target\sudoku-validator-1.0.0.jar board.csv 3
echo   java -jar target\sudoku-validator-1.0.0.jar board.csv 27
echo.

REM Ask if user wants to run the application
set /p run="Do you want to run the application now? (y/n): "
if /i "%run%"=="y" (
    echo.
    set /p csvfile="Enter CSV file path: "
    set /p mode="Enter mode (0/3/27): "
    echo.
    echo Running application...
    java -jar target\sudoku-validator-1.0.0.jar "%csvfile%" "%mode%"
)

pause