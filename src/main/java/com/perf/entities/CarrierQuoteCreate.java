package com.perf.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.input.params.CarrierQuoteInput;
import com.perf.input.params.InputEntries;
import com.perf.vo.ShipmentDetails;

public class CarrierQuoteCreate {
	InputEntries input = new InputEntries();
	CarrierQuoteInput carrierQuoteInput = new CarrierQuoteInput();
	
	public void carrierQuoteCreate(List<ShipmentDetails> shipmentDeatilsList) throws IOException {
		for(ShipmentDetails shipmentDetails : shipmentDeatilsList) {
			String shipmentId = shipmentDetails.getShipmentId();
			HttpConnection httpConnection = new HttpConnection();
			HttpURLConnection conn = httpConnection.httpPutConnection(
					carrierQuoteInput.getUrl(shipmentId), carrierQuoteInput.buildPayload());
			if(conn.getResponseCode()!=200) {
				System.out.println("Error Response Code");
				if(conn.getResponseCode()==401) {
					AuthToken.setAuthToken(input.authUrl);
					conn = httpConnection.httpPutConnection(
							carrierQuoteInput.getUrl(shipmentId), carrierQuoteInput.buildPayload());
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
	}
	
	public void connect() throws IOException {
		AuthToken.setAuthToken(input.authUrl);
		ShipmentCreate shipmentCreate = new ShipmentCreate();
		List<ShipmentDetails> shipmentDeatilsList = shipmentCreate.connect();
		carrierQuoteCreate(shipmentDeatilsList);
	}
	
	public static void main(String[] args) {
		CarrierQuoteCreate carrierQuoteCreate = new CarrierQuoteCreate();
		try {
			carrierQuoteCreate.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
