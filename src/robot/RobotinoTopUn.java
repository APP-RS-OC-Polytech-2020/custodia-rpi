package robot;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import deplacement.Edge;
import deplacement.Vertex;
import deplacement.ShortestPath;
import deplacement.Map;
import camera.GestionCamera;


public class RobotinoTopUn implements Runnable{
	private ArrayList<Vertex> v;
	private ArrayList<Edge> e;
	private ArrayList<Vertex> regularPath;
	private ShortestPath sp;
	private Vertex vCurrent;
	private Vertex vPast;
	private Robot robot;
	private Timer timer;
	private GestionCamera gestionCamera;
	
	public RobotinoTopUn(Robot r){
		this.vCurrent = new Vertex("SalleAPP1",1);
		this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
            	if(robot.isManual==false) {
            		try {
						checkAround();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	}
                
            }
        }, 0, 35);
        this.gestionCamera = new GestionCamera(r);
        new Thread(this.gestionCamera).start();
        
		ArrayList<Vertex> v=new ArrayList<Vertex>();
		v.add(new Vertex("SalleAPP1",1));
		v.add(new Vertex("SalleAPP2",2));
		v.add(new Vertex("SortieSalleAPP",3));
		v.add(new Vertex("SortieSalleGauche",4));
		v.add(new Vertex("SortieSalleDroite",5));

		ArrayList<Edge> e=new ArrayList<Edge>();
		e.add(new Edge(7,v.get(0),v.get(1),180,180));
		e.add(new Edge(9,v.get(1),v.get(2),180,90));
		e.add(new Edge(14,v.get(2),v.get(3),180,-90));
		e.add(new Edge(10,v.get(2),v.get(4),180,90));
		
		
		ArrayList<Vertex> regularPath=new ArrayList<Vertex>();
		regularPath.add(v.get(0));
		regularPath.add(v.get(1));
		regularPath.add(v.get(2));
		regularPath.add(v.get(3));
		regularPath.add(v.get(2));
		regularPath.add(v.get(4));
		regularPath.add(v.get(2));
		regularPath.add(v.get(1));
		
		this.robot = r;
		this.vCurrent = pathCycle();
	}
	
	
	public void newError(Vertex vOrigin, Vertex vDestination){
		sp=new ShortestPath(new Map(v,e),vOrigin);
		sp.getAllPaths();
		sp.getPath(vDestination);
	}
	
	public void modeAuto() throws InterruptedException {
		while(robot.RobotIsOk ||this.gestionCamera.findQRCode){
			Thread.sleep(500);
		}
		while(robot.RobotIsOk==false || this.gestionCamera.findQRCode== false) {
				this.robot.drive(90, 0, 0, 15);
		}
		this.vPast = this.vCurrent;
		this.vCurrent = pathCycle();
		Edge currentEdge = getCurrentEdge(this.vPast,this.vCurrent);
		if(currentEdge!=null){
			int angle;
			if(this.vPast.equals(currentEdge.getVertA())){
				angle = currentEdge.getActionToB();
			}else{
				angle = currentEdge.getActionToA();
			}
			this.robot.rotate(angle);
		}	
	}
	
	public Edge getCurrentEdge(Vertex vPast,Vertex vCurrent){
		for(Edge edges: this.e){
			if(edges.getVertA().equals(vPast)||edges.getVertB().equals(vPast)){
				if(edges.getVertA().equals(vCurrent)||edges.getVertB().equals(vCurrent)){
					return edges;
				}
			}
		}
		return null;//degeulasse
	}
	
	//fonction qui se declenche toutes les 35 secondes quand mode auto activé
	public void checkAround() throws InterruptedException {
		if(robot.isManual==false) {
			int i = -90;
			while(i!=90){
				robot.rotate(10);
				Thread.sleep(1000);
				i=i+10;
			}
		}
		robot.rotate(-90);
	}
	
	
    //need to remove regularPath from parameter
    public Vertex pathCycle(){
    	for(Vertex v:regularPath){
    		if(v.equals(this.vCurrent)){
    			if(regularPath.indexOf(v)==regularPath.size()-1){
    				return regularPath.get(0);
    			}else{
    				return regularPath.get(regularPath.indexOf(v));
    			}
    		}
    	}
		return this.vCurrent;
    }
    
    public void run() {
    	if(robot.isManual==false) {
    		try {
				modeAuto();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
	public void start(){
		System.out.println("Initialisation mode auto");
	}
}