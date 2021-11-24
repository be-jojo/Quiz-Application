package application.Listeners;

import javafx.event.EventHandler;
import javafx.scene.Node;

// Used as a communicator to add screens in private stack pane
public interface NewScreenListener extends EventHandler{

	public void changeScreen(Node node);
	public void removeTopScreen();
}
