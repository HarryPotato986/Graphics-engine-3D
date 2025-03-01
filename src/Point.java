/* This provides an easy way to store coordinates */
public class Point {

    public double X;
    public double Y;
    public double Z;
    public double[] pos;

    public Point(double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
        pos = new double[]{x, y, z};
    }
}
