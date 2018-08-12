package com.hxzm.common.kit;

import java.util.UUID;

public class PKkit {
	
	public static String getpk(){
        return UUID.randomUUID().toString();
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().length());
	}
}
