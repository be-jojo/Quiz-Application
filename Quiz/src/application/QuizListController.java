package application;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import application.Listeners.NewScreenListener;
import application.Models.Quiz;
import application.Models.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public class QuizListController implements Initializable{
	@FXML private FlowPane quizListContainer;
	private NewScreenListener screenListener;
	

	// My variables
	private Map<Quiz, Integer> allQuizzes = null;
	private java.util.Set<Quiz> keys;
	private Student student;
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void setScreenListener(NewScreenListener screenListener) {
		this.screenListener = screenListener;
		
	}
	
	public void setCards() {
		for(Quiz quiz: keys) {
			FXMLLoader fLoader = new FXMLLoader(getClass().getResource("FXMLFiles/QuizCardLayout.fxml"));
			try {
				Node node = fLoader.load();
				// After getting the data we want to show them on cards 
				// we have to create object of cardController
				QuizCardLayoutController quizCardController = fLoader.getController();
				quizCardController.setQuiz(quiz);
				quizCardController.setStudent(this.student);
				quizCardController.setTotal_questions(allQuizzes.get(quiz) + "");
				quizCardController.setScreenListener(this.screenListener);
				quizListContainer.getChildren().add(node);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		allQuizzes = Quiz.getQuizzesQuestionCount();
		keys = allQuizzes.keySet();
		
	}
}
