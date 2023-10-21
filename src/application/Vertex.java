package application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Vertex extends Circle {
	
	private String name;
	private List<Edge> adjacents = new ArrayList<Edge>();
    private List<Edge> revAdjacents = new ArrayList<Edge>();
    private double x;
    private double y;
    private boolean isSelected;
    
    //position of the vertex in the canvas?
	
    public Vertex()
    {
    	
    }
    
	public Vertex(String name, double x, double y)
	{
		//TODO: see if you can work with the Circle extension
		super();
		this.name = name;
		this.x = x;
		this.y = y;
		isSelected = false;
	}

	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void isSelected(boolean value)
	{
		isSelected = value;
		
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public List<Edge> getAdjacents()
	{
		return adjacents;
	}
	
	public List<Edge> getRevAdjacents()
	{
		return revAdjacents;
	}
	
	public void addAdjacent(Edge edge)
	{
		adjacents.add(edge);
	}
	
	public void addRevAdjacent(Edge edge)
	{
		revAdjacents.add(edge);
	}
	
}
