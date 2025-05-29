package com.example.springRest.Response;

import java.sql.Timestamp;
import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseBean {
	private String status;
	private Integer code;
	private String message;
	private Long timestamp;
	public ResponseBean(String status, Integer code, String message, Long timestamp) {
		super();
		this.status = status;
		this.code = code;
		this.message = message;
		this.timestamp = timestamp;
	}

}
