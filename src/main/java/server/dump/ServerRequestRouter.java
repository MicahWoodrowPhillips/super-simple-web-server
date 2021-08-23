package server.dump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import static java.util.Objects.nonNull;
import static server.util.ConstantStrings.*;


public class ServerRequestRouter implements Runnable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRequestRouter.class);
    private final Socket socket;
    private final Controller controller;

    public ServerRequestRouter(final Controller controller, final Socket socket)
    {
        this.controller = controller;
        this.socket = socket;
    }

    @Override
    public void run(){
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            RequestFactory requestBuilder = new RequestFactory();
            Request request = requestBuilder.createRequest(bufferedReader);

            LOGGER.info("Request created");
            Response response = null;
            if(nonNull(request))
            {
                LOGGER.info("Received a request");
                if (request.getVerb().equals(GET)) {
                    response = controller.handleGet(request);
                }
                else if (request.getVerb().equals(POST)) {
                    response = controller.handlePost(request);
                }
                else if (request.getVerb().equals(DELETE)) {
                    response = controller.handleDelete(request);
                }

                // Write response to output stream.
                if(nonNull(response))
                {
                    writeResponse(response, socket.getOutputStream());
                }
                else // Request did not have one of the three supported verbs.
                {
                    writeErrorResponse(socket.getOutputStream());
                }
                    
                socket.close();
            }
        }

        catch(Exception j)
        {
            LOGGER.info("Exception in router loop: ");
            j.printStackTrace();
        }
    }

    private void writeErrorResponse(final OutputStream outputStream)
    {
        Response response = new Response("Invalid request method", 405);
        writeResponse(response, outputStream);
    }

    private void writeResponse(final Response response, final OutputStream outputStream)
    {
        try
        {
            outputStream.write(response.getBytes());
            outputStream.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
