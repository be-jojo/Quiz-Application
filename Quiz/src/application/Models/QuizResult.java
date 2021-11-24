package application.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.cj.xdevapi.Statement;

import application.SQLConnector;
import application.Models.Quiz.MetaData;

public class QuizResult {
		private Integer idInteger; 
		private Quiz quiz;
		private Student student;
		private Integer rightAnswerInteger;
		private Timestamp timestamp;
		
		{ // Initialization block runs no matter what is constructor
			timestamp = new Timestamp(new Date().getTime());
		}
		
		public QuizResult() { }
		
		public QuizResult(Quiz quiz, Student student, Integer rightAnswerInteger) {
			super();
			this.quiz = quiz;
			this.student = student;
			this.rightAnswerInteger = rightAnswerInteger;
		}
		
		public QuizResult(Integer idInteger, Quiz quiz, Student student, Integer rightAnswerInteger) {
			super();
			this.idInteger = idInteger;
			this.quiz = quiz;
			this.student = student;
			this.rightAnswerInteger = rightAnswerInteger;
		}
		
		public Integer getIdInteger() {
			return idInteger;
		}
		public void setIdInteger(Integer idInteger) {
			this.idInteger = idInteger;
		}
		
		public Quiz getQuiz() {
			return quiz;
		}
		public void setQuiz(Quiz quiz) {
			this.quiz = quiz;
		}
		public Student getStudent() {
			return student;
		}
		public void setStudent(Student student) {
			this.student = student;
		}
		public Integer getRightAnswerInteger() {
			return rightAnswerInteger;
		}
		public void setRightAnswerInteger(Integer rightAnswerInteger) {
			this.rightAnswerInteger = rightAnswerInteger;
		}
		public Timestamp getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Timestamp timestamp) {
			this.timestamp = timestamp;
		}
		
		
		public boolean save(Map<Question, String> userAnswers) {
			try {
				String query = "\r\n"
						+ "INSERT INTO `quiz`.`quizresults`(`quizid`, `studentId`, `correctAnswers`, `date_time`)\r\n"
						+ "VALUES(?, ?, ?, current_timestamp());";
				Connection connection = SQLConnector.getConnection();
				PreparedStatement pStatement = connection.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
				pStatement.setInt(1, this.getStudent().getId());
				pStatement.setInt(2, this.getQuiz().getQuizId());
				pStatement.setInt(3, this.getRightAnswerInteger());
				int result = pStatement.executeUpdate();
				ResultSet rKeys = pStatement.getGeneratedKeys();
				if(rKeys.next()) {
					this.setIdInteger(rKeys.getInt(1));
					return saveQuizResultDetails(userAnswers);
				}
				
				int rSet = pStatement.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		
		public boolean saveQuizResultDetails(Map<Question, String> userAnswers) {
			return QuizResultDetails.saveQuizResultDetails(this, userAnswers);
		}
}
