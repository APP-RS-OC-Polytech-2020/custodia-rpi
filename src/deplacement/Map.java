package deplacement;

import java.util.ArrayList;

public class Map {
	ArrayList<Vertex> vertices;
	ArrayList<Edge> edges;
	public Map(ArrayList<Vertex> v,ArrayList<Edge> e){
		this.vertices=v;
		this.edges=e;
	}	
}