import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleShape3D {

    public int[][] RELATIONS;
    public int[][] FACE_RELATIONS;

    public Point[] points;
    public Line[] lines;
    public Face[] faces;
    private int FOCAL_LENGTH;

    public SimpleShape3D(int[][] vertices, int[][] relations, int[][] faceRelations, int focalLength) {
        RELATIONS = relations;
        FACE_RELATIONS = faceRelations;
        FOCAL_LENGTH = focalLength;

        //generates points array
        points = new Point[vertices.length];
        for (int i = 0; i<vertices.length; i++) {
            points[i] = new Point(vertices[i][0],vertices[i][1],vertices[i][2]);
        }

        generateLineAndFaceArrays();
    }

    private void generateLineAndFaceArrays() {
        //generates lines array
        lines = new Line[RELATIONS.length];
        for (int i = 0; i< RELATIONS.length; i++) {
            lines[i] = new Line(points[RELATIONS[i][0]],points[RELATIONS[i][1]]);
        }

        faces = new Face[FACE_RELATIONS.length];
        for (int i = 0; i < FACE_RELATIONS.length; i++) {
            Point[] tempArray = new Point[FACE_RELATIONS[i].length];
            for (int j = 0; j < FACE_RELATIONS[i].length; j++) {
                tempArray[j] = points[FACE_RELATIONS[i][j]];
            }
            faces[i] = new Face(tempArray,FOCAL_LENGTH);
        }
    }

    public void draw(Graphics g) {
        boolean wireFrame = false;
        if (!wireFrame) {
            g.setColor(Color.RED);
            for (Face face : faces) {
                face.draw(g,FOCAL_LENGTH);
            }
        }
        g.setColor(Color.BLACK);
        for (Line line : lines) {
            line.draw(g,FOCAL_LENGTH);
        }
    }

    public void rotateXAxis(int angle) {
        double radAngle = Math.toRadians(angle);
        double sin = Math.sin(radAngle);
        double cos = Math.cos(radAngle);

        for (int i = 0; i < points.length; i++) {
            Point oldPoint = points[i];
            double x = oldPoint.X;
            double y = oldPoint.Y;
            double z = oldPoint.Z;
            Point newPoint = new Point(x, (y * cos) + (z * sin), (y * (-1 * sin)) + (z * cos));

            points[i] = newPoint;
        }

        generateLineAndFaceArrays();
    }

    public void rotateYAxis(int angle) {
        double radAngle = Math.toRadians(angle);
        double sin = Math.sin(radAngle);
        double cos = Math.cos(radAngle);

        for (int i = 0; i < points.length; i++) {
            Point oldPoint = points[i];
            double x = oldPoint.X;
            double y = oldPoint.Y;
            double z = oldPoint.Z;
            Point newPoint = new Point((x * cos) + (z * sin), y, (x * (-1 * sin)) + (z * cos));

            points[i] = newPoint;
        }

        generateLineAndFaceArrays();
    }

    public void rotateZAxis(int angle) {
        double radAngle = Math.toRadians(angle);
        double sin = Math.sin(radAngle);
        double cos = Math.cos(radAngle);

        for (int i = 0; i < points.length; i++) {
            Point oldPoint = points[i];
            double x = oldPoint.X;
            double y = oldPoint.Y;
            double z = oldPoint.Z;
            Point newPoint = new Point((x * cos) + (y * sin), (x * (-1 * sin)) + (y * cos), z);

            points[i] = newPoint;
        }

        generateLineAndFaceArrays();
    }

    public void updateFocalLength(int newValue) {
        FOCAL_LENGTH = newValue;
    }
}
