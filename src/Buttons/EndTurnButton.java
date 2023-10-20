package Buttons;

import javax.swing.*;

import Player.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EndTurnButton extends JButton {
    private int currentPlayer = 1;

    public EndTurnButton(JButton diceButton, JButton actionsButton, JLabel secondsLeft, Timer timer, JFrame frame, List<Player> players) {
        int seconds = 60;
        this.setBounds(0, 500, 100, 110);
        this.setFont(new Font("Times New Roman", Font.BOLD, 15));
        this.setFocusable(false);
        this.setText("<html>End<br>Turn</html>");
        diceButton.setEnabled(true);
        actionsButton.setEnabled(true);
        System.out.println("game has started");
        secondsLeft.setText(String.valueOf(seconds));
        timer.start();

    }
}
    
