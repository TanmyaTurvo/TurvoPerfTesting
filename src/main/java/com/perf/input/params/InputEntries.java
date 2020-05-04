package com.perf.input.params;

public class InputEntries {
	
	public String domain = "http://dev-cr-perf-testing.turvo.net";
	private String username = "regression.broker@turvo.com";
	private String password = "Temp@123";
	public String authUrl = domain + "/api/oauth/token?grant_type=password&client_id=magellan-ws&client_secret=secret&username=" + username + "&scope=read+write&type=business&password=" + password;
	
}
