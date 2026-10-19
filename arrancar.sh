#!/bin/bash
# Pone en marcha todo el proyecto: base de datos en Docker + aplicación.
# Uso (en macOS o Linux):  ./arrancar.sh
# Solo hace falta tener instalado Docker y Java 17 (Maven se descarga solo con mvnw).

# Trabajar siempre desde la carpeta del proyecto
cd "$(dirname "$0")"

if ! docker info > /dev/null 2>&1; then
    echo "Docker no está arrancado. Abre Docker Desktop y vuelve a ejecutar este script."
    exit 1
fi

echo "Arrancando el servidor MariaDB..."
if ! docker compose up -d; then
    echo "No se ha podido arrancar el contenedor. ¿Está ocupado el puerto 3306 por otro MySQL/MariaDB?"
    exit 1
fi

# La primera vez el contenedor tarda unos segundos en cargar sql/bbdd.sql
echo "Esperando a que la base de datos esté lista..."
intentos=0
until docker exec mariadb-asociacion mariadb -uroot -proot -e "select 1 from asociacion_juegos.asociacion" > /dev/null 2>&1; do
    intentos=$((intentos + 1))
    if [ $intentos -ge 30 ]; then
        echo "La base de datos no responde. Mira los logs con: docker logs mariadb-asociacion"
        exit 1
    fi
    sleep 2
done

echo "Base de datos lista. Compilando y arrancando la aplicación..."
./mvnw -q compile exec:java
