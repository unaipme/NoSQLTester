## MongoDB

### English

An instance of the database server is needed, which can be downloaded [here](https://mongodb.com/download-center). The server runs on a example dataset called Restaurants, and requires some configuration before the application can correctly connect to it.

First, a user with permission to make all CRUD operations needs to be created in the database in which the dataset has been inserted. If no user has been created, in the installation section I will show how to do it.

Second, five environmental variables must be set for the application to establish the connection.

- ``MONGODB_USER`` must contain the username to connect to the database.
- ``MONGODB_PASSWORD`` must contain the password of said user.
- ``MONGODB_DB`` is the name of the database in which the user was created and the dataset copied to.
- ``MONGODB_URI`` is the address to the server instance.
- ``MONGODB_PORT`` is the port to which the server listens.

An example for all variables that is run locally is show below.

#### Installation

The latest version with which the app has worked properly is 3.4.1. The installation folder used is ``/opt`` but can be what you will. Note that the downloaded version is for Ubuntu specifically. Choose your download in the link above.

Also, the used directory for logging and for data storing can be chosen at your will.

```
cd /opt
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-ubuntu1604-3.4.1.tgz
tar -xf mongodb-linux-x86_64-ubuntu1604-3.4.1.tgz
mv mongo*/ mongo/
mkdir -p /data/mongo
/opt/mongo/bin/mongod --dbpath /data/mongo --fork --logpath /var/log/mongod.log
```
At this point, the server is up and running. Next is to copy the dataset, create a user that can make CRUD operations, and then set the environmental variables.

To copy the dataset:

```
wget https://raw.githubusercontent.com/mongodb/docs-assets/primer-dataset/primer-dataset.json
/opt/mongo/bin/mongoimport --db <your db name> --collection restaurants --drop --file primer-dataset.json
```

To create the user:

```
/opt/bin/mongo
use <preferred db's name>
db.createRole({
	role: "testerrole",
    	privileges: [{
		resource: {db: <your db>, collection: "restaurants"},
		actions: ["insert", "find", "update", "remove"]
	}],
	roles: []
});
db.createUser({
	user: <username>,
    pwd: <password>,
    roles: ["testerrole"]
});
```
To set the variables, as if the server was running locally:
```
export MONGODB_USER=<username just set>
export MONGODB_PASSWORD=<password just set>
export MONGODB_DB=<preferred db's name>
export MONGODB_URI=localhost
export MONGODB_PORT=27017
```
At this point, everything is ready to work with the application.



### Spanish

Una instancia de la base de datos es necesaria, la cual se puede descargar [aquí](https://mongodb.com/download-center). El servidor debe contener un dataset de ejemplo llamado Restaurants, y requiere unas configuraciones antes de que la aplicación pueda conectar correctamente.

Primero, un usuario con permiso para realizar operaciones CRUD es necesario, y debe ser creado en la base de datos en el cual el dataset se inserte. Si no se ha creado ningún usuario, en la sección de instalación te explico cómo hacerlo.

Segundo, cinco variables de entorno deben existir para establecer la conexión.

- ``MONGODB_USER`` debe contener el nombre del usuario con el que conectarse.
- ``MONGODB_PASSWORD`` debe contener la contraseña del mismo.
- ``MONGODB_DB`` es el nombre de la base de datos en la que se ha creado al usuario y donde ha sido copiado el dataset.
- ``MONGODB_URI`` es la dirección al servidor.
- ``MONGODB_PORT`` es el puerto al que escucha el servidor.

Un ejemplo de todas las variables se encuentra en la próxima sección.

#### Instalación

La versión mas reciente con la cual la aplicación ha funcionado es 3.4.1. El directorio de instalación de los ejemplos es ``/opt`` pero puedes usar cualquiera. Fíjate que la versión que se descarga es para Ubuntu. Elige el que más se ajuste a ti en el enlace superior.

También pueden variarse los directorios que se usan para almacenar los datos y registros.

```
cd /opt
wget https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-ubuntu1604-3.4.1.tgz
tar -xf mongodb-linux-x86_64-ubuntu1604-3.4.1.tgz
mv mongo*/ mongo/
mkdir -p /data/mongo
/opt/mongo/bin/mongod --dbpath /data/mongo --fork --logpath /var/log/mongod.log
```
En este punto, el servidor debería funcionar. Lo próximo es copiar el dataset, crear el usuario y ajustar las variables.

Para copiar el dataset:

```
wget https://raw.githubusercontent.com/mongodb/docs-assets/primer-dataset/primer-dataset.json
/opt/mongo/bin/mongoimport --db <nombre de tu base> --collection restaurants --drop --file primer-dataset.json
```

Para crear el usuario:

```
/opt/bin/mongo
use <preferred db's name>
db.createRole({
	role: "testerrole",
    	privileges: [{
		resource: {db: <your db>, collection: "restaurants"},
		actions: ["insert", "find", "update", "remove"]
	}],
	roles: []
});
db.createUser({
	user: <username>,
    pwd: <password>,
    roles: ["testerrole"]
});
```
Para definir las variables, si el servidor se ejecuta localmente:
```
export MONGODB_USER=<nombre de usuario>
export MONGODB_PASSWORD=<contraseña>
export MONGODB_DB=<nombre de base>
export MONGODB_URI=localhost
export MONGODB_PORT=27017
```
En este momento, todo está listo para que la aplicación se conecte directamente a MongoDB.
