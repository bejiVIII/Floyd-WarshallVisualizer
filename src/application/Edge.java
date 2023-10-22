package application;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Edge extends Arrow {
	
	private Vertex source;
	private Vertex target;

	private int value;
    private Text valueText = new Text();

	public Edge(int value, Vertex source, Vertex target, double startX, double startY, double endX, double endY, double arrowHeadSize)
	{
		super(startX, startY, endX, endY, arrowHeadSize);
		
		this.value = value;
		this.source = source;
		this.target = target;
		
		valueText.setId(String.valueOf(value));
		valueText.setX((startX + endX) / 2);
		valueText.setY((startY + endY) / 2);
		valueText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16.0)); 
		valueText.setText(String.valueOf(value));
		valueText.setFill(Color.BLUE);
	}
	
	public Text getValueText()
	{
		return valueText;
		
	}
	
	public Vertex getVSource()
	{
		return source;
	}
	
	public Vertex getVTarget()
	{
		return target;
	}
	
	public int getValue()
	{
		return value;
	}
}
