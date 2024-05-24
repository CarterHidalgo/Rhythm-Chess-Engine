package helper;

public class Coord {
    /*
     * A 2D Vector similar to Vec2 but with some additional methods such as the add and isValid
     * method. This is used specifically to traverse the board without falling off the edge.
     * 
     * Includes directional Coords for incrementing a coord in a rook or bishop direction by 1 square
     */

    public static final Coord[] rookDirections = {new Coord(1, 0), new Coord(0, 1), new Coord(-1, 0), new Coord(0, -1)};
    public static final Coord[] bishopDirections = {new Coord(1, 1), new Coord(-1, 1), new Coord(-1, -1), new Coord(1, -1)};

    private int rank;
    private int file;

    public Coord(int rank, int file) {
        this.rank = rank;
        this.file = file;
    }

    public Coord(int index) {
        rank = index / 8;
        file = index % 8;
    }

    public int getIndex() {
        return 8 * rank + file;
    }

    public int getRank() {
        return rank;
    }

    public int getFile() {
        return file;
    }

    public boolean isValid() {
        return rank >= 0 && rank < 8 && file >= 0 && file < 8;
    }

    public boolean isNotEdge() {
        return rank > 0 && rank < 7 && file > 0 && file < 7;
    }

    public Coord add(Coord addend) {
        return new Coord(rank + addend.getRank(), file + addend.getFile());
    }

    public Coord mul(int magnitude) {
        return new Coord(rank * magnitude, file * magnitude);
    }

    public String toString() {
        return "rank: " + this.rank + "\nfile: " + this.file;
    }
}
