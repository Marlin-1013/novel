package com.java.common;

public class Users {		//유저 데이터 싱글톤
	public class getUserClass extends Users {

	}

	private static Users USER = new Users();
	public String username;
	
	private Users() {		
		
	}
	
	public static Users getUserClass() {
		if(USER == null) {
			USER = new Users();
		}
		return USER;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
