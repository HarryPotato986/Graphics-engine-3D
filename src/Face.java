import java.awt.*;

/* This stores the relation between Lines */
public class Face {

    public Point[] orderedPoints;


    public Face(Point[] points, int focalLength) {
        orderedPoints = points;

    }

    public void draw(Graphics g,int focalLength) {
        int[] Xs;
        int[] Ys;
        int numOfPoints;

        Xs = new int[orderedPoints.length];
        Ys = new int[orderedPoints.length];
        numOfPoints = orderedPoints.length;
        for (int i = 0; i < orderedPoints.length; i++) {
            Xs[i] = ((focalLength* orderedPoints[i].X)/(focalLength+ orderedPoints[i].Z))+400;
            Ys[i] = ((focalLength* orderedPoints[i].Y)/(focalLength+ orderedPoints[i].Z))+400;
        }

        g.fillPolygon(Xs,Ys,numOfPoints);
    }
}
