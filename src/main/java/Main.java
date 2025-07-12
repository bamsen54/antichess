

import static com.raylib.Raylib.*;

public class Main {

    public static void main( String[] args ) {

        InitWindow(1024, 789, "Anti Chess");

        SetTargetFPS(144);

        AntiChess.run();

        System.out.println(5);

        CloseWindow();
    }
}