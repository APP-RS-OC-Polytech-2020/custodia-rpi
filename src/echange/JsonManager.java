package echange;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonManager {
	private JSONObject obj;
	public JsonManager(JSONObject o){
		this.obj=o;
	}

	public JSONObject rotateRobot(float rotation, String hostname) throws JSONException{
		obj.put("manualMode",false);
		obj.put("type","command");
		obj.put("mode","rotation");
		obj.put("info","rzerze");
		
		JSONObject rotate = new JSONObject();
		rotate.put("rotate", rotation);
		
		JSONObject host = new JSONObject();
		host.put("ip", hostname);
		
		obj.put("data", rotate);
		obj.put("robot", host);
		return obj;
	}
	
	public JSONObject driveRobot(float x, float y,float force, String hostname)throws JSONException{
		obj.put("manualMode",false);
		obj.put("type","command");
		obj.put("mode","move");
		
		JSONObject info = new JSONObject();
		info.put("x", x);
		info.put("y", y);
		info.put("power", force);
		
		JSONObject host = new JSONObject();
		host.put("ip", hostname);
		
		obj.put("data", info);
		obj.put("robot", host);
		return obj;
	}
}