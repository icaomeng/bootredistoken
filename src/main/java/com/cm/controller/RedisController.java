package com.cm.controller;
 
import java.io.IOException;
import java.util.List;

import com.cm.entity.User;
import com.cm.mapper.UserMapper;
import com.cm.service.UserService;
import com.cm.utils.AjaxObj;
import com.cm.utils.CommUtils;
import com.cm.utils.JsonUtils;
import com.cm.utils.ReturnValCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 
@RestController
@RequestMapping("redis")
public class RedisController {

	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private UserService userService;
	@Autowired
	private RedisTemplate redisTemplate1;
	
	/**
	 * 向redis存入单个字符串
	 * @return
	 */
	@GetMapping("/test")
	public  String redisJsonResult() {
//		System.out.println(password);
		redisTemplate.opsForValue().set("user-cache", "wulongwei");
		return redisTemplate.opsForValue().get("user-cache");
	}

	/**
	 * 向redis存入一个json对象
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/getUser")
	public User saveUser(User user) throws IOException {
		user.setUuid(CommUtils.getUUID());
		userService.insertUser(user);
		redisTemplate.opsForValue().set("user", JsonUtils.objectToJson(user));
		return JsonUtils.jsonToPojo(redisTemplate.opsForValue().get("user"), User.class);
	}

	// 获取redis集合
	@GetMapping("/listUserRedis")
	public List<User> listUserRedis() throws IOException {
		redisTemplate1.delete("listuser");
		List<User> users = userService.listUser();
		redisTemplate1.opsForList().rightPushAll("listuser", JsonUtils.objectToJson(users));
		List<User> list = redisTemplate1.opsForList().range("listuser", 0, -1);
		return list;
	}

	// 先判断redis是否存在此key
	@GetMapping("/saveList")
	public AjaxObj saveList() throws IOException {
		// 删除key，防止key重复
        // redisTemplate1.delete("listuser");
		Boolean key = redisTemplate.hasKey("listuser");
		System.err.println(key);
		if(key){
			System.err.println("从redis获取");
			// 如果redis有key，那么就从redis获取数据，提高效率
			// 从下标0获取，-1为获取所有数据
			List<User> list = redisTemplate1.opsForList().range("listuser", 0, -1);
			return new AjaxObj(ReturnValCode.RTN_VAL_CODE_SUCCESS, "从redis获取",list);
		}else{
			System.err.println("从数据库获取");
			// 不存在就从数据库获取数据
			List<User> users = userService.listUser();
			// 存入redis，方便下次获取
			// 按照先进先出的顺序来添加
			redisTemplate1.opsForList().rightPushAll("listuser", users);
			return new AjaxObj(ReturnValCode.RTN_VAL_CODE_SUCCESS, "从数据库获取",users);
		}
	}

	// 获取集合
	@GetMapping("/listUser")
	public List<User> listUser() throws IOException {
		List<User> users = userService.listUser();
		return users;
	}

	// 获取集合使用注解
	@GetMapping("/listUseraite")
	public AjaxObj listUseraite(){
		List<User> users = userService.listUserRedis();
		return new AjaxObj(ReturnValCode.RTN_VAL_CODE_SUCCESS, "请求成功",users);
	}

	// 获取集合使用注解，根据uuid
	@GetMapping("/listUserRedisUuid")
	public AjaxObj listUserRedisUuid(String uuid){
		List<User> users = userService.listUserRedisUuid(uuid);
		return new AjaxObj(ReturnValCode.RTN_VAL_CODE_SUCCESS, "请求成功",users);
	}

	// 删除数据并更新缓存
	@GetMapping("/deleteByUuid")
	public AjaxObj deleteByUuid(String uuid){
		userService.deleteUser(uuid);
		return new AjaxObj(ReturnValCode.RTN_VAL_CODE_SUCCESS, "请求成功");
	}

	// 修改数据并更新缓存
	@GetMapping("/updateByUuid")
	public AjaxObj updateByUuid(User user){
		userService.updateUser(user);
		return new AjaxObj(ReturnValCode.RTN_VAL_CODE_SUCCESS, "请求成功");
	}

	// 新增数据并更新缓存
	@GetMapping("/forSave")
	public AjaxObj forSave(){
		userService.forSave();
		return new AjaxObj(ReturnValCode.RTN_VAL_CODE_SUCCESS, "请求成功");
	}










}