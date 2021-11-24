package application.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import application.SQLConnector;

public class Question {
	Quiz quiz;
	Integer questionId;
	String question;
	String op1, op2, op3, op4, answer;
	
	public Question() {}
	
	public Question(Quiz quiz, Integer questionId, String question, String op1, String op2, String op3, String op4,
			String answer) {
		super();
		this.quiz = quiz;
		this.questionId = questionId;
		this.question = question;
		this.op1 = op1;
		this.op2 = op2;
		this.op3 = op3;
		this.op4 = op4;
		this.answer = answer;
	}
	
	public static class MetaData{
		public static final String TABLE_NAME = "questions";
		public static final String QUIZID = "quizId";
		public static final String QUIZ_QUESTION = "questionText";
		public static final String OPTION1 = "option1";
		public static final String OPTION2 = "option2";
		public static final String OPTION3 = "option3";
		public static final String OPTION4 = "option4";
		public static final String ANSWER = "answer";
	}
	
	public Quiz getQuiz() {
		return quiz;
	}
	public void setQuiz(Quiz quiz) {
		this.quiz = quiz;
	}
	public Integer getQuestionId() {
		return questionId;
	}
	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getOp1() {
		return op1;
	}
	public void setOp1(String op1) {
		this.op1 = op1;
	}
	public String getOp2() {
		return op2;
	}
	public void setOp2(String op2) {
		this.op2 = op2;
	}
	public String getOp3() {
		return op3;
	}
	public void setOp3(String op3) {
		this.op3 = op3;
	}
	public String getOp4() {
		return op4;
	}
	public void setOp4(String op4) {
		this.op4 = op4;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public boolean saveToDatabase() {
		// inserting quiz title provided by user
		// and returns the id of the raw affected
		boolean flag = true;
		try {
			String str = "INSERT INTO %s ( %s, %s, %s, %s, %s, %s, %s)\r\n"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)";
			String query = String.format(str, MetaData.TABLE_NAME, MetaData.QUIZ_QUESTION, MetaData.OPTION1,
					MetaData.OPTION2, MetaData.OPTION3, MetaData.OPTION4, MetaData.ANSWER, MetaData.QUIZID);
			Connection connection = SQLConnector.getConnection();
			PreparedStatement pStatement = connection.prepareStatement(query);
			pStatement.setString(1, this.question);
			pStatement.setString(2, this.op1);
			pStatement.setString(3, this.op2);
			pStatement.setString(4, this.op3);
			pStatement.setString(5, this.op4);
			pStatement.setString(6, this.answer);
			pStatement.setInt(7, this.quiz.getQuizId());
			int rid = pStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			flag = false;
		}
		return flag;
	}
	
	@Override
	public String toString() {
		return this.question;
	}
}
