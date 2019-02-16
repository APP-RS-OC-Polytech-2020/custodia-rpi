package Robot;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonManager {
	private JSONObject obj;
	public JsonManager(JSONObject o){
		this.obj=o;
	}
	public boolean getModeManual()throws JSONException{
		boolean modeManual = (boolean) obj.getBoolean("modeManuel");
		return modeManual;
	}
	public String getIPAddress()throws JSONException{
		String address = obj.getJSONObject("data").getString("ip");
		return address;
	}
	public float getX() throws JSONException {
		float posX = obj.getJSONObject("data").getInt("x");
		return posX;
	}
	public float getY() throws JSONException {
		float posY = obj.getJSONObject("data").getInt("y");
		return posY;
	}
	public float getAngle() throws JSONException {
		float radian =obj.getJSONObject("data").getJSONObject("angle").getLong("degree");
		return radian;
	}
	public float getForce() throws JSONException {
		float force = obj.getJSONObject("data").getInt("force");
		return force;
	}
	
	public float getRotation() throws JSONException{
		float rotation = obj.getInt("rotate");
		return rotation;
	}
}