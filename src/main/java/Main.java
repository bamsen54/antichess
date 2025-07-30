
import static com.raylib.Raylib.*;

public class Main {

    public static void main( String[] args ) {

        InitWindow((int) (1024 * 0.85), (int) (789 * 0.85), "Anti Chess");

        SetTargetFPS(144);

        AntiChess.run("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w - - 46 1");

        CloseWindow();
    }
}