package server.dump;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Properties;

import static java.util.Objects.isNull;

public class ServerRunner
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);
    private static final Integer DEFAULT_PORT = 8080;
    private static final String CONFIG = "simple.properties";
    private static final String NAME_CONFIG = "server.name";
    private static final String PORT = "server.port";

    public static void main(String [] args)
    {
        LOGGER.info("Info level logging is on.");

        Properties properties = createProperties();
        
        InetAddress inetAddress = bindAddress(properties);
        if(isNull(inetAddress))
        {
            LOGGER.info("InetAddress null, cannot attempt to start server.");
            return;
        }

        // Start server thread.
        ServerRequestRouter server = null;
        while (true)
        {
            try(ServerSocket serverSocket = new ServerSocket(Integer.parseInt(properties.getProperty(PORT)), 50, inetAddress))
            {
                server = new ServerRequestRouter(new Controller(), serverSocket.accept());
                Thread thread = new Thread(server);
                thread.start();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static InetAddress bindAddress(Properties properties)
    {
        try
        {
            String hostname = properties.getProperty(NAME_CONFIG);
            return InetAddress.getByName(hostname);
        }
        catch (UnknownHostException e)
        {
            return null;
        }
    }

    private static Properties createProperties()
    {
        try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(CONFIG))
        {
            Properties properties = new Properties();
            properties.load(inputStream);

            properties.keySet()
                    .forEach(propKey ->
                    {
                        String value = properties.getProperty((String)propKey);
                        if (value != null) {
                            properties.put((String)propKey, value);
                        }
                    });

            return properties;
        }
        catch (IOException e)
        {
            System.out.println("Error occurred while reading configuration: " + e.getMessage());
            return null;
        }

    }
}
