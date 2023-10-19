package Tile;

import java.util.ArrayList;
import java.util.List;

public class LoadTileTransaction {

    public static List<Tile> gameTiles = new ArrayList<>();

    public List<Tile> loadTiles() {
        for (int i = 0; i <= 14; i++) {
            String name = i == 0 ? "Square 0" : "Square" + i;
            TileType type;
            int price;

            if (i == 0) {
                type = TileType.START;
                price = 50;
            } else if (i == 1 || i >= 11) {
                type = TileType.SPECIAL;
                price = 100;
            } else {
                type = TileType.NORMAL;
                price = i == 2 ? 111 : 100;
            }

            Tile tile = new Tile(name, i, type, price);
            gameTiles.add(tile);
        }

        return gameTiles;
    }
}