package application;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;


import application.Models.Question;
import application.Models.Quiz;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class QuizResultScreenController implements Initializable{
	@FXML private PieChart qAttemptChart;
	@FXML private PieChart scoreChart;
	@FXML private Label headLabel;
	@FXML private VBox questionsContainer;
	
	// My Variables
	private Map<Question, String> userAnswers;
	private Integer totalCorrectAnswer = 0;
	private Quiz quiz ;
	private List<Question> questionList;
	
	private Integer notAttempted = 0;
	private Integer attempted = 0;;


	public void setValues(Map<Question, String> userAnswers, Integer totalCorrectAnswer, Quiz quiz,
			List<Question> questionList) {
		this.userAnswers = userAnswers;
		this.totalCorrectAnswer = totalCorrectAnswer;
		this.quiz = quiz;
		this.questionList = questionList;
		
		this.attempted = this.userAnswers.keySet().size();
		this.notAttempted = this.questionList.size() - attempted;
		
		setValuesToChart();
		renderQuestion();
	}

	private void setValuesToChart() {
		ObservableList<PieChart.Data> attempteDatas = this.qAttemptChart.getData();
		attempteDatas.add(new PieChart.Data(String.format("Attempted (%d)", this.attempted), this.attempted));
		attempteDatas.add(new PieChart.Data(String.format("Not Attempted (%d)", this.notAttempted), this.notAttempted));
		

		ObservableList<PieChart.Data> scoreData = this.scoreChart.getData();
		scoreData.add(new PieChart.Data(String.format("Correct (%d)", this.totalCorrectAnswer), this.totalCorrectAnswer));
		scoreData.add(new PieChart.Data(String.format("Incorrect (%d)", this.attempted - this.totalCorrectAnswer), this.attempted - this.totalCorrectAnswer));
	}
	
	private void renderQuestion() {

		for(int i = 0; i < this.questionList.size(); i++) {
			FXMLLoader fLoader = new FXMLLoader(getClass().getResource("FXMLFiles/QuizResultScreenQuestion.fxml"));
			try {
				Node node = fLoader.load();
				QuizResultScreenQuestionController qScreenQuestionController = fLoader.getController();
				qScreenQuestionController.setValues(this.questionList.get(i), this.userAnswers.get(this.questionList.get(i)));
				questionsContainer.getChildren().add(node);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}

}
