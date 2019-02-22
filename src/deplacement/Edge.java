package deplacement;

public class Edge {
	private int distance;
	private Vertex vertA;
	private Vertex vertB;
	private int actionToA;
	private int actionToB;
	public Edge(int d, Vertex va, Vertex vb, int aa, int ab){
		this.distance=d;
		this.vertA=va;
		this.vertB=vb;
		this.actionToA=aa;
		this.actionToB=ab;
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
	public int getActionToA() {
		return actionToA;
	}
	public void setActionToA(int actionToA) {
		this.actionToA = actionToA;
	}
	public int getActionToB() {
		return actionToB;
	}
	public void setActionToB(int actionToB) {
		this.actionToB = actionToB;
	}
	
}