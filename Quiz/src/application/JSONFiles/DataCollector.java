package application.JSONFiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import application.Models.Question;
import application.Models.Quiz;
import application.Models.Student;


public class DataCollector {
	public static void main(String[] args) throws IOException, JSONException {
		readAndSaveUserData();
	}
	
	public static void readAndSaveQuizzesData() throws IOException, JSONException {
		String folderPath = "src/application/JSONFiles";
		File folder = new File(folderPath);
		String[] fileNames = folder.list();
		for(String fileName: fileNames) {
			File file = new File(folder + "/" + fileName);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			
			JSONObject jsonObject = new JSONObject(stringBuilder.toString());
			JSONArray resultArray = jsonObject.getJSONArray("results");
			
			Quiz quiz = new Quiz();
			ArrayList<Question> questions = new ArrayList<>(); 
			for(int i = 0; i < resultArray.length(); i++) {
				String objectString = resultArray.get(i).toString();
				JSONObject object = new JSONObject(objectString);
				Question question = new Question();
				
				quiz.setQuizTitle(object.get("category").toString());
				question.setQuestion(object.getString("question"));
				JSONArray incorrectAnswers = object.getJSONArray("incorrect_answers");
				question.setOp1(incorrectAnswers.get(0).toString());
				question.setOp2(incorrectAnswers.get(0).toString());
				question.setOp3(incorrectAnswers.get(0).toString());
				question.setOp4(object.get("correct_answer").toString());
				question.setAnswer(object.getString("correct_answer"));
				questions.add(question);
				question.setQuiz(quiz);
				System.out.println(question);
				System.out.println(quiz);
			}
			
			quiz.saveQuestions(questions);
		}
	}
	
	public static void readAndSaveUserData() throws IOException, JSONException {
			File file = new File("src/application/JSONFiles/users.json");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			while((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line);
			}
			
			JSONArray resultArray = new JSONArray(stringBuilder.toString());
			
			for(int i = 0; i < resultArray.length(); i++) {
				String objectString = resultArray.get(i).toString();
				JSONObject object = new JSONObject(objectString);
				
				Student student = new Student();
				student.setFirstName(object.getString("firstName"));
				student.setLastName(object.getString("lastName"));
				student.setEmail(object.getString("email"));
				student.setPassword(object.getInt("password")+"");
				student.setMobile(object.getInt("phone")+"");
				student.setGender('M');
				student.saveToDatabase();
			}
	}
}
