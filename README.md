# [F19] Distributed Systems - Course Project
The **Distributed File System** has been written as a project for Innopolis University Distributed Systems course, Fall 2019.
## Installation
### Naming server
1. Run an AWS instance and install docker-compose on it.
1. Clone [docker compose file](https://raw.githubusercontent.com/Shakirovrrr/Innopolis-DS-F19-Project/master/ServerNaming/docker-compose.yml) from GitHub repository.
2. `docker-compose up`


### Storage servers
1. Run required number of instances and install docker-compose on them. 
1. Clone [docker compose file](https://raw.githubusercontent.com/Shakirovrrr/Innopolis-DS-F19-Project/master/ServerStorage/docker-compose.yml) from GitHub repository.
2. In docker-compose.yml file in `command` line specify private address of naming server and private own address of storage server correspondingly.
3. `docker-compose up`

### CLient
#### Docker way
1. Download [docker-compose.yml for Client](https://github.com/Shakirovrrr/Innopolis-DS-F19-Project/suites/338581469/artifacts/521281)
2. Run `docker-compose up -d` in directory of download
3. Run `docker ps` to get the container ID
4. Run `docker exec -ti <container ID> /bin/bash`
5. Inside the container run `./Client <naming server IP>`
6. Here you go.

#### JAR way
1. [Download archive](https://github.com/Shakirovrrr/Innopolis-DS-F19-Project/suites/338581469/artifacts/521281)
2. Extract JAR
3. Run `java -jar Client.jar <naming server IP>`
4. Here you go.

## Specification
### Functionality
File system's users should be able to perform certain operations on files and directories.
```On files: upload, download, create, remove copy, move, get info.```
```On directories: create, remove, list.```

All the **files are meant to be replicated** on multiple storage servers such that the **DFS become fault-tolerant**: when a storage node fails or offline, the data is accessable on other storages that keep the file replica.

### High-level System Diagram
The system consists of Client, Naming Server and Multiple Storage Servers.

![](https://i.imgur.com/2A1a0Md.png)

Naming Server keeps the filesTree - structure of directories and files, metadata of files. It assigns IP to each file by which it can be accessed by Storages or Client. 
Also Naming Server keeps IPs of each running Storage Server.

Storage Server keeps files. From time to time (each 5 seconds) it pings the Naming Server with a heartbeat. If there is no a heartbeat within 10 seconds, Naming Server removes the Storage from runnig Storages list.

Client is a console application that allows to perform a set of operations on files and directories.

### Client commands

0. `init` - clear all
1. `touch <new_file_path> `- create empty file
2. `get <remote path> (local path)` - download file
3. `put <local path> (remote path)` - upload file
4. `rm <path>` - delete file
5. `info <path>` - file info
6. `cp <from> <to>` - copy file
7. `mv <from> <to>` - move file
8. `cd <path>` - open directory
9. `ls (path)` - read directory
10. `mkdir (path)` - create directory

11. `help` - list available commands
12. `setdd <path>` - set a directory for downloads
13. `getdd` - get current directory for downloads

`<arg>` - required argument, `(arg)` - optional argument
### Storage-Naming-Client commands implementation
#### init
![](https://i.imgur.com/ri4DkOA.png)
```
Possible Acknowledgements: OK
```
#### touch
![](https://i.imgur.com/vGojcFB.png)
```
Possible Acknowledgements: OK, INCORRECT_NAME, FILE_OR_DIRECTORY_DOES_NOT_EXIST, FILE_OR_DIRECTORY_ALREADY_EXISTS
```
#### get
![](https://i.imgur.com/FUs6FcE.png)
- storageIp - where the Client should download the file
-  fileId - globally unique file ID 
```
Possible Response codes: OK, TOUCHED,INCORRECT_NAME, FILE_OR_DIRECTORY_DOES_NOT_EXIST, NO_NODES_AVAILABLE
```
#### put
![](https://i.imgur.com/P4J3jcJ.png)
- storageIP - where the Client should download the file
- fileId - globally unique file ID 
- [replicasIPs] - IPs of Storages to which the file should be replicated

```At step(4) after uploading the file to primary storage, it aknowledges Client and Naming Server. Right after that, Storage starts sending file to replicated storages - after finishing of uploading each notifies the Naming Server```
```
Possible Response codes: OK, NO_NODES_AVAILABLE, INCORRECT_NAME, FILE_OR_DIRECTORY_DOES_NOT_EXIST, FILE_OR_DIRECTORY_ALREADY_EXISTS 
```
#### rm
![](https://i.imgur.com/H9odBSM.png)
```
Possible Acknowledgements: OK,CONFIRMATION_REQUIRED, FILE_OR_DIRECTORY_DOES_NOT_EXIST 
```
```Each Storage pings Naming server with a heartbit with period of 5 seconds. Every 6th heartbeat is fetchFile request. As a response Naming server sends the list of fileIPs that the storage should keep (thus, all the others excess files are removed). Also, Naming server sends a list of tuples {fileIP, storageIP} - certain files from corresponding storages should be requested by the Storage to be downloaded (handles cases of replication failure while uploading from client). ```
#### info
![](https://i.imgur.com/bCbi23q.png)
```
Possible Acknowledgements: OK, INCORRECT_NAME, FILE_OR_DIRECTORY_DOES_NOT_EXIST
```
#### cp
![](https://i.imgur.com/808bmna.png)

```Naming server keeps structures of unique fileIDs and Storage paths by which the file could be reached. Thus, when copying, Naming Server just adds a new path to the corresponding file list.```
```
Possible Acknowledgements: OK, INCORRECT_NAME, FILE_OR_DIRECTORY_DOES_NOT_EXIST, FILE_OR_DIRECTORY_ALREADY_EXISTS 
```
#### mv
![](https://i.imgur.com/MgshiSy.png)

```Naming server keeps structures of unique fileIDs and Storage paths by which the file could be reached. Thus, when moving, Naming Serverat first adds a new path to the corresponding file list, and then deletes the fromPath from the corresponding file list.```
```
Possible Acknowledgements: OK, INCORRECT_NAME, FILE_OR_DIRECTORY_DOES_NOT_EXIST, FILE_OR_DIRECTORY_ALREADY_EXISTS
```
#### ls
![](https://i.imgur.com/M8oy1zh.png)

```
Possible Acknowledgements: OK, FILE_OR_DIRECTORY_DOES_NOT_EXIST
```
#### cd
![](https://i.imgur.com/YPLJO2m.png)

```If the requested path exists in NAming Server fileTree, it notifies Client with either success or fail. Current directory is displayed in console.```
```
Possible Acknowledgements: OK, FILE_OR_DIRECTORY_DOES_NOT_EXIST
```
#### mkdir
![](https://i.imgur.com/MiEuXDU.png)

```
Possible Acknowledgements: OK, FILE_OR_DIRECTORY_DOES_NOT_EXIST, FILE_OR_DIRECTORY_ALREADY_EXISTS
```
### Communication protocols
In our project custom protocols based on Socket connection were used being used. Aknowledgements are being sent as Objects within ObjectOutputStream. Those objects contains Response Codes and some optional field depending in the type of command.

While file downloading/uploading (get/put) Client and Storage Server use the following protocol alike TCP:

  ![](https://i.imgur.com/rLzyqar.png)     ![](https://i.imgur.com/iUZRiwJ.png) 


#### Response Codes

| Code |Meaning | Commands|
| -------- | -------- |-----|
| OK  |    successful execution  | all
|  NO_NODES_AVAILABLE  |    no nodes available for uploading a file  | put, get
| INCORRECT_NAME   | requested path is "/"     |put, info, touch, get, cp, mv|
| FILE_OR_DIRECTORY_DOES_NOT_EXIST  |   no requested path exists   |put, info, get, cp, mv, cd, ls, mkdir|
| FILE_OR_DIRECTORY_ALREADY_EXISTS  |   requested(for creation) path is already exist   |put, cp, mv, mkdir, rm|
|  TOUCHED |  everything is OK, but the requested file has no content     |get|
| CONFIRMATION_REQUIRED   | procedure requires confirmation for continuing    |rm|

## Implementation details
### Technologies
* Java
* Gradle
* Docker





### Continuous integration

CI is done for the project! 
```
Push to github -> auto gradle build -> push to dockerhub
```

![](https://i.imgur.com/Z659f9P.png)


## Team
* Elena Lukyanchikova, SE-17-01 -- (client, documentation)
* Rim Rakhimov, SB --  (naming server, deployment)
* Ruslan Shakirov, SE-17-01 --  (storage server, deployment)
