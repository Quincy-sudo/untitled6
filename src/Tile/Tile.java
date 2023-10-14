package Tile;

import Player.Player;

public class Tile {
    private final int position;
    private final TileType type;
    private final int price;
    private Player owner;
   private int cost;

    public Tile(int position, TileType type, int price) {
        this.position = position;
        this.type = type;
         this.price = price;

    }

    public int getPosition() {
        return position;
    }

    public TileType getType() {
        return type;
    }
 public int getPrice() {
        return price;
    }
 public int getCost() {
        return this.cost;
    }
    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
