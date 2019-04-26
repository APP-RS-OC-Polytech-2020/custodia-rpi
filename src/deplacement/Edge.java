package deplacement;

public class Edge {
	private int distance;
	private Vertex vertA;
	private Vertex vertB;
	private float actionToA;
	private float actionToB;
	public Edge(int d, Vertex va, Vertex vb, float e, float ab){
		this.distance=d;
		this.vertA=va;
		this.vertB=vb;
		this.actionToA=e;
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
	public float getActionToA() {
		return actionToA;
	}
	public void setActionToA(int actionToA) {
		this.actionToA = actionToA;
	}
	public float getActionToB() {
		return actionToB;
	}
	public void setActionToB(int actionToB) {
		this.actionToB = actionToB;
	}
	
}