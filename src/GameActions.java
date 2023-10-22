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

    List<Player> players;

    private static final List<String> usedNames = new ArrayList<>(); // Add this line
    int currentPlayer = 1;
    private List<JButton> playerButtons;
    private boolean turnEnded = true;

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