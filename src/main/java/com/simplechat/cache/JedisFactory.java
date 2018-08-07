package com.simplechat.cache;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.SQLException;

public class JedisFactory extends GenericObjectPoolConfig {

    @Value("${database.redis.host}")
    private static String host;

    @Value("${database.redis.port}")
    private static Integer port;

    @Value("${database.redis.timeout}")
    private static Integer timeout;

    @Value("${database.redis.password}")
    private static String password;


    private static JedisPool jedisPool;

    static  {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);

        jedisPool = new JedisPool(
                poolConfig,
                "localhost",
                6379,
                5000
              //  ""
        );
    }

//    public JedisPool getJedisPool() {
//        return jedisPool;
//    }


    private JedisFactory() {}

    public static Jedis getConnection() {
        return jedisPool.getResource();
    }
}
