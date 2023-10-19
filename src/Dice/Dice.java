package Dice;

import java.util.Random;

public class Dice {
    private final Random random;

    public Dice() {
        random = new Random();
    }

    public Dice(int dice1, int dice2, boolean b, Random random) {
        this.random = random;
    }

    public int roll() {
        int dice1 = random.nextInt(6) + 1;
        int dice2 = random.nextInt(6) + 1;
        return dice1 + dice2;
    }
}