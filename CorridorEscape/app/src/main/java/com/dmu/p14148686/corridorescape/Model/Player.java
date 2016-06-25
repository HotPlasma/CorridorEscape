package com.dmu.p14148686.corridorescape.Model;

/**
 * Created by P14148686 on 11/04/2016.
 */
public class Player implements Comparable<Player> { // Used to compare players between eachother
    private String name;
    private int score;

    public Player(String name, int score)
    {
        super();
        this.name = name;
        this.score = score;
    }

    public String getName()
    {
        return name;
    }

    public int getScore()
    {
        return score;
    }

    public int compareTo(Player other) // Allows comparison between players
    {
        if (other.getScore() < this.score) return -1;
        if (other.getScore() > this.score) return 1;
        return 0;
    }
}
