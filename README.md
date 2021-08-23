# A Super Simple Web Server
Simple java version of a web server, trying to steer clear of cheating (i.e., Spring).

This very simple server is just to show a fast implementation of a simple web server in Java.  It makes several assumptions when being run:
1. You have a Redis server running on the default port (6379) and it is local.
3. You are running this locally, perhaps from an IDE.
4. Your requests you are sending this server are using the verbs GET, POST, and DELETE.  Any others reply with a 405.
5. Your POST requests are used to both Create and Update data in the Redis instance. Also assumes JSON body.
6. Your GET requests are there to retrieve data from the Redis instance.
7. Your DELETE requests are there to retrieve data from the Redis instance. Also assumes JSON body.

The sample Postman collection illustrates the three accepted request methods, along with a PUT to demonstrate unsupported behavior.
