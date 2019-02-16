package Robot;

import java.util.ArrayList;

public class RobotinoTopUn {
	private ArrayList<Vertex> v;
	private ArrayList<Edge> e;
	private ArrayList<Vertex> regularPath;
	private ShortestPath sp;
	
	public RobotinoTopUn(){
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
	}
	
	
	public void newError(Vertex vOrigin, Vertex vDestination){
		sp=new ShortestPath(new Map(v,e),vOrigin);
		sp.getAllPaths();
		sp.getPath(vDestination);
	}
	
    //need to remove regularPath from parameter
    public Vertex pathCycle(Vertex vCurrent){
    	for(Vertex v:regularPath){
    		if(v.equals(vCurrent)){
    			if(regularPath.indexOf(v)==regularPath.size()-1){
    				return regularPath.get(0);
    			}else{
    				return regularPath.get(regularPath.indexOf(v));
    			}
    		}
    	}
		return vCurrent;
    }
}
