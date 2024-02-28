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


## Update - New work TBA
Just to get myself working with some demonstrable Java code again I'm going to revisit this and make an image store out of it, complete 
with a front-end portal, docker container for deployment, SQL (maybe Cassandra) db for storing images as blobs, sign-in, account management,
image view, and request sanitization.  Who knows what else, just in an idea stage right now.  Again, this is meant to just be a simple and 
easily readable sample service just showcasing some CRUD operations on an incredibly bare-bones Java interface.  However, to make things
interesting and more modern I might update it to be using Spring in some capacity.  I really want to use Webflux more, so maybe I'll do 
something with that.  Who knows!
