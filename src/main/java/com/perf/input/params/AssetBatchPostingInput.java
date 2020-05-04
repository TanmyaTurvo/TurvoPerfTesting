package com.perf.input.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.perf.utils.Utils;

public class AssetBatchPostingInput {
	InputEntries input = new InputEntries();
	public String url = input.domain + "/api/network/asset-batches";
	public String inputFile = "LocationIDs.txt";
	public int assetPostingThreads = 5;
	public long carrierId = 65;

	public String getAssetBatchPostingPostData(List<Double> originCoords, List<Double> destinationCoords, 
			String date) {

		Map<String, Object> mainMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();

		Map<String, Object> networks = new HashMap<>();
		networks.put("id", 3369119);
		networks.put("key", "assetSource.network");
		networks.put("value", "My network");

		List<Map<String, Object>> assets = new ArrayList<>();
		for (int i = 0; i <= 2; i++) {


			Map<String, Object> asset = new HashMap<>();
			Map<String,Object> availableDateMap = new HashMap<>();
			availableDateMap.put("date", date);
			availableDateMap.put("timezone","America/Los_Angeles");
			availableDateMap.put("flex", 0);
			availableDateMap.put("hasTime", true);
			availableDateMap.put("userDate", date);

			Map<String,Object> quote = new HashMap<>();
			Map<String,Object> quoteUnits = new HashMap<>();
			quoteUnits.put("id", 100065);
			quoteUnits.put("key", "1550");
			quoteUnits.put("value", "USD");
			quote.put("value", 112);
			quote.put("units", quoteUnits);

			Map<String,Object> origin = new HashMap<>();
			Map<String,Object> destination = new HashMap<>();

			Map<String,Object> originGPS = new HashMap<>();
			List<Double> originCoordinates = originCoords;
			originGPS.put("coordinates", originCoordinates);
			origin.put("gps", originGPS);

			Map<String,Object> destinationGPS = new HashMap<>();
			List<Double> destinationCoordinates = destinationCoords;
			destinationGPS.put("coordinates", destinationCoordinates);
			origin.put("gps", destinationGPS);

			Map<String,Object> equipment = new HashMap<>();
			Map<String,Object> equipmentType = new HashMap<>();
			equipmentType.put("id", 100026);
			equipmentType.put("key", "1200");
			equipmentType.put("value", "Van");
			equipment.put("equipment_size", equipmentType);

			Map<String,Object> equipmentSize = new HashMap<>();
			equipmentSize.put("id", 191916);
			equipmentSize.put("key", "1380");
			equipmentSize.put("value", "8 ft");
			equipment.put("equipment_size", equipmentSize);

			asset.put("available_date_time", availableDateMap);
			asset.put("carrier_id", carrierId);
			asset.put("notes", "Notes for posting");
			asset.put("quote", quote);
			asset.put("origin", origin);
			asset.put("destination", destination);
			asset.put("equipment", equipment);

			assets.add(asset);

		}

		mainMap.put("networks", networks);
		mainMap.put("assets", assets);

		String json = Utils.mapToJson(mainMap);
		return json;

	}

}
