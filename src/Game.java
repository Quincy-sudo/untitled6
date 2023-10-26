import Buttons.ActionsButton;
import Buttons.DiceButton;
import Buttons.EndTurnButton;
import Buttons.StartGameButton;
import Player.Player;
import Tile.LoadTileTransaction;
import Tile.Tile;
import Tile.TileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Game extends JFrame {

    private final java.util.List<Tile> gameBoard;
    int currentPlayer = 1;
    private int seconds = 60;
    private final JButton startGameButton = new JButton();
    private final List<Player> players = new ArrayList<>();
    private final List<JButton> playerButtons = new ArrayList<>();
  /*  private final JButton actionsButton = new JButton();*/
    private final JLabel seconds_left = new JLabel();
    private final List<JLabel> playerScoreLabels = new ArrayList<>();
    Timer timer = new Timer(1000, this::Timer);
    private final JProgressBar progressBar = new JProgressBar();
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


        JFrame frame = new JFrame();
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

        frame.add(progressBar);
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

        DiceButton diceButton = new DiceButton(players, gameBoard, playerScoreLabels, currentPlayer);
        frame.add(diceButton);
        diceButton.setEnabled(false);

        ActionsButton actionsButton = new ActionsButton(frame, playerButtons, players, gameBoard);
        frame.add(actionsButton);

        EndTurnButton endTurnButton = new EndTurnButton(playerButtons, players, currentPlayer, diceButton, seconds_left, timer, diceButton, actionsButton);

        StartGameButton StartGameButton = new StartGameButton(diceButton, actionsButton, seconds_left, timer, frame, endTurnButton);
        frame.add(StartGameButton);

        promptPlayerNames();
        frame.add(StartGameButton);
        frame.add(titleLabel);
        frame.add(scrollPane);
        frame.add(time_label);
        frame.add(seconds_left);
        frame.setVisible(true);

    }
    private void Timer (ActionEvent e) {
        seconds--;
        seconds_left.setText(String.valueOf(seconds));
        if (seconds <= 0) {
            ((Timer) e.getSource()).stop();
            System.out.print("Game over\n");
        }

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
