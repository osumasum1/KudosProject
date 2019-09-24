# KudosProject

## Diagram

![Diagram](https://github.com/osumasum1/KudosProject/blob/master/DiagramKudos.png)

## Kudos Service

Kudos service was developed with DropWizard and Java. It uses MongoDB.
The following end-points were developed fot this service.
* POST /kudos/createkudo
* GET /kudos
* GET /kudos/simpleKudos
* GET /kudos/destino/{id}
* DELETE /kudos/{id}
* DELETE /kudos/user/{id}

### RabbitMQ

* Messaging - pattern reply-correlation pattern was used to send the kudos of the user to Users service. The server is located in Kudos service.
* Point-to-point pattern used to delete all kudos of a user that was deleted. Receiver is located in Kudos.
* Point-to-point pattern used to request the calculation of kudos of a user. Sender is located in Kudos.

### InfluxDB

InfluxDB was used to store a log with the resources that were called by the user. 


## Users Service

Users service was developed with DropWizard and Java. It uses MYSQL DB.
The following end-points were developed fot this service.
* GET /users/{id}
* GET /users     2 optional query parameters (firstname, nickname)
* GET /users/simple
* POST /users
* DELETE /users/{id}

### RabbitMQ

* Messaging - pattern reply-correlation pattern was used to receive all kudos of a user from Kudos service. The client is located in users service.
* Point-to-point pattern used to delete all kudos of a user that was deleted. Sender is located in Users.

### InfluxDB

InfluxDB was used to store a log with the resources that were called by the user. 


## Stats Service

Stats service was developed with DropWizard and Java. It uses MYSQL DB and Mongo. These DBs are the same that users and kudos services are usinng.
The following end-points were developed fot this service.
* GET /stats/kudos/{id}
* PUT /stats

### RabbitMQ

* Point-to-point pattern used to calculated the kudos of a user. Receiver is located in Stats.

### InfluxDB

InfluxDB was used to store a log with the resources that were called by the user. 
