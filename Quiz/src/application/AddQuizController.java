package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import application.Models.Question;
import application.Models.Quiz;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Text;

public class AddQuizController implements Initializable {

	@FXML private Text updateErrorText;
    @FXML private TextField quizTitleTF;
    @FXML private Button addTitleBtn;
    @FXML private TextArea questionTA;
    @FXML private TextField option1TF;
    @FXML private TextField option2TF;
    @FXML private TextField option3TF;
    @FXML private TextField option4TF;
    @FXML private RadioButton option1RB;
    @FXML private RadioButton option2RB;
    @FXML private RadioButton option3RB;
    @FXML private RadioButton option4RB;
    @FXML private Button nextQuestionBtn;
    @FXML private Button submitBtn;
    
    @FXML private TreeView<?> treeView;
    
    private ToggleGroup optionGroup;
    
    // My variables to store quiz title with all questions
    private Quiz quiz = new Quiz();
    private ArrayList<Question> questions = new ArrayList<>();
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// to set up toggle group of all options
		setUpRadioButton();
		renderQuizes();
	}
	
	private void setUpRadioButton() {
		optionGroup = new ToggleGroup();
		option1RB.setToggleGroup(optionGroup);
		option2RB.setToggleGroup(optionGroup);
		option3RB.setToggleGroup(optionGroup);
		option4RB.setToggleGroup(optionGroup);
	}
	
	private boolean validateQuestion() {
		if(quiz == null) {
			updateErrorText.setText("Please enter and save the quiz title");
			return false;
		}
		
		String questionString = this.questionTA.getText();
		String op1 = this.option1TF.getText();
		String op2 = this.option2TF.getText();
		String op3 = this.option3TF.getText();
		String op4 = this.option4TF.getText();
		Toggle selectedOption = optionGroup.getSelectedToggle();
		
		if(questionString.trim().isEmpty() || 
			op1.trim().isEmpty() || op2.trim().isEmpty() ||
			op3.trim().isEmpty() || op4.trim().isEmpty() || selectedOption == null){
			
			//Notifications.create().darkStyle().hideAfter(Duration.millis(2000))
    		//.text("Invalid title").title("Quiz Title").show();
			updateErrorText.setText("Enter valid question and options : All Required");
			 if(selectedOption == null) {
				updateErrorText.setText("Select correct answer");
				return false;
			}
			return false;
		}
		return true;
	}
	
	public boolean addQuestion() {
		boolean valid = validateQuestion();
		if(valid) {
			// storing question into Question class object
			Question question = new Question();
			question.setQuestion(questionTA.getText().trim());
			question.setOp1(option1TF.getText().trim());
			question.setOp2(option2TF.getText().trim());
			question.setOp3(option3TF.getText().trim());
			question.setOp4(option4TF.getText().trim());
						
			Toggle selectedOption = optionGroup.getSelectedToggle();
			String ans = null;
			if(selectedOption == option1RB) {
				ans = option1TF.getText().trim();
			}else if(selectedOption == option2RB) {
				ans = option2TF.getText().trim();
			}else if(selectedOption == option3RB) {
				ans = option3TF.getText().trim();
			}else if(selectedOption == option4RB) {
				ans = option4TF.getText().trim();
			}
			question.setAnswer(ans);
			question.setQuiz(quiz);
						
			//quiz.setQuizTitle(quiz);
			questions.add(question);
			// After adding question to questions ArrayList
			// clear all the fields from window
			questionTA.clear();
			option1TF.clear();
			option2TF.clear();
			option3TF.clear();
			option4TF.clear();
					
			updateErrorText.setText("Question Added Successfully");
					/*System.out.println("--------------------------------");
					for(Question q: questions) {
						System.out.println(q);
					}*/
		}
		return valid;
	}
	
	@FXML
    void addNextQuestion(ActionEvent event) {
		addQuestion();	
    }
 
    @FXML
    void addQuizTitle(ActionEvent event) {
    	String title = quizTitleTF.getText();
    	
    	if(title.trim().isEmpty()) {
    		
    		//Notifications.create().darkStyle().hideAfter(Duration.millis(2000))
    		//.text("Invalid title").title("Quiz Title").show();
    		updateErrorText.setText("Enter valid Title");
    	}
    	else {
    		// Once adding title it can not be edited
    		quizTitleTF.setEditable(false);
    		quiz.setQuizTitle(title);
    		updateErrorText.setText("title Added");
    	}
    }

    @FXML
    void submitQuiz(ActionEvent event) {
    	boolean flag = addQuestion();
    	if(flag) {
    		flag = quiz.saveQuestions(questions);
    		if(flag) {
    			updateErrorText.setText("Quiz Submitted");
    		}else {
    			updateErrorText.setText("Failed");
    		}
    	}
    }
    
    @FXML
    public void renderQuizes() {
    	Map<Quiz, List<Question>> quizesMap = Quiz.getQuizes();
    	java.util.Set<Quiz> quizes = quizesMap.keySet();
    	TreeItem root = new TreeItem("Quizes");
    	for(Quiz quiz: quizes) {
    		TreeItem quizTreeItem = new TreeItem(quiz);
    		
    		List<Question> questions = quizesMap.get(quiz);
    		for(Question question : questions) {
    			TreeItem questionItem = new TreeItem(question);
    			questionItem.getChildren().add(new TreeItem("A " + question.getOp1()));
    			questionItem.getChildren().add(new TreeItem("B " + question.getOp2()));
    			questionItem.getChildren().add(new TreeItem("C " + question.getOp3()));
    			questionItem.getChildren().add(new TreeItem("D " + question.getOp4()));
    			questionItem.getChildren().add(new TreeItem("Answer " + question.getAnswer()));
    			quizTreeItem.getChildren().add(questionItem);
    		}
    		quizTreeItem.setExpanded(true);
    		root.getChildren().add(quizTreeItem);
    	}
		root.setExpanded(true);
    	this.treeView.setRoot(root);
    }

}
