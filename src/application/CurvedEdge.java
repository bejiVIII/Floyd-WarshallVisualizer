package application;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CurvedEdge extends CubicCurve {
	
	private Text textValue = new Text();
	private int value;
	private Vertex source;
	private Vertex target;

	public CurvedEdge(
			int value,
			Vertex source,
			Vertex target,
			double startX,
			double startY,
			double controlX1,
			double controlY1,
			double controlX2,
			double controlY2,
			double endX,
			double endY
			)
	{
		super();
		this.value = value;
		this.source = source;
		this.target = target;
		
		this.setStroke(Color.BLACK);
		this.setStrokeWidth(1);
		this.setStartX(startX);
		this.setStartY(startY);
		this.setControlX1(controlX1);
		this.setControlX2(controlX2);
		this.setControlY1(controlY1);
		this.setControlY2(controlY2);
		this.setEndX(endX);
		this.setEndY(endY);
		this.setFill(null);
		
		textValue.setId(String.valueOf(value));
		textValue.setX(startX - 100);
		textValue.setY(startY - 100);
		textValue.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14.0)); 
		textValue.setText(String.valueOf(value));
		textValue.setFill(Color.BLUE);
	}
	
	public Text getTextValue()
	{
		return textValue;
	}
	
	
}
