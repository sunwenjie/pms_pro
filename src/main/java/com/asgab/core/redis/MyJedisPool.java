package com.asgab.core.redis;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import redis.clients.jedis.Jedis;

import redis.clients.jedis.JedisPool;

import redis.clients.jedis.JedisPoolConfig;

public class MyJedisPool {

  private static JedisPool pool;

  // 静态代码初始化池配置

  /*
   * static {
   * 
   * try {
   * 
   * Properties props = new Properties();
   * 
   * props.load(MyJedisPool.class.getClassLoader().getResourceAsStream("redis.properties"));
   * 
   * // 创建jedis池配置实例
   * 
   * JedisPoolConfig config = new JedisPoolConfig();
   * 
   * // 设置池配置项值
   * 
   * 
   * config.setMaxTotal(Integer.valueOf(props.getProperty("jedis.pool.maxActive")));
   * config.setMaxIdle(Integer.valueOf(props.getProperty("jedis.pool.maxIdle")));
   * config.setMaxWaitMillis(Long.valueOf(props.getProperty("jedis.pool.maxWait")));
   * 
   * 
   * config.setTestOnBorrow(Boolean.valueOf(props.getProperty("jedis.pool.testOnBorrow")));
   * 
   * config.setTestOnReturn(Boolean.valueOf(props.getProperty("jedis.pool.testOnReturn")));
   * 
   * // 根据配置实例化jedis池
   * 
   * pool = new JedisPool(config, props.getProperty("redis.ip"),
   * Integer.valueOf(props.getProperty("redis.port")));
   * 
   * } catch (IOException e) {
   * 
   * e.printStackTrace();
   * 
   * } }
   */


  /** 获得jedis对象 */

  public static Jedis getJedisObject() {

    return pool.getResource();

  }

  /** 归还jedis对象 */

  public static void recycleJedisOjbect(Jedis jedis) {

    pool.returnResource(jedis);

  }

  /**
   * 
   * 测试jedis池方法
   * 
   */

  public static void main(String[] args) {

    Jedis jedis = getJedisObject();// 获得jedis实例

    // 获取jedis实例后可以对redis服务进行一系列的操作

    jedis.set("name", "zhuxun");

    System.out.println(jedis.get("name"));

    jedis.del("name");

    System.out.println(jedis.exists("name"));

    jedis.lpush("tutorial-list", "Redis");
    jedis.lpush("tutorial-list", "Mongodb");
    jedis.lpush("tutorial-list", "Mysql");
    // 获取存储的数据并输出
    List<String> list = jedis.lrange("tutorial-list", 0, 100);
    for (int i = 0; i < list.size(); i++) {
      System.out.println("Stored string in redis:: " + list.get(i));
    }


    jedis.del("tutorial-list");

    recycleJedisOjbect(jedis); // 将 获取的jedis实例对象还回迟中

  }

}
