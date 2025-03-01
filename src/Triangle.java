public class Triangle {

    RotationMatrix matRot = new RotationMatrix();


    public Vec3D[] points;

    Triangle(Vec3D p1, Vec3D p2, Vec3D p3) {
        points = new Vec3D[]{p1,p2,p3};
    }

    Triangle(Triangle triangle) {
        Vec3D p1 = new Vec3D(triangle.points[0].X, triangle.points[0].Y, triangle.points[0].Z);
        Vec3D p2 = new Vec3D(triangle.points[1].X, triangle.points[1].Y, triangle.points[1].Z);
        Vec3D p3 = new Vec3D(triangle.points[2].X, triangle.points[2].Y, triangle.points[2].Z);

        points = new Vec3D[]{p1,p2,p3};
    }

    public void translate(float x, float y, float z) {
        points[0].X += x; points[0].Y += y; points[0].Z += z;
        points[1].X += x; points[1].Y += y; points[1].Z += z;
        points[2].X += x; points[2].Y += y; points[2].Z += z;
    }

    public void rotate(float angleDeg, RotationAxis axis) {
        matRot.updateMatrix(angleDeg, axis);
        points[0] = matRot.RotationMultiply(points[0]);
        points[1] = matRot.RotationMultiply(points[1]);
        points[2] = matRot.RotationMultiply(points[2]);
    }
}
