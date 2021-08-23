package server.dump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import static server.util.ConstantStrings.*;

import static java.util.Objects.isNull;

public class RequestFactory
{
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public Request createRequest(final BufferedReader bufferedReader) throws UnsupportedOperationException
    {
        Map<String, String> headers = new HashMap<>();
        Request request = null;

        try
        {
            // parse the first line.
            String header = bufferedReader.readLine();
            StringTokenizer tokenizer = new StringTokenizer(header);
            headers.put(METHOD, tokenizer.nextToken().toUpperCase());
            headers.put(RESOURCE, tokenizer.nextToken().replaceAll("/", ""));
            headers.put(PROTOCOL, tokenizer.nextToken());

            // finish grabbing headers
            headers = parseRemainingHeaders(bufferedReader, headers);

            LOGGER.info("Creating Request");
            request = new Request();
            request.setVerb(headers.get(METHOD));
            request.setContentLength(headers.get(CONTENT_LENGTH));
            request.setContentType(headers.get(CONTENT_TYPE));
            request.setParams(headers.get(RESOURCE).trim().replaceAll("/", ""));

            // grab body if it could have one
            if(!request.getVerb().equals(GET))
            {
                LOGGER.info("Creating Request Body");
                request.setBody(parseRequestBody(bufferedReader));
            }

            if(isNull(request))
            {
                LOGGER.info("Request is null");
                throw new UnsupportedOperationException();
            }
        }
        catch(IOException e)
        {
            e.getStackTrace();
        }

        return request;
    }

    private Map<String, String> parseRemainingHeaders(final BufferedReader bufferedReader, final Map<String, String> headers) throws IOException
    {
        String line;
        while ((line = bufferedReader.readLine()) != null && !line.isEmpty())
        {
            int index = line.indexOf(':');
            if (index > 0)
            {
                headers.put(line.substring(0, index).toLowerCase(), line.substring(index + 1).trim());
            }
        }

        return headers;
    }

    private Map<String, String> parseRequestBody(BufferedReader bufferedReader) throws IOException
    {
        Map<String, String> body = new HashMap<>();

        if(!bufferedReader.ready())
        {
            throw new IOException("No message body!");
        }

        String line = bufferedReader.readLine();
        while (line != null)
        {
            if(line.contains("{") || line.contains("}"))
            {
                line = bufferedReader.readLine();
                continue;
            }
            int index = line.indexOf(':');
            if (index > 0)
            {
                body.put(line.substring(0, index).trim().replaceAll("\"", "").toUpperCase(),
                        line.substring(index + 1).trim().replaceAll("\"", ""));
            }
            if (!line.contains(",") || line.contains("}") || line.contains("\u0000"))
            {
                line = null;
            }
            else
            {
                line = bufferedReader.readLine();
            }
        }
        return body;
    }
}
