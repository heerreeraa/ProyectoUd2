# Manual de puesta en marcha

## 1. Requisitos

- Git, para descargar el proyecto.
- Docker Desktop, donde se ejecuta el servidor MariaDB.
- Java 17 (JDK).
- IntelliJ IDEA (vale la edición Community).

Maven no hace falta instalarlo: viene incluido en IntelliJ y, además, el proyecto trae el *Maven Wrapper* (`mvnw` y `mvnw.cmd`), que lo descarga solo la primera vez.

## 2. Descargar el proyecto

Desde la terminal:

```bash
git clone https://github.com/heerreeraa/ProyectoUd2.git
```

O desde IntelliJ: *File > New > Project from Version Control* y pegar la misma URL.

## 3. Arrancar la base de datos

1. Abrir Docker Desktop y esperar a que ponga *Engine running*.
2. En la carpeta del proyecto, ejecutar:

```bash
docker compose up -d
```

La primera vez se crea el contenedor `mariadb-asociacion` (MariaDB 11.4, puerto 3306, usuario `root`, contraseña `root`). Al crearse, ejecuta automáticamente `sql/bbdd.sql`: crea la base de datos `asociacion_juegos` con sus tablas y los datos de prueba (6 socias, 8 juegos y 5 partidas).

Para comprobar que funciona:

```bash
docker exec -it mariadb-asociacion mariadb -uroot -proot -e "select nombre from asociacion_juegos.socia"
```

Si sale el error "port is already allocated", hay otro MySQL o MariaDB usando el puerto 3306. Hay que pararlo (`docker ps` para ver su nombre y `docker stop <nombre>`) y volver a ejecutar `docker compose up -d`.

## 4. Abrir y ejecutar en IntelliJ

1. *File > Open* y elegir la carpeta del proyecto. Si aparece *Load Maven project*, pulsar Load y esperar a que se descarguen las librerías.
2. En *File > Project Structure > Project > SDK* elegir un JDK 17. Si no hay ninguno: *Add SDK > Download JDK > 17*.
3. Abrir `src/main/java/org/example/App.java` y pulsar el botón verde de ejecutar que aparece junto a `main`.

## 5. Arranque automático

Hemos preparado dos scripts que hacen todo lo anterior de una vez: comprueban que Docker está abierto, levantan la base de datos, esperan a que esté lista y arrancan la aplicación.

- macOS / Linux: `./arrancar.sh`
- Windows: doble clic en `arrancar.bat`

## 6. Primer inicio de sesión

La primera vez que se ejecuta, la aplicación pide crear el usuario administrador. Después hay que iniciar sesión con él (3 intentos como máximo). Desde el menú *6. Usuarios* se pueden crear más usuarios, cambiar la contraseña y ver el registro de sesiones.

- Los usuarios se guardan en `datos/usuarios.dat` (fichero binario, la contraseña no se guarda: se guarda su hash SHA-256 con sal).
- Cada entrada, salida o intento fallido se apunta en `datos/sesiones.log`.
- Si se olvida la contraseña o el fichero se daña, basta con borrar `datos/usuarios.dat` y volver a crear el administrador.

## 7. Conectarse a un servidor en otro equipo

La base de datos no tiene por qué estar en el mismo ordenador que la aplicación. Los datos de conexión están en `config.properties`:

```properties
host=localhost
puerto=3306
bbdd=asociacion_juegos
usuario=root
password=root
```

Para usar el servidor de otro equipo de la red, se cambia `host` por su IP (por ejemplo `host=192.168.1.40`). No hace falta recompilar.

## 8. Ejecutar los tests

Con Docker arrancado, en IntelliJ: clic derecho en `src/test/java` > *Run 'All Tests'*. O desde la terminal:

```bash
./mvnw test
```

Los tests trabajan con la base de datos real, pero crean sus propios datos de prueba y los borran al terminar.

## 9. Volver a los datos de prueba iniciales

Para borrar la base de datos y crearla otra vez desde `sql/bbdd.sql`:

```bash
docker compose down -v
docker compose up -d
```

## 10. Ficheros que genera la aplicación

- `exportaciones/`: los ficheros XML y JSON exportados (socias, juegos, partidas, metadatos, histórico y estadísticas).
- `datos/`: usuarios y registro de sesiones.

Las dos carpetas se crean solas y no se suben al repositorio.
