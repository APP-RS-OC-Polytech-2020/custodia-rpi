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
		e.add(new Edge(7,v.get(0),v.get(1),180,180));
		e.add(new Edge(9,v.get(1),v.get(2),180,90));
		e.add(new Edge(14,v.get(2),v.get(3),180,-90));
		e.add(new Edge(10,v.get(2),v.get(4),180,90));
		
		
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
	
	public void newError(Vertex vOrigin, Vertex vDestination){
		sp=new ShortestPath(new Map(v,e),vOrigin);
		sp.getAllPaths();
		sp.getPath(vDestination);
	}
	
	public void modeAuto() throws InterruptedException, JSONException {
		while(this.robot.RobotIsOk ||this.gestionCamera.findQRCode){
			Thread.sleep(500);
		}
		while(this.robot.RobotIsOk==false || this.gestionCamera.findQRCode== false) {
				this.sr.EnvoiDriveRobot(40,0,8);
				Thread.sleep(8000);
				this.sr.EnvoiDriveRobot(0,0,0);
				checkAround();
		}
		this.vPast = this.vCurrent;
		this.vCurrent = pathCycle();
		Edge currentEdge = getCurrentEdge(this.vPast,this.vCurrent);
		System.out.println("currentedge"+currentEdge);
		if(currentEdge!=null){
			int angle;
			if(this.vPast.equals(currentEdge.getVertA())){
				angle = currentEdge.getActionToB();
			}else{
				angle = currentEdge.getActionToA();
			}
			System.out.println(angle);
			this.sr.EnvoiRotateRobot(angle);
		}	
	}
	
	public void checkAround() throws JSONException, InterruptedException{
		if(!this.robot.RobotIsOk ||!this.gestionCamera.findQRCode){
			int i = 0;
			while(i!=8 && this.gestionCamera.findQRCode == false){
				this.sr.EnvoiRotateRobot(10);
				Thread.sleep(100);
				this.sr.EnvoiRotateRobot(0);
				Thread.sleep(3000);
				i=i+1;	
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
		return null;//degeulasse
	}
	
    public Vertex pathCycle(){
    	for(Vertex v:this.regularPath){
    		if(v.getId()==this.vCurrent.getId()){
    			if(regularPath.indexOf(v)==regularPath.size()-1){
    				return regularPath.get(0);
    			}else{
    				System.out.println((regularPath.indexOf(v))+1);
    				System.out.println("item"+regularPath.get((regularPath.indexOf(v))+1));
    				return regularPath.get(regularPath.indexOf(v)+1);
    			}
    		}
    	}
		return this.vCurrent;
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