package com.znvoid.demo.util;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	 public static String getJsonString(String key, Object value) throws JSONException{
	    
	        JSONObject jsonObject = new JSONObject();
	      
				jsonObject.put(key, value);
				return jsonObject.toString();
				
			
	        
	        
	    }

	 public static JSONObject getJsonObject(String key, Object value) throws JSONException
	    {
	        JSONObject jsonObject = new JSONObject();
	        jsonObject.put(key, value);
	        return jsonObject;
	    }
}
