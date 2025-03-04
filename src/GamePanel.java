import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class GamePanel extends JPanel implements /*ChangeListener,*/ ActionListener, KeyListener {

    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 800;

    public static final Vec3D TEMP_CAMERA = new Vec3D(0,0,0);
    public Vec3D vCamera = new Vec3D(0,0,0);
    public Vec3D vLookDir;
    public float fYaw = 0.0f;

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
        this.addKeyListener(this);

        //label = new JLabel();
        //FLSlider = new JSlider(10,300,300);

        //FLSlider.setPreferredSize(new Dimension(300,50));

        //label.setText(String.valueOf(FLSlider.getValue()));

        //FLSlider.addChangeListener(this);

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


        Vec3D vUp = new Vec3D(0, 1, 0);
        Vec3D vTarget = new Vec3D(0, 0, 1);
        RotationMatrix matCameraRot = new RotationMatrix(fYaw, RotationAxis.Y);
        vLookDir = Matrix.vectorMultiply(vTarget, matCameraRot);
        vTarget = Vec3D.add(vCamera, vLookDir);

        PointAtMatrix matCamera = new PointAtMatrix(vCamera, vTarget, vUp);
        matCamera.invert();

        ArrayList<Triangle> trisToSort = new ArrayList<>();

        //trisToSort.addAll(donutMesh.prepareTrisToDraw(g, matProj, DONUT_ANGLE_X, DONUT_ANGLE_Z, Color.BLUE));
        //trisToSort.addAll(cubeMesh.prepareTrisToDraw(g, matProj, CUBE_ANGLE_X, CUBE_ANGLE_Z, Color.RED));
        trisToSort.addAll(axisMesh.prepareTrisToDraw(g, matProj, matCamera, vCamera, 0, 0, Color.GRAY));
        //trisToSort.addAll(testTriangleMesh.prepareTrisToDraw(g, matProj, matCamera, vCamera, 0, 0, Color.GRAY));

        drawTriangles(g, trisToSort);
    }

    public void drawTriangles(Graphics g, ArrayList<Triangle> trisToSort) {
        Triangle[] triArrToSort = new Triangle[trisToSort.size()];

        Arrays.sort(trisToSort.toArray(triArrToSort), (t1, t2) -> {
            float z1 = (t1.points[0].Z + t1.points[1].Z + t1.points[2].Z) / 3.0f;
            float z2 = (t2.points[0].Z + t2.points[1].Z + t2.points[2].Z) / 3.0f;
            return Float.compare(z2, z1);
        });


        for (Triangle triToDraw : triArrToSort) {

            Triangle[] clipped = new Triangle[]{new Triangle(triToDraw), new Triangle(triToDraw)};
            ArrayList<Triangle> listTriangles = new ArrayList<>();

            listTriangles.add(triToDraw);
            int nNewTriangles = 1;

            for (int p = 0; p < 4; p++) {

                int nTrisToAdd = 0;
                while (nNewTriangles > 0) {
                    Triangle test = listTriangles.get(0);
                    listTriangles.remove(0);
                    nNewTriangles--;

                    switch (p) {
                        case 0: nTrisToAdd = Triangle.clipAgainstPlane(new Vec3D(0.0f, 0.0f, 0.0f), new Vec3D(0.0f, 1.0f, 0.0f), test, clipped[0], clipped[1]);
                            break;
                        case 1: nTrisToAdd = Triangle.clipAgainstPlane(new Vec3D(0.0f, (float) SCREEN_HEIGHT - 1, 0.0f), new Vec3D(0.0f, -1.0f, 0.0f), test, clipped[0], clipped[1]);
                            break;
                        case 2: nTrisToAdd = Triangle.clipAgainstPlane(new Vec3D(0.0f, 0.0f, 0.0f), new Vec3D(1.0f, 0.0f, 0.0f), test, clipped[0], clipped[1]);
                            break;
                        case 3: nTrisToAdd = Triangle.clipAgainstPlane(new Vec3D((float) SCREEN_WIDTH - 1, 0.0f, 0.0f), new Vec3D(-1.0f, 0.0f, 0.0f), test, clipped[0], clipped[1]);
                            break;
                    }

                    for (int w = 0; w < nTrisToAdd; w++) {
                        listTriangles.add(clipped[w]);
                        //System.out.println(w);
                    }
                }
                nNewTriangles = listTriangles.size();
            }

            for (Triangle tri : listTriangles) {

                g.setColor(tri.color);
                int[] Xs = new int[]{(int) tri.points[0].X, (int) tri.points[1].X, (int) tri.points[2].X};
                int[] Ys = new int[]{(int) tri.points[0].Y, (int) tri.points[1].Y, (int) tri.points[2].Y};
                g.fillPolygon(Xs, Ys, 3);


                g.setColor(Color.black);
                g.drawLine((int) tri.points[0].X, (int) tri.points[0].Y, (int) tri.points[1].X, (int) tri.points[1].Y);
                g.drawLine((int) tri.points[1].X, (int) tri.points[1].Y, (int) tri.points[2].X, (int) tri.points[2].Y);
                g.drawLine((int) tri.points[2].X, (int) tri.points[2].Y, (int) tri.points[0].X, (int) tri.points[0].Y);

            }
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

    Mesh axisMesh = new Mesh("D:\\Codeing Projects\\graphics engine 3D\\Axis.obj");

    Mesh testTriangleMesh = new Mesh("D:\\Codeing Projects\\graphics engine 3D\\TestTriangle.obj");


    /*
    @Override
    public void stateChanged(ChangeEvent e) {

        label.setText(String.valueOf(FLSlider.getValue()));
        FOCAL_LENGTH = FLSlider.getValue();

        shape.updateFocalLength(FOCAL_LENGTH);
        repaint();
    }
    */

    @Override
    public void actionPerformed(ActionEvent e) {

        //shape.rotateYAxis(5);
        //shape.rotateXAxis(5);
        //repaint();

        //System.out.println("bruh");

        CUBE_ANGLE_X += 1.25f;
        CUBE_ANGLE_Z += 2.5f;
        DONUT_ANGLE_X += 2.5f;
        DONUT_ANGLE_Z += 1.25f;
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Vec3D vForward = Vec3D.multiply(vLookDir, 0.3f);
        switch (e.getKeyCode()) {
            case 16 -> vCamera.Y -= 0.3f; //shift
            case 32 -> vCamera.Y += 0.3f; //space

            case 37 -> vCamera.X += 0.3f; //left
            case 39 -> vCamera.X -= 0.3f; //right
            case 38 -> vCamera.Z += 0.3f; //up
            case 40 -> vCamera.Z -= 0.3f; //down

            case 65 -> fYaw += 3.0f; //a
            case 68 -> fYaw -= 3.0f; //d
            case 87 -> vCamera = Vec3D.add(vCamera, vForward); //w
            case 83 -> vCamera = Vec3D.subtract(vCamera, vForward); //s
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println(e.getKeyCode());
    }
}
