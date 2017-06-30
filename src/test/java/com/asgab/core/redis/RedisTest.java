package com.asgab.core.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisTest {

  public static void main(String[] args) {

    Jedis jedis = MyJedisPool.getJedisObject();
    jedis.set("name", "zhuxun");

    System.out.println(jedis.get("name"));

    jedis.del("name");


    /*
     * // 连接本地的 Redis 服务 Jedis jedis = new Jedis("192.168.116.214"); System.out.println(
     * "Connection to server sucessfully"); // 查看服务是否运行 System.out.println("Server is running: " +
     * jedis.ping());
     * 
     * jedis.set("w3ckey", "Redis tutorial"); // 获取存储的数据并输出 System.out.println(
     * "Stored string in redis:: " + jedis.get("w3ckey"));
     * 
     * // 存储数据到列表中 jedis.lpush("tutorial-list", "Redis"); jedis.lpush("tutorial-list", "Mongodb");
     * jedis.lpush("tutorial-list", "Mysql"); // 获取存储的数据并输出 List<String> list =
     * jedis.lrange("tutorial-list", 0, 5); for (int i = 0; i < list.size(); i++) {
     * System.out.println("Stored string in redis:: " + list.get(i)); }
     * 
     * jedis.close();
     */
  }
}
