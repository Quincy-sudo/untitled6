package Player;
import Tile.Tile;

public class Player {
    private String name;
    private int score;
    private int position;
    private int resources;
    
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.resources = 1500;
        this.position = 0;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
public void increaseScore(int amount) {
    score += amount;
}
    public int getPosition() {
        return position;
    }
public int getResources() {
        return this.resources;
    }
    public void setPosition(int position) {
        this.position = position;
    }
    public void setResources(int resources) {
        this.resources = resources;
    }
public void buyTile(Tile tile) {
        if (resources >= tile.getPrice()) {
            resources -= tile.getPrice();
            tile.setOwner(this);
        } else {
            System.out.println("You don't have enough resources to buy this tile.");
        }
    }
}