
import static com.raylib.Raylib.*;

public class Main {

    public static void main( String[] args ) {

        InitWindow((int) (1024 * 0.85), (int) (789 * 0.85), "Anti Chess");

        SetTargetFPS(144);

        AntiChess.run("8/5K2/8/8/8/8/5p2/8 b - - 45 1");

        CloseWindow();
    }
}