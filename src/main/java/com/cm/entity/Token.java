package com.cm.entity;
 
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class Token implements Serializable {
	private static final long serialVersionUID = 2919875280591926465L;
	private String uuid;
	private String token;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
}