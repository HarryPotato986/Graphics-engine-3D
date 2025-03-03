public class ProjectionMatrix extends Matrix{

    ProjectionMatrix(float fFov, float fAspectRatio, float fNear, float fFar) {
        super(4, 4);

        float fFovRad = 1.0f / (float) Math.tan(fFov * 0.5f / 180.0f * Math.PI);
        matrix[0][0] = fAspectRatio * fFovRad;
        matrix[1][1] = fFovRad;
        matrix[2][2] = fFar / (fFar - fNear);
        matrix[3][2] = (-fFar * fNear) / (fFar - fNear);
        matrix[2][3] = 1.0f;
        matrix[3][3] = 0.0f;
    }

    public Vec3D projMultiply(Vec3D oldVec) {
        float x, y, z, w;
        float[][] m = this.matrix;

        x = oldVec.X * m[0][0] + oldVec.Y * m[1][0] + oldVec.Z * m[2][0] + m[3][0];
        y = oldVec.X * m[0][1] + oldVec.Y * m[1][1] + oldVec.Z * m[2][1] + m[3][1];
        z = oldVec.X * m[0][2] + oldVec.Y * m[1][2] + oldVec.Z * m[2][2] + m[3][2];
        w = oldVec.X * m[0][3] + oldVec.Y * m[1][3] + oldVec.Z * m[2][3] + m[3][3];

        if (w != 0.0f) {
            x /= w; y /= w; z /= w;
        }

        return new Vec3D(x, y, z);
    }
}
