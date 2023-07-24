package com.nagpassignment.flipkart.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JsonDataReader {
    public static List<PageData> getPageData(String pageName,String value1,String value2) {
        List<PageData> pageDataList = new ArrayList<PageData>();

        try {
            // Read the JSON file into a string
            String jsonData = new String(Files.readAllBytes(Paths.get("src/main/java/com/nagpassignment/flipkart/data/testData.json")));

            // Parse the JSON string
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray pageArray = jsonObject.getJSONArray(pageName);

            // Iterate over the page objects and extract the data
            for (int i = 0; i < pageArray.length(); i++) {
                JSONObject pageObj = pageArray.getJSONObject(i);
                String val1 = pageObj.getString(value1);
                String val2 = pageObj.getString(value2);

                pageDataList.add(new PageData(val1, val2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pageDataList;
    }
}
