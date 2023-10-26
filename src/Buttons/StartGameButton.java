package Buttons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartGameButton extends DefaultButton {
     final JButton diceButton;
     final JButton actionsButton;
     final JLabel secondsLeft;
     final Timer timer;
     final JFrame frame;
     final JButton endTurnButton;

    public StartGameButton(JButton diceButton, JButton actionsButton, JLabel secondsLeft, Timer timer, JFrame frame, JButton endTurnButton) {
        super("Start");
        this.diceButton = diceButton;
        this.actionsButton = actionsButton;
        this.secondsLeft = secondsLeft;
        this.timer = timer;
        this.frame = frame;
        this.endTurnButton = endTurnButton;
        this.setEnabled(true);
        this.setBounds(0, 500, 98, 110); // Adjust these values as needed
        this.setFont(new Font("Times New Roman", Font.BOLD, 20));
        this.setText("Start");
        this.addActionListener(e -> {
            diceButton.setEnabled(true);
            actionsButton.setEnabled(true);
            secondsLeft.setText("60"); // Reset the seconds variable
            System.out.println("game has started");
            timer.start(); // Start the timer
            frame.remove(StartGameButton.this);
            frame.add(endTurnButton);
            frame.revalidate();
            frame.repaint();
            diceButton.setEnabled(true);
        });
    }
}