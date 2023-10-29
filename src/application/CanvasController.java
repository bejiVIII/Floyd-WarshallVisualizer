package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
	int numberOfVertex = 1;
	
	private ArrayList<Vertex> vertices = new ArrayList<Vertex>();
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private ArrayList<CurvedEdge> curvedEdges = new ArrayList<CurvedEdge>();
	private ArrayList<String> shortestPath = new ArrayList<String>();
	
	String[][] V;
	String[][] P;
	
	//TODO: make the canvas zoomable and pannable.
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		placingComboBox.getItems().add("VARF");
		placingComboBox.getItems().add("ARC");
		
		mainPane.addEventHandler(MouseEvent.MOUSE_CLICKED, hnd);
        mainPane.setOnScroll(this::imageScrolled);
        //check mainscene
        resultsPane.setMaxHeight(Double.POSITIVE_INFINITY);
        textFlowResults.setMaxHeight(Double.POSITIVE_INFINITY);
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
        	//System.out.println(event.getDeltaY());
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
					
					//TODO: what if we remove a vertex
	
					if(vertices.size() == 0)
					{
						numberOfVertex = 1;
					}
						
					Vertex vertex = new Vertex(numberOfVertex, event.getSceneX(), event.getSceneY(), 20, Color.RED);
	
					mainPane.getChildren().add(vertex);
					mainPane.getChildren().add(vertex.getIdText());
					vertices.add(vertex);

					System.out.println("Vertices size: " + vertices.size());
					System.out.println("Added vertex: " + numberOfVertex);
					
					numberOfVertex++;
					System.out.print("list: ");
					
					for(Vertex v : vertices)
					{
						System.out.print(v.getName() + " ");
					}
					System.out.println("\n");
				} 
				else // IF COMBOBOX IS ON "ARC"
		        {
					if(eventTarget.toString().contains("AnchorPane"))
					{
						for(Vertex sv : vertices)
						{
							if(sv.isSelected()) // IF VERTEX ALREADY SELECTED
							{
								sv.isSelected(false);
							}
						}
						
						return;
					}
					
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
						        						        
						        td.showAndWait();
						        
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
			//TODO: AFISARE SUCCESORII SI PREDECESORII VARFURILOR textFlowResults
			
			if(event.getButton() == MouseButton.SECONDARY)
			{
				if(eventTarget.toString().contains("AnchorPane"))
				{
					printSuccessorsAndPredecessors();
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
					
					//TODO:
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
	
	public void initializeAndPrintMatrices()
	{
		V = new String[vertices.size()][vertices.size()];
		P = new String[vertices.size()][vertices.size()];

		//Initialize V with "0" and P with "∅"
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
						int vertexSourceName = e.getVSource().getName();
						int vertexTargetName = e.getVTarget().getName();
						
						// i + 1 and j + 1 because i and j start from 0 and
						// we have 1 -> n vertices
						if(vertexSourceName == i + 1 &&  vertexTargetName == j + 1)
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
				if(i != j && V[i][j] == "0")
				{	
					V[i][j] = String.valueOf(Double.POSITIVE_INFINITY);	
				}
			}
		}
		
		textFlowAppend("V: ", 20);
		printMatrixWithInfinityElements(V);
		
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
		
		textFlowAppend("P: ", 20);
		printMatrix(P);
	}
	
	public void printMatrix(String[][] m)
	{
		textFlowAppend();

		for(int i = 0; i < vertices.size(); i++)
		{
			for(int j = 0; j < vertices.size(); j++)
			{
				textFlowAppend(String.valueOf(m[i][j] + " "), 20);
			}
			textFlowAppend();
		}
		textFlowAppend();
		textFlowAppend();
	}
	
	public void printMatrixWithInfinityElements(String[][] m)
	{
		textFlowAppend();

		for(int i = 0; i < vertices.size(); i++)
		{
			for(int j = 0; j < vertices.size(); j++)
			{
				if(m[i][j].equals(String.valueOf(Double.POSITIVE_INFINITY)))
				{
					textFlowAppend("inf ", 20);
				}
				else
				{
					textFlowAppend(String.valueOf(m[i][j] + " "), 20);

				}
			}
			textFlowAppend();
		}
		textFlowAppend();
		textFlowAppend();
	}
	
	public void printSuccessorsAndPredecessors()
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
	}
	
	public void executeFloydWarshallAndPrint()
	{
		double altDist = 0.0;
		for(int k = 0; k < vertices.size(); k++)
		{
			textFlowAppend("T" + (k + 1) + "(V): ", 20);
			textFlowAppend();

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
					if(V[i][j].equals(String.valueOf(Double.POSITIVE_INFINITY)))
					{
						textFlowAppend("inf ", 20);
					}
					else
					{
						textFlowAppend(String.valueOf(V[i][j] + " "), 20);
					}
					
				}
				textFlowAppend();
			}
			
			textFlowAppend();
			textFlowAppend();
			
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
			
			textFlowAppend("P: ", 20);
			printMatrix(P);
		}
		
		textFlowAppend();
		textFlowAppend();
	}
	
	public void Execute()
	{
		textFlowResults.getChildren().clear();
		resultsPane.setVisible(true);
		
		initializeAndPrintMatrices();
		executeFloydWarshallAndPrint();
		
		printPaths();
	}
	
	public void textFlowAppend(String text, double fontSize)
	{
		Text t = new Text(text);
		t.setFont(Font.font("verdana", FontWeight.BOLD, fontSize));
		textFlowResults.getChildren().add(t);
	}
	
	public void textFlowAppend()
	{
		Text t = new Text(String.valueOf("\n"));
		textFlowResults.getChildren().add(t);
	}
	
	public void printPaths()
	{
		textFlowAppend("Drumurile de cost minim: \n", 20);
		
		for(int i = 1; i <= vertices.size(); i++)
		{
			for(int j = 1; j <= vertices.size(); j++)
			{
				if(i == j) continue;
				textFlowAppend("[" + i + ", " + j + "]: ", 20);
				doStuff(i, j);	
			}
		}
	}
	
	public void doStuff(int source, int dest)
	{
		if(shortestPath.size() == 0)
		{
			shortestPath.add(String.valueOf(dest));
		}
		if(P[source - 1][dest - 1].equals("∅"))
		{
			ArrayList<Integer> intArr = new ArrayList<Integer>();
			
			for(int i = 0; i < shortestPath.size(); i++)
			{
				intArr.add(Integer.valueOf(shortestPath.get(i)));
			}
			
			Collections.reverse(intArr);
			
			for(int i = 0; i < intArr.size(); i++)
			{
				if(i != intArr.size() - 1)
				{
					textFlowAppend(intArr.get(i) + "->", 20);
				}
				else
				{
					textFlowAppend(String.valueOf(intArr.get(i)), 20);
				}
			}
			
			shortestPath.clear();
			intArr.clear();
			textFlowAppend();
			return;
		}
		else
		{
			shortestPath.add(P[source - 1][dest - 1]);
		}
		
		doStuff(source, Integer.valueOf(P[source - 1][dest - 1]));
	}
	
	public void clearCanvas()
	{
		vertices.clear();
		edges.clear();
		System.out.print("list size after clear button pressed: " + vertices.size() + "\n");
		
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Vertex")));
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Text")));
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("Edge")));
		mainPane.getChildren().removeIf(child -> (child.getTypeSelector().equals("CurvedEdge")));
		
		textFlowResults.getChildren().clear();
	}
}
