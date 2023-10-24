package application;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Vertex extends Circle {
	
	private int name;
	//private List<Edge> adjacents = new ArrayList<Edge>();
    //private List<Edge> revAdjacents = new ArrayList<Edge>();
    private boolean isSelected;
    private Text textId = new Text();
    private ArrayList<Vertex> successors; 
    private ArrayList<Vertex> predecessors;
    
    //position of the vertex in the canvas?
    
	public Vertex(int name, double x, double y, double radius, Color color)
	{
		//TODO: see if you can work with the Circle extension
		super();
		
		successors = new ArrayList<Vertex>();
		predecessors = new ArrayList<Vertex>();
		
		this.setId(String.valueOf(name));
		this.name =	name;
		this.setCenterX(x);
		this.setCenterY(y);
		this.setRadius(radius);
		this.setFill(color);
		
		textId.setId(String.valueOf(name));
		textId.setX(x - 8);
		textId.setY(y - 25);
		textId.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14.0)); 

		textId.setText(String.valueOf(name));
		textId.setFill(Color.BROWN);
		isSelected = false;
	}
	
	public void addSuccessor(Vertex v)
	{
		successors.add(v);
	}
	
	public void addPredecessor(Vertex v)
	{
		predecessors.add(v);
	}
	
	public ArrayList<Vertex> getSuccessors()
	{
		return successors;
	}
	
	public ArrayList<Vertex> getPredecessors()
	{
		return predecessors;
	}
	
	public int getName()
	{
		return name;
	}
	
	public Text getIdText()
	{
		return textId;
	}
	
	public boolean isSelected()
	{
		return isSelected;
	}
	
	public void isSelected(boolean value)
	{
		isSelected = value;
		if(value == true)
		{
			this.setFill(Color.DARKRED);
		}
		else
		{
			this.setFill(Color.RED);
		}
	}

}
