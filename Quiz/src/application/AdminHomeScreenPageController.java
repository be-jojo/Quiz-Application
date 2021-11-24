package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.Parent;
import javafx.scene.control.Tab;

public class AdminHomeScreenPageController implements Initializable {
	@FXML private TabPane adminTabPane;
	@FXML private Tab addQuizPane;
	@FXML private Tab addStudentPane;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			Parent nodeParent = FXMLLoader.load(getClass().getResource("FXMLFiles/addQuiz.fxml"));
			addQuizPane.setContent(nodeParent);
			
			 Parent addStudentNode = FXMLLoader.load(getClass().getResource("FXMLFiles/AdminStudentTab.fxml"));
			addStudentPane.setContent(addStudentNode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
