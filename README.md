# Proyecto de Ejemplo de un ABM con MVC para la materia Programacion Orientada a Objetos 2 de la oo2.

Pasos a seguir para levantar el proyecto:
 * Version de Java 17 o superior.
 * Maven 3 o superior.
 * Plugin de lombok configurado en su IDE.
 * MySQL como Base de Datos.
 * Crear una Base de Datos con la siguiente instruccion **create database nombre;** 
 * Abrir el proyecto y revisar que se descarguen las dependencias, en caso de que no abrir una terminal en la raiz del proyecto y ejecutar esta instruccion: **mvn clean install**
 * Configurar las variables de entorno para que el archivo application.properties las reconozca antes de iniciar la aplicacion:
   * DB_URL -> colocar la url de la base de datos.
   * DB_USERNAME -> colocar tu usuario de la base de datos.
   * DB_PASSWORD -> colocar tu password de la base de datos.
 * Ejecutar el proyecto. Si todo esta correcto aparece que la aplicacion inicio en x segundos en el puerto 8080.
 * Abrir el navegador e ir a la siguiente url: **http://localhost:8080/auth/login**
 * En la carpeta de configurations/seeder una vez creadas las tablas de la bd se ejecuta la clase UsersSeeder que inicializa la bd con dos usuarios:
   * **ADMIN: username: admin@gmail.com password: foo1234.**
   * **USER: username: user@hotmail.com password: foo1234.**
 * Iniciar sesion con algun usuario.  
