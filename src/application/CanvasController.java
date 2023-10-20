package application;

import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CanvasController {
	
	@FXML
	private Pane mainPane;
	@FXML
	private Button clearButton;
	
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	
	public void click()
	{
		mainPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event)
			{
				if(event.getButton() == MouseButton.PRIMARY)
				{
//					System.out.println("x: " + event.getSceneX());
//					System.out.println("Y: " + event.getSceneY());
					
			        TextInputDialog td = new TextInputDialog(); 
			        td.setHeaderText("Introduceti numele varfului!");
			        td.setContentText("Numele varfului: ");
			        td.setTitle("Nume varf");

			        Optional<String> result = td.showAndWait();
			        if (result.isPresent() && !td.getEditor().getText().isEmpty()) { // ???
			        	String vName = td.getEditor().getText();
			        	
			        	Circle c = new Circle();
			        	c.setId(vName);
						c.setCenterX(event.getSceneX());
						c.setCenterY(event.getSceneY());
						c.setRadius(20);
						c.setFill(Color.RED);
						
						
						Text t = new Text();
						t.setId(vName);
						t.setX(event.getSceneX() - 8);
						t.setY(event.getSceneY() + 5);
					    t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14)); 

						t.setText(vName);
						t.setFill(Color.WHITE);
						
						mainPane.getChildren().add(c);
						mainPane.getChildren().add(t);
				        vertices.add(new Vertex(vName));
				        System.out.println("Added vertex with name: " + vName);
			        } else {
			        	Alert a = new Alert(AlertType.ERROR, "NUME VARFUS!!", ButtonType.OK);
			        	a.show();
			        	return;
			        }
			        
			        System.out.print("list: ");
			        for(Vertex vertex : vertices)
			        {
			        	System.out.print(vertex.getName() + " ");
			        }
			        System.out.println("\n");

				}
				if(event.getButton() == MouseButton.SECONDARY)
				{
					EventTarget eventTarget = event.getTarget();
					
					if(eventTarget.toString().contains("AnchorPane"))
					{
						return;
					}
					else
					{
						if(eventTarget.toString().contains("Text"))
						{
							Text t = (Text)event.getTarget();
							
							mainPane.getChildren().remove(eventTarget);
							
							String vName = t.getText();

							mainPane.getChildren().removeIf(child -> (child.getId().equals(vName) && child.getTypeSelector().equals("Circle")));
							
							for(int i = 0; i < vertices.size(); i++)
							{
								if(vertices.get(i).getName().equals(t.getId()))
								{
									vertices.remove(i);
								}
							}
							System.out.print("list after deleted from text: ");
					        for(Vertex vertex : vertices)
					        {
					        	System.out.print(vertex.getName() + " ");
					        }
					        System.out.println("\n");
						}
						else
						{
							Circle c = (Circle)event.getTarget(); // !!!
							//you can cast "EventTarget" to "Circle" because
							//"Circle" implements "EventTarget" 
							String vName = c.getId();
							
							mainPane.getChildren().remove(eventTarget);
							mainPane.getChildren().removeIf(child -> (child.getId().equals(vName) && child.getTypeSelector().equals("Text")));

							
							for(int i = 0; i < vertices.size(); i++)
							{
								if(vertices.get(i).getName().equals(c.getId()))
								{
									vertices.remove(i);
								}
							}
							
							System.out.print("list: ");
					        for(Vertex vertex : vertices)
					        {
					        	System.out.print(vertex.getName() + " ");
					        }
					        
					        System.out.println("\n");
						}
						
					}
				}
			}
			
		});
	}
	
	public void clearCanvas()
	{
		ObservableList<Node> children = mainPane.getChildren();
		
		vertices.clear();
		System.out.print("list size after clear button pressed: " + vertices.size());
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Circle")));
	}
}
