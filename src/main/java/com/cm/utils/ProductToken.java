package com.cm.utils;
 
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
/**
 * 生产token,如果同一用户重复登录，token的时效重置
 * 生成token类，登录成功后调用，如果同一用户重复登录，token的时效重置
 */
@Service
public class ProductToken {

	@Autowired
	private StringRedisTemplate redisTemplate;
 
	/**
	 * 生成有时效的token
	 * 之后调其他接口需要校验adminId和token值
	 * @param key
	 * @param value
	 */
	public Map<String, String> productToken(String key, String value) {
		Map<String, String> infoMap = new HashMap<String, String>();
		if(redisTemplate.opsForValue().get(key) == null) {
			//将登陆的信息保存如redis
			redisTemplate.opsForValue().set(key, value);
			infoMap.put(key, value);
		}
		//设置token有效的时间
		redisTemplate.expire(key, 3000, TimeUnit.SECONDS);
		return infoMap;
	}
	

 
}