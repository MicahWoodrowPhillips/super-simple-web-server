package server.dump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.isNull;
import static server.util.ConstantStrings.URI;

public class Controller
{
    Logger LOGGER = LoggerFactory.getLogger(getClass());
    DateTimeFormatter format = DateTimeFormatter.BASIC_ISO_DATE;

    public Response handleGet(Request request)
    {
        Response response = new Response(Cache.peek(request.getParams()));

        if (isNull(response.getContent()) || response.getContent().length() == 0)
        {
            response = noAssociatedRecordResponse();
        }

        return response;
    }

    private Response noAssociatedRecordResponse()
    {
        return new Response("No associated record found.", 404);
    }

    public Response handlePost(Request request)
    {
        LOGGER.info("Handling POST request");
        Response response = null;

        // Invalid data
        if (isNull(request.getBody()) || isNull(request.getBody().get(URI)))
        {
            response = new Response("Invalid data", 403);
        }

        // New data
        // Existing data
        else if(Cache.push(request.getBody().get(URI), OffsetDateTime.now().format(format)))
        {
            response = new Response("", 200);
        }

        return response;
    }

    public Response handleDelete(Request request)
    {
        Response response = null;
        // Invalid data
        if (isNull(request.getBody()) || isNull(request.getBody().get(URI)))
        {
            response = new Response("Invalid data", 403);
        }

        // New data
        // Existing data
        else if(Cache.delete(request.getBody().get(URI)))
        {
            response = new Response("", 200);
        }

        else
        {
            response = new Response("", 404);
        }

        return response;
    }
}
