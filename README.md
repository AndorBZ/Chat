# Chat
  - #### Desarrollado por Andor Belszegi.
## Ejecutar servidor.
  - #### Desde terminal, siturase dentro del directorio 'Server' incluido en el repositorio Github.
  - #### Ejecutar el siguiente comando:
        java main.andor.chat.Main

## Conectarse al servidor.
  - #### Desde terminal, ejecutar el siguiente comando:
        telnet 127.0.0.1 6789

## Explicación
  - #### Cuando un cliente se conecta al servidor, se le pide ingresar un nombre de usuario.
  - #### El servidor lleva un historial de todos los usuarios conectados.
  - #### Un usuario puede estar conectado o desconectado
  - #### Cuando un usuario se conecta, se le informa del número de usuarios conectados. También se informa al resto de usuarios conectados que ha entrado un usuario nuevo al chat.
  - #### Cuando un usuario envía un mensaje, les llega diréctamente a los usuarios conectados. En cambio, a los usuarios desconectados se le envía el mensaje a su lista de mensajes no recibidos por estar desconectados.
  - #### Cuando el usuario se desconecta, se les informa al resto de usuarios conectados.
  - #### Cuando el servidor va a cerrar, se les informa al resto de usuarios conectados.
