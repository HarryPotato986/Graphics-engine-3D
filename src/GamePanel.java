import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;
    public static final int FOCAL_LENGTH = 100;

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.RED);
        shape.draw(g);
    }


    int[][] vertices = {{-50,-50,-50},{50,-50,-50},{50,50,-50},{-50,50,-50},{-50,-50,50},{50,-50,50},{50,50,50},{-50,50,50}};
    int[][] relations = {{0,1},{1,2},{2,3},{3,0},{4,5},{5,6},{6,7},{7,4},{0,4},{1,5},{2,6},{3,7}};
    int[][] faceRelations = {{0,1,2,3},{4,5,6,7},{0,4,5,1},{3,7,6,2},{0,4,7,3},{1,5,6,2}};
    SimpleShape3D shape = new SimpleShape3D(vertices,relations,faceRelations,FOCAL_LENGTH);


}
