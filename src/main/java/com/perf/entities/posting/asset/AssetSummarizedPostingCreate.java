package com.perf.entities.posting.asset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;

import com.perf.authentication.AuthToken;
import com.perf.connection.HttpConnection;
import com.perf.entities.location.LocationDetailsUtils;
import com.perf.input.params.AssetSummarizedPostingInput;
import com.perf.input.params.InputEntries;
import com.perf.utils.Utils;

public class AssetSummarizedPostingCreate {
	
	AssetSummarizedPostingInput assetSummarizedPostingInput = new AssetSummarizedPostingInput();
	InputEntries input = new InputEntries();
	public void postAsset(List<Double> origin1Coords, List<Double> origin2Coords,
			String availableDate) {
		for(int i=0 ; i<assetSummarizedPostingInput.assetPostingThreads ; i++) {
			try {
				HttpConnection httpConnection = new HttpConnection();
				HttpURLConnection conn = httpConnection.httpPostConnection(assetSummarizedPostingInput.url, 
						assetSummarizedPostingInput.getAssetSummarizedPostingPostData(origin1Coords, origin2Coords, availableDate));
				if(conn.getResponseCode()!=200) {
					System.out.println("Error Response Code");
					if(conn.getResponseCode()==401) {
						AuthToken.setAuthToken(input.authUrl);
						conn = httpConnection.httpPostConnection(assetSummarizedPostingInput.url, 
								assetSummarizedPostingInput.getAssetSummarizedPostingPostData(origin1Coords, origin2Coords, availableDate));
					}
				}
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String output;
				while((output = br.readLine())!=null) {
					System.out.println(output);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public void connect() throws IOException {
		AuthToken.setAuthToken(input.authUrl);
		LocationDetailsUtils locationDetailsUtils = new LocationDetailsUtils();
		List<List<Double>> locationCoords = locationDetailsUtils.getCoordinates(2*assetSummarizedPostingInput.assetPostingThreads);
		List<String> dates = Utils.getDates(assetSummarizedPostingInput.assetPostingThreads);
		int numThreads = assetSummarizedPostingInput.assetPostingThreads - 1;
		while (numThreads >= 0) {
			postAsset(locationCoords.get(2*numThreads), locationCoords.get(2*numThreads+1), dates.get(numThreads));
			numThreads--;
		}
	}
	
	public static void main(String[] args) {
		AssetSummarizedPostingCreate assetSummarizedPostingCreate = new AssetSummarizedPostingCreate();
		try {
			assetSummarizedPostingCreate.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
