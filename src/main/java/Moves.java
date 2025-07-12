import java.util.ArrayList;

// for making moves and generating boards for all moves
// and for getting legal moves for each piece
public class Moves {

    // checks if type of moved is pawn, then if it does double move, the square
    // between the start position and end position becomes en_passant_square
    // iff a pawn of opposite color can "capture" that square.
    public static int[] get_en_passant_square(Game game, int from_col, int from_row, int to_col, int to_row) {

        final char type_at_col_row = game.board[from_row][from_col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type( type_at_col_row );

        if( Character.toUpperCase(type_at_col_row) != 'P' )
            return new int[] {};

        if( Math.abs( to_row - from_row ) != 2 )
            return new int[] {};

        // from here it is pawn that makes double move

        // from_col so that column is the same. (int) ( from_row + from_col ) / 2 gives row in between
        // start and end square
        final int en_passant_row = ( from_row + to_row ) / 2;
        final int[] potential_en_passant_square = { from_col, en_passant_row };

        final char type_to_left  = to_col > 0 ? game.board[to_row][to_col - 1] : ' ';
        final char type_to_right = to_col < 7 ? game.board[to_row][to_col + 1] : ' ';

        if( color_of_piece_at_col_row.equals("white") ) {

            if( type_to_left == 'p' || type_to_right == 'p' )
                return potential_en_passant_square;
        }

        else {

            if( type_to_left == 'P' || type_to_right == 'P' )
                return potential_en_passant_square;
        }

        return new int[] {};
    }
    
    public static Game capture_pawn_when_en_passant(Game game, int from_col, int from_row, int to_col, int to_row) {

        if( game.en_passant_square.length != 2 )
            return game;

        Game game_with_captured_pawn = game.get_copy();

        final char piece_type_to_move   = game_with_captured_pawn.board[from_row][from_col];
        final String color_piece_to_move = Util.get_color_of_piece_type( piece_type_to_move );

        if( Character.toUpperCase( piece_type_to_move ) != 'P' )
            return game_with_captured_pawn;

        if( to_col != game.en_passant_square[0] || to_row != game.en_passant_square[1] )
            return game_with_captured_pawn;

        if( color_piece_to_move.equals( "white" ) )
            game_with_captured_pawn.board[from_row][to_col] = ' ';


        else
            game_with_captured_pawn.board[from_row][to_col] = ' ';

        return game_with_captured_pawn;
    }

    public static Game make_move(Game game, int from_col, int from_row, int to_col, int to_row) {

        Game game_with_move = game.get_copy();

        // "capture" pawn when en passant, needs to be done before ame_with_move.board[from_row][from_col] = ' ';
        // since it uses the fact that there is a pawn there
        game_with_move = capture_pawn_when_en_passant( game_with_move, from_col, from_row, to_col, to_row );

        char piece_type_to_move = game.board[from_row][from_col];

        game_with_move.board[from_row][from_col] = ' ';
        game_with_move.board[to_row  ][to_col  ] = piece_type_to_move;


        // before move happens check if en passant happened
        //game_with_move.en_passant_square = new int[]{};
        game_with_move.en_passant_square = get_en_passant_square(game, from_col, from_row, to_col, to_row);
        
        if( game_with_move.turn.equals("white") )
            game_with_move.turn = "black";

        else
            game_with_move.turn = "white";

        return game_with_move;
    }

    // get legal moves supposing that a king is at (col, row)
    public static ArrayList<int[]> get_king_moves(Game game, int col, int row) {

        final char type_at_col_row = game.board[row][col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type( type_at_col_row );

        ArrayList<int[]> moves = new ArrayList<>();

        int[][] steps = {{- 1, - 1}, {- 1, 0}, {- 1, 1}, {0, - 1}, {0, 1}, {1, - 1}, {1, 0}, {1, 1}};

        for( int[] step : steps) {

            final int col_move = col + step[0];
            final int row_move = row + step[1];

            final int[] move = {col_move, row_move};

            if( !Util.check_if_move_is_on_board(move) )
                continue;

            final char type_at_col_move_row_move = game.board[row_move][col_move];
            final String color_of_type_at_move_col_move_row = Util.get_color_of_piece_type(type_at_col_move_row_move);

            // pseudo legal iff square does not have piece of same color
            if( !color_of_piece_at_col_row.equals(color_of_type_at_move_col_move_row) )
                moves.add(move);
        }

        return moves;
    }

    public static ArrayList<int[]> get_moves_in_direction(Game game, int col, int row, int col_dir, int row_dir) {

        final char type_at_col_row = game.board[row][col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type( type_at_col_row );

        ArrayList<int[]> moves = new ArrayList<>();

        for( int step = 1; step < 7; step++ ) {

            final int col_move = col + col_dir * step;
            final int row_move = row + row_dir * step;

            final int[] move = {col_move, row_move};

            if( !Util.check_if_move_is_on_board(move) )
                continue;

            final char type_at_col_move_row_move = game.board[row_move][col_move];
            final String color_of_type_at_move_col_move_row = Util.get_color_of_piece_type(type_at_col_move_row_move);

            // piece of same color at (col_move, row_move)
            if( color_of_piece_at_col_row.equals( color_of_type_at_move_col_move_row ))
                break;

            // piece of opposite color at (col_move, row_move)
            else if( !color_of_type_at_move_col_move_row.equals("empty") ) {
                moves.add(move);
                break;
            }

            moves.add(move);
        }

        return moves;
    }

    public static ArrayList<int[]> get_moves_in_straight_directions(Game game, int col, int row) {

        ArrayList<int[]> moves = new ArrayList<>();

        moves.addAll(get_moves_in_direction(game, col, row,   0, - 1));
        moves.addAll(get_moves_in_direction(game, col, row,   1,   0));
        moves.addAll(get_moves_in_direction(game, col, row,   0,   1));
        moves.addAll(get_moves_in_direction(game, col, row, - 1,   0));

        return moves;
    }

    public static ArrayList<int[]> get_moves_in_diagonal_directions(Game game, int col, int row) {

        ArrayList<int[]> moves = new ArrayList<>();

        moves.addAll(get_moves_in_direction(game, col, row,   1, - 1));
        moves.addAll(get_moves_in_direction(game, col, row,   1,   1));
        moves.addAll(get_moves_in_direction(game, col, row,   - 1,   1));
        moves.addAll(get_moves_in_direction(game, col, row, - 1,    - 1));

        return moves;
    }

    public static ArrayList<int[]> get_queen_moves(Game game, int col, int row) {

        ArrayList<int[]> moves = new ArrayList<>();

        moves.addAll( get_moves_in_straight_directions(game, col, row) );
        moves.addAll( get_moves_in_diagonal_directions(game, col, row) );

        return moves;
    }

    public static ArrayList<int[]> get_rook_moves(Game game, int col, int row) {

        return get_moves_in_straight_directions(game, col, row);
    }

    public static ArrayList<int[]> get_bishop_moves(Game game, int col, int row) {

        return get_moves_in_diagonal_directions(game, col, row);
    }

    public static ArrayList<int[]> get_knight_moves(Game game, int col, int row) {

        ArrayList<int[]> moves = new ArrayList<>();

        final char type_at_col_row = game.board[row][col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type( type_at_col_row );

        final int[] steps = { - 2, - 1, 1, 2 };

        for( int row_step: steps ) {

            for( int col_step: steps ) {

                if( Math.abs(row_step) == Math.abs(col_step) )
                    continue;

                final int col_move = col + col_step;
                final int row_move = row + row_step;

                final int[] move = {col_move, row_move};

                if( !Util.check_if_move_is_on_board(move) )
                    continue;

                final char type_at_col_move_row_move = game.board[row_move][col_move];
                final String color_of_type_at_move_col_move_row = Util.get_color_of_piece_type(type_at_col_move_row_move);

                if( color_of_piece_at_col_row.equals( color_of_type_at_move_col_move_row ))
                    continue;

                moves.add(move);
            }
        }

        return moves;
    }

    public static ArrayList<int[]> get_pawn_moves(Game game, int col, int row) {

        ArrayList<int[]> moves = new ArrayList<>();

        final char type_at_col_row = game.board[row][col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type( type_at_col_row );

        int move_direction = ( color_of_piece_at_col_row.equals("white") ) ? - 1 : 1;

        // standard "forward" move
        if( color_of_piece_at_col_row.equals("white") ) {

            if (row > 0 && game.board[row + move_direction][col] == ' ' )
                moves.add(new int[]{col, row + move_direction});
        }

        else {

            if (row < 7 && game.board[row + move_direction][col] == ' ' )
                moves.add(new int[]{col, row + move_direction});
        }

        if( color_of_piece_at_col_row.equals("white") ) {

            // double move (if at start square and (row + move_direction) and ( row + 2 * move_direction) are empty)
            if( row == 6 && game.board[row + move_direction][col] == ' ' && game.board[row + 2 *  move_direction][col] == ' ')
                moves.add(new int[] {col, row + 2 * move_direction});

            // capture
            if( row + move_direction >= 0) {

                // sets a black piece at en passant square so that
                // the ifs below are true
                if( game.en_passant_square.length == 2 )
                    game.board[game.en_passant_square[1]][game.en_passant_square[0]] = 'p';

                if (col > 0 && Util.get_color_of_piece_type(game.board[row + move_direction][col - 1]).equals("black"))
                    moves.add(new int[]{col - 1, row + move_direction});

                if (col < 7 && Util.get_color_of_piece_type(game.board[row + move_direction][col + 1]).equals("black"))
                    moves.add(new int[]{col + 1, row + move_direction});

                // remove black piece at en passant square
                if( game.en_passant_square.length == 2 )
                    game.board[game.en_passant_square[1]][game.en_passant_square[0]] = ' ';

            }
        }

        else {

            if( row == 1 && game.board[row + move_direction][col] == ' ' && game.board[row + 2 *  move_direction][col] == ' ')
                moves.add(new int[] {col, row + 2 * move_direction});

            // capture

            if( row + move_direction <= 7) {

                // sets a white piece at en passant square so that
                // the ifs below are true
                if( game.en_passant_square.length == 2 )
                    game.board[game.en_passant_square[1]][game.en_passant_square[0]] = 'P';

                if (col > 0 && Util.get_color_of_piece_type(game.board[row + move_direction][col - 1]).equals("white"))
                    moves.add(new int[]{col - 1, row + move_direction});

                if (col < 7 && Util.get_color_of_piece_type(game.board[row + move_direction][col + 1]).equals("white"))
                    moves.add(new int[]{col + 1, row + move_direction});

                // remove white piece at en passant square
                if( game.en_passant_square.length == 2 )
                    game.board[game.en_passant_square[1]][game.en_passant_square[0]] = ' ';
            }
        }

        return moves;
    }

    public static ArrayList<int[]> get_pseudo_legal_moves(Game game, int col, int row) {

        final char type_at_col_row = game.board[row][col];

        switch( Character.toUpperCase( type_at_col_row ) ) {

            case 'K': return get_king_moves(   game, col, row);
            case 'Q': return get_queen_moves(  game, col, row);
            case 'R': return get_rook_moves(   game, col, row);
            case 'B': return get_bishop_moves( game, col, row);
            case 'N': return get_knight_moves( game, col, row);
            case 'P': return get_pawn_moves(   game, col, row);

            default: return new ArrayList<int[]>();
        }
    }

    public static boolean check_if_move_is_capture(Game game, int from_col, int from_row, int to_col, int to_row) {

        final char type = game.board[from_row][from_col];

        if( type == ' ' )
            return false;

        if( Character.toUpperCase( type ) == 'P' )
            return from_col != to_col;

        //System.out.println("!!" + (game.board[to_row][to_col] != ' ') );
        return game.board[to_row][to_col] != ' ';
    }

    // same as get_pseudo_legal_moves but if capture is possible it is mandatory
    public static ArrayList<int[]> get_legal_moves(Game game, int col, int row) {

        ArrayList<int[]> pseudo_legal_moves = get_pseudo_legal_moves( game, col, row );

        boolean capture_is_possible = false;


        final String color = Util.get_color_of_piece_type( game.board[row][col] );

        for( int row_ = 0; row_ < 8; row_++ ) {

            for( int col_ = 0; col_ < 8; col_++ ) {

                final String color_of_piece_at_col_row = Util.get_color_of_piece_type( game.board[row_][col_] );

                if( !color_of_piece_at_col_row.equals(color) )
                    continue;

                ArrayList<int[]> pseudo_legal_moves_of_piece = get_pseudo_legal_moves( game, col_, row_ );

                for( int[] move: pseudo_legal_moves_of_piece) {

                    if( check_if_move_is_capture(game, col_, row_, move[0], move[1]) )
                        capture_is_possible = true;
                }
            }
        }

        //System.out.println(capture_is_possible);
        if( !capture_is_possible )
            return pseudo_legal_moves;


        ArrayList<int[]> legal_moves = new ArrayList<>();

        for( int[] move: pseudo_legal_moves ) {

            if( check_if_move_is_capture( game, col, row, move[0], move[1] ) )
                legal_moves.add( move );
        }

        return legal_moves;
    }
}
