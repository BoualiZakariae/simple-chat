package com.simplechat.repository;

import com.simplechat.cache.JedisFactory;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
public class CacheRepositoryRedis implements CacheRepository{

    @Override
    public void addAuthKeyForUser(String userId, String authKey) {

        try (Jedis jedis = JedisFactory.getConnection()) {

            jedis.set(authKey, userId);

        } catch (Exception e) {

        }
    }

    @Override
    public String getUserIdByAuthKey(String authKey) {

        try (Jedis jedis = JedisFactory.getConnection()) {

            return jedis.get(authKey);

        } catch (Exception e) {

        }

        return null;
    }

    @Override
    public void storeActivationCodeForMobile(String mobile, String activationCode) {

        try (Jedis jedis = JedisFactory.getConnection()) {

            jedis.hset(mobile, String.valueOf(activationCode), "");

            // expire activation code after specific time
            jedis.expire(mobile, 10*60);
        } catch (Exception e) {

        }
    }

    @Override
    public String getInfoByActivationCodeAndMobile(String mobile, String code) {

        try (Jedis jedis = JedisFactory.getConnection()) {

            return jedis.hget(mobile, code);
        } catch (Exception e) {

        }
        return null;
    }
}
