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
    private List<JLabel> playerScoreLabels;
    private final List<Tile> gameBoard;
    private final JButton dice_button;
    private final Dice dice;
     private static List<String> usedNames = new ArrayList<>(); // Add this line
    private int currentPlayer = 1;
    private List<JButton> playerButtons; // Add this line
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
    public void performDiceRollAction() {
        dice_button.addActionListener(e -> {
            if (turnEnded) {
                // Roll the dice and perform actions for the current player
                int diceRoll = rollDice();
                System.out.println(players.get(currentPlayer - 1).getName() + " rolled a " + diceRoll);

                // Get the current player's position on the game board
                int currentPosition = players.get(currentPlayer- 1).getPosition();

                // Update the player's position by adding the dice roll
                int newPosition = currentPosition + diceRoll;

                // Check if the new position exceeds the maximum position on the game board
                if (newPosition >= gameBoard.size()) {
                    newPosition = newPosition % gameBoard.size();
                    // Add resources to the player when they pass the start tile
                    players.get(currentPlayer- 1).setResources(players.get(currentPlayer- 1).getResources() + 250); // Add 200 resources
                    System.out.println(players.get(currentPlayer- 1).getName() + "'s resources increased to " + players.get(currentPlayer- 1).getResources());
                }

                players.get(currentPlayer - 1).setPosition(newPosition);
                // Find the tile corresponding to the new position
                Tile currentTile = gameBoard.get(newPosition);

                // Output the tile information
                System.out.println(players.get(currentPlayer - 1).getName() + " landed on " + currentTile.getName() + " - " + currentTile.getType());

                // Check if the current tile is a special tile
                if (currentTile.getType() == TileType.SPECIAL) {
                    // Increase the player's score by a certain amount
                    players.get(currentPlayer- 1).increaseScore(10); // Increase the score by 10 (you can change the amount as needed)

                    System.out.println(players.get(currentPlayer- 1).getName() + "'s score increased to " + players.get(currentPlayer- 1).getScore());
                    // Update the player's score label
                    playerScoreLabels.get(currentPlayer- 1).setText(" Score: " + players.get(currentPlayer- 1).getScore());

                } else if (currentTile.getOwner() == null && currentTile.getType() != TileType.START && currentTile.getType() != TileType.SPECIAL) {
                    // Ask the player if they want to buy the tile
                    int response = JOptionPane.showConfirmDialog(null, "Do you want to buy tile " + currentTile.getPosition() + " for " + currentTile.getPrice() + " resources?", "Buy Tile", JOptionPane.YES_NO_OPTION);
                    if (response == JOptionPane.YES_OPTION) {
                        players.get(currentPlayer- 1).buyTile(currentTile);
                        players.get(currentPlayer- 1).getTilesOwned().add(currentTile);
                        System.out.println(players.get(currentPlayer- 1).getName() + " you have bought " + currentTile.getName() );
                    }
                }
                dice_button.setEnabled(false);

                turnEnded = false; // Set turnEnded to false to indicate that the current player's turn has not ended
            } else {
                System.out.println("End your turn before the next player can roll the dice.");
            }
        });
    }

    private int rollDice() {
        // Generate random numbers between 1 and 6 for two dice
        int dice1 = (int) (Math.random() * 6) + 1;
        int dice2 = (int) (Math.random() * 6) + 1;
        // Calculate the sum of the two dice rolls

        return dice1 + dice2;
    }
    public void addEndTurnButtonAction(JButton endTurnButton) {
    endTurnButton.addActionListener(e -> {
        // Increment currentPlayer or reset to 1 if it exceeds the number of players
        currentPlayer = currentPlayer % players.size() + 1;
        System.out.println(currentPlayer - 1 + " has ended their turn");
        // Set turnEnded to true to allow the next player to roll the dice
        turnEnded = true;

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