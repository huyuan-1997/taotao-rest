package com.taotao.rest.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.rest.dao.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 对缓存数据库进行操作    CRUD
 * @author 胡园
 *
 */
public class JedisClientSingle implements JedisClient{
	@Autowired
	private JedisPool jedisPool;
	/**
	 * 通过key获取值
	 */
	public String get(String key){
		//获得连接
		Jedis jedis = jedisPool.getResource();
		//执行操作
		String string = jedis.get(key);
		//关闭连接
		jedis.close();
		return string;
	}
	//存入字符串
	@Override
	public String set(String key, String value) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		String string = jedis.set(key, value);
		jedis.close();
		return string;
	}
	//取出hash类型的数据
	@Override
	public String hget(String hkey, String key) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		String string = jedis.hget(hkey, key);
		jedis.close();
		return string;
	}
	@Override
	public long hset(String hkey, String key, String value) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hset(hkey, key, value);
		jedis.close();
		return result;
	}
	//指定递增
	@Override
	public long incr(String key) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.incr(key);
		jedis.close();
		return result;
	}
	//设置键的存活时间
	@Override
	public long expire(String key, int second) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.expire(key,second);
		jedis.close();
		return result;
	}
	//销毁指定key
	@Override
	public long ttl(String key) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.ttl(key);
		jedis.close();
		return result;
	}
	//删除key
	@Override
	public long del(String key) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.del(key);
		jedis.close();
		return result;
	}
	//删除hash类型的数据
	@Override
	public long hdel(String hkey, String key) {
		// TODO Auto-generated method stub
		Jedis jedis = jedisPool.getResource();
		Long result = jedis.hdel(hkey, key);
		jedis.close();
		return result;
	}
	
}
