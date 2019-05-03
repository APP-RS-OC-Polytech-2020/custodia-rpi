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
		e.add(new Edge(7,v.get(0),v.get(1),180,-90));
		e.add(new Edge(9,v.get(1),v.get(2),90,-90));
		e.add(new Edge(14,v.get(2),v.get(3),-90,90));
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
	
	public void newError(Vertex vOrigin, Vertex vDestination) throws JSONException, InterruptedException{
		sp=new ShortestPath(new Map(v,e),vOrigin);
		sp.getAllPaths();
		sp.getPath(vDestination);
		this.regularPath = this.sp.getCorrectPath();
		this.vPast = this.vCurrent;
		this.vCurrent = pathCycle();
		Edge currentEdge = getCurrentEdge(this.vPast,this.vCurrent);
		if(currentEdge!=null){
			changeDirection();	
		}	
		this.robot.alerteCapteur = false;
	}
	
	public void findVertex(String qrcode) throws JSONException, InterruptedException{
		for(Vertex ve: this.v){
			if(ve.getName().equals(qrcode)){
				newError(vCurrent, ve);
			}
		}
	}
	
	public void modeAuto() throws InterruptedException, JSONException {
		if(this.robot.alerteCapteur == true){
			findVertex(this.robot.lieuAlerte);
		}
		
		while(this.robot.RobotIsOk==false || this.gestionCamera.findQRCode== false) {
				this.sr.EnvoiDriveRobot(40,0,7);
				Thread.sleep(8000);
				this.sr.EnvoiDriveRobot(0,0,0);
				Thread.sleep(1000);
				checkAround();
		}
		this.vPast = this.vCurrent;
		this.vCurrent = pathCycle();
		Edge currentEdge = getCurrentEdge(this.vPast,this.vCurrent);
		if(currentEdge!=null){
			changeDirection();	
		}		
	}
	
	public void changeDirection() throws JSONException, InterruptedException{
		Edge currentEdge = getCurrentEdge(this.vPast,this.vCurrent);
		float angle;
		if(this.vPast.equals(currentEdge.getVertA())){
			angle = currentEdge.getActionToB();
		}else{
			angle = currentEdge.getActionToA();
		}
		this.robot.demiTourOk=true;
		this.robot.phiBeforeRotation = this.robot.phi + angle+15;
		if(this.robot.phiBeforeRotation>180){
			this.robot.phiBeforeRotation=-180+(this.robot.phiBeforeRotation-180);
		}
		this.sr.EnvoiRotateRobot((float) -1);
		Thread.sleep(6000);
		while(this.robot.phi < this.robot.phiBeforeRotation+5 && this.robot.phi > this.robot.phiBeforeRotation-5){
			Thread.sleep(500);
		}
		this.sr.EnvoiDriveRobot(0,0,0);
		this.gestionCamera.findQRCode= false;
		this.robot.demiTourOk=false;
		this.robot.RobotIsOk = false;
	}
	
	public void checkAround() throws JSONException, InterruptedException{
		this.robot.phiBeforeRotation= this.robot.phi+5;
		if(this.robot.phiBeforeRotation>180){
			this.robot.phiBeforeRotation=-180+(this.robot.phiBeforeRotation-180);
		}
		if(!this.robot.RobotIsOk ||!this.gestionCamera.findQRCode){
			this.sr.EnvoiRotateRobot((float) 1);
			Thread.sleep(1000);
			while(!this.gestionCamera.findQRCode&& (this.robot.phi < this.robot.phiBeforeRotation+5 && this.robot.phi > this.robot.phiBeforeRotation-5)){
				Thread.sleep(100);
			}
			this.sr.EnvoiRotateRobot(0);
		}
	}
	
	
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