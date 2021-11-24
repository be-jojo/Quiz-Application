package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import application.Listeners.NewScreenListener;
import application.Models.Quiz;
import application.Models.Student;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

public class QuizCardLayoutController {
	@FXML private Label quizTitle;
	@FXML private Label totalQuestions;
	@FXML private Button satartButton;
	
	// My variables
	private Quiz quiz;
	private Student student;
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
		this.quizTitle.setText(this.quiz.getQuizTitle());
	}

	private NewScreenListener screenListener;
	public void setScreenListener(NewScreenListener screenListener) {
		this.screenListener = screenListener;
	}
	
	public void setTotal_questions(String s) {
		this.totalQuestions.setText(s);
	}
	
	// Event Listener on Button[#satartButton].onAction
	@FXML
	public void startQuiz(ActionEvent event) {
		FXMLLoader fLoader = new FXMLLoader(getClass().getResource("FXMLFiles/QuestionScreen.fxml"));
		try {
			Node node = fLoader.load();
			QuestionScreenController qScreenController = fLoader.getController();
			qScreenController.setStudent(this.student);
			qScreenController.setQuiz(this.quiz);
			qScreenController.setNewScreenListener(this.screenListener);
			this.screenListener.changeScreen(node);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
