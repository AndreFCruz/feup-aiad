package utils;

import java.awt.*;
import java.io.Serializable;

public class GraphicSettings implements Serializable {
    public int x, y;
    public Color color;

    public GraphicSettings(int x, int y, Color c) {
        this.x = x;
        this.y = y;
        this.color = c;
    }
}
