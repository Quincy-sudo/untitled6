import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import Buttons.DiceButton;
import Buttons.EndTurnButton;
import Player.Player;
import Tile.LoadTileTransaction;
import Tile.Tile;
import Dice.Dice;
import Tile.TileType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;

public class Game extends JFrame {

    private final java.util.List<Tile> gameBoard;
    int currentPlayer = 1;
    private int seconds = 60;
    private final JFrame frame = new JFrame();
    private final JButton startGameButton = new JButton();
    private final List<Player> players = new ArrayList<>();
    private final List<JButton> playerButtons = new ArrayList<>();
    private final JButton actionsButton = new JButton();
    private final JButton dice_button = new JButton();
    private final JLabel seconds_left = new JLabel();
    private final List<JLabel> playerScoreLabels = new ArrayList<>();


    // Custom OutputStream that appends text to the textfield
    private static class TextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;
        private final int maxTextLength;

        public TextAreaOutputStream(JTextArea textArea, int maxTextLength) {
            this.textArea = textArea;
            this.maxTextLength = maxTextLength;
        }

        public void write(int b) {
            textArea.append(String.valueOf((char) b));
            if (textArea.getText().length() > maxTextLength) {
                textArea.setText(""); // Clear text if it exceeds the limit
            }
            textArea.setCaretPosition(textArea.getDocument().getLength()); // Scroll to the bottom
        }
    }

    public Game() {
        LoadTileTransaction loadTileTransaction = new LoadTileTransaction();
        gameBoard = loadTileTransaction.loadTiles();


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 650);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(null);
        frame.setResizable(false);

        JLabel titleLabel = new JLabel();
        titleLabel.setBounds(175, 10, 650, 50);
        titleLabel.setFont(new Font("Times New Roman", Font.BOLD, 40));
        titleLabel.setForeground(new Color(255, 255, 255)); // Set color to white
        titleLabel.setText("Making It Better");

        JTextArea textfield = new JTextArea();
        textfield.setBounds(100, 100, 620, 400);
        textfield.setBackground(new Color(25, 25, 25));
        textfield.setForeground(new Color(255, 255, 255));
        textfield.setFont(new Font("Times New Roman", Font.BOLD, 20));
        textfield.setBorder(BorderFactory.createBevelBorder(1));
        textfield.setEditable(false);
        textfield.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(textfield);
        scrollPane.setBounds(100, 100, 540, 400);

        DiceButton diceButton = new DiceButton(players, gameBoard, playerScoreLabels, currentPlayer);
        frame.add(diceButton);
        diceButton.setEnabled(false);

        PrintStream printStream = new PrintStream(new TextAreaOutputStream(textfield, 20000));
        System.setOut(printStream);
        System.setErr(printStream);

        int numPlayers;
        while (true) {
            String numPlayersStr = JOptionPane.showInputDialog("Enter the number of players (1-4) or type 'exit' to quit:");
            if ("exit".equalsIgnoreCase(numPlayersStr)) {
                System.exit(0); // Exit the program if the user types 'exit'
            }
            try {
                numPlayers = Integer.parseInt(numPlayersStr);
                if (numPlayers >= 1 && numPlayers <= 4) {
                    break; // Exit the loop if the number is valid
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid number of players. Please enter a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
            }

        }
        for (int i = 0; i < numPlayers; i++) {
            final int finalI = i; // Create a final copy of i
            String playerName = "Player " + (i + 1);
            players.add(new Player(playerName)); // Use add() method to add a new player
            JButton playerButton = new JButton();
            playerButton.setBounds(0, 100 + (i * 100), 98, 50);
            playerButton.setFont(new Font("Times New Roman", Font.BOLD, 15));
            playerButton.setFocusable(false);
            playerButton.setText(playerName);
            JLabel scoreLabel = new JLabel("Score: 0 ");
            playerScoreLabels.add(scoreLabel);
            playerButton.addActionListener(e -> {
                playerScoreLabels.add(scoreLabel);
                JLabel resourceLabel = new JLabel("Resources: " + players.get(finalI).getResources());
                playerScoreLabels.add(resourceLabel);
                JLabel tilesOwnedLabel = new JLabel("Tiles Owned: " + players.get(finalI).getTilesOwned().stream().map(Tile::getName).collect(Collectors.joining(", ")));
                playerScoreLabels.add(tilesOwnedLabel);
                // Create a new JFrame to display the player's score, resources and tiles owned
                JFrame scoreFrame = new JFrame("Score, Resources and Tiles Owned");
                scoreFrame.setSize(400, 200);
                scoreFrame.setLayout(new BoxLayout(scoreFrame.getContentPane(), BoxLayout.Y_AXIS)); // Set layout to BoxLayout to arrange labels vertically
                scoreFrame.add(scoreLabel);
                scoreFrame.add(resourceLabel);
                scoreFrame.add(tilesOwnedLabel);
                scoreFrame.setVisible(true);
            });
            playerButtons.add(playerButton); // Use add() method to add the button to the list
            frame.add(playerButton);
        }


        seconds_left.setBounds(535, 510, 100, 100);
        seconds_left.setBackground(new Color(25, 25, 25));
        seconds_left.setForeground(new Color(255, 0, 0));
        seconds_left.setFont(new Font("Times New Roman", Font.BOLD, 60));
        seconds_left.setBorder(BorderFactory.createBevelBorder(1));
        seconds_left.setOpaque(true);
        seconds_left.setHorizontalAlignment(JTextField.CENTER);
        seconds_left.setText(String.valueOf(seconds));

        JLabel time_label = new JLabel();
        time_label.setBounds(535, 510, 100, 25);
        time_label.setBackground(new Color(50, 50, 50));
        time_label.setForeground(new Color(255, 0, 0));
        time_label.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        time_label.setHorizontalAlignment(JTextField.CENTER);
        time_label.setText("timer >:D");

        actionsButton.setBounds(125, 525, 200, 100); // Adjust these values as needed
        actionsButton.setFont(new Font("Times New Roman", Font.BOLD, 35));
        actionsButton.setFocusable(false);
        actionsButton.setEnabled(false);
        actionsButton.setText("Actions");
        actionsButton.addActionListener(a -> {
            String[] options = {"Buy Tile", "Trade Tile", "Buy Tile from Player", "Offer Current Tile", "Option 5"};
            String selectedOption = (String) JOptionPane.showInputDialog(
                    frame,
                    playerButtons.get(currentPlayer - 1).getText() + ", choose an action:",
                    "Action Selection - " + currentPlayer,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            Tile currentTile = gameBoard.get(players.get(currentPlayer - 1).getPosition());
            if (selectedOption != null) {
                System.out.println(playerButtons.get(currentPlayer - 1).getText() + " You selected: " + selectedOption);

                switch (selectedOption) {
                    case "Buy Tile":
                        // Check if the tile is a start or special tile
                        if (currentTile.getType() == TileType.START || currentTile.getType() == TileType.SPECIAL) {
                            System.out.println("You cannot buy the start or special tiles.");
                        } else if (currentTile.getOwner() != null) {
                            // Check if the tile is already owned by another player
                            JOptionPane.showMessageDialog(null, "This tile is already owned by Player " + currentTile.getOwner().getName(), "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            // Check if the player has enough resources to buy the tile
                            if (players.get(currentPlayer - 1).getResources() >= currentTile.getPrice()) {
                                // Deduct the cost of the tile from the player's resources
                                players.get(currentPlayer - 1).setResources(players.get(currentPlayer - 1).getResources() - currentTile.getPrice());
                                // Change the owner of the tile to the current player
                                currentTile.setOwner(players.get(currentPlayer - 1));
                                players.get(currentPlayer - 1).getTilesOwned().add(currentTile);
                                System.out.println(players.get(currentPlayer - 1).getName() + " bought tile " + currentTile.getPosition());
                            } else {
                                System.out.println("You do not have enough resources to buy this tile.");
                            }
                        }
                        break;
                    case "Trade Tile":
                        // Ask the player which tile they want to trade
                        String tileToTrade = JOptionPane.showInputDialog(
                                frame,
                                "Enter the name of the tile you want to trade:",
                                "Tile Trading - " + currentPlayer,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        // Ask the player who they want to trade with
                        String playerToTradeWith = JOptionPane.showInputDialog(
                                frame,
                                "Enter the name of the player you want to trade with:",
                                "Tile Trading - " + currentPlayer,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        // Find the player object for the player to trade with
                        Player tradePartner = players.stream()
                                .filter(player -> player.getName().equals(playerToTradeWith))
                                .findFirst()
                                .orElse(null);
                        // Find the tile object for the tile to trade
                        Optional<Tile> optionalTile = gameBoard.stream()
                                .filter(tile -> tile.getName().equals(tileToTrade))
                                .findFirst();
                        // If the trade partner exists and the current player owns the tile they want to trade
                        if (tradePartner != null && optionalTile.isPresent() && optionalTile.get().getOwner().equals(players.get(currentPlayer - 1))) {
                            Tile tileToTradeObject = optionalTile.get();
                            // Change the owner of the tile to the trade partner
                            tileToTradeObject.setOwner(tradePartner);
                            // Remove the tile from the current player's list of owned tiles
                            players.get(currentPlayer - 1).getTilesOwned().remove(currentTile);
                            // Add the tile to the trade partner's list of owned tiles
                            tradePartner.getTilesOwned().add(currentTile);
                            System.out.println(players.get(currentPlayer - 1).getName() + " traded tile " + tileToTrade + " with " + tradePartner.getName());
                        } else {
                            System.out.println("Trade failed. Check if the tile and player you want to trade with exist and you own the tile.");
                        }
                        break;
                    case "Buy Tile from Player":
                        // Ask the player who they want to buy from
                        String playerToBuyFrom = JOptionPane.showInputDialog(
                                frame,
                                "Enter the name of the player you want to buy from:",
                                "Tile Buying - " + currentPlayer,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        String tileToBuy = JOptionPane.showInputDialog(
                                frame,
                                "Enter the name of the tile you want to buy:",
                                "Tile Buying - " + currentPlayer,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        // Find the player object for the player to buy from
                        Player seller = players.stream()
                                .filter(player -> player.getName().equals(playerToBuyFrom))
                                .findFirst()
                                .orElse(null);
                        // If the seller exists and owns the tile the current player wants to buy
                        if (seller != null && seller.getTilesOwned().stream().anyMatch(tile -> tile.getName().equals(tileToBuy))) {
                            Tile tileToBuyObject = seller.getTilesOwned().stream().filter(tile -> tile.getName().equals(tileToBuy)).findFirst().get();
                            // Check if the player has enough resources to buy the tile
                            if (players.get(currentPlayer - 1).getResources() >= tileToBuyObject.getPrice()) {
                                // Deduct the cost of the tile from the player's resources
                                players.get(currentPlayer - 1).setResources(players.get(currentPlayer - 1).getResources() - tileToBuyObject.getPrice());
                                // Change the owner of the tile to the current player
                                tileToBuyObject.setOwner(players.get(currentPlayer - 1));
                                // Remove the tile from the seller's list of owned tiles
                                seller.getTilesOwned().remove(tileToBuyObject);
                                // Add the tile to the current player's list of owned tiles
                                players.get(currentPlayer - 1).getTilesOwned().add(tileToBuyObject);
                                System.out.println(players.get(currentPlayer - 1).getName() + " bought tile " + tileToBuy + " from " + seller.getName());
                            } else {
                                System.out.println("You do not have enough resources to buy this tile.");
                            }
                        } else {
                            System.out.println("Purchase failed. Check if the tile and player you want to buy from exist and the player owns the tile.");
                        }

                        break;
                }
                if (selectedOption.equals("Offer Current Tile")) {
                    // Check if the tile has already been offered
                    if (currentTile.isOffered()) {
                        System.out.println("This tile has already been offered. You cannot offer it again.");
                    } else {
                        // Check if the current player owns the tile they want to offer
                        // Check if the tile is owned by any player
                        boolean isTileOwned = players.stream()
                                .anyMatch(player -> player.getTilesOwned().contains(currentTile));

                        // If the tile is not owned by any player
                        if (!isTileOwned) {
                            // Ask the player who they want to offer the tile to
                            String playerToOfferTo = JOptionPane.showInputDialog(
                                    frame,
                                    "Enter the name of the player you want to offer the tile to:",
                                    "Tile Offering - " + currentPlayer,
                                    JOptionPane.QUESTION_MESSAGE
                            );
                            // Find the player object for the player to offer to
                            Player offerTo = players.stream()
                                    .filter(player -> player.getName().equals(playerToOfferTo))
                                    .findFirst()
                                    .orElse(null);
                            // If the offerTo player exists
                            if (offerTo != null) {
                                // Check if the tile is a special tile or the start tile
                                if (currentTile.getType() == TileType.SPECIAL || currentTile.getPosition() == 0) {
                                    System.out.println("Offer failed. You cannot offer special tiles or the start tile.");
                                } else {
                                    // Check if the player has enough resources to buy the tile
                                    if (offerTo.getResources() >= currentTile.getPrice()) {
                                        // Deduct the cost of the tile from the player's resources
                                        offerTo.setResources(offerTo.getResources() - currentTile.getPrice());
                                        // Change the owner of the tile to the offerTo player
                                        currentTile.setOwner(offerTo);
                                        // Add the tile to the list of tiles owned by the offerTo player
                                        offerTo.getTilesOwned().add(currentTile);
                                        // Mark the tile as offered
                                        currentTile.setOffered(true);
                                        System.out.println(offerTo.getName() + " has used their resources to buy the tile " + currentTile.getPosition() + " from " + players.get(currentPlayer - 1).getName());
                                    } else {
                                        System.out.println(offerTo.getName() + " does not have enough resources to buy this tile.");
                                    }
                                }
                            } else {
                                System.out.println("Offer failed. Check if the player you want to offer to exists.");
                            }
                        } else {
                            System.out.println("Offer failed. The tile is owned by another player.");
                        }
                    }
                }
            }
        });

        Timer timer = new Timer(1000, e -> {
            seconds--;
            seconds_left.setText(String.valueOf(seconds));
            if (seconds <= 0) {
                ((Timer) e.getSource()).stop();
                System.out.print("Game over\n");

            }

        });
        EndTurnButton endTurnButton = new EndTurnButton(playerButtons, players, currentPlayer, diceButton);

        startGameButton.setBounds(0, 500, 98, 110);// Adjust these values as needed
        startGameButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        startGameButton.setFocusable(false);
        startGameButton.setText("Start");
        startGameButton.addActionListener(e -> {
            dice_button.setEnabled(true);
            actionsButton.setEnabled(true);
            seconds = 60; // Reset the seconds variable
            System.out.println("game has started");
            seconds_left.setText(String.valueOf(seconds)); // Update the seconds_left label
            timer.start(); // Start the timer
            frame.remove(startGameButton);
            frame.add(endTurnButton);
            frame.revalidate();
            frame.repaint();
            diceButton.setEnabled(true);

        });
        promptPlayerNames();
        frame.add(startGameButton);
        frame.add(titleLabel);
        frame.add(scrollPane);
        frame.add(time_label);
        frame.add(seconds_left);
        frame.add(actionsButton);
        frame.setVisible(true);

    }

    public void promptPlayerNames() {
        if (playerButtons != null && players != null) {
            for (int i = 0; i < playerButtons.size(); i++) {
                JButton button = playerButtons.get(i);
                if (button != null) {
                    String newName = GameActions.Uniquename(button.getText());
                    if (newName != null) {
                        Player player = players.get(i);
                        if (player != null) {
                            player.setName(newName);
                            button.setText(newName);
                            System.out.print("Player " + (i + 1) + " has changed their name to " + newName + "\n");
                        }
                    }
                }
            }
        }
    }
}
