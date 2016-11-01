package de.deadlocker8.mines.application;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import de.deadlocker8.mines.logic.Board;
import de.deadlocker8.mines.logic.Settings;
import de.deadlocker8.mines.logic.Tile;
import fontAwesome.FontIcon;
import fontAwesome.FontIconType;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller
{
	@FXML AnchorPane mainPane;
	@FXML Button buttonStart;

	public Stage stage;
	public Image icon = new Image("de/deadlocker8/mines/application/icon.png");
	private final ResourceBundle bundle = ResourceBundle.getBundle("de/deadlocker8/mines/application/", Locale.GERMANY);

	private Board board;
	public Settings settings;
	private GridPane grid;

	public void setStage(Stage stage)
	{
		this.stage = stage;
	}

	public void init()
	{
		if(settings == null)
		{
			settings = new Settings(10, 10, 10);
		}
	}

	public void buttonStart()
	{
		board = new Board(settings.getWidth(), settings.getHeight(), settings.getNumberOfBombs());
		clearGrid();
		setGrid();
	}

	private void clearGrid()
	{
		mainPane.getChildren().remove(grid);
	}

	private void setGrid()
	{
		grid = new GridPane();		
		
		double itemWidth = 750 / board.getWidth();
		double itemHeight = 675 / board.getHeight();		

		for(int y = 0; y < board.getHeight(); y++)
		{			
			for(int x = 0; x < board.getWidth(); x++)
			{
				final int row = y;
				final int column = x;
				
				Label currentLabel = new Label();
				currentLabel.setAlignment(Pos.CENTER);
				currentLabel.setPrefWidth(itemWidth);
				currentLabel.setPrefHeight(itemHeight);	
				currentLabel.setStyle("-fx-background-color: lightgrey; -fx-border-width: 1; -fx-border-color: black;");
				
				currentLabel.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override
					public void handle(MouseEvent event)
					{
						if(!board.getTiles()[row][column].isRevealed())
						{							
							if(event.getButton().equals(MouseButton.PRIMARY))
							{													
								//open tile
								if(board.getTiles()[row][column].isBomb())
								{
									//lose
									clearGrid();
									showGrid(row, column, false);
									
									Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("Game Over");
									alert.setHeaderText("");
									alert.setContentText("You have lost");	
									alert.initOwner(stage);
									Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
									dialogStage.getIcons().add(icon);									
									alert.showAndWait();
								}
								else
								{
									//open tile		
									
									int numberOfNearBombs = board.getTiles()[row][column].getNumberOfNearBombs();								
									
									if(numberOfNearBombs == 0)
									{
										flood(row, column);
									}
									else
									{
										board.getTiles()[row][column].setRevealed(true);
										openTile(currentLabel, numberOfNearBombs, true);
									}
								}							
							}
							else if(event.getButton().equals(MouseButton.SECONDARY))
							{							
								//toggle flag
								
								if(board.getTiles()[row][column].isFlagged())
								{							
									currentLabel.setGraphic(null);								
									board.getTiles()[row][column].setFlagged(false);
								}
								else
								{								
									FontIcon iconFlag = new FontIcon(FontIconType.FLAG);
									iconFlag.setColor(Color.RED);
									iconFlag.setSize(18);
									currentLabel.setGraphic(iconFlag);
									board.getTiles()[row][column].setFlagged(true);
								}
							}
						}
					}
				});

				grid.add(currentLabel, x, y);
				GridPane.setHalignment(currentLabel, HPos.CENTER);
			}		

			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPrefWidth(itemWidth);
			columnConstraints.setMinWidth(itemWidth);
			grid.getColumnConstraints().add(columnConstraints);

			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPrefHeight(itemHeight);
			rowConstraints.setMinHeight(itemHeight);
			grid.getRowConstraints().add(rowConstraints);
		}

		mainPane.getChildren().add(grid);
		AnchorPane.setTopAnchor(grid, 100.0);
		AnchorPane.setLeftAnchor(grid, 25.0);
		AnchorPane.setRightAnchor(grid, 25.0);
		AnchorPane.setBottomAnchor(grid, 25.0);
	}

	private void showGrid(int row, int column, boolean paintBombsGreen)
	{
		grid = new GridPane();

		double itemWidth = 750 / board.getWidth();
		double itemHeight = 675 / board.getHeight();

		for(int y = 0; y < board.getHeight(); y++)
		{
			for(int x = 0; x < board.getWidth(); x++)
			{
				Label currentLabel = new Label();

				if(board.getTiles()[y][x].isBomb())
				{
					FontIcon iconBomb = new FontIcon(FontIconType.BOMB);
					currentLabel.setText("");
					currentLabel.setGraphic(iconBomb);					
					currentLabel.setStyle("-fx-border-width: 1; -fx-border-color: black;");
					
					if(paintBombsGreen)
					{
						currentLabel.setStyle("-fx-background-color: green; -fx-border-width: 1; -fx-border-color: black;");
					}
					else
					{
						if(y == row && x == column)
						{
							currentLabel.setStyle("-fx-background-color: red; -fx-border-width: 1; -fx-border-color: black;");
						}
					}
				}
				else
				{
					int numberOfNearBombs = board.getTiles()[y][x].getNumberOfNearBombs();
					openTile(currentLabel, numberOfNearBombs, false);
				}

				currentLabel.setAlignment(Pos.CENTER);
				currentLabel.setPrefWidth(itemWidth);
				currentLabel.setPrefHeight(itemHeight);

				grid.add(currentLabel, x, y);
				GridPane.setHalignment(currentLabel, HPos.CENTER);
			}

			ColumnConstraints columnConstraints = new ColumnConstraints();
			columnConstraints.setPrefWidth(itemWidth);
			columnConstraints.setMinWidth(itemWidth);
			grid.getColumnConstraints().add(columnConstraints);

			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPrefHeight(itemHeight);
			rowConstraints.setMinHeight(itemHeight);
			grid.getRowConstraints().add(rowConstraints);
		}

		mainPane.getChildren().add(grid);
		AnchorPane.setTopAnchor(grid, 100.0);
		AnchorPane.setLeftAnchor(grid, 25.0);
		AnchorPane.setRightAnchor(grid, 25.0);
		AnchorPane.setBottomAnchor(grid, 25.0);
	}

	public void openSettings()
	{
		try
		{
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/de/deadlocker8/mines/application/Settings.fxml"));

			Parent root = (Parent)fxmlLoader.load();
			Stage newStage = new Stage();
			newStage.setScene(new Scene(root, 600, 300));
			newStage.setTitle("Settings");
			newStage.initOwner(stage);

			newStage.getIcons().add(icon);
			SettingsController newController = fxmlLoader.getController();
			newController.init(newStage, icon, this);

			newStage.initModality(Modality.APPLICATION_MODAL);
			newStage.setResizable(false);
			newStage.showAndWait();

		}
		catch(IOException e1)
		{
			e1.printStackTrace();
		}
	}

	private void openTile(Label currentLabel, int numberOfNearBombs, boolean checkWinning)
	{
		switch(numberOfNearBombs)
		{
			case 0:
				currentLabel.setText(" ");
				currentLabel.setStyle("-fx-background-color: transparent; -fx-border-width: 1; -fx-border-color: black;");
				break;
			case 1:
				currentLabel.setText("1");
				currentLabel.setStyle("-fx-font-size: 16; -fx-text-fill: blue; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-width: 1; -fx-border-color: black;");
				break;
			case 2:
				currentLabel.setText("2");
				currentLabel.setStyle("-fx-font-size: 16; -fx-text-fill: green; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-width: 1; -fx-border-color: black;");
				break;
			case 3:
				currentLabel.setText("3");
				currentLabel.setStyle("-fx-font-size: 16; -fx-text-fill: red; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-width: 1; -fx-border-color: black;");
				break;
			case 4:
				currentLabel.setText("4");
				currentLabel.setStyle("-fx-font-size: 16; -fx-text-fill: darkblue; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-width: 1; -fx-border-color: black;");
				break;
			case 5:
				currentLabel.setText("5");
				currentLabel.setStyle("-fx-font-size: 16; -fx-text-fill: darkred; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-width: 1; -fx-border-color: black;");
				break;
			default:
				currentLabel.setText("" + numberOfNearBombs);
				currentLabel.setStyle("-fx-font-size: 16; -fx-text-fill: darkred; -fx-font-weight: bold; -fx-background-color: transparent; -fx-border-width: 1; -fx-border-color: black;");
				break;
		}
		
		if(checkWinning)
		{
			checkWinning();
		}
	}
	
	private void flood(int row, int column)
	{
		if(row < 0 || row > board.getHeight() - 1)
		{
			return;
		}
		
		if(column < 0 || column > board.getWidth() - 1)
		{
			return;
		}
		
		Tile currentTile = board.getTiles()[row][column]; 
		if(currentTile.isRevealed())
		{
			return;
		}
		
		if(currentTile.getNumberOfNearBombs() != 0)
		{
			if(currentTile.getNumberOfNearBombs() != -1)
			{
				currentTile.setRevealed(true);
				openTile((Label)grid.getChildren().get((row * board.getWidth() + column)), currentTile.getNumberOfNearBombs(), true);
				return;
			}
			else
			{
				return;
			}			
		}
	
		currentTile.setRevealed(true);	
		openTile((Label)grid.getChildren().get((row * board.getWidth() + column)), 0, true);
		
		flood(row - 1, column);
		flood(row + 1, column);
		flood(row, column - 1);
		flood(row, column + 1);		
	}

	private void checkWinning()
	{		
		if(board.getNumberofUnrevealedTiles() == settings.getNumberOfBombs())
		{
			clearGrid();
			showGrid(0, 0, true);
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Victory!");
			alert.setHeaderText("");
			alert.setContentText("You successfully defused all mines");
			alert.initOwner(stage);
			Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(icon);
			alert.showAndWait();
		}
	}
	
	public void about()
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("About " + bundle.getString("app.name"));
		alert.setHeaderText(bundle.getString("app.name"));
		alert.setContentText("Version:     " + bundle.getString("version.name") + "\r\nDate:         " + bundle.getString("version.date") + "\r\nAuthor:      Robert Goldmann\r\n");
		alert.initOwner(stage);
		Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
		dialogStage.getIcons().add(icon);
		alert.showAndWait();
	}
}