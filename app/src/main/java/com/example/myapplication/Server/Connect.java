package com.example.myapplication.Server;

import redis.clients.jedis.Jedis;

public class Connect
{
    Jedis jedis;
    public Connect()
    {
        jedis = new Jedis("192.168.0.164", 6379);
        jedis.auth("123456");
        jedis.append("a", "1");
    }

}
