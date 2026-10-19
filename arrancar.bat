@echo off
REM Pone en marcha todo el proyecto en Windows: base de datos en Docker + aplicación.
REM Uso: doble clic en arrancar.bat o ejecutarlo desde la consola.
REM Solo hace falta tener instalado Docker Desktop y Java 17 (Maven se descarga solo con mvnw).
chcp 65001 > nul
cd /d "%~dp0"

docker info > nul 2>&1
if errorlevel 1 (
    echo Docker no esta arrancado. Abre Docker Desktop y vuelve a ejecutar este script.
    pause
    exit /b 1
)

echo Arrancando el servidor MariaDB...
docker compose up -d
if errorlevel 1 (
    echo No se ha podido arrancar el contenedor. Esta ocupado el puerto 3306?
    pause
    exit /b 1
)

echo Esperando a que la base de datos este lista...
set intentos=0
:esperar
docker exec mariadb-asociacion mariadb -uroot -proot -e "select 1 from asociacion_juegos.asociacion" > nul 2>&1
if not errorlevel 1 goto lista
set /a intentos+=1
if %intentos% geq 30 (
    echo La base de datos no responde. Mira los logs con: docker logs mariadb-asociacion
    pause
    exit /b 1
)
timeout /t 2 > nul
goto esperar

:lista
echo Base de datos lista. Compilando y arrancando la aplicacion...
call mvnw.cmd -q compile exec:java
pause
