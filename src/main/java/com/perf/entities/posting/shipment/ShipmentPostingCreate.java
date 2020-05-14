package com.perf.entities.posting.shipment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.entities.ShipmentCreate;
import com.perf.input.params.InputEntries;
import com.perf.input.params.ShipmentPostingInput;
import com.perf.vo.ShipmentDetails;

public class ShipmentPostingCreate {
	
	ShipmentPostingInput shipmentPostingInput = new ShipmentPostingInput();
	InputEntries input = new InputEntries();
	public void postShipment(String shipmentId) {
		for(int i=0 ; i<shipmentPostingInput.shipmentPostingLoop ; i++) {
			try {
				HttpConnection httpConnection = new HttpConnection();
				HttpURLConnection conn = httpConnection.httpPostConnection(shipmentPostingInput.url, 
						shipmentPostingInput.getShipmentPostingPostData(shipmentId));
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpPostConnection(shipmentPostingInput.url, 
								shipmentPostingInput.getShipmentPostingPostData(shipmentId));
					}
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				while((output = br.readLine())!=null) {
					System.out.println("SHIPMENTPOSTING: " + output);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public void connect() throws IOException {
		AuthToken.setAuthToken(input.authUrl);
		ShipmentCreate shipmentCreate = new ShipmentCreate();
		List<ShipmentDetails> shipmentDetailsList = shipmentCreate.connect();
		for(ShipmentDetails accountDetail : shipmentDetailsList) {
			postShipment(accountDetail.getShipmentId());
		}
	}
	
	public static void main(String[] args) {
		ShipmentPostingCreate shipmentPostingCreate = new ShipmentPostingCreate();
		try {
			shipmentPostingCreate.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
