
import static com.raylib.Colors.*;
import static com.raylib.Raylib.*;

import java.util.ArrayList;
import java.util.HashMap;


//public static Color dark  = Raylib.color(50, 125, 50);

public class Gui {

    public static int board_position_x;
    public static int board_position_y;
    public static int square_size;

    public static Color light = Raylib.color(241, 242, 249);
    public static Color dark  = Raylib.color(53, 53, 64);

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
}
