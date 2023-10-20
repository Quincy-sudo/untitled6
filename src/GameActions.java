import Dice.Dice;
import Player.Player;
import Tile.Tile;
import Tile.TileType;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameActions {

    private final List<Player> players;
    private final List<JLabel> playerScoreLabels;
    private final List<Tile> gameBoard;
    private final JButton dice_button;
    private final Dice dice;
    private static final List<String> usedNames = new ArrayList<>(); // Add this line
    int currentPlayer = 1;
    private List<JButton> playerButtons;
    private boolean turnEnded = true;

    public GameActions(List<Player> players, List<JLabel> playerScoreLabels, List<Tile> gameBoard, JButton dice_button, Dice dice, int currentPlayer, boolean turnEnded, List<JButton> playerButtons) {
        this.players = players;
        this.playerScoreLabels = playerScoreLabels;
        this.gameBoard = gameBoard;
        this.dice_button = dice_button;
        this.dice = dice;
        this.currentPlayer = currentPlayer;
        this.turnEnded = turnEnded;
        this.playerButtons = playerButtons; // Initialize playerButtons
    }
    public GameActions(List<Player> players, List<JLabel> playerScoreLabels, List<Tile> gameBoard, JButton diceButton, Dice dice, int currentPlayer, boolean turnEnded) {
        this.players = players;
        this.playerScoreLabels = playerScoreLabels;
        this.gameBoard = gameBoard;
        this.dice_button = diceButton;
        this.dice = dice;
        this.currentPlayer = currentPlayer;
        this.turnEnded = turnEnded;
    }
   
    public void addEndTurnButtonAction(JButton endTurnButton) {
        endTurnButton.addActionListener(e -> {
            // Increment currentPlayer or reset to 1 if it exceeds the number of players
            System.out.println(currentPlayer + " has ended their turn");
            // Set turnEnded to true to allow the next player to roll the dice
            turnEnded = true;
            currentPlayer = (currentPlayer % players.size()) + 1;
            // Enable the dice_button for the next player's turn
            dice_button.setEnabled(true);
        });
    }
    public void displayFinalState() {
        System.out.println("Final State of Play:");
        for (Player player : players) {
            System.out.println(player.getName() + " - Position: " + player.getPosition());
            System.out.println("Score: " + player.getScore());
            System.out.println("Resources: " + player.getResources());
            System.out.println("Tiles Owned: " + player.getTilesOwned().stream().map(Tile::getName).collect(Collectors.joining(", ")));
        }
    }
    public static String Uniquename(String playerLabel) {
        String name;
        do {
            Component frame = null;
            name = JOptionPane.showInputDialog(null, "Enter new name for " + playerLabel);
            if (name != null && name.trim().isEmpty()) {
                // Name is empty, show a message
                JOptionPane.showMessageDialog(null, "Name cannot be empty. Please enter a different name.");
            } else if (name != null && name.length() > 8) {
                // Name is more than 8 characters long, show a message
                JOptionPane.showMessageDialog(null, "Name must be up to 8 characters long. Please enter a different name.");
            } else if (usedNames.contains(name)) {
                // Name is already used, show a message
                JOptionPane.showMessageDialog(null, "Name '" + name + "' is already in use. Please enter a different name.");
            }
        } while (name != null && (name.trim().isEmpty() || name.length() > 8 || usedNames.contains(name)));
        if (name != null) {
            usedNames.add(name);
        }
        return name;
    }
}