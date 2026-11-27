@echo off
setlocal
cd /d "%~dp0"

set "JAVA_BIN="
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\javac.exe" set "JAVA_BIN=%JAVA_HOME%\bin"

if not defined JAVA_BIN (
    for /d %%D in ("%ProgramFiles%\Java\jdk*") do (
        if exist "%%~fD\bin\javac.exe" set "JAVA_BIN=%%~fD\bin"
    )
)

if not defined JAVA_BIN (
    echo JDK not found. Install a JDK or set JAVA_HOME.
    exit /b 1
)

set "BUILD_DIR=%TEMP%\uaspbokel4-build"
if not exist "%BUILD_DIR%" mkdir "%BUILD_DIR%"

"%JAVA_BIN%\javac.exe" -d "%BUILD_DIR%" SistemPerpustakaan.java
if errorlevel 1 exit /b 1

"%JAVA_BIN%\java.exe" -cp "%BUILD_DIR%" SistemPerpustakaan
