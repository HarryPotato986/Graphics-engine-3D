/* This provides an easy way to store coordinates */
public class Point {

    public int X;
    public int Y;
    public int Z;
    public int[] pos;

    public Point(int x, int y, int z) {
        X = x;
        Y = y;
        Z = z;
        pos = new int[]{x, y, z};
    }
}
