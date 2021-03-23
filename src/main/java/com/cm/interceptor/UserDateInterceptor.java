package com.cm.interceptor;

import com.cm.entity.dto.TokenInfo;
import com.cm.mapper.TokenMapper;
import com.cm.utils.CommUtils;
import com.cm.utils.JsonUtils;
import com.cm.utils.ResponseBase;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * redis中获取token判断
 */
public class UserDateInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenMapper tokenMapper;


	/**
	 * 在请求处理之前进行调用（Controller方法调用之前）
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {

		// 检查用户传递的 token是否合法
		TokenInfo tokenInfo = this.getUserToKen(request);
		if (StringUtils.isBlank(tokenInfo.getAdminId()) && StringUtils.isBlank(tokenInfo.getToken())) {
			// 返回登录
			System.out.println("没有传入对应的身份信息，返回登录");
			return false;
		}
		try {
			String token = tokenInfo.getToken();
			String adminId = tokenInfo.getAdminId();
			// 需要拿数据库中的比较
			int type = tokenMapper.getType(adminId, token);
			if(type!=0){
				System.err.println("校验成功");
				return true;
			} else {
				System.err.println("校验失败，返回登录");
				return false;
			}
		} catch (Exception e) {
			System.err.println("校验失败,对应的信息匹配错误，返回登录");
			return false;
		}

	}

	/**
	 * 在cookie中获取用户传递的token，数据库判断
	 */
	private TokenInfo getUserToKen(HttpServletRequest request) {
		TokenInfo info = new TokenInfo();
		String adminId = request.getHeader("adminId");
		String token = request.getHeader("token");
		if (StringUtils.isNotBlank(adminId) && StringUtils.isNotBlank(token)) {
			info.setAdminId(adminId);
			info.setToken(token);
		}
		return info;
	}


	/**
	 * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView mv)
			throws Exception {

	}

	/**
	 * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行 （主要是用于进行资源清理工作）
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception ex)
			throws Exception {

	}

	public void returnErrorResponse(HttpServletResponse response, ResponseBase result)
			throws IOException, UnsupportedEncodingException {
		OutputStream out = null;
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json");
			out = response.getOutputStream();
			out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
			out.flush();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

}