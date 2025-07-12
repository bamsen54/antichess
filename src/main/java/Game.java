import com.raylib.Raylib;

import java.util.ArrayList;

// keeps track of everything related to current game
// is node in minimax
public class Game {

    public char[][] board = {{'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                             {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                             {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                             {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                             {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                             {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                             {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                             {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
                            };

    public String turn    = "white";

    // if en passant is possible, this is the en passant square.
    // en passant square is the square that pawn "captures" but there is no piece there
    public int[] en_passant_square = {};
    public String toString() {

        StringBuilder to_string = new StringBuilder();

        to_string.append("\n");
        to_string.append("Turn: ").append(this.turn).append("\n");

        for( int row = 0; row < 8; row++ ) {

            for( int col = 0; col < 8; col++ ) {

                if( this.board[row][col] != ' ')
                    to_string.append(this.board[row][col]);

                else
                    to_string.append("+");
            }

            to_string.append("\n");
        }

        return to_string.toString();
    }

    public Game get_copy() {

        Game copy = new Game();

        for( int row = 0; row < 8; row++ ) {

            for( int col = 0; col < 8; col++ )
                copy.board[row][col] = this.board[row][col];
        }

        copy.turn = this.turn;

        copy.en_passant_square = this.en_passant_square;

        return copy;
    }
}
