package echange;

public class Robot
{
    protected final String hostname;
    public boolean RobotIsOk = false;//une fois recalibre
    protected boolean isManual;

    public Robot(String hostname){
        this.hostname = hostname;
        this.isManual=true;
    }

    public boolean isRobotIsOk() {
		return RobotIsOk;
	}

	public void setRobotIsOk(boolean robotIsOk) {
		RobotIsOk = robotIsOk;
	}

	public boolean isManual() {
		return isManual;
	}

	public void setManual(boolean isManual) {
		this.isManual = isManual;
	}

       
}