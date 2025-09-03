@echo off
title Executando Finansys API

:: Muda o diretorio de trabalho para a pasta onde o script esta localizado
cd /d "%~dp0"

echo.
echo ==================================================
echo      INICIANDO A APLICACAO FINANSYS API...
echo ==================================================
echo.

REM Executa o arquivo .jar que esta na subpasta 'target'
java -jar "target\finansys-api-0.0.1-SNAPSHOT.jar"

echo.
echo ==================================================
echo      APLICACAO FINALIZADA.
echo ==================================================
echo Pressione qualquer tecla para fechar esta janela...
pause