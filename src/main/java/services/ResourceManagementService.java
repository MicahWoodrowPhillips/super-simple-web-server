package services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.cache.Cache;
import server.model.Request;
import server.model.Response;

import java.time.format.DateTimeFormatter;
import java.util.Map;

import static java.util.Objects.nonNull;
import static server.util.ConstantStrings.VERSION;

public class ResourceManagementService
{
    Logger LOGGER = LoggerFactory.getLogger(getClass());
    DateTimeFormatter dateTimeFormatter;


    public ResourceManagementService(DateTimeFormatter format)
    {
        this.dateTimeFormatter = format;
    }

    public Response getUri(Request request)
    {
        //
        String keyString = Cache.peek(request.getResource());
        Map<String, String> params = request.getParams();
        if(nonNull(params))
        {
            keyString += params.get(VERSION);
        }

        String s = Cache.peek(keyString);

        if (nonNull(s) && !s.isEmpty())
        {
            return new Response(s, 200);
        }

        else
        {
            return new Response("No such resource", 404);
        }

    }
}
