package application;

import javafx.fxml.FXML;

import javafx.scene.control.Label;

import javafx.scene.shape.Circle;

public class ProgressCircleController {
	@FXML
	private Circle circle;
	@FXML
	private Label label;
	
	public void setLabel(Integer number) {
		label.setText(number.toString());
	}
	
	public void setCurrentQuestColor() {
		circle.setFill(javafx.scene.paint.Color.web("#2c79f5"));
		//label.setTextFill(javafx.scene.paint.Color.valueOf("black"));
	}
	
	public void setCheckedQuestColor() {
		circle.setFill(javafx.scene.paint.Color.web("#03fc4e"));
		//label.setTextFill(javafx.scene.paint.Color.valueOf("black"));
	}
	
	public void setUncheckedQuestColor() {
		circle.setFill(javafx.scene.paint.Color.web("#f00567"));
		//label.setTextFill(javafx.scene.paint.Color.valueOf("black"));
	}
	
}
