package server.dump;

import org.apache.http.HttpStatus;

import static java.util.Objects.isNull;

public class Response
{
    private static final String HTTP_VER = "HTTP/1.1";
    private static final String NEWLINE = "\r\n";
    private static final String NEWLINEx2 = NEWLINE + NEWLINE;

    private String content;
    private Integer status;

    public Response(final String content)
    {
        this(content, 200);
    }

    public Response(final String content, final Integer status)
    {
        this.content = content;
        this.status = status;
    }

    public String getContent()
    {
        return this.content;
    }

    public byte[] getBytes()
    {
        return (HTTP_VER + " " + setStatusStringFromStatusCode(status) + NEWLINEx2 + content).getBytes();
    }

    private String setStatusStringFromStatusCode(final Integer status)
    {
        if(isNull(status))
        {
            return "500 INTERNAL SERVER ERROR";
        }
        switch (status)
        {
            case HttpStatus.SC_OK:
                return "200 OK";
            case HttpStatus.SC_BAD_REQUEST:
                return "400 BAD REQUEST";
            case HttpStatus.SC_FORBIDDEN:
                return "403 FORBIDDEN";
                case HttpStatus.SC_METHOD_NOT_ALLOWED:
            return "405 METHOD NOT ALLOWED";
            case HttpStatus.SC_INTERNAL_SERVER_ERROR:
                return "500 INTERNAL SERVER ERROR";
            case HttpStatus.SC_NOT_FOUND:
            default:
                return "404 NOT FOUND";
        }
    }
}
