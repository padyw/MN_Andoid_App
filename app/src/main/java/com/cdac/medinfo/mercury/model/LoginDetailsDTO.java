package com.cdac.medinfo.mercury.model;

public class LoginDetailsDTO {

	private String loginId;

	private String password;
	
	private String captchaKey;
	
	private String captchaValue;
	
	public LoginDetailsDTO() {
		super();
	}

	public LoginDetailsDTO(String loginId, String password) {
		super();
		this.loginId = loginId;
		this.password = password;
	}
	
	public LoginDetailsDTO(String loginId, String password, String captchaKey, String captchaValue) {
		super();
		this.loginId = loginId;
		this.password = password;
		this.captchaKey = captchaKey;
		this.captchaValue = captchaValue;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCaptchaKey() {
		return captchaKey;
	}

	public void setCaptchaKey(String captchaKey) {
		this.captchaKey = captchaKey;
	}

	public String getCaptchaValue() {
		return captchaValue;
	}

	public void setCaptchaValue(String captchaValue) {
		this.captchaValue = captchaValue;
	}

	@Override
	public String toString() {
		return "LoginDetailsDTO [loginId=" + loginId + ", password=" + password + ", captchaKey=" + captchaKey
				+ ", captchaValue=" + captchaValue + "]";
	}
}
