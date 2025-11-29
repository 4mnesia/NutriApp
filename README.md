# NutriApp Backend 🦦📱
Qué hace
- La idea es tener una API que sirva los datos para la aplicación de nutrición.
- Aquí manejamos toda la lógica de usuarios, base de datos y conexión

# 🛠️ Tecnologías que usamos
* ☕︎ **Java 17** (La base de todo)
* 🍃 **Spring Boot** (Para levantar el servidor rápido)
* 🐳 **Docker** (Para que funcione en cualquier compu sin instalar tanto)
* 🐬 **MySQL/SQL** (Base de datos relacional)
* 🪶 **Maven** (Para las dependencias)

# 📋 Requisitos Previos
* [Java JDK 17](https://www.oracle.com/java/technologies/downloads/) (o superior)
* [Maven](https://maven.apache.org/) 
* [Docker](https://www.docker.com/) y [Docker Compose](https://docs.docker.com/compose/)
* Un cliente SQL (como [XAMPP](https://www.apachefriends.org/es/index.html) o [Workbench](https://dev.mysql.com/downloads/workbench/))

# 📢 Archivos importantes
- `pom.xml`: dependencias y configuración Maven.
- `setup_database.sql`: script para crear la base de datos.
- `Dockerfile`, `docker-compose.yml`: para construir y ejecutar la app en contenedores.
- `src/main/resources/application*.properties`: configuraciones por perfil.

# 📝 Tests
```
.\mvnw.cmd test
```

# 🗂️ Estructura del proyecto
```
src/
  main/
    java/com/example/NutriApp/
      controller/
      service/
      repository/
      model/
      dto/
      assembler/
    resources/
      application*.properties
```
