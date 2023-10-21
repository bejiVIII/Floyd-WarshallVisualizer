package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class CanvasController implements Initializable
{
	
	@FXML
	private Pane mainPane;
	@FXML
	private Button clearButton;
	@FXML
	private ComboBox<String> placingComboBox = new ComboBox<String>();;
	
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	
	private boolean isSelecting = false;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		System.out.println("in init");
		
		placingComboBox.getItems().add("VARF");
		placingComboBox.getItems().add("ARC");

		mainPane.addEventHandler(MouseEvent.MOUSE_CLICKED, hnd);

		System.out.println("sus");
	}
	
	
	
	EventHandler<MouseEvent> hnd = new EventHandler<MouseEvent>()
	{
		public void handle(MouseEvent event) {
			EventTarget eventTarget = event.getTarget();

			if(event.getButton() == MouseButton.PRIMARY)
			{

				if(placingComboBox.getValue() == null || placingComboBox.getValue() == "VARF")
				{
					TextInputDialog td = new TextInputDialog(); 
			        td.setHeaderText("Introduceti numele varfului!");
			        td.setContentText("Numele varfului: ");
			        td.setTitle("Nume varf");
			        
			        // TODO: the length of the vName must be <5.
			        
			        Optional<String> result = td.showAndWait();
			        
		        	String vName = td.getEditor().getText();

			        if (result.isPresent() && !vName.isEmpty()) { // ???
			        	if(vertices.size() > 0)
			        	{
			        		for(Vertex v: vertices)
				        	{
				        		if(v.getName().equals(vName))
				        		{
						        	//System.out.println(vName);
						        	//System.out.println(v.getName());

				        			Alert a = new Alert(AlertType.ERROR, "UN VARF CU ACEST NUME EXISTA DEJA!", ButtonType.OK);
						        	a.show();
						        	return;
				        		}
				        	}
			        	}
			        		
			        	Circle vertex = new Circle();
			        	vertex.setId(vName);
			        	vertex.setCenterX(event.getSceneX());
			        	vertex.setCenterY(event.getSceneY());
			        	vertex.setRadius(20);
			        	vertex.setFill(Color.RED);
						
						Text t = new Text();
						t.setId(vName);
						t.setX(event.getSceneX() - 8);
						t.setY(event.getSceneY() - 30);
					    t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 14)); 

						t.setText(vName);
						t.setFill(Color.BLACK);
						
						mainPane.getChildren().add(vertex);
						mainPane.getChildren().add(t);
				        vertices.add(new Vertex(vName, event.getSceneX(), event.getSceneY()));
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
				else // IF COMBOBOX IS ON "ARC"
		        {		
					//TODO: BUCLA
					if(eventTarget.toString().contains("Circle"))
					{
						Circle c = (Circle)eventTarget;

						for(Vertex v : vertices)
						{
							if(v.isSelected())
							{
								System.out.println("A VERTEX IS SELECTED");
								
								double startX = v.getX();
								double startY = v.getY();
								double endX = c.getCenterX();
								double endY = c.getCenterY();
								
								Arrow a = new Arrow(startX, startY, endX, endY);
								
								mainPane.getChildren().add(a);
								
								v.isSelected(false);
								return;
							}
						}
						
						for(Vertex v : vertices)
						{
							if(v.getName().equals(c.getId()))
							{
								v.isSelected(true);
								//c.setFill(Color.DARKRED);
							}
						}
						
						for(Vertex v : vertices)
						{
							System.out.println(v.getName() + "[x: " + v.getX() + ", y: " + v.getY() + "]");
						}
					}
		        }
		    }
			
			if(event.getButton() == MouseButton.SECONDARY)
			{
				//TODO: edge remove uwu
				if(eventTarget.toString().contains("AnchorPane"))
				{
					return;
				}
				else if(eventTarget.toString().contains("Text"))
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
					
				}
				
			}
			else
			{
				
			}
		}
	};
	
	public void clearCanvas()
	{
		vertices.clear();
		System.out.print("list size after clear button pressed: " + vertices.size());
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Circle")));
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Text")));

	}

}
