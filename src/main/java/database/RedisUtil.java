package database;

import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
	
	// https://www.cnblogs.com/whcwkw1314/p/8922031.html
	
	private Jedis jedis = null;
	private JedisPool jedisPool = null;
	private Boolean single = null;
	
	public RedisUtil(String ip, int port){
		this.jedis = new Jedis(ip, port);
		this.single = true;
	}
	
	/**	redis配置文件
		redis.maxTotal=100
		redis.maxIdle=30
		redis.minIdle=10
		redis.url=192.168.202.200 
		redis.port=6379
	 */
	public RedisUtil(Properties prop){
		//创建连接池配置对象
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //最大连接数
        poolConfig.setMaxTotal(Integer.parseInt(prop.get("redis.maxTotal").toString()));
        //最大空闲连接数
        poolConfig.setMaxIdle(Integer.parseInt(prop.get("redis.maxIdle").toString()));
        //最小空闲连接数
        poolConfig.setMinIdle(Integer.parseInt(prop.get("redis.minIdle").toString()));
        //创建连接池
        this.jedisPool = new JedisPool(poolConfig, prop.get("redis.url").toString(), Integer.parseInt(prop.get("redis.port").toString()));
        this.single = false;
	}
	
	public Jedis getJedis(){
		if(this.single)
			return this.jedis;
		else
			return this.jedisPool.getResource();
	}
	
	public void close(){
		if(this.single)
			this.jedis.close();
		else
			this.jedisPool.close();
	}

}
