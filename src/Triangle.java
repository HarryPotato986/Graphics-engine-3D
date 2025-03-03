import java.awt.*;

public class Triangle {

    //RotationMatrix matRot = new RotationMatrix();


    public Vec3D[] points;
    public Vec3D normal;

    public float lum;
    public Color color;

    Triangle(Vec3D p1, Vec3D p2, Vec3D p3) {
        points = new Vec3D[]{p1,p2,p3};
        generateNormal();
        lum = 0.0f;
    }

    Triangle(Triangle triangle) {
        Vec3D p1 = new Vec3D(triangle.points[0].X, triangle.points[0].Y, triangle.points[0].Z);
        Vec3D p2 = new Vec3D(triangle.points[1].X, triangle.points[1].Y, triangle.points[1].Z);
        Vec3D p3 = new Vec3D(triangle.points[2].X, triangle.points[2].Y, triangle.points[2].Z);

        points = new Vec3D[]{p1,p2,p3};
        generateNormal();
        lum = 0.0f;
    }

    private void generateNormal() {
        Vec3D[] p = points;
        Vec3D line1 = new Vec3D(Vec3D.subtract(p[1], p[0]));
        Vec3D line2 = new Vec3D(Vec3D.subtract(p[2], p[0]));
        normal = new Vec3D(Vec3D.crossProduct(line1, line2));

        normal.normalize();
    }

    public void translate(float x, float y, float z) {
        points[0].X += x; points[0].Y += y; points[0].Z += z;
        points[1].X += x; points[1].Y += y; points[1].Z += z;
        points[2].X += x; points[2].Y += y; points[2].Z += z;

        generateNormal();
    }

    /*
    public void rotate(float angleDeg, RotationAxis axis) {
        matRot.updateMatrix(angleDeg, axis);
        points[0] = matRot.RotationMultiply(points[0]);
        points[1] = matRot.RotationMultiply(points[1]);
        points[2] = matRot.RotationMultiply(points[2]);

        generateNormal();
    }
    */

    public void updateLighting(Vec3D lightDir) {
        float dp = Vec3D.dotProduct(normal, lightDir);
        float temp = (dp + 1.0f) / 2.0f;
        if (temp <= 1.0f) {
            lum = temp;
        } else {
            lum = 1.0f;
        }
    }

    public void updateColor(Color newColor) {
        int R = (int) (newColor.getRed() * lum);
        int G = (int) (newColor.getGreen() * lum);
        int B = (int) (newColor.getBlue() * lum);
        color = new Color(R, G, B);
    }

    public void forceColor(Color newColor) {
        color = newColor;
    }
}
