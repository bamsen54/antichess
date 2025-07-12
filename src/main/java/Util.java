import java.util.ArrayList;

public class Util {

    // example: 'K' -> "white" ' ' -> "empty" 'p' -> "black"
    public static String get_color_of_piece_type(char type) {

        if( type == ' ' )
            return "empty";

        if( type == Character.toUpperCase(type) )
            return "white";

        else
            return "black";
    }

    // if move[0] and move[1] are both in [0, 7] return true, false otherwise
    public static boolean check_if_move_is_on_board(int[] move) {

        final int col = move[0];
        final int row = move[1];

        return col >= 0 && col <= 7 && row >= 0 && row <= 7;
    }

    public static boolean check_if_array_list_contains_move(ArrayList<int[]> moves, int[]move_to_check) {

        for( int[] move : moves ) {

            if( move[0] == move_to_check[0] && move[1] == move_to_check[1] )
                return true;
        }

        return false;
    }
}
