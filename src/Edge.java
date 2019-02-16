package Robot;

public class Edge {
	private int distance;
	private Vertex vertA;
	private Vertex vertB;
	public Edge(int d, Vertex va, Vertex vb){
		this.distance=d;
		this.vertA=va;
		this.vertB=vb;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public Vertex getVertA() {
		return vertA;
	}
	public void setVertA(Vertex vertA) {
		this.vertA = vertA;
	}
	public Vertex getVertB() {
		return vertB;
	}
	public void setVertB(Vertex vertB) {
		this.vertB = vertB;
	}
	
}