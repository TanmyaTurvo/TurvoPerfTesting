package com.perf.entities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.input.params.InputEntries;
import com.perf.input.params.ShipmentCreateInput;

public class ShipmentCreateLineItem {

	ShipmentCreateInput shipmentCreateInput = new ShipmentCreateInput();
	InputEntries input = new InputEntries();
	
	public void setShipmentCreateLineItem(String shipmentId, String customerOrderId){
		HttpConnection httpConnection = new HttpConnection();
		HttpURLConnection conn = httpConnection.httpPutConnection(shipmentCreateInput.getLineItemUrl(shipmentId),
				shipmentCreateInput.getShipmentLineItemPayload(customerOrderId));
		try {
			if(conn.getResponseCode()!=200) {
				System.out.println("Error Response Code");
				if(conn.getResponseCode()==401) {
					System.out.println("Auth token expired. Generating again");
					AuthToken.setAuthToken(input.authUrl);
					conn = httpConnection.httpPutConnection(shipmentCreateInput.getLineItemUrl(shipmentId), 
							shipmentCreateInput.getShipmentLineItemPayload(customerOrderId));
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
			
		}
		catch (Exception e){				
			System.out.println("Exception.");
			e.printStackTrace();
		}
	}
	
}
