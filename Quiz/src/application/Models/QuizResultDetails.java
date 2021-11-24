package application.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;


import application.SQLConnector;

public class QuizResultDetails {
	private Integer idInteger;
	private Question question;
	private String userAnswer;
	private QuizResult quizResult;
	
	public QuizResultDetails() {
		super();
	}
	
	public QuizResultDetails(Question question, String userAnswer, QuizResult quizResult) {
		super();
		this.question = question;
		this.userAnswer = userAnswer;
		this.quizResult = quizResult;
	}
	
	public QuizResultDetails(Integer idInteger, Question question, String userAnswer, QuizResult quizResult) {
		super();
		this.idInteger = idInteger;
		this.question = question;
		this.userAnswer = userAnswer;
		this.quizResult = quizResult;
	}
	
	public Integer getIdInteger() {
		return idInteger;
	}
	public void setIdInteger(Integer idInteger) {
		this.idInteger = idInteger;
	}
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	public String getUserAnswer() {
		return userAnswer;
	}
	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}
	public QuizResult getQuizResult() {
		return quizResult;
	}
	public void setQuizResult(QuizResult quizResult) {
		this.quizResult = quizResult;
	}
	
	
	public static boolean saveQuizResultDetails(QuizResult quizResultP, Map<Question, String> userAnswers) {
		try {
			String str = "INSERT INTO `quiz`.`quizresult_details`(`quiz_result_id`, `questionId`, `user_answers`)\r\n"
					+ "VALUES(?, ?, ?);";
			Connection connection = SQLConnector.getConnection();
			PreparedStatement pStatement = connection.prepareStatement(str);
			
			java.util.Set<Question> questions =  userAnswers.keySet();
			
			for(Question q: questions) {
				pStatement.setInt(1, quizResultP.getIdInteger());
				pStatement.setInt(2, q.getQuestionId());
				pStatement.setString(3, userAnswers.get(q));
				pStatement.addBatch();
			}
			int[] results = pStatement.executeBatch();
			if(results.length > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
}
