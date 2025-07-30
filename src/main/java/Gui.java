
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

import java.util.ArrayList;
import java.util.HashMap;


//public static Color dark  = Raylib.color(50, 125, 50);

public class Gui {

    public static String game_information = "white to move";

    public static int board_position_x;
    public static int board_position_y;
    public static int square_size;

    public static Color light = Raylib.color(241, 242, 249);
    public static Color dark  = Raylib.color(53, 53, 64);

    public static int font_size = 20;

    public static HashMap<Character, Texture> icons = new HashMap<Character, Texture>();

    public static void load_textures() {

        icons.put('K', Raylib.load_texture("icons/white-king.png",   square_size, square_size));
        icons.put('k', Raylib.load_texture("icons/black-king.png",   square_size, square_size));
        icons.put('Q', Raylib.load_texture("icons/white-queen.png",  square_size, square_size));
        icons.put('q', Raylib.load_texture("icons/black-queen.png",  square_size, square_size));
        icons.put('R', Raylib.load_texture("icons/white-rook.png",   square_size, square_size));
        icons.put('r', Raylib.load_texture("icons/black-rook.png",   square_size, square_size));
        icons.put('B', Raylib.load_texture("icons/white-bishop.png", square_size, square_size));
        icons.put('b', Raylib.load_texture("icons/black-bishop.png", square_size, square_size));
        icons.put('N', Raylib.load_texture("icons/white-knight.png", square_size, square_size));
        icons.put('n', Raylib.load_texture("icons/black-knight.png", square_size, square_size));
        icons.put('P', Raylib.load_texture("icons/white-pawn.png",   square_size, square_size));
        icons.put('p', Raylib.load_texture("icons/black-pawn.png",   square_size, square_size));
    }

    public static void draw_board() {

        for( int row = 0; row < 8; row++ ) {

            for( int col = 0; col < 8; col++ ) {

                final int s = square_size;

                int x, y;

                if( AntiChess.flipped ) {
                    x = Gui.board_position_x + ( 7 - col ) * s;
                    y = Gui.board_position_y + ( 7 - row ) * s;
                }

                else {
                    x = Gui.board_position_x + col * s;
                    y = Gui.board_position_y + row * s;
                }

                if( ActivePiece.col == col && ActivePiece.row == row ) {

                    DrawRectangle(x, y, s, s, Raylib.color(0, 255, 0, 150));

                    continue;
                }

                if( ( col + row ) % 2 == 0)
                    DrawRectangle(x, y, s, s, light);
                else
                    DrawRectangle(x, y, s, s, dark);
            }
        }
    }

    public static void draw_pieces(Game game) {

        for( int row = 0; row < 8; row++ ) {

            for( int col = 0; col < 8; col++ ) {

                final int s = square_size;

                int x, y;

                if( AntiChess.flipped ) {

                    x = Gui.board_position_x + ( 7 - col ) * s;
                    y = Gui.board_position_y + ( 7 - row ) * s;
                }

                else {

                    x = Gui.board_position_x + col * s;
                    y = Gui.board_position_y + row * s;
                }

                final char piece_at_square = game.board[row][col];

                if( piece_at_square == ' ' )
                    continue;

                if( row == ActivePiece.row && col == ActivePiece.col )
                    continue;

                Texture piece_texture = Gui.icons.get( piece_at_square ); // corresponding texture to piece at square
                DrawTexture( piece_texture, x, y, WHITE );
            }
        }
    }

    public static void display_legal_moves() {

        if( ActivePiece.type == ' ' )
            return;

        ArrayList<Move> moves = Moves.get_legal_moves( AntiChess.main_game, ActivePiece.col, ActivePiece.row );
        final int s = square_size;

        for( Move move: moves ) {

            int col = move.to_col;
            int row = move.to_row;

            final int non_flipped_col = col;
            final int non_flipped_row  = row;

            if( AntiChess.flipped ) {

                col = 7 - col;
                row = 7 - row;
            }

            final int x = Gui.board_position_x + s * col;
            final int y = Gui.board_position_y + s * row;

            if( AntiChess.main_game.board[non_flipped_row][non_flipped_col] == ' ')
                DrawCircle(x + s / 2, y + s / 2, (float) (0.15 * s / 2), ORANGE);

            else
                DrawRectangle(x, y, s, s, ORANGE);
        }
    }

    public static void display_en_passant_square(Game game) {

        if( game.en_passant_square.length != 2 )
            return;

        final int s = square_size;

        int col, row;

        col = game.en_passant_square[0];
        row = game.en_passant_square[1];

        if( AntiChess.flipped ) {

            col = 7 - col;
            row = 7 - row;
        }

        final int x = Gui.board_position_x + col * s;
        final int y = Gui.board_position_y + row * s;

        DrawRectangle(x, y, s, s, YELLOW);
    }

    public static void display_promotion_choices( Move promotion_move ) {

        final int to_row = promotion_move.to_row;
        final int to_col = promotion_move.to_col;

        if( to_row == 0 ) {

            char[] icons_to_draw = {'K', 'Q', 'R', 'B', 'N'};

            for( int k = 0; k < icons_to_draw.length; k++ ) {

                int row = k;
                int col = to_col;

                if( AntiChess.flipped ) {

                    col = 7 - col;
                    row = 7 - row;
                }

                final int x = board_position_x + col * square_size;
                final int y = board_position_y + row * square_size;

                DrawRectangle( x, y, square_size, square_size, DARKGRAY);

                DrawTexture( icons.get( icons_to_draw[k] ), x, y, WHITE) ;
            }

            return;
        }

        char[] icons_to_draw = {'k', 'q', 'r', 'b', 'n'};

        for( int k = 0; k < icons_to_draw.length; k++ ) {

            int row = k;
            int col = to_col;

            if( AntiChess.flipped ) {

                col = 7 - col;
                row = 7 - row;
            }

            final int x = board_position_x + col * square_size;
            final int y = board_position_y + (7 - row)   * square_size;

            DrawRectangle( x, y, square_size, square_size, DARKGRAY);

            DrawTexture( icons.get( icons_to_draw[k] ), x, y, WHITE) ;
        }
    }

    public static void choose_promotion() {

        Vector2 mouse = GetMousePosition();

        final int x = (int) mouse.x();
        final int y = (int) mouse.y();

        int col_clicked = (int) Math.floor( (double) (x - Gui.board_position_x) / Gui.square_size );
        int row_clicked = (int) Math.floor( (double) (y - Gui.board_position_y) / Gui.square_size );

        if( AntiChess.flipped ) {

            col_clicked = 7 - col_clicked;
            row_clicked = 7 - row_clicked;
        }

        int to_col = AntiChess.promotion_move.to_col;
        int to_row = AntiChess.promotion_move.to_row;

        if( col_clicked != to_col )
            return;

        if( to_row == 0 ) {

            if( row_clicked == 0 )
                AntiChess.promotion_move.promote_to = 'K';

            else if( row_clicked == 1 )
                AntiChess.promotion_move.promote_to = 'Q';

            else if( row_clicked == 2 )
                AntiChess.promotion_move.promote_to = 'R';

            else if( row_clicked == 3 )
                AntiChess.promotion_move.promote_to = 'B';

            else if( row_clicked == 4 )
                AntiChess.promotion_move.promote_to = 'N';

            if( AntiChess.promotion_move.promote_to != ' ' ) {

                AntiChess.main_game = Moves.make_move(AntiChess.main_game, AntiChess.promotion_move);

                AntiChess.promotion_move = null;

                AntiChess.program_state = "game";
            }

            return;
        }

        if( row_clicked == 7 )
            AntiChess.promotion_move.promote_to = 'k';

        else if( row_clicked == 6 )
            AntiChess.promotion_move.promote_to = 'q';

        else if( row_clicked == 5 )
            AntiChess.promotion_move.promote_to = 'r';

        else if( row_clicked == 4 )
            AntiChess.promotion_move.promote_to = 'b';

        else if( row_clicked == 3 )
            AntiChess.promotion_move.promote_to = 'n';

        if( AntiChess.promotion_move.promote_to != ' ' ) {

            AntiChess.main_game = Moves.make_move(AntiChess.main_game, AntiChess.promotion_move);

            AntiChess.promotion_move = null;

            AntiChess.program_state = "game";
        }

        if( GameOver.is_game_over( AntiChess.main_game ) )
            AntiChess.program_state = "game over";
    }

    public static void draw_game_information(Game game) {

        final int width  = GetScreenWidth();
        final int height = GetScreenHeight();

        if( AntiChess.program_state.equals( "promotion" ) )
            Gui.game_information = game.turn + " to promote";

        else if( !GameOver.is_game_over( game ) )
            Gui.game_information = game.turn + " to move";

        else
            Gui.game_information = GameOver.get_game_over_status( game );

        int half_text_width = MeasureText( Gui.game_information, Gui.font_size ) / 2;
        DrawText(Gui.game_information, width / 2 - half_text_width, height - 50, Gui.font_size, WHITE);
    }
}
