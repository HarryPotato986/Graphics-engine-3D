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
        color = Color.BLACK;
    }

    Triangle(Triangle triangle) {
        Vec3D p1 = new Vec3D(triangle.points[0].X, triangle.points[0].Y, triangle.points[0].Z);
        Vec3D p2 = new Vec3D(triangle.points[1].X, triangle.points[1].Y, triangle.points[1].Z);
        Vec3D p3 = new Vec3D(triangle.points[2].X, triangle.points[2].Y, triangle.points[2].Z);

        points = new Vec3D[]{p1,p2,p3};
        generateNormal();
        lum = triangle.lum;
        color = triangle.color;
    }

    private void generateNormal() {
        Vec3D[] p = points;
        Vec3D line1 = Vec3D.subtract(p[1], p[0]);
        Vec3D line2 = Vec3D.subtract(p[2], p[0]);
        normal = Vec3D.crossProduct(line1, line2);

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
        float temp = Math.min(((dp + 1.0f) / 2.0f), 1.0f);
        lum = Math.max(temp, 0.0f);
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



    public static int clipAgainstPlane(Vec3D planeP, Vec3D planeN, Triangle triIn, Triangle triOut1, Triangle triOut2) {
        planeN.normalize();

        Vec3D p0 = new Vec3D(triIn.points[0]);
        p0.normalize();
        Vec3D p1 = new Vec3D(triIn.points[1]);
        p1.normalize();
        Vec3D p2 = new Vec3D(triIn.points[2]);
        p2.normalize();

        Vec3D[] insidePoints = new Vec3D[3]; int nInsidePointsCount = 0;
        Vec3D[] outsidePoints = new Vec3D[3]; int nOutsidePointsCount = 0;

        float d0 = planeN.X * p0.X + planeN.Y * p0.Y + planeN.Z * p0.Z - Vec3D.dotProduct(planeN, planeP);
        float d1 = planeN.X * p1.X + planeN.Y * p1.Y + planeN.Z * p1.Z - Vec3D.dotProduct(planeN, planeP);
        float d2 = planeN.X * p2.X + planeN.Y * p2.Y + planeN.Z * p2.Z - Vec3D.dotProduct(planeN, planeP);

        if (d0 >= 0) {
            insidePoints[nInsidePointsCount++] = new Vec3D(triIn.points[0]);
        } else outsidePoints[nOutsidePointsCount++] = new Vec3D(triIn.points[0]);
        if (d1 >= 0) {
            insidePoints[nInsidePointsCount++] = new Vec3D(triIn.points[1]);
        } else outsidePoints[nOutsidePointsCount++] = new Vec3D(triIn.points[1]);
        if (d2 >= 0) {
            insidePoints[nInsidePointsCount++] = new Vec3D(triIn.points[2]);
        } else outsidePoints[nOutsidePointsCount++] = new Vec3D(triIn.points[2]);


        if (nInsidePointsCount == 0) {
            return 0;
        }

        if (nInsidePointsCount == 3) {
            triOut1 = new Triangle(triIn);
            return 1;
        }

        if (nInsidePointsCount == 1 && nOutsidePointsCount == 2) {

            triOut1.lum = triIn.lum;
            //triOut1.color = triIn.color;
            triOut1.forceColor(Color.BLUE);

            triOut1.points[0] = new Vec3D(insidePoints[0]);

            triOut1.points[1] = Vec3D.intersectPlane(planeP, planeN, new Vec3D(insidePoints[0]), new Vec3D(outsidePoints[0]));
            triOut1.points[2] = Vec3D.intersectPlane(planeP, planeN, new Vec3D(insidePoints[0]), new Vec3D(outsidePoints[1]));

            triOut1.generateNormal();

            return 1;
        }

        if (nInsidePointsCount == 2 && nOutsidePointsCount == 1) {

            //triOut1.lum = triIn.lum; triOut1.color = triIn.color;
            //triOut2.lum = triIn.lum; triOut2.color = triIn.color;

            triOut1.lum = triIn.lum; triOut1.forceColor(Color.GREEN);
            triOut2.lum = triIn.lum; triOut2.forceColor(Color.RED);


            triOut1.points[0] = new Vec3D(insidePoints[0]);
            triOut1.points[1] = new Vec3D(insidePoints[1]);
            triOut1.points[2] = Vec3D.intersectPlane(planeP, planeN, new Vec3D(insidePoints[0]), new Vec3D(outsidePoints[0]));

            triOut2.points[0] = new Vec3D(insidePoints[1]);
            triOut2.points[1] = new Vec3D(triOut1.points[2]);
            triOut2.points[2] = Vec3D.intersectPlane(planeP, planeN, new Vec3D(insidePoints[1]), new Vec3D(outsidePoints[0]));

            /*
            System.out.println("triangle 1: " + triOut1.points[0].X + ", " + triOut1.points[0].Y + ", " + triOut1.points[0].Z);
            System.out.println("            " + triOut1.points[1].X + ", " + triOut1.points[1].Y + ", " + triOut1.points[1].Z);
            System.out.println("            " + triOut1.points[2].X + ", " + triOut1.points[2].Y + ", " + triOut1.points[2].Z);

            System.out.println("triangle 2: " + triOut2.points[0].X + ", " + triOut2.points[0].Y + ", " + triOut2.points[0].Z);
            System.out.println("            " + triOut2.points[1].X + ", " + triOut2.points[1].Y + ", " + triOut2.points[1].Z);
            System.out.println("            " + triOut2.points[2].X + ", " + triOut2.points[2].Y + ", " + triOut2.points[2].Z);
            */

            return 2;
        }
        return 0;
    }
}










