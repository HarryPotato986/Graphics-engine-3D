public class Triangle {

    RotationMatrix matRot = new RotationMatrix();


    public Vec3D[] points;
    public Vec3D normal;

    public float lum;

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
        Vec3D line1 = new Vec3D(p[1].X - p[0].X, p[1].Y - p[0].Y, p[1].Z - p[0].Z);
        Vec3D line2 = new Vec3D(p[2].X - p[0].X, p[2].Y - p[0].Y, p[2].Z - p[0].Z);
        normal = new Vec3D(
                line1.Y * line2.Z - line1.Z * line2.Y,
                line1.Z * line2.X - line1.X * line2.Z,
                line1.X * line2.Y - line1.Y * line2.X);

        float l = (float) Math.sqrt(normal.X * normal.X + normal.Y * normal.Y + normal.Z * normal.Z);
        normal.X /= l; normal.Y /= l; normal.Z /= l;
    }

    public void translate(float x, float y, float z) {
        points[0].X += x; points[0].Y += y; points[0].Z += z;
        points[1].X += x; points[1].Y += y; points[1].Z += z;
        points[2].X += x; points[2].Y += y; points[2].Z += z;

        generateNormal();
    }

    public void rotate(float angleDeg, RotationAxis axis) {
        matRot.updateMatrix(angleDeg, axis);
        points[0] = matRot.RotationMultiply(points[0]);
        points[1] = matRot.RotationMultiply(points[1]);
        points[2] = matRot.RotationMultiply(points[2]);

        generateNormal();
    }

    public void updateLighting(Vec3D lightDir) {
        float dp = normal.X * lightDir.X + normal.Y * lightDir.Y + normal.Z * lightDir.Z;
        lum = (dp + 1.0f) / 2.0f;
    }
}
