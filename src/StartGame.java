import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StartGame {

	public static void main(String[] args) {
		StartGame game = new StartGame();
	    game.createMainMenu();
	}
	
	public void createMainMenu() {
		JFrame frame = new JFrame("Making It Better");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel titleLabel = new JLabel("Making It Better");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.BLACK);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        gbc.insets = new Insets(1, 0, 5, 0); // Add some spacing below the title
        panel.add(titleLabel, gbc);

        // Add the mini paragraph label
        JLabel paragraphLabel = new JLabel("A board game set to inspire those to solve the challenges of the Pu Ngaol");
        paragraphLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        paragraphLabel.setHorizontalAlignment(JLabel.CENTER);
        paragraphLabel.setForeground(Color.BLACK);

        gbc.gridy = 1; // Place it below the title
        panel.add(paragraphLabel, gbc);

        JButton startButton = new JButton("Start Game");
        startButton.setBackground(Color.WHITE);
        startButton.setForeground(Color.BLACK);
        startButton.addActionListener(e -> new Game());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2; // Span two columns
        gbc.insets = new Insets(20, 0, 0, 0); // Add some spacing above the button
        panel.add(startButton, gbc);

        panel.setBackground(Color.LIGHT_GRAY);

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);	
	}

}
