public class RotationMatrix extends Matrix{

    RotationMatrix(float angleDeg, RotationAxis axis) {
        super(4, 4);

        float angleRad = (float) Math.toRadians(angleDeg);

        switch (axis) {
            case X:
                this.matrix[0][0] = 1.0f;
                this.matrix[1][1] = (float) Math.cos(angleRad);
                this.matrix[1][2] = (float) Math.sin(angleRad);
                this.matrix[2][1] = (float) -Math.sin(angleRad);
                this.matrix[2][2] = (float) Math.cos(angleRad);
                this.matrix[3][3] = 1.0f;
                break;
            case Y:
                this.matrix[0][0] = (float) Math.cos(angleRad);
                this.matrix[1][1] = 1.0f;
                this.matrix[2][0] = (float) Math.sin(angleRad);
                this.matrix[0][2] = (float) -Math.sin(angleRad);
                this.matrix[2][2] = (float) Math.cos(angleRad);
                this.matrix[3][3] = 1.0f;
                break;
            case Z:
                this.matrix[0][0] = (float) Math.cos(angleRad);
                this.matrix[0][1] = (float) Math.sin(angleRad);
                this.matrix[1][0] = (float) -Math.sin(angleRad);
                this.matrix[1][1] = (float) Math.cos(angleRad);
                this.matrix[2][2] = 1.0f;
                this.matrix[3][3] = 1.0f;
                break;

        }
    }

    public Vec3D RotationMultiply(Vec3D oldVec) {
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

    public void updateMatrix(float angleDeg, RotationAxis axis) {
        float angleRad = (float) Math.toRadians(angleDeg);

        switch (axis) {
            case X:
                this.matrix[0][0] = 1.0f;
                this.matrix[1][1] = (float) Math.cos(angleRad);
                this.matrix[1][2] = (float) Math.sin(angleRad);
                this.matrix[2][1] = (float) -Math.sin(angleRad);
                this.matrix[2][2] = (float) Math.cos(angleRad);
                this.matrix[3][3] = 1.0f;
                break;
            case Y:
                this.matrix[0][0] = (float) Math.cos(angleRad);
                this.matrix[1][1] = 1.0f;
                this.matrix[2][0] = (float) Math.sin(angleRad);
                this.matrix[0][2] = (float) -Math.sin(angleRad);
                this.matrix[2][2] = (float) Math.cos(angleRad);
                this.matrix[3][3] = 1.0f;
                break;
            case Z:
                this.matrix[0][0] = (float) Math.cos(angleRad);
                this.matrix[0][1] = (float) Math.sin(angleRad);
                this.matrix[1][0] = (float) -Math.sin(angleRad);
                this.matrix[1][1] = (float) Math.cos(angleRad);
                this.matrix[2][2] = 1.0f;
                this.matrix[3][3] = 1.0f;
                break;

        }
    }
}


