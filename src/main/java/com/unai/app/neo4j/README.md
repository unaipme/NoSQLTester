## Neo4j

### English

An instance of the database server is needed, which can be downloaded [here](https://neo4j.com/download/). The example model has been coded following an example dataset provided with Neo4j itself.

For the connection to the app to work properly, four environmental variables must be set.

- ``NEO4J_BOLT_USER`` must store the database user's name.
- ``NEO4J_BOLT_PASSWORD`` must contains the password of such user.
- ``NEO4J_BOLT_URL`` must contain the address to the **BOLT** interface of Neo4j, following the format ``bolt://<address>:<port>``
- ``NEO4J_URL`` must contain the full address and authentication for the **HTTP** interface. It must follow this format: ``http://<username>:<password>@<address>:<port>``.

Below, I'll show what the values for that variables would be if running the server locally.

Also, the Neo4j server requires that the environmental variable ``JAVA_HOME`` is set, and points to the Java installation folder.

#### Installation

The last version with which the server has worked with the application properly, has been 3.1.0. You can change the installation directory from ``/opt`` to what you will.

```
cd /opt
wget https://neo4j.com/artifact.php?name=neo4j-community-3.1.0-unix.tar.gz
tar -xf artifact.php?name=neo4j-community-3.1.0-unix.tar.gz
mv neo4j*/ neo4j/
/opt/neo4j/bin/neo4j start
```

At this point, the server is up and running. To install the example dataset to which the application is modelled, access the web application of Neo4j, which is in ``http://localhost:7474``.

You'll have to log in, with the default user and password, both ``neo4j``. The first time you log in, the password must be changed. Remember the password, as it will be used for the application.

Once logged in, type ``:play movie graph`` in the command prompt and follow the steps that will pop up until you copy the dataset.

The next thing is to set the environmental variables up. The values for a locally running server would be as shown below, if you followed the steps so far:

```
export NEO4J_BOLT_USER=neo4j
export NEO4J_BOLT_PASSWORD=<whatever password you just set>
export NEO4J_BOLT_URL=bolt://localhost:7687
export NEO4J_URL=http://neo4j:<password>@localhost:7474
```

At this point, the server should be up, the data loaded and the variables ready, so that the app should be able to connect to Neo4j.

### Spanish

Una instancia de servidor Neo4j es necesaria. Se puede descargar desde [aquí](https://neo4j.com/download/). El modelo de ejemplo ha sido programado siguiendo un dataset de ejemplo provisto por el mismo Neo4j.

Para que la conexión desde la aplicación funcione, cuatro variables de entorno deben existir.

- ``NEO4J_BOLT_USER`` debe guardar el nombre del usuario de la base de datos.
- ``NEO4J_BOLT_PASSWORD`` contendrá la contraseña del usuario.
- ``NEO4J_BOLT_URL`` contendrá la dirección a la interfaz **BOLT** Neo4j, la cual debe seguir el formato ``bolt://<dirección>:<puerto>``
- ``NEO4J_URL`` debe contener la autenticación y dirección a la interfaz **HTTP**. Deberá seguir el formato ``http://<usuario>:<contraseña>@<dirección>:<puerto>``.

Debajo, enseñaré unos valores de ejemplo para estas variables si el servidor ejecutara en local.

El servidor de Neo4j requiere que la variable de entorno ``JAVA_HOME`` exista y apunte al directorio de instalación de Java.

#### Instalación

La versión mas reciente con la cual se ha comprobado que la aplicación funciona es 3.1.0. Puedes cambiar el directorio de instalacion de ``/opt`` a lo que tu quieras.

```
cd /opt
wget https://neo4j.com/artifact.php?name=neo4j-community-3.1.0-unix.tar.gz
tar -xf artifact.php?name=neo4j-community-3.1.0-unix.tar.gz
mv neo4j*/ neo4j/
/opt/neo4j/bin/neo4j start
```

En este momento, el servidor ya está funcionando. Para instalar el dataset de ejemplo que usa la aplicación, accede a la aplicación web de Neo4j, mediante ``http://localhost:7474``.

Deberás iniciar sesión con usuario y contraseña por defecto, ambos ``neo4j``. Tras eso, tendrás que cambiar de contraseña. Recuérdala, porque deberás usarla para la aplicación.

Una vez dentro, escribe en el terminal el comando ``:play movie graph``, y sigue las instrucciones que aparecerán para copiar el dataset.

El próximo paso es actualizar las variables de entorno. Si has seguido los pasos hasta ahora, o si el entorno va a ser local, los valores deberán parecerse a estos:

```
export NEO4J_BOLT_USER=neo4j
export NEO4J_BOLT_PASSWORD=<la contraseña que acabas de actualizar>
export NEO4J_BOLT_URL=bolt://localhost:7687
export NEO4J_URL=http://neo4j:<contraseña>@localhost:7474
```

En este momento, el servidor funciona, los datos están ya introducidos y las variables ya están definidas, por lo cual todo está listo para que la aplicación se conecte a Neo4j.