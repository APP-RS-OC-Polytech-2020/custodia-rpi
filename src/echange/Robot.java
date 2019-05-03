package echange;

public class Robot
{
    protected final String hostname;
    public boolean RobotIsOk = false;//une fois recalibre
    protected boolean isManual;
    public boolean demiTourOk = false;
    public float phi;
    public float phiBeforeRotation;
    public boolean alerteCapteur = false;
    public String lieuAlerte;

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

	public boolean isDemiTourOk() {
		return demiTourOk;
	}

	public void setDemiTourOk(boolean demiTourOk) {
		this.demiTourOk = demiTourOk;
	}

	public float getPhi() {
		return phi;
	}

	public void setPhi(float phi) {
		this.phi = phi;
	}

	public float getPhiBeforeRotation() {
		return phiBeforeRotation;
	}

	public void setPhiBeforeRotation(float phiBeforeRotation) {
		this.phiBeforeRotation = phiBeforeRotation;
	}    
}