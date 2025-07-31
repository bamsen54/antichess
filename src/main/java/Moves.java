import java.util.ArrayList;

// for making moves and generating boards for all moves
// and for getting legal moves for each piece
public class Moves {

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

    public static Game update_clock(Game game, int from_col, int from_row, int to_col, int to_row) {

        Game updated = game.get_copy();

        final char type = game.board[from_row][from_col];

        // pawn move including en passant
        if( type == 'P' || type == 'p' )
            updated.half_move_clock = 0;

            // capture
        else if( game.board[to_row][to_col] != ' ')
            updated.half_move_clock = 0;

            // neither
        else
            updated.half_move_clock = updated.half_move_clock + 1;

        return updated;
    }

    public static Game update_history(Game game, int from_col, int from_row, int to_col, int to_row) {

        Game updated = game.get_copy();

        updated.history.add( Notation.fen_without_clocks( updated ) );

        return updated;
    }

    public static Game make_move(Game game, Move move) {

        Game game_with_move = game.get_copy();

        final int from_col = move.from_col;
        final int from_row = move.from_row;
        final int to_col   = move.to_col;
        final int to_row   = move.to_row;

        game_with_move = capture_pawn_when_en_passant( game_with_move, from_col, from_row, to_col, to_row );

        game_with_move.en_passant_square = new int[] {};
        game_with_move.en_passant_square = get_en_passant_square(game_with_move, from_col, from_row, to_col, to_row);

        game_with_move = update_clock( game_with_move, from_col, from_row, to_col, to_row );

        char type = game_with_move.board[from_row][from_col];

        if( move.promote_to != ' ')
            type = move.promote_to;

        game_with_move.board[from_row][from_col] = ' ';
        game_with_move.board[to_row][to_col]     = type;

        if( game_with_move.turn.equals( "black" ) )
            game_with_move.full_move_number++;


        if( game_with_move.turn.equals("white") )
            game_with_move.turn = "black";

        else
            game_with_move.turn = "white";

        game_with_move = update_history( game_with_move, from_col, from_row, to_col, to_row );


        return game_with_move;
    }

    public static ArrayList<Move> get_king_moves(Game game, int col, int row) {

        final char type_at_col_row = game.board[row][col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type( type_at_col_row );

        ArrayList<Move> moves = new ArrayList<>();

        int[][] steps = {{- 1, - 1}, {- 1, 0}, {- 1, 1}, {0, - 1}, {0, 1}, {1, - 1}, {1, 0}, {1, 1}};

        for( int[] step : steps) {

            final int col_move = col + step[0];
            final int row_move = row + step[1];

            Move move = new Move(col, row, col_move, row_move);

            if( !Util.check_if_square_is_on_board( new int[]{ col_move, row_move} ) )
                continue;

            final char type_at_col_move_row_move = game.board[row_move][col_move];
            final String color_of_type_at_move_col_move_row = Util.get_color_of_piece_type(type_at_col_move_row_move);


            if( type_at_col_move_row_move == ' ')
                move.capture = false;

            else if( !color_of_piece_at_col_row.equals( color_of_type_at_move_col_move_row ) )
                move.capture = true;

            else
                continue;

            moves.add(move);
        }

        return moves;
    }

    public static ArrayList<Move> get_moves_in_direction(Game game, int col, int row, int col_dir, int row_dir) {

        final char type_at_col_row = game.board[row][col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type( type_at_col_row );

        ArrayList<Move> moves = new ArrayList<>();

        for( int step = 1; step < 8; step++ ) {

            final int col_move = col + col_dir * step;
            final int row_move = row + row_dir * step;

            Move move = new Move(col, row, col_move, row_move);

            if( !Util.check_if_square_is_on_board( new int[] {col_move, row_move} ))
                continue;

            final char type_at_col_move_row_move = game.board[row_move][col_move];
            final String color_of_type_at_move_col_move_row = Util.get_color_of_piece_type(type_at_col_move_row_move);

            // piece of same color at (col_move, row_move)
            if( color_of_piece_at_col_row.equals( color_of_type_at_move_col_move_row ))
                break;

            // piece of opposite color at (col_move, row_move) capture
            else if( !color_of_type_at_move_col_move_row.equals("empty") ) {

                move.capture = true;
                moves.add(move);

                break;
            }

            moves.add(move);
        }

        return moves;
    }

    public static ArrayList<Move> get_moves_in_straight_directions(Game game, int col, int row) {

        ArrayList<Move> moves = new ArrayList<>();

        moves.addAll( get_moves_in_direction( game,  col, row,   0, - 1) );
        moves.addAll( get_moves_in_direction( game,  col, row,   1,   0) );
        moves.addAll( get_moves_in_direction( game,  col, row,   0,   1) );
        moves.addAll( get_moves_in_direction( game,  col, row, - 1,   0) );

        return moves;
    }

    public static ArrayList<Move> get_moves_in_diagonal_directions(Game game, int col, int row) {

        ArrayList<Move> moves = new ArrayList<>();

        moves.addAll( get_moves_in_direction( game,  col, row,   1, - 1) );
        moves.addAll( get_moves_in_direction( game,  col, row,   1,   1) );
        moves.addAll( get_moves_in_direction( game,  col, row, - 1,   1) );
        moves.addAll( get_moves_in_direction( game,  col, row, - 1, - 1) );

        return moves;
    }

    public static ArrayList<Move> get_queen_moves(Game game, int col, int row) {

        ArrayList<Move> moves = new ArrayList<>();

        moves.addAll( get_moves_in_straight_directions( game, col, row ) );
        moves.addAll( get_moves_in_diagonal_directions( game, col, row ) );

        return moves;
    }

    public static ArrayList<Move> get_rook_moves(Game game, int row, int col) {

        return get_moves_in_straight_directions( game, row, col );
    }

    public static ArrayList<Move> get_bishop_moves(Game game, int row, int col) {

        return get_moves_in_diagonal_directions( game, row, col );
    }

    public static ArrayList<Move> get_knight_moves(Game game, int col, int row) {

        ArrayList<Move> moves = new ArrayList<>();

        final char type_at_col_row = game.board[row][col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type(type_at_col_row);

        final int[] steps = {-2, -1, 1, 2};

        for (int row_step : steps) {

            for (int col_step : steps) {

                if (Math.abs(row_step) == Math.abs(col_step))
                    continue;

                final int col_move = col + col_step;
                final int row_move = row + row_step;

                Move move = new Move(col, row, col_move, row_move);

                if (!Util.check_if_square_is_on_board(new int[]{col_move, row_move}))
                    continue;

                final char type_at_col_move_row_move = game.board[row_move][col_move];
                final String color_of_type_at_move_col_move_row = Util.get_color_of_piece_type(type_at_col_move_row_move);

                boolean not_empty      = game.board[row_move][col_move] != ' ';
                boolean opposite_color = !color_of_piece_at_col_row.equals(color_of_type_at_move_col_move_row );

                if( !not_empty )
                    moves.add( move );

                else if( opposite_color ) {

                    move.capture = true;
                    moves.add( move);
                }
            }
        }

        return moves;
    }

    // takes a move returns 5 different versions in an ArrayList with different
    // promote_to the different 5 pieces
    //
    public static ArrayList<Move> get_move_list_with_all_promotions(Game game, Move move) {

        Move promote_to_king   = move.get_copy();
        Move promote_to_queen  = move.get_copy();
        Move promote_to_rook   = move.get_copy();
        Move promote_to_bishop = move.get_copy();
        Move promote_to_knight = move.get_copy();

        if( game.turn.equals( "white" ) ) {

            promote_to_king.promote_to = 'K';
            promote_to_queen.promote_to = 'Q';
            promote_to_rook.promote_to = 'R';
            promote_to_bishop.promote_to = 'B';
            promote_to_knight.promote_to = 'N';
        }

        else {

            promote_to_king.promote_to = 'k';
            promote_to_queen.promote_to = 'q';
            promote_to_rook.promote_to = 'r';
            promote_to_bishop.promote_to = 'b';
            promote_to_knight.promote_to = 'n';
        }


        ArrayList<Move> all_promotions = new ArrayList<>();

        all_promotions.add( promote_to_king );
        all_promotions.add( promote_to_queen );
        all_promotions.add( promote_to_rook );
        all_promotions.add( promote_to_bishop );
        all_promotions.add( promote_to_knight );

        return all_promotions;
    }

    public static ArrayList<Move> get_pawn_moves(Game game, int col, int row) {

        ArrayList<Move> moves = new ArrayList<>();

        int[] move_square;


        final char type_at_col_row = game.board[row][col];
        final String color_of_piece_at_col_row = Util.get_color_of_piece_type( type_at_col_row );

        boolean pawn_start_rank = false;

        if( color_of_piece_at_col_row.equals( "white" ) && row == 6 )
            pawn_start_rank = true;

        else if( color_of_piece_at_col_row.equals( "black" ) && row == 1  )
            pawn_start_rank = true;


        int forward_direction;

        if( color_of_piece_at_col_row.equals( "white" ) )
            forward_direction = - 1;

        else
            forward_direction = 1;

        // standard forward move
        move_square = new int[]{col, row + forward_direction};
        Move standard_move = new Move( col, row, col, row + forward_direction);
        if( Util.check_if_square_is_on_board( move_square ) && Util.check_square_is_empty(game,  move_square ) ) {

            if( Util.check_if_piece_is_on_back_rank( move_square ))
                moves.addAll( get_move_list_with_all_promotions( game, standard_move ) );

            else
                moves.add( standard_move );
        }

        // double move
        move_square      = new int[] {col, row + 2 * forward_direction};
        Move double_move = new Move( col, row, col, row + 2 * forward_direction );

        // if moves is not empty that means that standard move is legal and therefore
        // first square in double move is empty
        if( !moves.isEmpty() && pawn_start_rank )
            moves.add( double_move );

        // capture left
        move_square    = new int[] {col - 1, row + forward_direction};
        Move left_move = new Move( col, row, col - 1, row + forward_direction );

        if( Util.check_if_square_is_on_board( move_square ) ) {

            final char left_type = game.board[row + forward_direction][col - 1];

            boolean not_empty      = !Util.check_square_is_empty( game,  move_square );
            boolean opposite_color = Util.is_different_color( left_type, type_at_col_row);

            if( not_empty && opposite_color ) {

                left_move.capture = true;

                if( row + forward_direction == 0 || row + forward_direction == 7 )
                    moves.addAll( get_move_list_with_all_promotions( game,  left_move ));

                else
                    moves.add( left_move );
            }
        }

        // capture right
        move_square     = new int[] {col + 1, row + forward_direction};
        Move right_move = new Move( col, row, col + 1, row + forward_direction );

        if( Util.check_if_square_is_on_board( move_square ) ) {

            final char right_type = game.board[row + forward_direction][col + 1];

            boolean not_empty      = !Util.check_square_is_empty( game,  move_square );
            boolean opposite_color = Util.is_different_color( right_type, type_at_col_row);

            if( not_empty && opposite_color ) {

                right_move.capture = true;

                if( row + forward_direction == 0 || row + forward_direction == 7 )
                    moves.addAll( get_move_list_with_all_promotions( game,  right_move ));

                else
                    moves.add( right_move );
            }
        }

        // en passant

        if( game.en_passant_square.length != 2)
            return moves;

        Move en_passant_left  = new Move( col, row, col - 1, row + forward_direction);
        Move en_passant_right = new Move( col, row, col + 1, row + forward_direction);

        en_passant_left.en_passant  = true;
        en_passant_left.capture     = true;

        en_passant_right.en_passant = true;
        en_passant_right.capture    = true;

        int[] en_passant = game.en_passant_square;

        int[] capture_left  = {col -1 , row + forward_direction};
        int[] capture_right = {col + 1, row + forward_direction};

        if( Util.check_if_square_are_equal( capture_left, en_passant ) )
            moves.add( en_passant_left );

        if( Util.check_if_square_are_equal( capture_right, en_passant ) )
            moves.add( en_passant_right );

        return moves;
    }

    public static ArrayList<Move> get_pseudo_legal_moves(Game game, int col, int row) {

        final char type = Character.toUpperCase( game.board[row][col] );

        ArrayList<Move> default_return = new ArrayList<>();

        return switch (type) {
            case 'K' -> get_king_moves(game, col, row);
            case 'Q' -> get_queen_moves(game, col, row);
            case 'R' -> get_rook_moves(game, col, row);
            case 'B' -> get_bishop_moves(game, col, row);
            case 'N' -> get_knight_moves(game, col, row);
            case 'P' -> get_pawn_moves(game, col, row);
            default  -> default_return;
        };
    }

    public static ArrayList<Move> get_legal_moves(Game game, int col, int row) {

        if( !AntiChess.capture_is_mandatory )
            return get_pseudo_legal_moves( game, col, row );

        final char type = game.board[row][col];

        boolean capture_is_possible = false;

        for( int r = 0; r < 8; r++ ) {

            for( int c = 0; c < 8; c++ ) {

                final char type_at_c_r = game.board[r][c];

                if( Util.is_different_color( type, type_at_c_r ) )
                    continue;

                ArrayList<Move> pseudo_legal = get_pseudo_legal_moves( game, c, r );

                for( Move move: pseudo_legal ) {

                    if( move.capture ) {

                        capture_is_possible = true;

                        break;
                    }

                }
            }
        }

        ArrayList<Move> pseudo_legal = get_pseudo_legal_moves(game, col, row);

        if( !capture_is_possible )
            return pseudo_legal;

        ArrayList<Move> legal_moves = new ArrayList<>();

        for( Move move: pseudo_legal ) {

            if( move.capture )
                legal_moves.add( move );
        }

        return legal_moves;
    }

    public static ArrayList<Move> get_all_legal_moves_for_color(Game game, String color) {

        ArrayList<Move> moves = new ArrayList<>();

        for( int row = 0; row < 8; row++ ) {

            for( int col = 0; col < 8; col++ ) {

                final char type = game.board[row][col];

                final String color_of_type = Util.get_color_of_piece_type( type );

                if( !color_of_type.equals( color) )
                    continue;

                moves.addAll( get_legal_moves( game, col, row) );
            }
        }

        return moves;
    }

    public static ArrayList<Game> get_all_possible_boards_from_position(Game game, String color) {

        game = game.get_copy();

        ArrayList<Game> games = new ArrayList<>();

        ArrayList<Move> moves = get_all_legal_moves_for_color( game, color );

        for( Move move: moves)
            games.add( make_move( game, move ) );

        return games;
    }
}
