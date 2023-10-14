package Tile;

public class Tile {
    private final int position;
    private final TileType type;

    public Tile(int position, TileType type) {
        this.position = position;
        this.type = type;

    }

    public int getPosition() {
        return position;
    }

    public TileType getType() {
        return type;
    }
}

