package Robot;

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
		this.correctPath.add(vDestination);
		while(vDestination!=this.vOrigin) {
			this.correctPath.add(0,this.predecessor.get(this.map.vertices.indexOf(vDestination)));
			vDestination=this.predecessor.get(this.map.vertices.indexOf(vDestination));
		}
		System.out.println(correctPath);
	}
	public void getShortestPathToAllVertices(){
		int numTotalVertices=this.map.vertices.size();
		for(int i=0;i<this.map.vertices.size();i++) {
			this.uncheckedVertex.add(this.map.vertices.get(i));
		}
		this.adjacentVertex.add(this.vOrigin);
		this.distance.set(this.map.vertices.indexOf(this.vOrigin), 0);
		//while(this.uncheckedVertex.size()!=0) {
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
		//}
		this.adjacentVertex.clear();
	}
	public void getNeighbor(Vertex v) {
		neighbor.clear();
		for(Edge e:map.edges) {
			if(e.getVertA().equals(v)) {
				neighbor.add(e.getVertB());
			}
			else if(e.getVertB().equals(v)) {
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
		if(distance.get(this.map.vertices.indexOf(vStart)).equals(maxValueDistance)) {
			currentDistance=weight;
		}else {
			currentDistance=distance.get(this.map.vertices.indexOf(vStart))+weight;
		}		
		if(distance.get(this.map.vertices.indexOf(vDestination))>currentDistance) {
			this.predecessor.set(this.map.vertices.indexOf(vDestination), vStart);
			this.distance.set(this.map.vertices.indexOf(vDestination),currentDistance);
		}	
	}
	private Edge getCurrentEdge(Vertex va,Vertex vb) {
		for(Edge e:this.map.edges) {
			if((va.equals(e.getVertA())&&vb.equals(e.getVertB()))||(vb.equals(e.getVertA())&&va.equals(e.getVertB()))) {
				return(e);
			}
		}
		return null;
	}
}