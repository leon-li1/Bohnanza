import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BohnanzaLoadingScreen extends JWindow implements ActionListener {

	//Class fields
	//public static GUI gui; //This sets the name of the gui frame

	//Instance fields
	private JPanel panel = new JPanel(); //creates a new panel
	private JProgressBar loadingBar = new JProgressBar(); //creates the progress bar
	private Timer progressBarTimer = new Timer(10, this); //timer
	private int count = 0; //counts the loading time
	private int max = 150; //time reached in order to exit progress bar

	//Main method
	public static void main(String[] args) {

		//Runs the constructor method
		new BohnanzaLoadingScreen();

	}

	//Constructor method
	public BohnanzaLoadingScreen() {

		//Calls the set up method
		objectsSetup();

		//Start the progress bar timer
		progressBarTimer.start();
	}

	//This method sets up the objects in the class
	private void objectsSetup() {

		//Panel setup
		panel.setLayout(new BorderLayout()); 
		JLabel splashImage = new JLabel(new ImageIcon("Images/loading.png"));
		splashImage.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
		panel.add(splashImage); 

		//Progress bar set up
		loadingBar.setMaximum(max);
		loadingBar.setForeground(new Color(255, 0, 0));
		loadingBar.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.RED));
		panel.add(loadingBar, BorderLayout.SOUTH);

		//Frame setup
		setContentPane(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	//This method implements the action listener
	public void actionPerformed(ActionEvent event) {

		//Checks if the source was the game start button
		if (event.getSource() == progressBarTimer) {

			//Set the value of the progress bar to count
			loadingBar.setValue(count);

			//Checks if the count is equal to the max
			if (count == max) {

				//Close the current window, stop the timers and open the main menu
				BohnanzaLoadingScreen.this.dispose();
				progressBarTimer.stop();
				new GUI();

			}

			//Increase count by 1
			count++;

		}

	}
}
/*
public class BohnanzaLoadingScreen {
	public static void main(String[] args) {
		new GUI();
	}
}
*/