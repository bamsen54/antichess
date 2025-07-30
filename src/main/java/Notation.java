import java.util.Arrays;

public class Notation {

    public static String fen(Game game) {

        StringBuilder fen_string = new StringBuilder();

        int number_of_consecutive_empty_squares = 0;


        for( int row = 0; row < 8; row++ ) {


            for( int col = 0; col < 8; col++ ) {

                if( game.board[row][col] == ' ' )
                    number_of_consecutive_empty_squares++;

                else {

                    if( number_of_consecutive_empty_squares > 0 ) {

                        fen_string.append( number_of_consecutive_empty_squares );

                        number_of_consecutive_empty_squares = 0;
                    }

                    fen_string.append(game.board[row][col]);
                }
            }

            if( number_of_consecutive_empty_squares > 0 )
                fen_string.append( number_of_consecutive_empty_squares );

            if( row < 7)
                fen_string.append("/");


            number_of_consecutive_empty_squares = 0;
        }

        // turn
        fen_string.append(" ").append(game.turn.charAt(0));

        // for castling but no castling in anti-chess
        fen_string.append(" -");

        if( game.en_passant_square.length == 2 )
            fen_string.append(" ").append(Util.square_to_algebraic(game.en_passant_square));

        else
            fen_string.append(" ").append("-");


        fen_string.append(" ").append( game.half_move_clock );
        fen_string.append(" ").append( game.full_move_number );


        return fen_string.toString();
    }

    // e.g returns rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - -
    // instead of  rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 0 1
    public static String fen_without_clocks(Game game) {

        String full_fen = fen( game );

        String[] fen_parts = full_fen.split(" ");

        StringBuilder fen_without_clock = new StringBuilder();

        for( int k = 0; k < fen_parts.length - 2; k++ )
            fen_without_clock.append( fen_parts[k] ).append(" ");

        // add one space at end so we remote it
        fen_without_clock.deleteCharAt(fen_without_clock.length() - 1 );

        return fen_without_clock.toString();
    }

    public static Game set_game_with_fen(String fen) {

        Game game = new Game();

        String[] split = fen.split(" ");

        String position         = split[0];
        String turn             = split[1];
        String castling         = split[2];
        String en_passant       = split[3];
        String half_move_clock  = split[4];
        String full_move_number = split[5];

        if( turn.equals( "w" ) )
            game.turn = "white";

        else
            game.turn = "black";

        if( en_passant.equals("-") )
            game.en_passant_square = new int[] {};

        else
            game.en_passant_square = Util.algebraic_to_square( en_passant );

        game.half_move_clock  = Integer.parseInt( half_move_clock );
        game.full_move_number = Integer.parseInt( full_move_number );

        String[] positions = position.split("/");

        int row = 0;
        int col = 0;

        for( String rank: positions ) {

            for( char element: rank.toCharArray() ) {

                if( !Character.isDigit( element ) ) {

                    game.board[row][col] = element;

                    col++;
                }

                else {

                    int number_of_empty = Character.digit( element, 10 );

                    for( int k = 0; k < number_of_empty; k++ ) {

                        game.board[row][col] = ' ';
                        col++;
                    }
                }
            }

            row++;
            col = 0;
        }

        return game;
    }
}
