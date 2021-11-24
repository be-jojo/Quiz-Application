package application.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import application.SQLConnector;


public class Quiz {
	private Integer quizId;
	private String quizTitle;
	
	public Quiz() { }
	public Quiz(Integer quizId, String quizTitle) {
		super();
		this.quizId = quizId;
		this.quizTitle = quizTitle;
	}
	
	public static class MetaData{
		public static final String TABLE_NAME = "quizs";
		public static final String TITLE = "title";
		public static final String QUIZID = "id";
	}
	
	public Integer getQuizId() {
		return quizId;
	}
	public void setQuizId(Integer quizId) {
		this.quizId = quizId;
	}
	public String getQuizTitle() {
		return quizTitle;
	}
	public void setQuizTitle(String quizTitle) {
		this.quizTitle = quizTitle;
	}
	
	public int saveToDatabase() {
		// inserting quiz title provided by user
		// and returns the id of the raw affected
		try {
			String str = "INSERT INTO %s (%s) VALUES(?)";
			String query = String.format(str, MetaData.TABLE_NAME, MetaData.TITLE);
			Connection connection = SQLConnector.getConnection();
			PreparedStatement pStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, this.quizTitle);
			int rawId = pStatement.executeUpdate();
			ResultSet rSet = pStatement.getGeneratedKeys();
			if(rSet.next()) {
				return rSet.getInt(rawId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return -1;
	}
	
	public boolean saveQuestions(ArrayList<Question> questions) {
		boolean flag = true;
		this.quizId = this.saveToDatabase();
		if(quizId == -1) {
			return false;
		}
		for(Question q: questions) {
			flag = flag && q.saveToDatabase();
		}
		return flag;
	}
	
	@SuppressWarnings("null")
	public static Map<Quiz, List<Question>> getQuizes() {
		Map<Quiz, List<Question>> quizes = new HashMap<>();
		Quiz keyQuiz = null;
		try {
			String query = "SELECT * FROM `quiz`.`quizs` JOIN `quiz`.`questions`\r\n"
					+ "ON `quizs`.`id` = `questions`.`quizId`;";
			Connection connection = SQLConnector.getConnection();
			PreparedStatement pStatement = connection.prepareStatement(query);
			
			ResultSet rSet = pStatement.executeQuery();;
			while(rSet.next()) {
				Quiz tempQuiz = new Quiz();
				tempQuiz.setQuizId(rSet.getInt(1));
				tempQuiz.setQuizTitle(rSet.getString(2));
				
				Question tempQuestion = new Question();
				tempQuestion.setQuiz(tempQuiz);
				tempQuestion.setQuestionId(rSet.getInt(3));
				tempQuestion.setQuestion(rSet.getString(4));
				tempQuestion.setOp1(rSet.getString(5));
				tempQuestion.setOp2(rSet.getString(6));
				tempQuestion.setOp3(rSet.getString(7));
				tempQuestion.setOp4(rSet.getString(8));
				tempQuestion.setAnswer(rSet.getString(9));
				if(keyQuiz != null && keyQuiz.equals(tempQuiz)) {
					quizes.get(keyQuiz).add(tempQuestion);
				}else {
					ArrayList<Question> quizQuestions = new ArrayList<>();
					quizes.put(tempQuiz, quizQuestions);
				}
				keyQuiz = tempQuiz;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return quizes;
	}
	
	@SuppressWarnings("null")
	public static Map<Quiz, Integer> getQuizzesQuestionCount() {
		Map<Quiz, Integer> quizes = new HashMap<>();
		try {
			String str = "SELECT `quizs`.`id`, %s, COUNT(*) \r\n"
					+ "FROM %s JOIN `quiz`.`questions`\r\n"
					+ "ON `quizs`.`id` = `questions`.`quizId`\r\n"
					+ "GROUP BY `quizs`.`id`";
			String query = String.format(str, MetaData.TITLE, MetaData.TABLE_NAME);
			Connection connection = SQLConnector.getConnection();
			PreparedStatement pStatement = connection.prepareStatement(query);
			
			ResultSet rSet = pStatement.executeQuery();;
			while(rSet.next()) {
				Quiz tempQuiz = new Quiz();
				tempQuiz.setQuizId(rSet.getInt(1));
				tempQuiz.setQuizTitle(rSet.getString(2));
				
				int count = rSet.getInt(3);
				quizes.put(tempQuiz, count);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return quizes;
	}
	
	public List<Question> getQuizQuestions() {
		List<Question> questions = new ArrayList<>();
		try {
			String query = "SELECT * FROM `quiz`.`questions` WHERE `questions`.`quizId` = ?";
			Connection connection = SQLConnector.getConnection();
			PreparedStatement pStatement = connection.prepareStatement(query);
			pStatement.setInt(1, this.quizId);
			ResultSet rSet = pStatement.executeQuery();;
			while(rSet.next()) {
				Question tempQuestion = new Question();
				tempQuestion.setQuestionId(rSet.getInt(1));
				tempQuestion.setQuestion(rSet.getString(2));
				tempQuestion.setOp1(rSet.getString(3));
				tempQuestion.setOp2(rSet.getString(4));
				tempQuestion.setOp3(rSet.getString(5));
				tempQuestion.setOp4(rSet.getString(6));
				tempQuestion.setAnswer(rSet.getString(7));
				
				tempQuestion.setQuiz(this);
				questions.add(tempQuestion);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return questions;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(!((obj) instanceof Quiz || obj == null)) {
			return false;
		}
		Quiz objQuiz = (Quiz) obj;
		if(this.quizId == objQuiz.quizId) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(quizId, quizTitle);
	}
	
	@Override
	public String toString() {
		return this.quizTitle;
	}
	/*public static void createTable() {
		String query =  "CREATE TABLE \"quiz\" (\r\n"
				+ "	\"id\"	INTEGER,\r\n"
				+ "	\"title\"	TEXT NOT NULL,\r\n"
				+ "	PRIMARY KEY(\"id\")\r\n"
				+ ")";
		Connection connection = SQLConnector.getConnection();
		try {
			PreparedStatement pStatement = connection.prepareStatement(query);
			pStatement.execute();
			System.out.println("Table created");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	
}
