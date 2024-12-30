@echo off
setlocal enabledelayedexpansion

:: modifica questo campo con la porta del server
set port=8080

set mainClass=Main
set serverProjectPath=%~dp0\Server
set jarFile=%serverProjectPath%\server.jar

set "dependencies=%serverProjectPath%\dependencies"

set "classpath=%jarFile%"
for %%i in ("%dependencies%\*.jar") do (
    set "classpath=!classpath!;%%i"
)

cd /d %serverProjectPath%

java -cp "!classpath!" %mainClass% %port%

if errorlevel 1 (
    echo Si e' verificato un errore durante l'esecuzione del file JAR.
)

pause
endlocal
exit /b