package echange;
import java.util.ArrayList;

import org.json.JSONException;

import deplacement.Edge;
import deplacement.Vertex;
import deplacement.ShortestPath;
import deplacement.Map;
import camera.GestionCamera;


public class GestionRobot implements Runnable{
	private ArrayList<Vertex> v;
	private ArrayList<Edge> e;
	private ArrayList<Vertex> regularPath;
	private ShortestPath sp;
	private Vertex vCurrent;
	private Vertex vPast;
	private Robot robot;
	private GestionCamera gestionCamera;
	private SocketRaspberry sr;
	private int currentPathId;
	private Map mapDep;
	/**
	 * Set all environment variables
	 * @param r
	 * @param sr
	 */
	public GestionRobot(Robot r, SocketRaspberry sr){
		this.vCurrent = new Vertex("SalleAPP1",1);
		this.sr = sr;
        this.gestionCamera = new GestionCamera(r, sr);
        new Thread(this.gestionCamera).start();
        
		this.v=new ArrayList<Vertex>();
		v.add(new Vertex("SalleAPP1",1));
		v.add(new Vertex("SalleAPP2",2));
		v.add(new Vertex("SortieSalleAPP",3));
		v.add(new Vertex("SortieSalleGauche",4));
		v.add(new Vertex("SortieSalleDroite",5));

		this.e=new ArrayList<Edge>();
		//e.add(new Edge(7,v.get(0),v.get(1),180,180));
		e.add(new Edge(9,v.get(1),v.get(2),-90,90));
		e.add(new Edge(14,v.get(2),v.get(3),90,-90));
		e.add(new Edge(10,v.get(2),v.get(4),90,-90));
		
		this.currentPathId=0;
		
		this.regularPath=new ArrayList<Vertex>();
		regularPath.add(v.get(0));
		regularPath.add(v.get(1));
		regularPath.add(v.get(2));
		regularPath.add(v.get(3));
		regularPath.add(v.get(2));
		regularPath.add(v.get(4));
		regularPath.add(v.get(2));
		regularPath.add(v.get(1));
		
		this.robot = r;	
	}
	/**
	 * Action to complete when robot arrived to qrcode
	 * @param vOrigin
	 * @param vDestination
	 * @throws InterruptedException
	 * @throws JSONException
	 */
	public void newError(Vertex vOrigin, Vertex vDestination) throws  InterruptedException, JSONException{	
		
		sp=new ShortestPath(new Map(v,e),vOrigin);
		sp.getAllPaths();
		sp.getPath(vDestination);
		System.out.println(sp.getCorrectPath());
		this.regularPath = this.sp.getCorrectPath();
		System.out.println("path"+ this.regularPath);
		System.out.println("courant"+ this.vPast);
		System.out.println("suivant"+ this.vCurrent);
		this.vCurrent = pathCycle();
		Edge currentEdge = getCurrentEdge(this.vPast,this.vCurrent);
		if(currentEdge!=null){
			changeDirection();	
		}	
		this.robot.alerteCapteur = false;
	}
	/**
	 * Find vertex
	 * @param qrcode
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public void findVertex(String qrcode) throws JSONException, InterruptedException{
		for(Vertex ve: this.v){
			if(ve.getName().equals(qrcode)){
				newError(vCurrent, ve);
			}
		}
	}
	/**
	 * Automatic surveillance mode
	 * @throws InterruptedException
	 * @throws JSONException
	 */
	public void modeAuto() throws InterruptedException, JSONException {
		
		while(this.robot.RobotIsOk==false || this.gestionCamera.findQRCode== false) {
				this.sr.EnvoiDriveRobot(40,0,7);
				Thread.sleep(8000);
				this.sr.EnvoiDriveRobot(0,0,0);
				Thread.sleep(1000);
				checkAround();
		}
		
		if(this.robot.alerteCapteur == true){
			System.out.println("error");
			findVertex(this.robot.lieuAlerte);
		}else {	
			this.vPast = this.vCurrent;
			this.vCurrent = pathCycle();
			Edge currentEdge = getCurrentEdge(this.vPast,this.vCurrent);
			if(currentEdge!=null){
			changeDirection();	
		}	
		}
	}
	
	/**
	 * Change direction of robot
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public void changeDirection() throws JSONException, InterruptedException{
		Edge currentEdge = getCurrentEdge(this.vPast,this.vCurrent);
		float angle;
		if(this.vPast.equals(currentEdge.getVertA())){
			angle = currentEdge.getActionToB();
		}else{
			angle = currentEdge.getActionToA();
		}
		System.out.println("angle"+angle);
		this.robot.demiTourOk=true;
		System.out.println("phi"+ this.robot.phi);
		this.robot.phiBeforeRotation = this.robot.phi + angle+10;
		if(this.robot.phiBeforeRotation>180){
			this.robot.phiBeforeRotation=-180+(this.robot.phiBeforeRotation-180);
		}
		System.out.println("angle valeur"+this.robot.phiBeforeRotation);
		this.sr.EnvoiRotateRobot((float) -1);
		Thread.sleep(6000);
		while(!(this.robot.phi < this.robot.phiBeforeRotation+5 && this.robot.phi > this.robot.phiBeforeRotation-5)){
			Thread.sleep(400);
		}
		System.out.println("ok wquand"+ this.robot.phi);
		this.sr.EnvoiDriveRobot(0,0,0);
		this.gestionCamera.findQRCode= false;
		this.robot.demiTourOk=false;
		this.robot.RobotIsOk = false;
	}
	/**
	 * Robot checks around itself
	 * @throws JSONException
	 * @throws InterruptedException
	 */
	public void checkAround() throws JSONException, InterruptedException{
		this.robot.phiBeforeRotation= this.robot.phi+5;
		if(this.robot.phiBeforeRotation>180){
			this.robot.phiBeforeRotation=-180+(this.robot.phiBeforeRotation-180);
		}
		if(!this.robot.RobotIsOk ||!this.gestionCamera.findQRCode){
			this.sr.EnvoiRotateRobot((float) 1);
			Thread.sleep(5000);
			while(!this.gestionCamera.findQRCode&& !(this.robot.phi < this.robot.phiBeforeRotation+5 && this.robot.phi > this.robot.phiBeforeRotation-5)){
				Thread.sleep(100);
			}
			this.sr.EnvoiRotateRobot(0);
		}
	}
	
	/**
	 * Get current edge
	 * @param vPast
	 * @param vCurrent
	 * @return edge
	 */
	public Edge getCurrentEdge(Vertex vPast,Vertex vCurrent){
		for(Edge edges: this.e){
				if(edges.getVertA().equals(vPast)||edges.getVertB().equals(vPast)){
					return edges;
				}
				if(edges.getVertA().equals(vCurrent)||edges.getVertB().equals(vCurrent)){
					return edges;
				}
		}
		return null;
	}
	/**
	 * Path cycle
	 * @return path
	 */
    public Vertex pathCycle(){		
		if(this.currentPathId==(regularPath.size()-1)){ 
			this.currentPathId=0;
			return regularPath.get(0);
		}
		else{
			this.currentPathId++;
			return regularPath.get(currentPathId);
		}
    }
    
    public void run() {
    	while(true){
	    	if(!this.robot.isManual()) {
	    		try {
					modeAuto();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
    	}
    }
    
	public void start(){
		System.out.println("Initialisation mode auto");
	}
}