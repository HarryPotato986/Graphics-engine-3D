import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class GamePanel extends JPanel implements ChangeListener, ActionListener {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;

    public static final Vec3D TEMP_CAMERA = new Vec3D(0,0,0);

    public int FOCAL_LENGTH = 300;

    public float CUBE_ANGLE_X = 0;
    public float CUBE_ANGLE_Z = 0;
    public float DONUT_ANGLE_X = 0;
    public float DONUT_ANGLE_Z = 0;

    //Projection Matrix
    float fNear = 0.1f;
    float fFar = 1000.0f;
    float fFov = 90.0f;
    float fAspectRatio = (float) SCREEN_HEIGHT / (float) SCREEN_WIDTH;


    ProjectionMatrix matProj = new ProjectionMatrix(fFov, fAspectRatio, fNear, fFar);



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

        for (float[] row : matProj.matrix) {
            System.out.println(Arrays.toString(row));
        }



    }

    public void paint(Graphics g) {
        super.paint(g);

        //g.setColor(Color.RED);
        //shape.draw(g);

        ArrayList<Triangle> trisToSort = new ArrayList<>();

        trisToSort.addAll(donutMesh.prepareTrisToDraw(g, matProj, DONUT_ANGLE_X, DONUT_ANGLE_Z, Color.BLUE));
        trisToSort.addAll(cubeMesh.prepareTrisToDraw(g, matProj, CUBE_ANGLE_X, CUBE_ANGLE_Z, Color.RED));

        drawTriangles(g, trisToSort);
    }

    public void drawTriangles(Graphics g, ArrayList<Triangle> trisToSort) {
        Triangle[] triArrToSort = new Triangle[trisToSort.size()];

        Arrays.sort(trisToSort.toArray(triArrToSort), (t1, t2) -> {
            float z1 = (t1.points[0].Z + t1.points[1].Z + t1.points[2].Z) / 3.0f;
            float z2 = (t2.points[0].Z + t2.points[1].Z + t2.points[2].Z) / 3.0f;
            return Float.compare(z2, z1);
        });

        for (Triangle tri : triArrToSort) {

            g.setColor(tri.color);
            int[] Xs = new int[]{(int) tri.points[0].X, (int) tri.points[1].X, (int) tri.points[2].X};
            int[] Ys = new int[]{(int) tri.points[0].Y, (int) tri.points[1].Y, (int) tri.points[2].Y};
            g.fillPolygon(Xs, Ys, 3);

            /*
            g.setColor(Color.black);
            g.drawLine((int) tri.points[0].X, (int) tri.points[0].Y, (int) tri.points[1].X, (int) tri.points[1].Y);
            g.drawLine((int) tri.points[1].X, (int) tri.points[1].Y, (int) tri.points[2].X, (int) tri.points[2].Y);
            g.drawLine((int) tri.points[2].X, (int) tri.points[2].Y, (int) tri.points[0].X, (int) tri.points[0].Y);
            */
        }
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

    Mesh donutMesh = new Mesh("D:\\Codeing Projects\\graphics engine 3D\\Donut.obj");



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
        DONUT_ANGLE_X += 2.5f;
        DONUT_ANGLE_Z += 1.25f;
        repaint();
    }
}
