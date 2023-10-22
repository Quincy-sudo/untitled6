package Buttons;

import Player.Player;
import Tile.Tile;
import Tile.TileType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class ActionsButton extends JButton {
    final JFrame frame;
    final List<JButton> playerButtons;
    final List<Player> players;
    final List<Tile> gameBoard;
    int currentPlayer = 1;
    boolean turnEnded = true;

    public ActionsButton(JFrame frame, List<JButton> playerButtons, List<Player> players, List<Tile> gameBoard, int currentPlayer) {
        this.frame = frame;
        this.playerButtons = playerButtons;
        this.players = players;
        this.gameBoard = gameBoard;
        this.currentPlayer = currentPlayer;
        this.setBounds(125, 525, 200, 100); // Adjust these values as needed
        this.setFont(new Font("Times New Roman", Font.BOLD, 35));
        this.setFocusable(false);
        this.setEnabled(false);
        this.setText("Actions");
        this.addActionListener(e -> {
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
    }
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
        System.out.println("Current player in ActionsButton is now: " + currentPlayer);
    }
    public void setTurnEnded(boolean turnEnded) {
        this.turnEnded = turnEnded;
    }
}