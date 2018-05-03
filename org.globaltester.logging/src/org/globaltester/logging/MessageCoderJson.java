package org.globaltester.logging;

import java.util.ArrayList;
import java.util.List;

import org.globaltester.logging.tags.LogTag;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageCoderJson {
	
	
	public static final String MSG = "msg";
	public static final String TAGS = "tags";
	public static final String TAG_ID = "id";
	public static final String TAG_ADDITIONALDATA = "additionalData";

	public static String encode(Message messageObject) {

		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put(MSG, messageObject.messageContent);
		if (!messageObject.getLogTags().isEmpty()) {
			jsonMsg.put(TAGS, getLogTagsAsJson(messageObject.getLogTags()));
		}
		return jsonMsg.toString();
		
	}

	private static JSONArray getLogTagsAsJson(List<LogTag> logTags) {
		JSONArray retVal = new JSONArray();
		for (LogTag curTag : logTags) {
			JSONObject jsonTag = new JSONObject();
			jsonTag.put(TAG_ID, curTag.getId());
			jsonTag.put(TAG_ADDITIONALDATA, new JSONArray(curTag.getAdditionalData()));
			
			retVal.put(jsonTag);
		}
		return retVal;
	}

	public static Message decode(String messageRep) {
		try {
		JSONObject jsonMessage = new JSONObject(messageRep);

		Message recoveredMessage = new Message(jsonMessage.getString(MSG));
		if (jsonMessage.has(TAGS)) {
			JSONArray tags = jsonMessage.getJSONArray(TAGS);
			for (int i = 0; i < tags.length(); i++) {
				recoveredMessage.addLogTag(decodeLogTag((JSONObject) tags.get(i)));
			}
		}
		
		return recoveredMessage;
		} catch (JSONException e) {
			// no valid JSON encoded message
			return null;
		}
	}

	private static LogTag decodeLogTag(JSONObject object) {
		ArrayList<String> list = new ArrayList<>();
		
		JSONArray tags = object.getJSONArray(TAG_ADDITIONALDATA);
		for (int i = 0; i < tags.length(); i++) {
			list.add(tags.getString(i));
		}
		
		return new LogTag(object.getString(TAG_ID), list.toArray(new String[]{}));
	}

}
