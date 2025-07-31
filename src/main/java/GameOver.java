import java.util.ArrayList;
import java.util.HashMap;

// for checking when game is over
public class GameOver {

    // player who can't move is the winner, weather it is because of
    // stalemate or because of no pieces
    // if stalemated, then sufficient material is needed
    public static String check_win_by_no_move(Game game) {

        game = game.get_copy();

        final String turn = game.turn;

        final ArrayList<Move> white_moves = Moves.get_all_legal_moves_for_color( game, "white" );
        final ArrayList<Move> black_moves = Moves.get_all_legal_moves_for_color( game, "black" );

        if( turn.equals( "white" ) && white_moves.isEmpty())
            return "white won";

        if( turn.equals( "black" ) && black_moves.isEmpty() )
            return "black won";

        return ""; // not a win
    }

    public static String analyze_piece_count(int[] piece_count_1, int[] piece_count_2) {

        if( piece_count_1[6] != 0 || piece_count_2[6] != 0 )
            return "";

        int[] only_king             = {1, 0, 0, 0, 0, 0, 0};
        int[] king_and_light_bishop = {1, 0, 0, 1, 0, 0, 0};
        int[] king_and_dark_bishop  = {1, 0, 0, 0, 1, 0, 0};
        int[] king_and_knight       = {1, 0, 0, 0, 0, 1, 0};
        int[] king_and_two_knights  = {1, 0, 0, 0, 0, 2, 0};


        // K + B v K + B (both bishop light)
        if( Util.check_int_arr_equality( piece_count_1, king_and_light_bishop ) ) {

            if( Util.check_int_arr_equality( piece_count_2, king_and_light_bishop ) )
                return "draw insufficient material";

        }

        // K + B v K + B (both bishop dark)
        if( Util.check_int_arr_equality( piece_count_1, king_and_dark_bishop ) ) {

            if( Util.check_int_arr_equality( piece_count_2, king_and_dark_bishop ) )
                return "draw insufficient material";
        }


        // one side has king only
        if( !Util.check_int_arr_equality( piece_count_2, only_king) )
            return "";

        //K v K
        if( Util.check_int_arr_equality( piece_count_1, only_king ) )
            return "draw insufficient material";

        // K + N v K
        else if( Util.check_int_arr_equality( piece_count_1, king_and_knight ) )
            return "draw insufficient material";

        // K + N + N v K
        else if( Util.check_int_arr_equality( piece_count_1, king_and_two_knights ) )
            return "draw insufficient material";

        // K + B v K (light)
        else if( Util.check_int_arr_equality( piece_count_1, king_and_light_bishop ) )
            return "draw insufficient material";

        // K + B v K (dark)
        else if( Util.check_int_arr_equality( piece_count_1, king_and_dark_bishop ) )
            return "draw insufficient material";

        return "";
    }

    public static String check_insuficcient_material(Game game) {

        // for example piece count for white at start is {1, 1, 2, 1, 1, 2, 8}
        // in the order king queen rook light square bishop dark square bishop knights and pawns
        int[] piece_count_white = new int[7];
        int[] piece_count_black = new int[7];

        for( int row = 0; row < 8; row++ ) {

            for( int col = 0; col < 8; col++ ) {

                final char type = game.board[row][col];

                if( type == ' ' )
                    continue;

                if( type == 'K' )
                    piece_count_white[0] = piece_count_white[0] + 1;

                else if( type == 'Q')
                    piece_count_white[1] = piece_count_white[1] + 1;

                else if( type == 'R' )
                    piece_count_white[2] = piece_count_white[2] + 1;

                else if( type == 'B' && ( row + col) % 2 == 0 )
                    piece_count_white[3] = piece_count_white[3] + 1;

                else if( type == 'B' && ( row + col) % 2 == 1 )
                    piece_count_white[4] = piece_count_white[4] + 1;

                else if( type == 'N')
                    piece_count_white[5] = piece_count_white[5] + 1;

                else if( type == 'P' )
                    piece_count_white[6] = piece_count_white[6] + 1;

                else if( type == 'k' )
                    piece_count_black[0] = piece_count_black[0] + 1;

                else if( type == 'q')
                    piece_count_black[1] = piece_count_black[1] + 1;

                else if( type == 'r' )
                    piece_count_black[2] = piece_count_black[2] + 1;

                else if( type == 'b' && ( row + col) % 2 == 0 )
                    piece_count_black[3] = piece_count_black[3] + 1;

                else if( type == 'b' && ( row + col) % 2 == 1 )
                    piece_count_black[4] = piece_count_black[4] + 1;

                else if( type == 'n')
                    piece_count_black[5] = piece_count_black[5] + 1;

                else if( type == 'p' )
                    piece_count_black[6] = piece_count_black[6] + 1;

            }
        }

        // we swap so since it does not matter which side has what pieces
        // for example in K + N vs K it does not matter weather white has K + N
        // and black has K or vice versa it is still a draw
        if( !analyze_piece_count( piece_count_white, piece_count_black ).isEmpty() )
            return "draw insufficient material";

        if( !analyze_piece_count( piece_count_black, piece_count_white ).isEmpty() )
            return "draw insufficient material";

        return "";
    }

    public static String check_threefold_repetition(Game game) {

        HashMap<String, Integer> frequency = new HashMap<String, Integer>();

        for( String fen_without_clock: game.history ) {

            if( frequency.containsKey( fen_without_clock ) ) {

                frequency.put( fen_without_clock, frequency.get( fen_without_clock) + 1 );

                if( frequency.get(fen_without_clock ) == 3 )
                    return "draw by three-fold repetition";
            }

            else
                frequency.put(fen_without_clock, 1);

        }

        return "";
    }

    public static String check_50_move_rule(Game game) {

        if( game.half_move_clock >= 50 )
            return "draw by 50 move rule";

        return "";

    }

    // returns
    // ' ' if game is not over
    // '-' if game ended in draw
    // 'w' if white won
    // 'b' if black won
    public static String get_game_over_status(Game game) {

        if( check_win_by_no_move( game ).equals( "white won" ) )
            return "white won";

        if( check_win_by_no_move( game ).equals( "black won" ))
            return "black won";

        if( check_insuficcient_material( game ).equals( "draw insufficient material" ) )
            return "draw insufficient material";

        if( check_threefold_repetition( game ).equals( "draw by three-fold repetition" ) )
            return "draw by three-fold repetition";

        if( check_50_move_rule( game ).equals( "draw by 50 move rule" ) )
            return "draw by 50 move rule";

        return "";
    }

    public static boolean is_game_over(Game game) {

        return !get_game_over_status( game ).isEmpty();
    }
}
