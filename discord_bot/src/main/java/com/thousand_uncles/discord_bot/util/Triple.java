package com.thousand_uncles.discord_bot.util;

public class Triple<A, B, C> {

    /**
     * The first element of this <code>Pair</code>
     */
    private A first;

    /**
     * The second element of this <code>Pair</code>
     */
    private B second;

    /**
     * The third element of this <code>Pair</code>
     */
    private C third;

    /**
     * Constructs a new <code>Pair</code> with the given values.
     *
     * @param first  the first element
     * @param second the second element
     */
    @SuppressWarnings("unused")
    public Triple(A first, B second, C third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    @SuppressWarnings("unused")
    public Triple(){

    }

    public void setFirst(A first) {
        this.first = first;
    }

    public A getFirst() {
        return first;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    public B getSecond() {
        return second;
    }

    public void setThird(C third) {
        this.third = third;
    }

    public C getThird() {
        return third;
    }

    @Override
    public String toString() {
        return first + ", " + second + ", " + third;
    }

    //getter for first and second
}
