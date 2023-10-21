package Buttons;

import javax.swing.*;

import Dice.Dice;
import Player.Player;
import Tile.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EndTurnButton extends JButton {
    private final List<Player> players;
    int currentPlayer = 1;
    boolean turnEnded = true;
    private List<JButton> playerButtons;
    private final JLabel seconds_left = new JLabel();
    private final JButton diceButton;
    public EndTurnButton(List<JButton> playerButtons,List<Player> players, int currentPlayer, JButton diceButton) {
        this.players = players;
        this.playerButtons = playerButtons;
        this.currentPlayer = currentPlayer;
        this.diceButton = diceButton;
        this.setBounds(0, 500, 100, 110); // Set the position and size
        this.setFont(new Font("Times New Roman", Font.BOLD, 15)); // Set the font
        this.setFocusable(false); // Set the focusable property
        this.setText("<html>End<br>Turn</html>"); // Set the text
        this.addActionListener(e -> EndTurn());
    }

    Timer timer = new Timer(1000, e -> {
        int seconds = 60;
        seconds--;
        seconds_left.setText(String.valueOf(seconds));
        if (seconds <= 0) {
            ((Timer) e.getSource()).stop();
            System.out.print("Game over\n");

        }

    });

    public void EndTurn() {
            int seconds = 60;
            seconds_left.setText(String.valueOf(seconds)); // Update the seconds_left label
            timer.start(); // Start the timer
            System.out.println(playerButtons.get(currentPlayer - 1).getText() + " has ended their turn");
            turnEnded = true;
            currentPlayer = ((currentPlayer) % players.size()) + 1; // Switch to the next player
            diceButton.setEnabled(true);

    }
}