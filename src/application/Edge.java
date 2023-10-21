package application;

public class Edge {
	
	private Vertex source;
	private Vertex target;
	private double weigth;
	
	//valoarea arcului
	
	public Edge(Vertex source, Vertex target, double weigth)
	{
		this.source = source;
		this.target = target;
		this.weigth = weigth;
	}
}
