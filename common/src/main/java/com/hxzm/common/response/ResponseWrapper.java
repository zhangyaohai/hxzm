package com.hxzm.common.response;


import org.apache.http.HttpStatus;

public class ResponseWrapper<T> {
	
	private Integer code;
	private String msg;
	private T obj;

	public ResponseWrapper(T obj){
		this.code = Message.SUCCESS.code;
		this.obj = obj;
	}

	public ResponseWrapper(int code, String message) {
		this.code = code;
		this.msg = message;
		this.obj = null;
	}
	
	public ResponseWrapper(Message message){
		this.code = message.code;
		this.msg = message.message;
		this.obj = null;
	}
	
	public ResponseWrapper(Message message,T obj){
		this.code = message.code;
		this.msg = message.message;
		this.obj = obj;
	}
	
   public static ResponseWrapper<?> error(String message) {
	   ResponseWrapper<?> response = new ResponseWrapper<>(HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
       return response;
    }

    public static ResponseWrapper<?> badRequest() {
    	ResponseWrapper<String> response = new ResponseWrapper<>(HttpStatus.SC_BAD_REQUEST, "不可用的请求");
        return response;
    }

    public T getObj() {
		return obj;
	}

	public void setObj(T obj) {
		this.obj = obj;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
