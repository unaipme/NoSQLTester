## Redis

### English

An instance of the database server is needed, which can be downloaded [here](https://redis.io/download). For the app to connect properly to the database, certain conditions must be met.

First, two environmental variables must be set. One is ``REDIS_URL``, which stores the URL to the host of the server, and the other is ``REDIS_PORT``, which must be the number of the port to which Redis listens. Below is shown how to set these variables if the server is running locally.

Second, the app does not support any kind of authentication. So no user will be used to connect to the server, and the protected mode must be off. Later, I will show how to turn this off.

#### Installation

The latest version with which the app gets to work properly is 3.2.6. It's optional to store the binaries in the ``/opt`` directory, change it at your will.

```
vi /etc/environment
# at this point, add the lines on example below
source /etc/environment
cd /opt
wget http://download.redis.io/releases/redis-3.2.6.tar.gz
tar -xf redis-3.2.6.tar.gz
mv redis*/ redis/
cd redis/
make
vi redis.conf
# search for protected-mode line and set it to no
/opt/redis/src/redis-server /opt/redis/redis.conf --daemonize yes
```

```
export REDIS_URL=localhost
export REDIS_PORT=6379
```

At this point, the server should be up and running, and listening to port 6379 by default.

If there is any problem when trying to connect to Redis from the app, check the configuration file and make sure Redis is binded to the correct IP address.

### Spanish

Una instancia de servidor de Redis es necesaria. Se puede descargar Redis desde [aquí](https://redis.io/download). Para que la aplicación se conecte debidamente, ciertas condiciones deben darse.

Primero, dos variables de entorno deben existir. Una es ``REDIS_URL``, la cual deberá contener la URL al host del servidor, y la otra ``REDIS_PORT``, que debe contener el número del puerto al que está escuchando. Debajo se muestra en un ejemplo los valores de las variables si el servidor corre en local.

Segundo, la aplicación no tiene soporte para autenticación. No se usará ningún usuario para conectar al servidor, y el modo protegido debe estar apagado. También se muestra después cómo hacerlo.

#### Instalación

La última versión de Redis con la cual se ha probado la aplicación es 3.2.6. Es opcional guardar los ejecutables en la carpeta ``/opt``, cámbialo como te plazca.

```
vi /etc/environment
# en este archivo, añadir las líneas mostradas debajo
source /etc/environment
cd /opt
wget http://download.redis.io/releases/redis-3.2.6.tar.gz
tar -xf redis-3.2.6.tar.gz
mv redis*/ redis/
cd redis/
make
vi redis.conf
# busca protected-mode y sustituye yes por no
/opt/redis/src/redis-server /opt/redis/redis.conf --daemonize yes
```

```
export REDIS_URL=localhost
export REDIS_PORT=6379
```

En este momento, el servidor ya debería estar funcionando, y escuchando al puerto 6379 por defecto.

Si hay algún problema a la hora de conectarse desde la aplicación a Redis, comprueba en el archivo de configuración si la dirección IP de bindeo está bien configurada.