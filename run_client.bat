@echo off
setlocal enabledelayedexpansion

:: modifica questo campo con l'indirizzo del server
set address=127.1.1.1 
:: modifica questo campo con la porta del server
set port=8080 

set clientProjectPath=%~dp0Client
set jarFile=%clientProjectPath%\client.jar

echo Esecuzione del file JAR con l'indirizzo %address% e la porta %port%
java -jar "%jarFile%" %address% %port%

if errorlevel 1 (
    echo Si e' verificato un errore durante l'esecuzione del file JAR.
) 

pause
endlocal
exit /b