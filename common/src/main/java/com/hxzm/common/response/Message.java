package com.hxzm.common.response;

public enum Message {
	
	SUCCESS(200,"成功"),
	TOKEN(401,"TOKEN 不可用"),
	FAIL_1001(10001,"请输入正确的手机号"),

	FAIL(100,"失败");

	public final int code;
    public final String message;
    
    Message(int code, String message) {
        this.code = code;
        this.message = message;
    }

	public int code() {
		return this.code;
	}

	public String message() {
		return this.message;
	}
}
