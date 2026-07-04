package com.thousand_uncles.discord_bot.bot.util;

public class Pair<U, V> {
    /**
     * The first element of this <code>Pair</code>
     */
    private U first;

    /**
     * The second element of this <code>Pair</code>
     */
    private V second;

    /**
     * Constructs a new <code>Pair</code> with the given values.
     *
     * @param first  the first element
     * @param second the second element
     */
    @SuppressWarnings("unused")
    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public Pair() {
    }

    public void setFirst(U first) {
        this.first = first;
    }

    public U getFirst() {
        return first;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return getFirst() + ", " + getSecond();
    }
}