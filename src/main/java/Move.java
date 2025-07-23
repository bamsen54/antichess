
// keeps track of a move
// whereas Moves.java makes moves and checks which moves are legal
public class Move {

    public int from_col;
    public int from_row;
    public int to_col;
    public int to_row;

    public boolean capture;
    public boolean en_passant;
    public String promote_to;

    Move(int from_col, int from_row, int to_col, int to_row) {

        this.from_col = from_col;
        this.from_row = from_row;
        this.to_col   = to_col;
        this.to_row   = to_row;

        this.capture  = false;
        this.en_passant = false;
        this.promote_to = "";
    }

    public Move get_copy() {

        Move copy = new Move(this.from_col, this.from_row, this.to_col, this.to_row);

        copy.capture    = this.capture;
        copy.en_passant = this.en_passant;
        copy.promote_to = this.promote_to;

        return copy;
    }

    public String toString() {

        StringBuilder tostring = new StringBuilder();


        tostring.append("(").append(this.from_col).append(", ").append(this.from_row).append(") -> ");
        tostring.append("(").append(this.to_col).append(", ").append(this.to_row).append(") ");

        if( this.capture )
            tostring.append("capture ");

        if( this.en_passant )
            tostring.append("en passant ");

        if( !this.promote_to.isEmpty() )
            tostring.append("promote: ").append(this.promote_to).append(" ");

        return tostring.toString();
    }
}
