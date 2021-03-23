package com.cm.entity.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 用户登录信息
 * @author ASUS
 *
 */
@Data
@ToString
public class TokenInfo {

	private String adminId;
	private String token;
}
