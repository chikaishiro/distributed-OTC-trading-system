package com.trading.brokergateway.Util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @Author: huangwenjun
 * @Description:
 * @Date: Created in 14:10  2018/5/22
 **/
public class LockTest {

    public static void main(String[] args) {
        for (int i = 1; i < 10; i ++) {
            new TestLock().start();
        }
    }

    static class TestLock extends Thread {

        static StoreUtilLock locker = new StoreUtilLock();

        JedisPool jedisPool = new JedisPool();

        @Override
        public void run() {
            Jedis jedis = jedisPool.getResource();
            locker.lock(jedis, "test1", "qwerqwer");

            // TODO 模拟业务
            System.out.println(Thread.currentThread().getName() + " get lock.....");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " release lock.....");

            locker.release(jedis, "test1", "qwerqwer");
        }
    }
}
