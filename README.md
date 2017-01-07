# NoSQLTester

## English

The goal of this project is to build an application with which it is possible to connect and test the performance of different NoSQL databases. It is built using Spring Boot framework (for the REST part and for the data access and management part with Spring Data).

At release point, my plan is to have built the java part of, at least, Redis, MongoDB, Neo4j and HBase. To use Gatling to test the application and the databases is within my plans too, so I'll do my best to get the Gatling simulations written as soon as possible.

### Using

This project requires of Maven to work, and an instance of the database system you want to test. In some cases, the tests are based on sample datasets that are provided by the developers of the database. The project will correctly execute even though no database connection can be done at execution time. This is because it will try to connect on each request.

To download and run this project:

```
git clone https://github.com/unaipme/NoSQLTester.git
mvn spring-boot:run
```

Now, I'm explaining how to get each database part running.

#### Redis

An instance of the database server is needed, which can be downloaded [here](https://redis.io/download). Once it is installed, the app requires of an environmental variable with key ``REDISCLOUD_URL``, the value of which must be:

```
redis://<username>:<password>@<hostname>:<port>
```

...or, if no username or password is configured, just:

```
redis://<hostname>:<port>
```

#### Neo4j

An instance of the database server is needed, which can be downloaded [here](https://neo4j.com/download/). In this case, the app requires multiple environmental variables to be set. ``NEO4J_BOLT_USER`` must contain the name of the Neo4j user. ``NEO4J_BOLT_PASSWORD`` must contain the password of said user. ``NEO4J_BOLT_URL`` must contain the URL for the **BOLT** interface of the database. And lastly, ``NEO4J_URL`` must contain the URL of the **HTTP** interface.

In the code, the sample Movie dataset is used. It can be installed by accessing Neo4j's browser app (on path ``/browser`` in the HTTP interface host), prompting ``:play movie graph`` and following the steps.

#### MongoDB

An instance of the database server is needed, which can be downloaded [here](https://mongodb.com/download-center). Then, some environmental variables are used to create the connections. ``MONGODB_USER`` must contain the name of the MongoDB user. ``MONGODB_PASSWORD`` must contain the password of said user. ``MONGODB_DB`` must contain the authentication database name. ``MONGODB_URI`` must contain the hostname of the server. And, lastly, ``MONGODB_PORT`` must contain the port in which the server runs.

In the code, the Restaurants sample dataset is used. To install it, copy [this JSON file](https://raw.githubusercontent.com/mongodb/docs-assets/primer-dataset/primer-dataset.json) to the database's _restaurants_ collection (That exact name is required) using the ``mongoimport`` command.

### HBase

An instance of the database server is needed, which can be downloaded [here](http://www.apache.org/dyn/closer.cgi/hbase/). I have NOT been able to make it work remotely, so this part of the API won't work unless the server is installed in the same computer that is running the API. Obviously, it won't work in Heroku either.

The Gatling part is still long due, but as for now, the Swagger Springfox documentation can be used to check that all endpoints work as expected. It's possible to use Postman to run some quick test if wanted.

**NoSQLTester** is now running online in [Heroku](https://nosqltester.herokuapp.com/), from which is possible to try the endpoints via the mentioned Springfox page, or directly use them via Postman.

If you find any of this code useful or if you have any question or whatever, don't hesitate to contact me!

## Spanish

El objetivo de este proyecto es construir una aplicación mediante la cual sea posible conectarse y probar (testear) diferentes sistemas de bases de datos NoSQL. Está construido usando la framework Spring Boot (para la parte de REST, y se han usado / usarán los diferentes módulos de Spring Data para cada DBMS).

Mi plan para release es haber construido las interfaces en Java de, por lo menos, los sistemas Redis, MongoDB, Neo4j y HBase. También está en mis planes utilizar Gatling para el proceso del testeo, así que también consideron mi objetivo para la versión de release el escribir las simulaciones de éste.

### Uso

Este proyecto requiere de Maven para funcionar, y una instancia de cada sistema de base de datos que se quiera usar. En algunos casos, el código se basa en datasets de ejemplo que proporcionan los desarrolladores de los mismos sistemas. El programa se ejecutará correctamente aunque no haya ninguna instancia de base de datos ejecutándose, ya que la conexión se realiza en cada request.

Para descargar y ejecutar este proyecto:
```
git clone https://github.com/unaipme/NoSQLTester.git
mvn spring-boot:run
```

Ahora explico como hacer funcionar cada una de las partes que corresponde a una base de datos:

#### Redis

Una instancia de servidor de Redis es necesaria. Se puede descargar Redis desde [aquí](https://redis.io/download). Cuando esté instalado, la aplicación también requerirá que exista una variable de entorno llamada ``REDISCLOUD_URL``, el valor del cual deberá ser:

```
redis://<username>:<password>@<hostname>:<port>
```

...o, si no se usa ningún usuario y contraseña:

```
redis://<hostname>:<port>
```

#### Neo4j

Una instancia de servidor Neo4j es necesaria. Se puede descargar desde [aquí](https://neo4j.com/download/). En este caso, la aplicación require de varias variables de entorno. ``NEO4J_BOLT_USER`` deberá contener el nombre del usuario de Neo4j. ``NEO4J_BOLT_PASSWORD`` deberá contener la contraseña de dicho usuario. ``NEO4J_BOLT_URL`` deberá contener la URL a la interfaz **BOLT** de la base de datos. ``NEO4J_URL`` deberá contener la URL a la interfaz **HTML** de la base de datos.

En el código, se usa el dataset de ejemplo de películas. Es posible instalarlo accediendo a la app de navegador (En la ruta ``/browser`` del interfaz HTTP), escribiendo ``:play movie graph`` en la consola y siguiendo los pasos que se muestran.

#### MongoDB

Una instancia de la base de datos es necesaria, la cual se puede descargar [aquí](https://mongodb.com/download-center). Algunas variables de entorno son también necesarias. ``MONGODB_USER`` debe contener el nombre del usuario de MongoDB. ``MONGODB_PASSWORD`` contendrá la contraseña de dicho usuario. ``MONGODB_DB`` debe contener el nombre de la base de datos de autenticación del usuario. ``MONGODB_URI`` deberá contener la ruta al servidor. ``MONGODB_PORT`` contendrá el puerto en el que se ejecuta el servidor.

El código se basa en el dataset de ejemplo Restaurants. Para instalarlo, copia [este archivo JSON](https://raw.githubusercontent.com/mongodb/docs-assets/primer-dataset/primer-dataset.json) a la colección _restaurants_ de la base de datos (El nombre ha de ser exacto) utilizando el comando ``mongoimport``.

### HBase

Una instancia de la base de datos es necesaria, la cual se puede descargar [aquí](http://www.apache.org/dyn/closer.cgi/hbase/). NO he sido capaz de hacer funcionar la API con un servidor remoto, es decir, el servidor debe estar instalado en la misma máquina que ejecuta la API. Tampoco funcionará por la misma razón en Heroku.

Aún queda hasta que pueda hacer la parte de Gatling, pero, por ahora, se puede usar Swagger Springfox para ver las endpoints disponibles y comprabar que funcionan. También se podría escribir algún test simple usando Postman.

**NoSQLTester** está disponible para probar en línea ahora mismo desde [Heroku](https://nosqltester.herokuapp.com/). Ahí se puede acceder a Springfox y comprobar que funciona bien, o se pueden usar las endpoints directamente desde Postman.

Si el código te es de ayuda, o si tienes una pregunta, o lo que sea, no dudes en contactar conmigo!
