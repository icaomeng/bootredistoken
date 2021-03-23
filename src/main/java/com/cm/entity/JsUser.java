package com.cm.entity;
 
import lombok.Data;

import java.io.Serializable;

@Data
public class JsUser implements Serializable {
	private static final long serialVersionUID = 5332542385182145442L;
	private String uuid;
	private String userName;
	private String password;
	private Integer age;
	private String sex;
	private String city;
}