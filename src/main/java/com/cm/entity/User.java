package com.cm.entity;
 
import lombok.Data;

import java.io.Serializable;

@Data
public class User implements Serializable {
	private static final long serialVersionUID = -5245115690870637354L;
	private String uuid;
	private String userName;
	private Integer userAge;
	private String userSex;
	private String city;
}