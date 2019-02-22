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
	private Robot robot;
	private Timer timer;
	private GestionCamera gestionCamera;
	private boolean findQRCode = false;
	
	public RobotinoTopUn(Robot r){
		this.vCurrent = new Vertex("SalleAPP1",1);
		this.timer = new Timer();
        this.timer.schedule(new TimerTask() {

            @Override
            public void run() {
            	if(robot.isManual==false) {
            		checkAround();
            	}
                
            }
        }, 0, 5);
        this.gestionCamera = new GestionCamera(r);
        
		ArrayList<Vertex> v=new ArrayList<Vertex>();
		v.add(new Vertex("SalleAPP1",1));
		v.add(new Vertex("SalleAPP2",2));
		v.add(new Vertex("SortieSalleAPP",3));
		v.add(new Vertex("SortieSalleGauche",4));
		v.add(new Vertex("SortieSalleDroite",5));

		ArrayList<Edge> e=new ArrayList<Edge>();
		e.add(new Edge(7,v.get(0),v.get(1)));
		e.add(new Edge(9,v.get(1),v.get(2)));
		e.add(new Edge(14,v.get(2),v.get(3)));
		e.add(new Edge(10,v.get(2),v.get(4)));
		
		
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
	}
	
	
	public void newError(Vertex vOrigin, Vertex vDestination){
		sp=new ShortestPath(new Map(v,e),vOrigin);
		sp.getAllPaths();
		sp.getPath(vDestination);
	}
	
	public void modeAuto() throws InterruptedException {
		this.robot.RobotIsOk= false;
		this.vCurrent = pathCycle();
		//Savoir comment on recupere info sur la direction + comment lancer mode Auto
		while(robot.RobotIsOk==false) {
			while(this.findQRCode== false) {
				this.robot.drive(90, 0, 0, 15);
			}
		}
		
		
	}
	//fonction qui se declenche toute les 5 secondes quand mode auto activé
	public void checkAround() {
		
		
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
}
