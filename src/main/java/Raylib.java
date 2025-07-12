
import static com.raylib.Raylib.*;
import static com.raylib.Colors.*;

public class Raylib {

    public static int[] mouse_position() {

        Vector2 mouse = GetMousePosition();

        final int x = (int) mouse.x();
        final int y = (int) mouse.y();

        return new int[]{x, y};
    }

    public static Color color( int r, int g, int b, int a ) {

        Color c = new Color();

        c.r((byte) r).g((byte) g).b((byte) b).a((byte) a);

        return c;
    }

    public static Color color( int r, int g, int b) {

        Color c = new Color();

        c.r((byte) r).g((byte) g).b((byte) b).a((byte) 255);

        return c;
    }

    public static Color color( int colour, int a) {

        Color c = new Color();

        c.r((byte) colour).g((byte) colour).b((byte) colour).a((byte) a);

        return c;
    }

    public static Color color( int colour) {

        Color c = new Color();

        c.r((byte) colour).g((byte) colour).b((byte) colour).a((byte) 255);

        return c;
    }

    public static void image(Image img, int x, int y, int w, int h) {

        ImageResize(img, w, h);

        Texture texture = LoadTextureFromImage(img);

        DrawTexture(texture, x, y, WHITE);
    }

    public static Texture load_texture(String path, int width, int height) {

        Image image = LoadImage(path);

        ImageResize(image, width, height);

        return LoadTextureFromImage(image);
    }
}
