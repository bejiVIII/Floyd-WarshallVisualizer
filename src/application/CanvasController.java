package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class CanvasController implements Initializable
{
	
	@FXML
	private Pane mainPane;
	@FXML
	private Button clearButton;
	@FXML
	private ComboBox<String> placingComboBox = new ComboBox<String>();
	@FXML
	private TextFlow textFlowResults = new TextFlow();
	@FXML
	private ScrollPane resultsPane;
	
	
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private ArrayList<CurvedEdge> curvedEdges = new ArrayList<CurvedEdge>();
	String[][] V;
	String[][] P;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		placingComboBox.getItems().add("VARF");
		placingComboBox.getItems().add("ARC");
		
		mainPane.addEventHandler(MouseEvent.MOUSE_CLICKED, hnd);
        mainPane.setOnScroll(this::imageScrolled);
        //check mainscene
        resultsPane.setVisible(false);
        //textFlowResults.setStyle("-fx-background-color: black;");
        
		System.out.println("Main controller initialized.");
	}
	
	private void setImageZoom(double factor) {
        mainPane.setScaleX(factor);
        mainPane.setScaleY(factor);
    }
	
	private void imageScrolled(ScrollEvent event) {
        // When holding CTRL mouse wheel will be used for zooming
        if (event.isControlDown()) {
        	System.out.println(event.getDeltaY());
            double delta = event.getDeltaY();
            double adjust = delta / 1000.0;
            double zoom = Math.min(10, Math.max(0.1, mainPane.getScaleX() + adjust));
            if(zoom < 1.0 || zoom > 2.0)
            {
            	return;
            }
            setImageZoom(zoom);
            event.consume();
        }
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
				        		if(String.valueOf(v.getId()).equals(vName))
				        		{
				        			Alert a = new Alert(AlertType.ERROR, "UN VARF CU ACEST NUME EXISTA DEJA!", ButtonType.OK);
						        	a.show();
						        	return;
				        		}
				        	}
			        	}			       
			        	
						try 
						{ 
							Integer.parseInt(vName); 
						}  
						catch (NumberFormatException e)  
						{
							Alert a = new Alert(AlertType.ERROR, "INTRODUCETI UN NUMAR!", ButtonType.OK);
				        	a.show();
				        	return;
						} 
						
			        	Vertex vertex = new Vertex(Integer.valueOf(vName), event.getSceneX(), event.getSceneY(), 20, Color.RED);

						mainPane.getChildren().add(vertex);
						mainPane.getChildren().add(vertex.getIdText());
				        vertices.add(vertex);
				        
				        //HERE w
				   
				        System.out.println("Vertices size: " + vertices.size());
				        
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
					System.out.println(eventTarget.toString());
					if(eventTarget.toString().contains("Circle"))
					{
						Vertex tv = (Vertex)eventTarget;

						for(Vertex sv : vertices)
						{
							if(sv.isSelected()) // IF VERTEX ALREADY SELECTED
							{
								System.out.println("A VERTEX IS SELECTED");
								
								TextInputDialog td = new TextInputDialog(); 
						        td.setHeaderText("Introduceti valoarea arcului! (Numar)");
						        td.setContentText("Valoarea arcului: ");
						        td.setTitle("Valoare arc");
						        
						        // TODO: the length of the vName must be <5.
						        
						        Optional<String> result = td.showAndWait(); // ??
						        
					        	String edgeValue = td.getEditor().getText();
					        	
						        if (!edgeValue.isEmpty())
						        {
						        	double startX = sv.getCenterX();
									double startY = sv.getCenterY();
									double endX = tv.getCenterX();
									double endY = tv.getCenterY();
									
						        	if(sv.getName() == tv.getName()) //BUCLA
						        	{
						        		System.out.println(edgeValue);
						        		System.out.println("BUCLA");
						        		try 
										{ 
											Integer.parseInt(edgeValue); 
										}  
										catch (NumberFormatException e)  
										{
											Alert a = new Alert(AlertType.ERROR, "INTRODUCETI UN NUMAR!", ButtonType.OK);
								        	a.show();
								        	return;
										} 
						        		
						        		CurvedEdge ce = new CurvedEdge(
						        				Integer.valueOf(edgeValue),
						        				sv,
						        				tv,
						        				startX,
						        				startY,
						        				startX - 280,
						        				startY,
						        				startX,
						        				startY - 280,
						        				startX,
						        				startY);
						        		
						        		curvedEdges.add(ce);
										//mdah
						        		sv.addSuccessor(tv);
						        		tv.addPredecessor(sv);
						        		sv.setFill(Color.RED);
						        	    mainPane.getChildren().add(ce);
										mainPane.getChildren().add(ce.getTextValue());
						        	}
						        	else // DACA NU E BUCLA
						        	{
						        		try 
										{ 
											Integer.parseInt(edgeValue); 
										}  
										catch (NumberFormatException e)  
										{
											Alert a = new Alert(AlertType.ERROR, "INTRODUCETI UN NUMAR!", ButtonType.OK);
								        	a.show();
								        	return;
										} 						        		
						        		
						        		System.out.println(edgeValue);
						        		
						        		Edge e = new Edge(Integer.valueOf(edgeValue), sv, tv, startX, startY, endX, endY, 10.0);
						        		
						        		edges.add(e);
						        		sv.addSuccessor(tv);
						        		tv.addPredecessor(sv);
						        		
						        		sv.setFill(Color.RED);
										mainPane.getChildren().add(e);
										mainPane.getChildren().add(e.getValueText());
						        	}

									sv.isSelected(false);
									return;
						        }
						        else
						        {
						        	sv.isSelected(false);
						        	Alert a = new Alert(AlertType.ERROR, "NU A FOST INTRODUSA VALOAREA ARCULUI!", ButtonType.OK);
						        	a.show();
						        	return;
						        }
							}
						}
						
						for(Vertex v : vertices)
						{
							if(v.getName() == tv.getName())
							{
								v.isSelected(true);
								
								v.setFill(Color.DARKRED);
							}
						}
						
						for(Vertex v : vertices)
						{
							System.out.println(v.getName() + "[x: " + v.getCenterX() + ", y: " + v.getCenterY() + "]");
						}
					}
		        }
		    }
			
			//TODO: REMOVE NODES WITH RIGHT CLICK
			if(event.getButton() == MouseButton.SECONDARY)
			{
				
				if(eventTarget.toString().contains("AnchorPane"))
				{
					for(Vertex v : vertices)
					{
						System.out.print("Succesorii varfului " + v.getName() + ": ");
						for(Vertex s : v.getSuccessors())
						{
							System.out.print(s.getName() + " ");
						}
						System.out.println("");
						
						System.out.print("Predecesorii varfului " + v.getName() + ": ");
						for(Vertex s : v.getPredecessors())
						{
							System.out.print(s.getName() + " ");
						}
						System.out.println("");
					}
					return;
				}
				else if(eventTarget.toString().contains("Text"))
				{
					Text t = (Text)event.getTarget();

					mainPane.getChildren().remove(eventTarget);
						
					String vName = t.getText();

					System.out.println("children: ");
					for(int i = 0; i < mainPane.getChildren().size(); i++)
					{
						if(mainPane.getTypeSelector().equals("Vertex"))
						{
							System.out.println(mainPane.getChildren().get(i));
						}
					}
					
					//setid for vertices
					//mainPane.getChildren().removeIf(child -> (child.getId().equals(vName) && child.getTypeSelector().equals("Vertex")));
					
					
					for(int i = 0; i < vertices.size(); i++)
					{
						if(vertices.get(i).getName() == Integer.valueOf(t.getId()))
						{
							vertices.remove(i);
						}
					}
						
					System.out.print("list after deleted from text: ");
			        for(Vertex vertex : vertices)
			        {
			        	System.out.print(vertex.getId() + " ");
			        }
			        System.out.println("\n");
				}
				else if(eventTarget.toString().contains("CubicCurve"))
				{
					//TODO: edge remove uwu
					//TODO: ...
					//TODO: the arrow gets removed when the vertex associated with it gets removed.

				}
				
			}
		}
	};
	
	public void Execute()
	{

		resultsPane.setVisible(true);
		
		V = new String[vertices.size()][vertices.size()];
		P = new String[vertices.size()][vertices.size()];

		for(int i = 0; i < vertices.size(); i++)
		{
			for(int j = 0; j < vertices.size(); j++)
			{	
				V[i][j] = "0";
				P[i][j] = "∅";
			}
		}

		for(int i = 0; i < vertices.size(); i++)
		{
			for(int j = 0; j < vertices.size(); j++)
			{	
				if(i != j)
				{
					for(Edge e : edges)
					{
						if(e.getVSource().getName() == i + 1 && e.getVTarget().getName() == j + 1)
						{
							V[i][j] = String.valueOf(e.getValue());
							continue;
						}
					}
				}
			}
		}
		
		for(int i = 0; i < vertices.size(); i++)
		{
			for(int j = 0; j < vertices.size(); j++)
			{	
				if(i != j)
				{
					if(V[i][j] == "0")
					{
						V[i][j] = String.valueOf(Double.POSITIVE_INFINITY);
					}
				}
			}
		}
		Text tv = new Text("V: \n");
		tv.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		textFlowResults.getChildren().add(tv);
		
		for(int i = 0; i < vertices.size(); i++)
		{
			for(int j = 0; j < vertices.size(); j++)
			{
				if(V[i][j].equals(String.valueOf(Double.POSITIVE_INFINITY)))
				{
					Text t = new Text("inf ");
					t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
					textFlowResults.getChildren().add(t);
				}
				else
				{
					Text t = new Text(String.valueOf(V[i][j] + " "));
					t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
					textFlowResults.getChildren().add(t);
				}
				
			}
			Text t = new Text(String.valueOf("\n"));
			//∅
			textFlowResults.getChildren().add(t);
			System.out.println("");
		}
		Text vSpace = new Text(String.valueOf("\n\n"));
		//∅
		textFlowResults.getChildren().add(vSpace);
		System.out.println("");
		for(int i = 0; i < vertices.size(); i++)
		{
			for(int j = 0; j < vertices.size(); j++)
			{
				if(i == j || V[i][j].equals(String.valueOf(Double.POSITIVE_INFINITY)))
				{
					P[i][j] = "∅";
				}
				else if(i != j)
				{
					P[i][j] = String.valueOf(i + 1);
				}
			}
		}
		
		Text tp = new Text("P: \n");
		tp.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
		textFlowResults.getChildren().add(tp);
		for(int i = 0; i < vertices.size(); i++)
		{
			for(int j = 0; j < vertices.size(); j++)
			{
				Text t = new Text(String.valueOf(P[i][j] + " "));
				t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
				textFlowResults.getChildren().add(t);
			}
			Text t = new Text(String.valueOf("\n"));
			//∅
			textFlowResults.getChildren().add(t);
		}
		Text pSpace = new Text(String.valueOf("\n\n"));
		//∅
		textFlowResults.getChildren().add(pSpace);
		
		// THE ACTUAL ITERATIONS
		double altDist = 0.0;
		for(int k = 0; k < vertices.size(); k++)
		{
			Text tk = new Text("T" + (k + 1) + "(V): \n");
			tk.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			textFlowResults.getChildren().add(tk);
			for(int i = 0; i < vertices.size(); i++)
			{
				for(int j = 0; j < vertices.size(); j++)
				{
					if(V[i][k].equals("inf"))
					{
						altDist = Double.POSITIVE_INFINITY + Double.parseDouble(V[k][j]);
					}
					else if(V[k][j].equals("inf"))
					{
						altDist = Double.parseDouble(V[i][k]) + Double.POSITIVE_INFINITY; 
					}
					else if(V[i][k].equals("inf") && V[k][j].equals("inf"))
					{
						altDist = Double.POSITIVE_INFINITY + Double.POSITIVE_INFINITY; // which is double.positive_infinity
					}
					else
					{
						altDist = Double.parseDouble(V[i][k]) + Double.parseDouble(V[k][j]);
					}
					if(Double.parseDouble(V[i][j]) > altDist)
					{
						
						V[i][j] = String.valueOf((int)altDist);
						P[i][j] = String.valueOf(P[k][j]);
					}
					//display
					if(V[i][j].equals(String.valueOf(Double.POSITIVE_INFINITY)))
					{
						Text t = new Text("inf ");
						t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
						textFlowResults.getChildren().add(t);
					}
					else
					{
						Text t = new Text(String.valueOf(V[i][j] + " "));
						t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
						textFlowResults.getChildren().add(t);
					}
					
				}
				Text t = new Text(String.valueOf("\n"));
				textFlowResults.getChildren().add(t);
			}
			
			Text vkSpace = new Text(String.valueOf("\n\n"));
			textFlowResults.getChildren().add(vkSpace);
			
			for(int i = 0; i < vertices.size(); i++)
			{
				for(int j = 0; j < vertices.size(); j++)
				{
					if(i == j || V[i][j].equals(String.valueOf(Double.POSITIVE_INFINITY)))
					{
						P[i][j] = "∅";
					}
				}
			}
			
			Text tpk = new Text("P: \n");
			tpk.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
			textFlowResults.getChildren().add(tpk);
			for(int i = 0; i < vertices.size(); i++)
			{
				for(int j = 0; j < vertices.size(); j++)
				{
					Text t = new Text(String.valueOf(P[i][j] + " "));
					t.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));
					textFlowResults.getChildren().add(t);
				}
				Text t = new Text(String.valueOf("\n"));
				
				textFlowResults.getChildren().add(t);
			}
			
			Text t = new Text(String.valueOf("\n"));
		
			textFlowResults.getChildren().add(t);
			
		}
		
		Text pkSpace = new Text(String.valueOf("\n\n"));
		textFlowResults.getChildren().add(pkSpace);
	}
	
	public void clearCanvas()
	{
		vertices.clear();
		edges.clear();
		System.out.print("list size after clear button pressed: " + vertices.size());
		
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Vertex")));
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Text")));
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Edge")));
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("CurvedEdge")));
	}
}
