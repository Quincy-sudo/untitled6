package Buttons;

import javax.swing.*;
import Tile.TileType;
import Dice.Dice;
import Player.Player;
import Tile.Tile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EndTurnButton extends DefaultButton {
    private List<Player> players;
    int currentPlayer = 1;
    boolean turnEnded = true;
    private final List<JButton> playerButtons;
    private final JLabel seconds_left;
    private final JButton diceButton;
    private final DiceButton diceButton_Player;
    private final ActionsButton actionsButton;
    private int seconds = 60;
    Timer timer;

    {
        new Timer(1000, this::Timer);
    }

    public EndTurnButton(List<JButton> playerButtons, List<Player> players, int currentPlayer, JButton diceButton, JLabel seconds_left, Timer timer, DiceButton diceButton_Player,ActionsButton actionsButton) {
        super("<html>End<br>Turn</html>");
        this.players = players;
        this.timer = timer;
        this.playerButtons = playerButtons;
        this.currentPlayer = currentPlayer;
        this.diceButton_Player = diceButton_Player;
        this.diceButton = diceButton;
        this.seconds_left = seconds_left;
        this.actionsButton = actionsButton;
        this.setEnabled(true);
        this.setFont(new Font("Times New Roman", Font.BOLD, 20));
        this.setBounds(0, 500, 100, 110); // Set the position and size
        this.addActionListener(e -> EndTurn());
    }


    public void EndTurn() {
        seconds_left.setText(String.valueOf(seconds)); // Update the seconds_left label
        timer.stop();
        timer.start(); // Start the timer
        System.out.println(playerButtons.get(currentPlayer - 1).getText() + " has ended their turn");
        turnEnded = true;
        currentPlayer = ((currentPlayer) % players.size()) + 1; // Switch to the next player
        diceButton.setEnabled(true);
        diceButton_Player.setTurnEnded(true);
        diceButton_Player.setCurrentPlayer(currentPlayer);
        actionsButton.setTurnEnded(true);
        actionsButton.setCurrentPlayer(currentPlayer);

    }

    private void Timer(ActionEvent e) {
        seconds--;
        seconds_left.setText(String.valueOf(seconds));
        if (seconds <= 0) {
            ((Timer) e.getSource()).stop();
            System.out.print("Game over\n");
        }

    }
}