import java.util.ArrayList;

public class Util {

    // length are always 2
    public static boolean check_if_square_are_equal(int[] square_1, int[] square_2) {

        return square_1[0] == square_2[0] && square_1[1] == square_2[1];
    }

    // example: 'K' -> "white" ' ' -> "empty" 'p' -> "black"
    public static String get_color_of_piece_type(char type) {

        if( type == ' ' )
            return "empty";

        if( type == Character.toUpperCase(type) )
            return "white";

        else
            return "black";
    }

    public static boolean is_different_color(char type_1, char type_2 ) {

        if( type_1 == ' ' || type_2 == ' ' )
            return false;

        if( get_color_of_piece_type( type_1).equals( get_color_of_piece_type( type_2 ) ) )
            return false;

        return true;
    }

    // if move[0] and move[1] are both in [0, 7] return true, false otherwise
    public static boolean check_if_square_is_on_board(int[] square) {

        final int col = square[0];
        final int row = square[1];

        return col >= 0 && col <= 7 && row >= 0 && row <= 7;
    }

    public static boolean check_square_is_empty(Game game, int[] square) {

        final int col = square[0];
        final int row = square[1];

        return game.board[row][col] == ' ';
    }

    public static boolean check_if_piece_is_on_back_rank( int[] square ) {

        return square[1] == 0 || square[1] == 7;
    }

    public static boolean check_if_move_square_is_in_moves(ArrayList<Move> moves, Move move_to_check) {


        for ( Move move: moves ) {

            if( move_to_check.to_col == move.to_col && move_to_check.to_row == move.to_row )
                return true;
        }

        return false;
    }
}
