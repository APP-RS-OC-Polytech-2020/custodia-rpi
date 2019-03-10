package echange;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonManager {
	private JSONObject obj;
	public JsonManager(JSONObject o){
		this.obj=o;
	}

	public void RotateRobot() throws JSONException{
		obj.put("info","rzerze");
	}
	
	public void driveRobot()throws JSONException{
		obj.put("info","rzerze");
	}
	
	public boolean getModeManual()throws JSONException{
		boolean modeManual = (boolean) obj.getBoolean("manualMode");
		return modeManual;
	}
	
}