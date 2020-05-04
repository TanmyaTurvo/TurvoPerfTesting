package com.perf.entities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.input.params.CustomerCreateInput;
import com.perf.input.params.InputEntries;
import com.perf.utils.Utils;

/*
 * Creates customer accounts and returns a List with customer ids
 */

public class CustomerCreate {
	
	CustomerCreateInput customerCreateInput = new CustomerCreateInput();
	InputEntries input = new InputEntries();
	
	public List<String> customerCreate(){
		List<String> accountIdList = new ArrayList<>();
		HttpConnection httpConnection = new HttpConnection();
		for (int i=0;i<customerCreateInput.customerCount;i++) {
			HttpURLConnection conn = httpConnection.httpPostConnection(customerCreateInput.url, customerCreateInput.getCustomerPayload());
			try {
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						System.out.println("Auth token expired. Generating again");
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpPostConnection(customerCreateInput.url, customerCreateInput.getCustomerPayload());
					}
				}
				BufferedReader br;
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				
				StringBuilder sb = new StringBuilder();
				while((output = br.readLine())!=null) {
					System.out.println(output);
					sb.append(output);
				}
				accountIdList.add(Utils.getValueFromJson(sb, "accountId"));
			}
			catch (Exception e){				
				System.out.println("Exception.");
				e.printStackTrace();
			}
		}
		System.out.println("Account Ids:");
		for(int i=0;i<accountIdList.size();i++) {
			System.out.println(accountIdList.get(i));
		}
		return accountIdList;
		
	}
	
	public List<String> connect() {
		AuthToken.setAuthToken(input.authUrl);
		CustomerCreate customerCreate = new CustomerCreate();
		return customerCreate.customerCreate();
	}
	
	public static void main(String[] args) {
		CustomerCreate customerCreate = new CustomerCreate();
		customerCreate.connect();
	}
	
}
