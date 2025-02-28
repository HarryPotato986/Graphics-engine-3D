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
            int x = oldPoint.X;
            int y = oldPoint.Y;
            int z = oldPoint.Z;
            long newY = Math.round((y * cos) + (z * sin));
            long newZ = Math.round((y * (-1 * sin)) + (z * cos));
            //System.out.println("new Y as int: " + (int) newY + "  as long: " + newY);
            Point newPoint = new Point(x, (int) newY, (int) newZ);

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
            int x = oldPoint.X;
            int y = oldPoint.Y;
            int z = oldPoint.Z;
            long newX = Math.round((x * cos) + (z * sin));
            long newZ = Math.round((x * (-1 * sin)) + (z * cos));
            Point newPoint = new Point((int) newX, y, (int) newZ);

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
            int x = oldPoint.X;
            int y = oldPoint.Y;
            int z = oldPoint.Z;
            long newX = Math.round((x * cos) + (y * sin));
            long newY = Math.round((x * (-1 * sin)) + (y * cos));
            Point newPoint = new Point((int) newX, (int) newY, z);

            points[i] = newPoint;
        }

        generateLineAndFaceArrays();
    }

    public void updateFocalLength(int newValue) {
        FOCAL_LENGTH = newValue;
    }
}
