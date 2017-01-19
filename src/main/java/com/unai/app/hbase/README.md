## HBase

### English

An instance of the database server is needed, which can be downloaded [here](http://www.apache.org/dyn/closer.cgi/hbase/). It must be running locally, because, as for now, the app does not support remote connections to HBase.

If no connection to HBase is achieved, a ``ConnectException`` will show every second or so on console, because HBase won't stop trying to connect. I have not been able to change this. The endpoints for other databases will work properly, though.

HBase server requires that the environmental variable ``JAVA_HOME`` is set, and points to the Java installation folder.

#### Installation

You can download any version you want from the link provided previously, but the latest version that has been tested and is known is the 1.3.0. Here's how to download, install, and run it from terminal.

Keep in mind that the mirror used is the closest one to my location, and it may change for you. Also the used directories (``/opt``, ``/data/hbase`` and ``/data/zookeeper``) can be changes at your will.

```
cd /opt
wget http://apache.uvigo.es/hbase/1.3.0/hbase-1.3.0-bin.tar.gz
tar -xf hbase-1.3.0-bin.tar.gz
mv hbase*/ hbase/
mkdir -p /data/hbase /data/zookeeper
vi hbase/conf/hbase-site.xml
# at this point copy in the XML, inside configuration tags, the sample below
/opt/hbase/bin/start-hbase.sh
```

```
<property>
  <name>hbase.rootdir</name>
  <value>file:///data/hbase</value>
</property>
<property>
  <name>hbase.zookeeper.property.dataDir</name>
  <value>/data/zookeeper</value>
</property>
```

At this point, the server should be up and running. You can try to make some quick queries with the HBase shell (``hbase/bin/hbase shell``), or directly start the app.

### Spanish

Una instancia de la base de datos es necesaria, la cual se puede descargar [aquí](http://www.apache.org/dyn/closer.cgi/hbase/). Tiene que ejecutarse en local porque, de momento, la aplicación no tiene soporte para conexiones remotas a HBase.

Si la aplicación no logra conectarse a HBase, mostrará un ``ConnectException`` cada segundo más o menos en consola, porque HBase no cesa en su intento de conectarse. No he sido capaz de hacer que deje de mostrar el error, pero no afecta al resto de la API, la cual se puede usar con total normalidad.

El servidor de HBase requiere que la variable de entorno ``JAVA_HOME`` exista y apunte al directorio de instalación de Java.

#### Instalación

Puedes descargar cualquier versión en el enlace de arriba, pero la última versión con la que se ha probado la API es la 1.3.0. Aquí se muestra como descargarlo, instalarlo y ejecutarlo.

Recuerda que el mirror usado es el más cercano a mi ubicación, así que el tuyo podría ser diferente. Los directorios usados (``/opt``, ``/data/hbase`` y ``/data/zookeeper``) se pueden cambiar a gusto del consumidor.

```
cd /opt
wget http://apache.uvigo.es/hbase/1.3.0/hbase-1.3.0-bin.tar.gz
tar -xf hbase-1.3.0-bin.tar.gz
mv hbase*/ hbase/
mkdir -p /data/hbase /data/zookeeper
vi hbase/conf/hbase-site.xml
# Ahora, copia en el XML, dentro de configuration, el ejemplo debajo
/opt/hbase/bin/start-hbase.sh
```

```
<property>
  <name>hbase.rootdir</name>
  <value>file:///data/hbase</value>
</property>
<property>
  <name>hbase.zookeeper.property.dataDir</name>
  <value>/data/zookeeper</value>
</property>
```

En este momento, el servidor debería estar funcionando correctamente. Puedes comprobarlo mediante el HBase shell (``hbase/bin/hbase shell``), o directamente abre la app.