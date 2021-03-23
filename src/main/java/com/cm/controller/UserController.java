package com.cm.controller;
 
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.cm.entity.dto.UserDto;
import com.cm.service.UserService;
import com.cm.utils.ResponseBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 把token存到redis中，每次请求需放入token，不然就报错
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 登陆成功返回token给前台，之后每调一个接口都需要校验token之后有效
	 */
	@GetMapping("/login")
	public ResponseBase UserLogin(UserDto userDto, HttpServletRequest request) {
		// token存到redis
//		return userService.UserLogin(userDto.getUserName(), userDto.getPassword());
		// token存到数据库
		return userService.userLogin(userDto.getUserName(), userDto.getPassword());
	}
	
	/**
	 * 查看用户的基本信息
	 */
	@GetMapping("/getUserInfo")
	public ResponseBase getUserInfo(String id) {
		return userService.getUserInfo(id);
	}

	/**
	 * 删除token
	 */
	@GetMapping("/delete")
	public void delete() {
		userService.deleteToken();
	}

}