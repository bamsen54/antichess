
// piece dragged when making move
public class ActivePiece {

    public static char type    = ' ';
    public static int col      = - 1;
    public static int row      = - 1;
    public static String color = null;

    public static void set(char _type, int _col, int _row, String _color) {

        type  = _type;
        col   = _col;
        row   = _row;
        color = _color;
    }

    public static void clear() {

        type  = ' ';
        col   = - 1;
        row   = - 1;
        color = null;
    }
}
