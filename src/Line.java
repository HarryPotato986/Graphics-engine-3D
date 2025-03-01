import java.awt.*;

/* This stores the relation between two Points */
public class Line {

    public Point point1;
    public Point point2;
    public Point[] points;

    public Line(Point pointA, Point pointB) {
        point1 = pointA;
        point2 = pointB;
        points = new Point[]{pointA,pointB};
    }

    public void draw(Graphics g,int focalLength) {
        g.drawLine((int) ((focalLength* point1.X)/(focalLength+ point1.Z))+400,(int) ((focalLength* point1.Y)/(focalLength+ point1.Z))+400,
                (int) ((focalLength* point2.X)/(focalLength+ point2.Z))+400, (int) ((focalLength* point2.Y)/(focalLength+ point2.Z))+400);
    }
}
