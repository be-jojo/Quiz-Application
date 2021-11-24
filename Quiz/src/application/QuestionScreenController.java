package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;

import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import application.Listeners.NewScreenListener;
import application.Models.Question;
import application.Models.Quiz;
import application.Models.QuizResult;
import application.Models.Student;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.RadioButton;

public class QuestionScreenController implements Initializable{
	@FXML private Label tiltleLabel;
	@FXML private Label timerLabel;
	@FXML private Label errorMessage;
	@FXML private Label questionLabel;
	@FXML private RadioButton option1RB;
	@FXML private ToggleGroup options;
	@FXML private RadioButton option2RB;
	@FXML private RadioButton option3RB;
	@FXML private RadioButton option4RB;
	@FXML private Button nextBtn;
	@FXML private Button submitBtn;
	@FXML private FlowPane progressFP;
	
	
	// Listeners
	private NewScreenListener newScreenListener;

	// My Variables
	private Student student;
	
	private Quiz quiz = null;
	private List<Question> questionList;
	private Question currentQuestion;
	int currentIndex = 0;
	private Map<Question, String> studentAnswers = new HashMap<>();
	private Integer totalCorrectAnswers = 0;
	
	//Timer fields
	private long min, hr, sec, totalSecs = 0;
	private Timer timer;
	
	
	// Getter for Screen Listener
	public NewScreenListener getNewScreenListener() {
		return newScreenListener;
	}
	//Setter for Screen Listener
	public void setNewScreenListener(NewScreenListener newScreenListener) {
		this.newScreenListener = newScreenListener;
	}
	
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	private static String format(long value) {
		if(value < 10) {
			return 0+""+value;
		}
		return value+"";
	}
	
	public void convertTime() {
		min = TimeUnit.SECONDS.toMinutes(totalSecs);
		sec = totalSecs - (min * 60);
		hr = TimeUnit.MINUTES.toHours(min);
		min = min - (hr * 60);
		timerLabel.setText(format(hr)+":"+format(min) + ":" + format(sec));
		totalSecs--;
	}
	
	private void setTimer() {
		totalSecs = this.questionList.size() * 30;	
		this.timer = new Timer();
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				// have to create fxml thread otherwise creates error
				javafx.application.Platform.runLater(new Runnable() {
					
					@Override
					public void run() {
						System.out.println(" after 1 second...");
						convertTime();
						if(totalSecs <= 0) {
							timer.cancel();
							timerLabel.setText("Time Out");
							// Saving data to database
							submitQuestion(null);
						}
					}
				});
			}
		};
		timer.schedule(timerTask, 0, 1000);
	}

	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
		this.tiltleLabel.setText(quiz.getQuizTitle());
		this.getData();
	}
	
	public void getData() {
		if(quiz != null) {
			this.questionList = quiz.getQuizQuestions();
			Collections.shuffle(this.questionList);
			renderProgress();
			loadNextQuestion();
			setTimer();
		}
	}
	
	private void loadNextQuestion() {
		
		if(!(currentIndex >= questionList.size())) {
			{
				Node node = this.progressFP.getChildren().get(currentIndex);
				ProgressCircleController pCircleController = (ProgressCircleController)node.getUserData();
				pCircleController.setCurrentQuestColor();
			}
			this.currentQuestion = this.questionList.get(currentIndex);
			List<String> options = new ArrayList<>();
			options.add(this.currentQuestion.getOp1());
			options.add(this.currentQuestion.getOp2());
			options.add(this.currentQuestion.getOp3());
			options.add(this.currentQuestion.getOp4());
			Collections.shuffle(options);
			
			this.questionLabel.setText(currentQuestion.getQuestion());
			this.option1RB.setText(options.get(0));
			this.option2RB.setText(options.get(1));
			this.option3RB.setText(options.get(2));
			this.option4RB.setText(options.get(3));

			currentIndex++;
		}
		else {
			hideNextButton();
			showSubmitButton();
		}
	}
	
	private void hideNextButton() {
		this.nextBtn.setVisible(false);
	}
	
	private void showNextButton() {
		this.nextBtn.setVisible(true);
	}
	
	private void hideSubmitButton() {
		this.submitBtn.setVisible(false);
	}
	
	private void showSubmitButton() {
		this.submitBtn.setVisible(true);
	}
	
	// 
	@FXML
	public void nextQuestion(ActionEvent event) {

		{
			// Checking answer
			boolean isCorrect = false;
			
			Node node = this.progressFP.getChildren().get(currentIndex-1);
			ProgressCircleController pCircleController = (ProgressCircleController)node.getUserData();
			
			RadioButton selectedOption = (RadioButton)options.getSelectedToggle();
			
			String selectedAnswer = selectedOption.getText();
			String CorrectAnswer = this.currentQuestion.getAnswer();
			if(selectedAnswer.equals(CorrectAnswer)) {
				isCorrect = true;
				this.totalCorrectAnswers++;
				pCircleController.setCheckedQuestColor();
			}else {
				pCircleController.setUncheckedQuestColor();
			}
			
			// Saving Answer to hashMap
			studentAnswers.put(this.currentQuestion, selectedAnswer);
		}
		
		this.loadNextQuestion();
		//this.deselectOption();
	}
	
	// Submit the quiz and open the quiz result screen
	@FXML
	public void submitQuestion(ActionEvent event) {
		QuizResult quizResult = new QuizResult(this.quiz, student, totalCorrectAnswers);
		if(quizResult.save(this.studentAnswers)) {
			System.out.print("Submited");
			timer.cancel();
			openResultScreen();
		}else {
			System.out.print("Not Submited");
		}
	}
	
	private void openResultScreen() {
		FXMLLoader fLoader = new FXMLLoader(getClass().getResource("FXMLFiles/QuizResultScreen.fxml"));
		try {
			Node node = fLoader.load();
			QuizResultScreenController qResultScreenController = fLoader.getController();
			qResultScreenController.setValues(this.studentAnswers, this.totalCorrectAnswers, this.quiz, this.questionList);
			this.newScreenListener.removeTopScreen();
			this.newScreenListener.changeScreen(node);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void renderProgress() {
		for(int i = 1; i <= this.questionList.size(); i++) {
			FXMLLoader fLoader = new FXMLLoader(getClass().getResource("ProgressCircle.fxml"));
			try {
				Node node = fLoader.load();
				ProgressCircleController pCircleController = fLoader.getController();
				pCircleController.setLabel(i);
				progressFP.getChildren().add(node);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*public void deselectOption() {
		option1RB.setSelected(false);
		option2RB.setSelected(false);
		option3RB.setSelected(false);
		option4RB.setSelected(false);
	}*/
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.hideSubmitButton();
		this.showNextButton();
	}
}
