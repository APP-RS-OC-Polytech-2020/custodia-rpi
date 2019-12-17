package echange;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonManager {
	private JSONObject obj;
	public JsonManager(JSONObject o){
		this.obj=o;
	}
	/**
	 * Provides rotation information for the robot
	 * @param rotation
	 * @param hostname
	 * @return
	 * @throws JSONException
	 */
	public JSONObject rotateRobot(float rotation, String hostname) throws JSONException{
		obj.put("manualMode",false);
		obj.put("type","command");
		obj.put("mode","rotation");
		
		JSONObject rotate = new JSONObject();
		rotate.put("rotate", rotation);
		
		JSONObject host = new JSONObject();
		host.put("ip", hostname);
		
		obj.put("data", rotate);
		obj.put("robot", host);
		return obj;
	}
	/**
	 * Provides drive information for the robot
	 * @param x
	 * @param y
	 * @param force
	 * @param hostname
	 * @return
	 * @throws JSONException
	 */
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