package server.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisDataException;

import java.util.logging.Logger;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Cache
{
    private static Jedis jedis;

    private static Jedis getRedisClient()
    {
        if (jedis == null)
        {
            jedis = new Jedis();
        }

        return jedis;
    }

    public static boolean push(String k, String v)
    {
        if(isNull(k) || k.contains("INVALID"))
        {
            return false;
        }
        getRedisClient().set(k,v);
        return true;
    }

    /**
     * Returns true if entry at key k removed, false if none existed with that key.
     * @param k
     * @return
     */
    public static boolean delete(String k)
    {
        if(nonNull(getRedisClient().get(k)))
        {
            getRedisClient().del(k);
            return true;
        }

        return false;
    }

    public static String peek(String k)
    {
        try
        {
            return getRedisClient().get(k);
        }
        catch(JedisDataException e)
        {
            Logger.getAnonymousLogger().info("No key \"" + k + "\" found.");
            return null;
        }
    }
}
