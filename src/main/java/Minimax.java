import java.security.DrbgParameters;
import java.util.ArrayList;

public class Minimax {

    public static float minimax_timer    = 0;
    public static float minimax_time_limit = 7000; // ms
    public static long time_at_start = - 1;

    public static int num_iter = 0;

    public static float get_piece_value(char type) {

        return switch( Character.toUpperCase( type ) ) {
            case 'K' -> 5;
            case 'Q' -> 9;
            case 'R' -> 5;
            case 'B' -> 3;
            case 'N' -> 3;
            case 'P' -> 1;
            default  -> 0;
        };
    }

    public static float get_static_score(Game game) {

        if( GameOver.is_game_over( game ) ) {

            String game_over_result = GameOver.get_game_over_status( game );

            if( game_over_result.equals( "white won" ) )
                return 1000000;

            if( game_over_result.equals( "black won") )
                return - 1000000;

            // draw
            if( !game_over_result.isEmpty() )
                return 0;
        }

        float white_score = 0;
        float black_score = 0;

        for( int row = 0; row < 8; row++ ) {

            for( int col = 0; col < 8; col++ ) {

                final char type =  game.board[row][col];

                if( type == ' ' )
                    continue;

                if( Util.get_color_of_piece_type( type ).equals( "white" ) )
                    white_score += get_piece_value( type );

                else
                    black_score += get_piece_value( type );
            }
        }

        return black_score - white_score;
    }

    public static float minimax(Game game, int depth, boolean is_maximizing) {

        Game node = game.get_copy();

        if( GameOver.is_game_over( node ) || depth == 0 )
            return get_static_score( node );

        if( System.currentTimeMillis() - time_at_start > minimax_time_limit )
            return get_static_score( node );

        if( is_maximizing ) {

            float value = -1000000;

            ArrayList<Game> children = Moves.get_all_possible_boards_from_position(node, "white");

            for (Game child : children) {


                child = child.get_copy();
                value = Math.max(value, minimax(child, depth - 1 , false));

            }

            return value;
        }

        float value = 1000000;

        ArrayList<Game> children = Moves.get_all_possible_boards_from_position( node, "black" );

        for( Game child: children ) {

            child = child.get_copy();
            value = Math.min(value, minimax(child, depth - 1, true));
        }

        return value;
    }

    public static Move get_best_move(Game game, boolean is_maximizing) {

        game = game.get_copy();

        time_at_start = System.currentTimeMillis();

        if( is_maximizing ) {

            ArrayList<Move> legal_moves = Moves.get_all_legal_moves_for_color( game, "white" );

            if( legal_moves.isEmpty() )
                return new Move( - 1, - 1, - 1, - 1 );

            Move best_move   = legal_moves.get(0);
            float best_value = - 1000000;

            for( int depth = 1; depth < 1000000; depth++ ) {

                if( System.currentTimeMillis() - time_at_start > minimax_time_limit )
                    return best_move;

                for (Move move : legal_moves) {

                    if( System.currentTimeMillis() - time_at_start > minimax_time_limit )
                        return best_move;

                    Game child = Moves.make_move(game, move);

                    float new_value = minimax(child, depth, false);

                    if (new_value > best_value) {

                        best_value = new_value;

                        best_move = move.get_copy();
                    }
                }
            }

            return best_move;
        }

        ArrayList<Move> legal_moves = Moves.get_all_legal_moves_for_color( game, "black" );

        if( legal_moves.isEmpty() )
            return new Move( - 1, - 1, - 1, - 1 );

        Move best_move   = legal_moves.get(0);
        float best_value = 1000000;

        for( int depth = 1; depth < 1000000; depth++ ) {

            System.out.println("depth: " + depth);

            for (Move move : legal_moves) {

                if( System.currentTimeMillis() - time_at_start > minimax_time_limit )
                    return best_move;

                Game child = Moves.make_move(game, move);

                float new_value = minimax(child, depth, true);

                if (new_value < best_value) {

                    best_value = new_value;

                    best_move = move.get_copy();
                }
            }
        }

        return best_move;
    }
}
