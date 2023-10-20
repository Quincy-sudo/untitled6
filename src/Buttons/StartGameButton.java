package Buttons;

import javax.swing.*;
import java.awt.*;

public class StartGameButton extends JButton {
    private final JButton diceButton;
    private final JButton actionsButton;
    private final JLabel secondsLeft;
    private final Timer timer;
    private final JFrame frame;
    private final JButton endTurnButton;
    private int seconds;

    public StartGameButton(JButton diceButton, JButton actionsButton, JLabel secondsLeft, Timer timer, JFrame frame, JButton endTurnButton) {
        this.diceButton = diceButton;
        this.actionsButton = actionsButton;
        this.secondsLeft = secondsLeft;
        this.timer = timer;
        this.frame = frame;
        this.endTurnButton = endTurnButton;
        this.seconds = 60;

        this.setBounds(0,500,98,110);
        this.setFont(new Font("Times New Roman", Font.BOLD, 20));
        this.setFocusable(false);
        this.setText("Start");
        this.addActionListener(e -> {
            this.diceButton.setEnabled(true);
            this.actionsButton.setEnabled(true);
            this.seconds = 60;
            System.out.println("game has started");
            this.secondsLeft.setText(String.valueOf(this.seconds));
            this.timer.start();
            this.frame.remove(this);
            this.frame.add(this.endTurnButton);
        });
    }
}