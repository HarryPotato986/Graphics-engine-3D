import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ChangeListener, ActionListener {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;
    public int FOCAL_LENGTH = 300;

    JLabel label;
    JSlider FLSlider;
    Timer timer;

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);

        label = new JLabel();
        FLSlider = new JSlider(10,300,300);

        FLSlider.setPreferredSize(new Dimension(300,50));

        label.setText(String.valueOf(FLSlider.getValue()));

        FLSlider.addChangeListener(this);

        this.add(FLSlider);
        this.add(label);

        timer = new Timer(100,this);
        timer.restart();

        //shape.rotateZAxis(-5);
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


    @Override
    public void stateChanged(ChangeEvent e) {

        label.setText(String.valueOf(FLSlider.getValue()));
        FOCAL_LENGTH = FLSlider.getValue();

        shape.updateFocalLength(FOCAL_LENGTH);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        shape.rotateYAxis(5);
        //shape.rotateXAxis(5);
        repaint();

    }
}
