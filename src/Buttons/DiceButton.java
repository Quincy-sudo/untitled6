package Buttons;

import Dice.Dice;
import Player.Player;
import Tile.Tile;
import Tile.TileType;

import javax.swing.*;
import java.awt.*;
import java.util.List;


public class DiceButton extends DefaultButton {

    private final List<Player> players;
    int currentPlayer = 1;
    boolean turnEnded = true;
    private final Dice dice;
    private final List<JLabel> playerScoreLabels;
    private final List<Tile> gameBoard;

    public DiceButton(List<Player> players, List<Tile> gameBoard, List<JLabel> playerScoreLabels,int currentPlayer) {
        super("Roll Dice");
        this.players = players;
    this.gameBoard = gameBoard;
    this.playerScoreLabels = playerScoreLabels;
    this.dice = new Dice();
    this.currentPlayer = currentPlayer;
    this.setBounds(335,525,200,100);
    this.addActionListener(e -> performDiceRollAction());
}

  public void performDiceRollAction() {
      if (turnEnded) {
          // Roll the dice and perform actions for the current player
          int diceRoll = dice.roll();
          System.out.println(players.get(currentPlayer - 1).getName() + " rolled a " + diceRoll);

          // Get the current player's position on the game board
          int currentPosition = players.get(currentPlayer - 1).getPosition();

          // Update the player's position by adding the dice roll
          int newPosition = currentPosition + diceRoll;

          // Check if the new position exceeds the maximum position on the game board
          if (newPosition >= gameBoard.size()) {
              newPosition = newPosition % gameBoard.size();
              // Add resources to the player when they pass the start tile
              players.get(currentPlayer - 1).setResources(players.get(currentPlayer - 1).getResources() + 250); // Add 200 resources
              System.out.println(players.get(currentPlayer - 1).getName() + "'s resources increased to " + players.get(currentPlayer - 1).getResources());
          }

          players.get(currentPlayer - 1).setPosition(newPosition);
          // Find the tile corresponding to the new position
          Tile currentTile = gameBoard.get(newPosition);

          // Output the tile information
          System.out.println(players.get(currentPlayer - 1).getName() + " landed on " + currentTile.getName() + " - " + currentTile.getType());

          // Check if the current tile is a special tile
          if (currentTile.getType() == TileType.SPECIAL) {
              // Increase the player's score by a certain amount
              players.get(currentPlayer - 1).increaseScore(10); // Increase the score by 10 (you can change the amount as needed)

              System.out.println(players.get(currentPlayer - 1).getName() + "'s score increased to " + players.get(currentPlayer - 1).getScore());
              // Update the player's score label
              playerScoreLabels.get(currentPlayer - 1).setText(" Score: " + players.get(currentPlayer - 1).getScore());

          } else if (currentTile.getOwner() == null && currentTile.getType() != TileType.START && currentTile.getType() != TileType.SPECIAL) {
              // Ask the player if they want to buy the tile
              int response = JOptionPane.showConfirmDialog(null, "Do you want to buy tile " + currentTile.getPosition() + " for " + currentTile.getPrice() + " resources?", "Buy Tile", JOptionPane.YES_NO_OPTION);
              if (response == JOptionPane.YES_OPTION) {
                  players.get(currentPlayer - 1).buyTile(currentTile);
                  players.get(currentPlayer - 1).getTilesOwned().add(currentTile);
                  System.out.println(players.get(currentPlayer - 1).getName() + " you have bought " + currentTile.getName());
              }
          }
          this.setEnabled(false);

          turnEnded = true; // Set turnEnded to false to indicate that the current player's turn has not ended

      } else {
          System.out.println("End your turn before the next player can roll the dice.");
      }
  }
    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}

