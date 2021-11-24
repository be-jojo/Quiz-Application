package application;

import java.net.URL;
import java.util.ResourceBundle;

import application.Models.Question;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class QuizResultScreenQuestionController implements Initializable {
	@FXML private Label questionLabel;
	@FXML private Label option1Label;
	@FXML private Label option2Label;
	@FXML private Label option3Label;
	@FXML private Label option4Label;
	
	// My Variables
	private Question questions;
	private String userAnswer;
	
	public void setValues(Question questions, String userAnswer) {
		this.questions = questions;
		this.userAnswer = userAnswer;
		setFields();
	}
	
	private void setFields() {
		this.questionLabel.setText(this.questions.getQuestion());
		this.option1Label.setText(this.questions.getOp1());
		this.option2Label.setText(this.questions.getOp2());
		this.option3Label.setText(this.questions.getOp3());
		this.option4Label.setText(this.questions.getOp4());
		
		if(option1Label.getText().trim().equalsIgnoreCase(this.questions.getAnswer())){
			option1Label.setTextFill(Color.web("#26ae60"));
		}
		else if(option2Label.getText().trim().equalsIgnoreCase(this.questions.getAnswer())){
			option2Label.setTextFill(Color.web("#26ae60"));
		}
		else if(option3Label.getText().trim().equalsIgnoreCase(this.questions.getAnswer())){
			option3Label.setTextFill(Color.web("#26ae60"));
		}
		else if(option4Label.getText().trim().equalsIgnoreCase(this.questions.getAnswer())){
			option4Label.setTextFill(Color.web("#26ae60"));
		}
		
		if(!userAnswer.trim().equalsIgnoreCase(this.questions.getAnswer().trim())) {
			if(option1Label.getText().trim().equalsIgnoreCase(this.userAnswer)){
				option1Label.setTextFill(Color.web("#B83227"));
			}
			else if(option2Label.getText().trim().equalsIgnoreCase(this.userAnswer)){
				option2Label.setTextFill(Color.web("#B83227"));
			}
			else if(option3Label.getText().trim().equalsIgnoreCase(this.userAnswer)){
				option3Label.setTextFill(Color.web("#B83227"));
			}
			else if(option4Label.getText().trim().equalsIgnoreCase(this.userAnswer)){
				option4Label.setTextFill(Color.web("#B83227"));
			}
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	
	

}
