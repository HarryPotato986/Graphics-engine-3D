public class Vec3D {
    public float X, Y, Z, W;

    Vec3D(float x, float y, float z) {
        X = x; Y = y; Z = z; W = 1.0f;
    }

    Vec3D(float x, float y, float z, float w) {
        X = x; Y = y; Z = z; W = w;
    }

    Vec3D(Vec3D v) {
        X = v.X; Y = v.Y; Z = v.Z; W = v.W;
    }

    public void normalize() {
        Vec3D result = new Vec3D(this);
        float l = length(result);
        result = divide(result, l);
        X = result.X;
        Y = result.Y;
        Z = result.Z;
        W = result.W;

        /*
        float l2 = length(this);
        if (l2 > 1) {
            System.out.println(l2);
        }
        */
    }

    public float len() {
        return length(this);
    }



    public static Vec3D add(Vec3D v1, Vec3D v2) {
        return new Vec3D(v1.X + v2.X, v1.Y + v2.Y, v1.Z + v2.Z);
    }

    public static Vec3D subtract(Vec3D v1, Vec3D v2) {
        return new Vec3D(v1.X - v2.X, v1.Y - v2.Y, v1.Z - v2.Z);
    }

    public static Vec3D multiply(Vec3D v, float k) {
        return new Vec3D(v.X * k, v.Y * k, v.Z * k);
    }

    public static Vec3D divide(Vec3D v, float k) {
        return new Vec3D(v.X / k, v.Y / k, v.Z / k);
    }

    public static float dotProduct(Vec3D v1, Vec3D v2) {
        return v1.X * v2.X + v1.Y * v2.Y + v1.Z * v2.Z;
    }

    public static Vec3D crossProduct(Vec3D v1, Vec3D v2) {
        float x = v1.Y * v2.Z - v1.Z * v2.Y;
        float y = v1.Z * v2.X - v1.X * v2.Z;
        float z = v1.X * v2.Y - v1.Y * v2.X;
        return new Vec3D(x,y,z);
    }

    public static float length(Vec3D v) {
        return (float) Math.sqrt(v.X * v.X + v.Y * v.Y + v.Z * v.Z);
    }

    public static Vec3D normalize(Vec3D v) {
        float l = length(v);
        return divide(v, l);
    }

    public static Vec3D intersectPlane(Vec3D planeP, Vec3D planeN, Vec3D lineStart, Vec3D lineEnd) {
        planeN.normalize();

        /*
        if (planeN.len() > 1.0f) {
            System.out.println(planeN.len());
        }
        */

        float planeDP = -Vec3D.dotProduct(planeN, planeP);
        float ad = Vec3D.dotProduct(lineStart, planeN);
        float bd = Vec3D.dotProduct(lineEnd, planeN);
        float t = (-planeDP - ad) / (bd - ad);
        Vec3D lineStartToEnd = Vec3D.subtract(lineEnd, lineStart);
        Vec3D lineToIntersect = Vec3D.multiply(lineStartToEnd, t);
        return Vec3D.add(lineStart, lineToIntersect);
    }
}
