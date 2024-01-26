/*import java.util.ArrayList;
import java.util.HashMap;

public class Shape3D {

    public Point[] points;
    public Line[] lines;
    private HashMap<Point,Point[]> connectedPoints;

    public Shape3D(int[][] vertices, int[][] relations) {

        //generates points array
        points = new Point[vertices.length];
        for (int i = 0; i<vertices.length; i++) {
            points[i] = new Point(vertices[i][0],vertices[i][1]);
        }

        //generates lines array
        lines = new Line[relations.length];
        for (int i = 0; i< relations.length; i++) {
            lines[i] = new Line(points[relations[i][0]],points[relations[i][1]]);
        }

        //generates connectedPoints 2D array
        for (Point point : points) {
            ArrayList<Point> tempList = new ArrayList<Point>();
            for (Line line : lines) {
                if (line.point1.equals(point)) {
                    tempList.add(line.point2);
                } else if (line.point2.equals(point)) {
                    tempList.add(line.point1);
                }
            }
            connectedPoints.put(point, new Point[tempList.size()]);
            for (int j = 0; j < tempList.size(); j++) {
                connectedPoints.get(point)[j] = tempList.get(j);
            }
        }

        for (Point startingPoint : points) {
            for (Point point : connectedPoints.get(startingPoint)) {

            }
        }
    }
}

 */
