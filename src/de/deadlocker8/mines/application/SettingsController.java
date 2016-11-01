package de.deadlocker8.mines.application;

import de.deadlocker8.mines.logic.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SettingsController
{
	@FXML private Slider sliderWidth;
	@FXML private Slider sliderHeight;
	@FXML private TextField textfieldNumberOfBombs;
	@FXML private Label labelBombs;

	public Stage stage;
	private Image icon;
	private Controller controller;
	private int maxNumberOfBombs;

	public void init(Stage stage, Image icon, Controller controller)
	{
		this.stage = stage;
		this.icon = icon;
		this.controller = controller;	
		
		sliderWidth.setValue(controller.settings.getWidth());
		sliderHeight.setValue(controller.settings.getHeight());
		textfieldNumberOfBombs.setText(String.valueOf(controller.settings.getNumberOfBombs()));
		adjustNumberOfBombs(controller.settings.getWidth(), controller.settings.getHeight());

		sliderWidth.valueProperty().addListener(new ChangeListener<Number>()
		{
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val)
			{
				adjustNumberOfBombs(new_val.intValue(), (int)sliderHeight.getValue());
			}
		});
		
		sliderHeight.valueProperty().addListener(new ChangeListener<Number>()
		{
			public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val)
			{
				adjustNumberOfBombs((int)sliderWidth.getValue(), new_val.intValue());
			}
		});
	}
	
	private void adjustNumberOfBombs(int width, int height)
	{
		maxNumberOfBombs = width * height;
		labelBombs.setText("(1-" + maxNumberOfBombs + ")");
	}

	public void save()
	{
		int width = (int)sliderWidth.getValue();
		int height = (int)sliderHeight.getValue();
		try
		{
			int numberOfBombs = Integer.parseInt(textfieldNumberOfBombs.getText());
			if(numberOfBombs < 1)
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Invalid");
				alert.setHeaderText("");
				alert.setContentText("Number of bombs should be greater than zero");
				alert.initOwner(stage);
				Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
				dialogStage.getIcons().add(icon);
				alert.showAndWait();
			}
			else if(numberOfBombs > maxNumberOfBombs)
			{
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Invalid");
				alert.setHeaderText("");
				alert.setContentText("Number of bombs should be less than " + maxNumberOfBombs);
				alert.initOwner(stage);
				Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
				dialogStage.getIcons().add(icon);
				alert.showAndWait();
			}
			else
			{
				controller.settings =  new Settings(width, height, numberOfBombs);
				stage.close();
			}
		}
		catch(Exception e)
		{
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Invalid");
			alert.setHeaderText("");
			alert.setContentText("Number of bombs is not a valid number");
			alert.initOwner(stage);
			Stage dialogStage = (Stage)alert.getDialogPane().getScene().getWindow();
			dialogStage.getIcons().add(icon);
			alert.showAndWait();
		}
	}
}