package com.perf.entities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Random;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.input.params.CarrierAssignmentInput;
import com.perf.input.params.InputEntries;
import com.perf.vo.ShipmentDetails;

public class CarrierAssignment {
	InputEntries input = new InputEntries();
	CarrierAssignmentInput carrierAssignmentInput = new CarrierAssignmentInput();
	
	public void carrierAssignment(List<String> carrierIdList, List<ShipmentDetails> shipmentDeatilsList) {
		Random rand = new Random();
		for(int i = 0; i<shipmentDeatilsList.size(); i++) {
			String carrierId = carrierIdList.get(rand.nextInt(carrierIdList.size()));
			try {
				HttpConnection httpConnection = new HttpConnection();
				HttpURLConnection conn = httpConnection.httpPutConnection(
						carrierAssignmentInput.getUrl(shipmentDeatilsList.get(i).getShipmentId()),
						carrierAssignmentInput.buildPayload(carrierId));
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpPutConnection(
								carrierAssignmentInput.getUrl(shipmentDeatilsList.get(i).getShipmentId()),
								carrierAssignmentInput.buildPayload(carrierId));
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
			} catch(Exception e) {
				System.out.println("Exception.");
				e.printStackTrace();
			}
		}
	}
	
	private List<String> fetchCarrierIdList(){
		List<String> carrierIdList = null;
		try {
			HttpConnection httpConnection = new HttpConnection();
			HttpURLConnection conn = httpConnection.httpGetConnection(carrierAssignmentInput.getCarrierListUrl());
			if(conn.getResponseCode()!=200) {
				System.out.println("Error Response Code");
				if(conn.getResponseCode()==401) {
					AuthToken.setAuthToken(input.authUrl);
					conn = httpConnection.httpGetConnection(carrierAssignmentInput.getCarrierListUrl());
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
			carrierIdList = carrierAssignmentInput.fetchCarrierIdList(sb.toString());
		} catch(Exception e) {
			System.out.println("Exception.");
			e.printStackTrace();
		}
		return carrierIdList;
	}
	
	public void connect() throws InterruptedException, IOException {
		AuthToken.setAuthToken(input.authUrl);
		ShipmentCreate shipmentCreate = new ShipmentCreate();
		List<String> carrierIdList = fetchCarrierIdList();
		if(carrierIdList != null && carrierIdList.size() != 0) {
			List<ShipmentDetails> shipmentDeatilsList = shipmentCreate.connect();
			carrierAssignment(carrierIdList, shipmentDeatilsList);
		} else {
			System.out.println("No carriers found");
		}
	}
	
	public static void main(String[] args) {
		CarrierAssignment carrierAssignment = new CarrierAssignment();
		try {
			carrierAssignment.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
