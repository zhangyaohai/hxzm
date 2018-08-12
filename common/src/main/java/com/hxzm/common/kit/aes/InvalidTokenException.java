package com.hxzm.common.kit.aes;

public class InvalidTokenException extends AuthenticationFailureException{
	
	private static final long serialVersionUID = -5527196205792659300L;

	public InvalidTokenException(String token) {
		super("token is invalid");
	}

}
