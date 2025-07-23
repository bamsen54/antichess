

import java.util.ArrayList;

import static com.raylib.Colors.WHITE;
import static com.raylib.Raylib.*;

public class AntiChess {

    // for debug
    static boolean capture_is_mandatory = true;

    // "game" for normal play "promotion" for promotion and "gameover" etc
    static String program_state = "game";

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

        if( ActivePiece.type != ' ' ) {

            ArrayList<Move> moves = Moves.get_legal_moves(main_game, ActivePiece.col, ActivePiece.row);

            for (Move move : moves) {
                System.out.println(move);

            }

        }
    }

    public static void start_piece_drag() {

        if( !program_state.equals("game") )
            return;


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

        if( !program_state.equals("game") )
            return;

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

        ArrayList<Move> legal_moves = Moves.get_legal_moves(main_game, col_active, row_active);

        // capture and promote doesn't matter here
        Move move = new Move(col_active, row_active, col, row);

        if( !Util.check_if_move_square_is_in_moves( legal_moves, move) )
           return;

        if( main_game.board[row][col] != ' ' )
            move.capture = true;

        System.out.println(move);

        main_game = Moves.make_move(main_game, move);
    }
}
