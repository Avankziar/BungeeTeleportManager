# BungeeTeleportManager

A Teleportplugin for Spigot and a Proxy Server(Bungee/Velocity).<br>
BTM has a <b>Wiki in [English](https://github.com/Avankziar/BungeeTeleportManager/wiki/ENG-Home) or [German](https://github.com/Avankziar/BungeeTeleportManager/wiki/GER-Home)</b>

# How to Install
To install BungeeTeleportManager (BTM), proceed as follows:
- Download the Jar file.
- Copy the jar file into the plugins folder on all servers (spigot and proxy).
- Restart all servers.
- Adjust the language on the Spigot server if required.
- Adjust the Mysql data on the Spigot server, then set Mysql.status to 'true'. Save the file.
- Restart the Spigot server.
- Assign permission, to be read and adjusted in the commands.yml, to the player groups.

# For Developer
## Integration
You can integrate BTM with the jar file and BTM.getPlugin() or you can call maven dependency:

```
<dependency>
  <groupId>me.avankziar</groupId>
  <artifactId>bungeeteleportmanager</artifactId>
  <version>VERSION</version>
</dependency>
```
Version can be found <b>[here](https://github.com/Avankziar/BungeeTeleportManager/packages/2208362)</b>!

## Usage
For Devs you can access BTM on serval options:
- InterfaceHub (IFH) has a Interface me.avankziar.ifh.spigot.teleport.Teleport which BTM access and provided a Teleport methode for you to use.
- BTM has a submechanic called AccessPermission https://github.com/Avankziar/BungeeTeleportManager/wiki/ENG-AccessPermission
