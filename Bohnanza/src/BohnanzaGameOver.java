import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.Clip;
import javax.swing.*;

public class BohnanzaGameOver extends JFrame implements ActionListener {

	//Instance fields
	private JLabel lblCongrats = new JLabel ("Thanks For Playing!"); //Label for the title
	private JButton exitGame = new JButton("Exit Game"); //Button to exit game 
	private JTextArea scoreInfo = new JTextArea (); //Text are to display the final score of the game
	private JLabel lblAnimation = new JLabel(); //Label to display the game animation
	private Font titleFont = new Font ("Calibri", Font.BOLD, 48); //Font for the title
	private Font textFont = new Font ("Calibri", Font.BOLD, 38); //Font for the text area and the labels
	private Font buttonFont = new Font ("Calibri", Font.BOLD, 32); //Font for the button
	private Clip clip; //Variable to enable sound (temporarily stores the sound file)

	//Constructor method
	public BohnanzaGameOver(String playerName) {

		//Call methods
		frameSetup();
		printStats(playerName);
		buttonSetup();
		repaint();

		/*//Play the Dodgebull theme song
		try {

			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("sounds/dodgebullSong.wav")));
			clip.start();

		} catch (Exception e) {
			
			System.out.println("Error");

		}*/
	}

	//This method sets up the frame
	private void frameSetup() {

		//Frame setup
		setSize(500, 550);
		setResizable(false);
		setLayout(null);
		setTitle("Bohnanza Exit Screen");
		setVisible(true);
		setLocationRelativeTo(null);
		setContentPane(new JLabel (new ImageIcon (new ImageIcon("Images/background.jpg").getImage().getScaledInstance(600, 600, 0))));
		
		//Title setup
		lblCongrats.setBounds(65, 35, 400, 40);
		lblCongrats.setFont(titleFont);
		lblCongrats.setForeground(Color.white);
		add(lblCongrats);

		//logo setup
		lblAnimation.setBounds(0, 80, 350, 400);
		lblAnimation.setIcon(new ImageIcon ("Images/end.png"));
		add(lblAnimation);

	}

	//This method prints the final stats of the game
	private void printStats(String playerName) {

		//Text area setup
		scoreInfo.setBounds(300, 200, 400, 100);
		scoreInfo.setFont(textFont);
		scoreInfo.setForeground(Color.white);
		scoreInfo.setLineWrap(true);
		scoreInfo.setWrapStyleWord(true);
		scoreInfo.setEditable(false);
		scoreInfo.setOpaque(false);
		scoreInfo.setText("Winner is\n " + playerName + "!");
		add(scoreInfo);

	}

	//This method sets up the button
	private void buttonSetup() {

		exitGame.setBounds(150, 420, 450, 50);
		exitGame.setFont(buttonFont);
		exitGame.setForeground(Color.white);
		exitGame.setOpaque(false);
		exitGame.setContentAreaFilled(false);
		exitGame.setBorderPainted(false);
		exitGame.setFocusPainted(false);
		exitGame.addActionListener(this);
		add(exitGame);

	}

	//This method implements the action listener
	public void actionPerformed(ActionEvent event) {

			//Exit the program
			System.exit(0);

	}

}
