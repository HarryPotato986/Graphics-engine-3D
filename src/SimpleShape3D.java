import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleShape3D {

    public Point[] points;
    public Line[] lines;
    public Face[] faces;
    private int FOCAL_LENGTH;

    public SimpleShape3D(int[][] vertices, int[][] relations, int[][] faceRelations, int focalLength) {
        FOCAL_LENGTH = focalLength;

        //generates points array
        points = new Point[vertices.length];
        for (int i = 0; i<vertices.length; i++) {
            points[i] = new Point(vertices[i][0],vertices[i][1],vertices[i][2]);
        }

        //generates lines array
        lines = new Line[relations.length];
        for (int i = 0; i< relations.length; i++) {
            lines[i] = new Line(points[relations[i][0]],points[relations[i][1]]);
        }

        faces = new Face[faceRelations.length];
        for (int i = 0; i < faceRelations.length; i++) {
            Point[] tempArray = new Point[faceRelations[i].length];
            for (int j = 0; j < faceRelations[i].length; j++) {
                tempArray[j] = points[faceRelations[i][j]];
            }
            faces[i] = new Face(tempArray,FOCAL_LENGTH);
        }
    }

    public void draw(Graphics g) {
        boolean wireFrame = true;
        if (wireFrame) {
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
}
