package com.trading.brokergateway.Methods;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import com.trading.brokergateway.Entity.Order;
import redis.clients.jedis.Jedis;

public class TTE {

    public static void main(String[] args) {
        Jedis jedis = null;
        try {
            jedis = new Jedis("localhost");
            Club clb = new Club();
            byte[] byt = SerializeUtil.serialize(clb);
            jedis.set("clb".getBytes(),byt);
            byte[] getByte = jedis.get("clb".getBytes());
            Object getObject = unserizlize(getByte);
            Club cb = (Club) getObject;
            System.out.println(cb.get());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    private static byte[] serialize(Object object) {
        ObjectOutputStream objectOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            byte[] getByte = byteArrayOutputStream.toByteArray();
            return getByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object unserizlize(byte[] binaryByte) {
        ObjectInputStream objectInputStream = null;
        ByteArrayInputStream byteArrayInputStream = null;
        byteArrayInputStream = new ByteArrayInputStream(binaryByte);
        try {
            objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object obj = objectInputStream.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}