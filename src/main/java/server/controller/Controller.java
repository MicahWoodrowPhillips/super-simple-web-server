package server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.cache.Cache;
import server.model.Request;
import server.model.Response;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.Objects.isNull;
import static server.util.ConstantStrings.*;

public class Controller
{
    Logger LOGGER = LoggerFactory.getLogger(getClass());
    DateTimeFormatter format = DateTimeFormatter.ISO_INSTANT;

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

        // New and Existing data
        else if(Cache.push(request.getBody().get(URI), createEntry(request)))
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

    private String createEntry(Request request)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        stringBuffer.append(URI + "-name:");
        stringBuffer.append(request.getBody().get(URI));
        stringBuffer.append("," + CONTENT_TYPE + ":");
        stringBuffer.append(request.getContentType());
        stringBuffer.append("," + CONTENT_LENGTH + ":");
        stringBuffer.append(request.getContentLength());
        stringBuffer.append(",Full-body:");
        stringBuffer.append(bodyToJson(request.getBody()));
        stringBuffer.append(",Timestamp:");
        stringBuffer.append(OffsetDateTime.now().format(format));
        stringBuffer.append("}");

        return stringBuffer.toString();
    }

    private String bodyToJson(Map<String, String> body)
    {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{");
        body.forEach((k,v) -> stringBuffer.append(k + ":" + v + ","));
        stringBuffer.setLength(stringBuffer.length()-1);
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}
