package com.trading.brokergateway.Util;

import redis.clients.jedis.Jedis;

public class StoreUtilLock {

    /**
     * 默认 非公平锁
     */
    private static final Boolean DEFALUT_FAIR = false;

    /**
     * 过期时间 默认 10 秒, 太短会导致锁不住， 如果业务无法在指定过期时间内 完成， 则必须加长过期时间
     */
    private static final Integer DEFAULT_EXPIRE_TIME = 10000;

    private Boolean isFair;

    private Integer expireTime;

    public StoreUtilLock() {
        isFair = DEFALUT_FAIR;
        expireTime = DEFAULT_EXPIRE_TIME;
    }

    public StoreUtilLock(boolean isFair, Integer expireTime) {
        isFair = isFair;
        expireTime = expireTime;
    }


    public void lock(Jedis jedis, String key, String uuid) {
        if (isFair) {
            fairLock(jedis, key, uuid);
        } else {
            unfairLock(jedis, key, uuid);
        }
    }


    public void fairLock(Jedis jedis, String key, String uuid) {
        // 通过一个队列维护， 参照 AQS 实现
    }


    public void release(Jedis jedis, String key, String uuid) {
        try {
            while (true) {
                boolean released = RedisTool.releaseDistributedLock(jedis, key, uuid);

                if (released) {
                    break;
                }
            }
        } finally {
            jedis.close();
        }
    }

    public void unfairLock(Jedis jedis, String key, String uuid) {
        while (true) {
            boolean locked = RedisTool.tryGetDistributedLock(jedis, key, uuid, expireTime);

            if (locked) {
                break;
            }
        }
    }
}

