package com.perf.input.params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.perf.utils.Utils;

public class AssetSummarizedPostingInput {
	InputEntries input = new InputEntries();
	public String url = input.domain + "/api/network/capacity/asset-summarised-batches";
	public int assetPostingThreads = 1;
	public long carrierId = 65;

	public String getAssetSummarizedPostingPostData(List<Double> origin1Coords, List<Double> origin2Coords, 
			String date) {

		Map<String, Object> mainMap = new HashMap<>();
		Map<String, Object> map = new HashMap<>();

		List<Map<String, Object>> networks = new ArrayList<Map<String, Object>>();
		Map<String, Object> network = new HashMap<>();
		network.put("id", 3369119);
		network.put("key", "assetSource.network");
		network.put("value", "My network");
		networks.add(network);

		List<Map<String, Object>> assets = new ArrayList<>();
		for (int i = 0; i <= 2; i++) {

			Map<String, Object> asset = new HashMap<>();
			Map<String,Object> availableDateMap = new HashMap<>();
			availableDateMap.put("date", date);
			availableDateMap.put("timezone","America/Los_Angeles");
			availableDateMap.put("flex", 0);
			availableDateMap.put("hasTime", true);
			availableDateMap.put("userDate", date);

			List<Map<String,Object>> origins = new ArrayList<Map<String,Object>>();
			Map<String,Object> origin1 = new HashMap<>();
			Map<String,Object> origin2 = new HashMap<>();

			Map<String,Object> origin1GPS = new HashMap<>();
			List<Double> origin1Coordinates = origin1Coords;
			origin1GPS.put("coordinates", origin1Coordinates);
			origin1.put("gps", origin1GPS);

			Map<String,Object> origin2GPS = new HashMap<>();
			List<Double> origin2Coordinates = origin2Coords;
			origin2GPS.put("coordinates", origin2Coordinates);
			origin2.put("gps", origin2GPS);
			
			origins.add(origin1);
			origins.add(origin2);

			Map<String,Object> equipment = new HashMap<>();
			Map<String,Object> equipmentType = new HashMap<>();
			equipmentType.put("id", 100026);
			equipmentType.put("key", "1200");
			equipmentType.put("value", "Van");
			equipment.put("equipment_type", equipmentType);

			asset.put("available_date_time", availableDateMap);
			asset.put("notes", "Notes for summarized posting");
			asset.put("origins", origins);
			asset.put("equipment", equipment);
			
			assets.add(asset);

		}

		mainMap.put("carrier_id", carrierId);
		mainMap.put("networks", networks);
		mainMap.put("summarised_assets", assets);

		String json = Utils.mapToJson(mainMap);		
		
		return json;

	}

}
