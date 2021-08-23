package server.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Request
{
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private String params;
    private String contentType;
    private String contentLength;
    private String verb = null;
    private Map<String, String> body;


    public String getVerb() {
        return this.verb;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public void setContentType(final String contentType)
    {
        this.contentType = contentType;
    }

    public void setContentLength(final String contentLength)
    {
        this.contentLength = contentLength;
    }

    public void setVerb(final String verb)
    {
        this.verb = verb;
    }

    public void setBody(final Map<String, String> body)
    {
        LOGGER.info("Body input");
        this.body = body;
    }

    public Map<String, String> getBody()
    {
        return this.body;
    }
}
