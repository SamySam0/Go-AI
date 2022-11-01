public class State implements Comparable<State> {

    private Move move;
    private int score;

    public State(Move move, int score) {
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return this.move;
    }

    public int getScore() {
        return this.score;
    }

    @Override
    public int compareTo(State other) {
        return Integer.compare(this.score, other.score);
    }

}
