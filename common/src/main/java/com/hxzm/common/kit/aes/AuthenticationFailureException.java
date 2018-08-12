package com.hxzm.common.kit.aes;

public class AuthenticationFailureException extends RuntimeException {

	private static final long serialVersionUID = -4178047179595517913L;

	public AuthenticationFailureException() {
    }

    public AuthenticationFailureException(String message) {
        super(message);
    }

    public AuthenticationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationFailureException(Throwable cause) {
        super(cause);
    }

    public AuthenticationFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
