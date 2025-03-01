import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class GamePanel extends JPanel implements ChangeListener, ActionListener {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;
    public int FOCAL_LENGTH = 300;

    public float CUBE_ANGLE_X = 0;
    public float CUBE_ANGLE_Z = 0;

    //Projection Matrix
    float fNear = 0.1f;
    float fFar = 1000.0f;
    float fFov = 90.0f;
    float fAspectRatio = (float) SCREEN_HEIGHT / (float) SCREEN_WIDTH;
    float fFovRad = 1.0f / (float) Math.tan(fFov * 0.5f / 180.0f * Math.PI);

    ProjectionMatrix matProj = new ProjectionMatrix();



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

        //this.add(FLSlider);
        //this.add(label);

        timer = new Timer(50,this);
        timer.restart();

        shape.rotateZAxis(-15);

        matProj.matrix[0][0] = fAspectRatio * fFovRad;
        matProj.matrix[1][1] = fFovRad;
        matProj.matrix[2][2] = fFar / (fFar - fNear);
        matProj.matrix[3][2] = (-fFar * fNear) / (fFar - fNear);
        matProj.matrix[2][3] = 1.0f;
        matProj.matrix[3][3] = 0.0f;

        for (float[] row : matProj.matrix) {
            System.out.println(Arrays.toString(row));
        }

    }

    public void paint(Graphics g) {
        super.paint(g);

        //g.setColor(Color.RED);
        //shape.draw(g);

        cubeMesh.draw(g, matProj, CUBE_ANGLE_X, CUBE_ANGLE_Z);
    }


    int[][] vertices = {{-50,-50,-50},{50,-50,-50},{50,50,-50},{-50,50,-50},{-50,-50,50},{50,-50,50},{50,50,50},{-50,50,50}};
    int[][] relations = {{0,1},{1,2},{2,3},{3,0},{4,5},{5,6},{6,7},{7,4},{0,4},{1,5},{2,6},{3,7}};
    int[][] faceRelations = {{0,1,2,3},{4,5,6,7},{0,4,5,1},{3,7,6,2},{0,4,7,3},{1,5,6,2}};
    SimpleShape3D shape = new SimpleShape3D(vertices,relations,faceRelations,FOCAL_LENGTH);


    Mesh cubeMesh = new Mesh(new ArrayList<>(){
        {
            //SOUTH FACE
            add(new Triangle(new Vec3D(0,0,0), new Vec3D(0,1,0), new Vec3D(1,1,0)));
            add(new Triangle(new Vec3D(0,0,0), new Vec3D(1,1,0), new Vec3D(1,0,0)));
            //EAST FACE
            add(new Triangle(new Vec3D(1,0,0), new Vec3D(1,1,0), new Vec3D(1,1,1)));
            add(new Triangle(new Vec3D(1,0,0), new Vec3D(1,1,1), new Vec3D(1,0,1)));
            //NORTH FACE
            add(new Triangle(new Vec3D(1,0,1), new Vec3D(1,1,1), new Vec3D(0,1,1)));
            add(new Triangle(new Vec3D(1,0,1), new Vec3D(0,1,1), new Vec3D(0,0,1)));
            //WEST FACE
            add(new Triangle(new Vec3D(0,0,1), new Vec3D(0,1,1), new Vec3D(0,1,0)));
            add(new Triangle(new Vec3D(0,0,1), new Vec3D(0,1,0), new Vec3D(0,0,0)));
            //TOP FACE
            add(new Triangle(new Vec3D(0,1,0), new Vec3D(0,1,1), new Vec3D(1,1,1)));
            add(new Triangle(new Vec3D(0,1,0), new Vec3D(1,1,1), new Vec3D(1,1,0)));
            //BOTTOM FACE
            add(new Triangle(new Vec3D(1,0,1), new Vec3D(0,0,1), new Vec3D(0,0,0)));
            add(new Triangle(new Vec3D(1,0,1), new Vec3D(0,0,0), new Vec3D(1,0,0)));
        }
    });

    @Override
    public void stateChanged(ChangeEvent e) {

        label.setText(String.valueOf(FLSlider.getValue()));
        FOCAL_LENGTH = FLSlider.getValue();

        shape.updateFocalLength(FOCAL_LENGTH);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        //shape.rotateYAxis(5);
        //shape.rotateXAxis(5);
        //repaint();

        CUBE_ANGLE_X += 1.25f;
        CUBE_ANGLE_Z += 2.5f;
        repaint();
    }
}
