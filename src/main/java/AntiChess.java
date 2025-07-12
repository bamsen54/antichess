

import java.util.ArrayList;

import static com.raylib.Colors.WHITE;
import static com.raylib.Raylib.*;

public class AntiChess {

    static Game main_game = new Game();

    final static int width           = GetScreenWidth();
    final static int height          = GetScreenHeight();
    final static int vertical_margin = 20;

    static boolean flipped = false; // if board is flipped

    public static void run() {

        Gui.square_size      = ( Math.min( width, height ) - 2 * vertical_margin ) /  8;

        // position of top left square
        Gui.board_position_x = width / 2 - 4 * Gui.square_size;
        Gui.board_position_y = vertical_margin;

        Gui.load_textures();

        while( !WindowShouldClose() ) {
            ClearBackground(Raylib.color(32));
            BeginDrawing();

            Loop();
            EndDrawing();
        }
    }

    public static void Loop() {

        DrawFPS(20, 20);
        Gui.draw_board();
        Gui.display_en_passant_square(main_game);
        Gui.display_legal_moves();
        Gui.draw_pieces(main_game);

        if( IsMouseButtonPressed(0) && ActivePiece.type == ' ' )
            start_piece_drag();

        if( IsMouseButtonReleased(0) && ActivePiece.type != ' ' )
            move_active_piece_to_drop_square();

        if( IsKeyPressed(KEY_F) ) {

            flipped = !flipped;
        }

        if( ActivePiece.type != ' ' ) {

            int[] mouse_position = Raylib.mouse_position();

            int x = mouse_position[0];
            int y = mouse_position[1];

            DrawTexture(Gui.icons.get(ActivePiece.type), x - Gui.square_size / 2, y - Gui.square_size / 2, WHITE);

            //Moves.get_pseudo_legal_moves(main_game, ActivePiece.col, ActivePiece.row);
        }
    }

    public static void start_piece_drag() {

        Vector2 mouse = GetMousePosition();

        final int x = (int) mouse.x();
        final int y = (int) mouse.y();

        int col = (int) Math.floor( (double) (x - Gui.board_position_x) / Gui.square_size );
        int row = (int) Math.floor( (double) (y - Gui.board_position_y) / Gui.square_size );

        if( flipped ) {

            col = 7 - col;
            row = 7 - row;
        }

        ActivePiece.clear();

        if( col < 0 || col > 7 || row < 0 || row > 7 )
            return;

        if( main_game.board[row][col] == ' ' )
            return;

        if( !Util.get_color_of_piece_type(main_game.board[row][col]).equals(main_game.turn) )
            return;

        ActivePiece.type  = main_game.board[row][col];
        ActivePiece.col   = col;
        ActivePiece.row   = row;
        ActivePiece.color = main_game.turn;
    }

    // moves active piece to drop square if it is a legal move
    public static void move_active_piece_to_drop_square() {

        // we always want to clear ActivePiece
        // when mouse is released so we store the info here
        final char type_active    = ActivePiece.type;
        final int col_active      = ActivePiece.col;
        final int row_active      = ActivePiece.row;
        final String color_active = ActivePiece.color;

        ActivePiece.clear();

        Vector2 mouse = GetMousePosition();

        final int x = (int) mouse.x();
        final int y = (int) mouse.y();

        int col = (int) Math.floor( (double) (x - Gui.board_position_x) / Gui.square_size );
        int row = (int) Math.floor( (double) (y - Gui.board_position_y) / Gui.square_size );

        if( flipped ) {

            col = 7 - col;
            row = 7 - row;
        }

        if( col < 0 || col > 7 || row < 0 || row > 7 )
            return;

        ArrayList<int[]> legal_moves = Moves.get_legal_moves(main_game, col_active, row_active);
        final int[] move = {col, row};

        if( !Util.check_if_array_list_contains_move( legal_moves, move) )
           return;

        main_game = Moves.make_move(main_game, col_active, row_active, col, row);
    }
}
