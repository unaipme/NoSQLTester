# NoSQLTester

## English

The goal of this project is to build an application with which it is possible to connect and try different NoSQL databases, and also to act as a guide for other developers to learn and understand the way these databases work and how to code for them. It is built using Spring Boot framework (for the REST part and for the data access and management part with Spring Data). At release point, my plan is to have built the java part of, at least, Redis, MongoDB, Neo4j and HBase.

This piece of software was built as a part of a research. You can read about the results in document [NoSQL ![PDF](http://ir.avisbudgetgroup.com/images/irIcons/ico_pdf.gif)](NoSQL.pdf).

### Using

For this project to properly work, it is highly recommended to have a running instance of each of the servers. The app will still start if the connection of some dbs fail, though. Instructions on how to install the servers follow later.

To run the app, it is required to have Maven installed. To download and run this project, run commands:

```
git clone https://github.com/unaipme/NoSQLTester.git
mvn spring-boot:run
```

From this point, the Swagger Springfox documentation and all endpoints will be available through address ``http://localhost:2426`` (Assuming it's being run locally). 

#### Redis

Go to the [``redis`` package readme file](src/main/java/com/unai/app/redis/README.md) to see an explanation on how to install and set the connection to the app for Redis.

#### Neo4j

Go to the [``neo4j`` package readme file](src/main/java/com/unai/app/neo4j/README.md) to see and explanation on how to install and set the connection to the app for Neo4j.

#### MongoDB

Go to the [``mongodb`` package readme file](src/main/java/com/unai/app/mongo/README.md) to see and explanation on how to install and set the connection to the app for MongoDB.

#### HBase

Go to the [``hbase`` package readme file](src/main/java/com/unai/app/hbase/README.md) to see and explanation on how to install and set the connection to the app for HBase.

The Swagger Springfox documentation can be used to check that all endpoints work as expected. It's possible to use Postman to run some quick test if wanted.

#### Start everything

If you have followed all the guides to install the servers, the following script will start up all servers and the app:

```
source /etc/environment
/opt/redis/src/redis-server /opt/redis/redis.conf --daemonize yes
/opt/mongodb/bin/mongod --dbpath /data/mongo --fork --logpath /var/log/mongod.log
/opt/neo4j/bin/neo4j start
/opt/hbase/bin/start-hbase.sh
cd /opt
git clone https://github.com/unaipme/nosqltester
cd nosqltester
mvn spring-boot:run
```

If you find any of this code useful or if you have any question or whatever, don't hesitate to contact me!

## Spanish

El objetivo de este proyecto es construir una aplicación mediante la cual sea posible conectarse y probar diferentes sistemas de bases de datos NoSQL. Está construido usando la framework Spring Boot (para la parte de REST, y se han usado / usarán los diferentes módulos de Spring Data para cada DBMS). Mi plan para release es haber construido las interfaces en Java de, por lo menos, los sistemas Redis, MongoDB, Neo4j y HBase.

Este software fue escrito como parte de un trabajo de investigación. Puedes leer el resultado en el documento [NoSQL ![PDF](http://ir.avisbudgetgroup.com/images/irIcons/ico_pdf.gif)](NoSQL.pdf).

### Uso

Este proyecto requiere de Maven para funcionar, y una instancia de cada sistema de base de datos que se quiera usar. En algunos casos, el código se basa en datasets de ejemplo que proporcionan los desarrolladores de los mismos sistemas. El programa se ejecutará correctamente aunque no haya ninguna instancia de base de datos ejecutándose, ya que la conexión se realiza en cada request.

Para descargar y ejecutar este proyecto:
```
git clone https://github.com/unaipme/NoSQLTester.git
mvn spring-boot:run
```

Desde ese momento, se abrirá la documentación Swagger Springfox y todos los endpoints de la API en la dirección ``http://localhost:2426`` (Asumiendo que estés en local).

#### Redis

Ve al archivo [readme del paquete ``redis`` ](src/main/java/com/unai/app/redis/README.md) para ver una explicación sobre cómo instalar y ejecutar Redis.

#### Neo4j

Ve al archivo [readme del paquete ``neo4j`` ](src/main/java/com/unai/app/neo4j/README.md) para ver una explicación sobre cómo instalar y ejecutar Neo4j.

#### MongoDB

Ve al archivo [readme del paquete ``mongo`` ](src/main/java/com/unai/app/mongo/README.md) para ver una explicación sobre cómo instalar y ejecutar MongoDB.

#### HBase

Ve al archivo [readme del paquete ``hbase`` ](src/main/java/com/unai/app/hbase/README.md) para ver una explicación sobre cómo instalar y ejecutar HBase.

#### Iniciar todo

Si has seguido las guías para instalar los servidores, el script de debajo iniciará todos los servidores y la aplicación:

```
source /etc/environment
/opt/redis/src/redis-server /opt/redis/redis.conf --daemonize yes
/opt/mongodb/bin/mongod --dbpath /data/mongo --fork --logpath /var/log/mongod.log
/opt/neo4j/bin/neo4j start
/opt/hbase/bin/start-hbase.sh
cd /opt
git clone https://github.com/unaipme/nosqltester
cd nosqltester
mvn spring-boot:run
```

**NoSQLTester** está disponible para probar en línea ahora mismo desde [Heroku](https://nosqltester.herokuapp.com/). Ahí se puede acceder a Springfox y comprobar que funciona bien, o se pueden usar las endpoints directamente desde Postman.

Si el código te es de ayuda, o si tienes una pregunta, o lo que sea, no dudes en contactar conmigo!
