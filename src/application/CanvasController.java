package application;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CanvasController {
	
	@FXML
	private Pane mainPane;
	
	public void click()
	{
		mainPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getButton() == MouseButton.PRIMARY)
				{
					System.out.println("x: " + event.getSceneX());
					System.out.println("Y: " + event.getSceneY());
					
					Circle c = new Circle();
					c.setCenterX(event.getSceneX());
					c.setCenterY(event.getSceneY());
					c.setRadius(20);
					c.setFill(Color.RED);
					
					mainPane.getChildren().add(c);
				}
				if(event.getButton() == MouseButton.SECONDARY)
				{
					mainPane.getChildren().remove(event.getTarget());
				}
			}
			
		});
	}
	
	public void clearCanvas()
	{
		ObservableList<Node> children = mainPane.getChildren();
		
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Circle")));
	}
}
