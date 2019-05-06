package deplacement;

import java.util.ArrayList;


public class ShortestPath {
	private Map map;
	private Vertex vOrigin;
	private Vertex vCurrent;
	private ArrayList<Vertex> correctPath;
	private ArrayList<Vertex> predecessor;
	private ArrayList<Integer> distance;
	private ArrayList<Vertex> adjacentVertex;
	private ArrayList<Vertex> uncheckedVertex;
	private ArrayList<Vertex> neighbor;
	private static final int maxValueDistance=5000;
	public ShortestPath(Map m,Vertex v){
		this.map=m;
		this.vOrigin=v;
		this.correctPath=new ArrayList<Vertex>();
		this.adjacentVertex=new ArrayList<Vertex>();
		this.uncheckedVertex=new ArrayList<Vertex>();
		this.neighbor=new ArrayList<Vertex>();
		this.predecessor=new ArrayList<Vertex>();
		this.distance=new ArrayList<Integer>();
		for(int i=0;i<map.vertices.size();i++) {
			this.predecessor.add(null);
			this.distance.add(maxValueDistance);
		}
		
	}
	
	public void getAllPaths() {
		getShortestPathToAllVertices();
		System.out.println(predecessor);
		System.out.println(distance);
	}
	
	public void getPath(Vertex vDestination) {
		getShortestPathToAllVertices();
		Vertex vFind = findIndexElement(vDestination);
		this.correctPath.add(vFind);
		while(vFind!=this.vOrigin) {
			//Y'a un bug ici
			this.correctPath.add(0,this.predecessor.get(this.map.vertices.indexOf(vFind)));
			vFind=this.predecessor.get(this.map.vertices.indexOf(vFind));
		}
	}
	
	public void getShortestPathToAllVertices(){
		int numTotalVertices=this.map.vertices.size();
		for(int i=0;i<this.map.vertices.size();i++) {
			this.uncheckedVertex.add(this.map.vertices.get(i));
		}
		this.adjacentVertex.add(this.vOrigin);
		Vertex vFind = findIndexElement(this.vOrigin);
		this.distance.set(this.map.vertices.indexOf(vFind), 0);
		for(int i=0;i<numTotalVertices;i++) {
			this.vCurrent=this.adjacentVertex.get(i);
			getNeighbor(this.vCurrent);
			addToAdjacentList();
			for(Vertex v:this.neighbor) {
				if(this.uncheckedVertex.contains(v)) {
					setPredecessorAndDistance(this.vCurrent,v,getCurrentEdge(this.vCurrent,v).getDistance());
				}
			}
			this.uncheckedVertex.remove(this.vCurrent);
		}
		this.adjacentVertex.clear();
	}
	
	public Vertex findIndexElement(Vertex v) {
		for(int i=0;i<this.map.vertices.size();i++) {
			if(v.getName()== this.map.vertices.get(i).getName()) {
				return this.map.vertices.get(i);
			}
		}
		return v;
	}
	
	public void getNeighbor(Vertex v) {
		neighbor.clear();
		for(Edge e:map.edges) {
			if(e.getVertA().getId()==(v.getId())) {
				neighbor.add(e.getVertB());
			}
			else if(e.getVertB().getId()==(v.getId())) {
				neighbor.add(e.getVertA());
			}
		}	
	}
	
	public void addToAdjacentList() {
		for(Vertex v:this.neighbor) {
			if(this.uncheckedVertex.contains(v) && !this.adjacentVertex.contains(v)) {
				this.adjacentVertex.add(v);
			}
			
		}
	}
	
	public void setPredecessorAndDistance(Vertex vStart, Vertex vDestination, int weight) {
		int currentDistance;
		Vertex vFindStart = findIndexElement(vStart);
		if(distance.get(this.map.vertices.indexOf(vFindStart)).equals(maxValueDistance)) {
			currentDistance=weight;
		}else {
			currentDistance=distance.get(this.map.vertices.indexOf(vFindStart))+weight;
		}
		Vertex vFindDestination = findIndexElement(vDestination);
		if(distance.get(this.map.vertices.indexOf(vFindDestination))>currentDistance) {
			this.predecessor.set(this.map.vertices.indexOf(vFindDestination), vStart);
			this.distance.set(this.map.vertices.indexOf(vFindDestination),currentDistance);
		}
	}
	
	private Edge getCurrentEdge(Vertex va,Vertex vb) {
		for(Edge e:this.map.edges) {
			if((va.getId()==e.getVertA().getId()&&vb.getId()==e.getVertB().getId())||(vb.getId()==e.getVertA().getId())&&va.getId()==e.getVertB().getId()) {
				return(e);
			}
		}
		return null;
	}
	
	public ArrayList<Vertex> getCorrectPath() {
		return correctPath;
	}
	
	public void setCorrectPath(ArrayList<Vertex> correctPath) {
		this.correctPath = correctPath;
	}		
}
