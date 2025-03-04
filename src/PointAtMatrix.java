public class PointAtMatrix extends SquareMatrix{

    PointAtMatrix(Vec3D pos, Vec3D target, Vec3D up) {
        super(4);

        Vec3D newForward = Vec3D.subtract(target, pos);
        newForward.normalize();

        Vec3D a = Vec3D.multiply(newForward, Vec3D.dotProduct(up, newForward));
        Vec3D newUp = Vec3D.subtract(up, a);
        newUp.normalize();

        Vec3D newRight = Vec3D.crossProduct(newUp, newForward);

        matrix[0][0] = newRight.X;	 matrix[0][1] = newRight.Y;	  matrix[0][2] = newRight.Z;   matrix[0][3] = 0.0f;
        matrix[1][0] = newUp.X;		 matrix[1][1] = newUp.Y;	  matrix[1][2] = newUp.Z;	   matrix[1][3] = 0.0f;
        matrix[2][0] = newForward.X; matrix[2][1] = newForward.Y; matrix[2][2] = newForward.Z; matrix[2][3] = 0.0f;
        matrix[3][0] = pos.X;		 matrix[3][1] = pos.Y;		  matrix[3][2] = pos.Z;		   matrix[3][3] = 1.0f;
    }

    public void invert() {
        float[][] result = new float[4][4];
        result[0][0] = matrix[0][0]; result[0][1] = matrix[1][0]; result[0][2] = matrix[2][0]; result[0][3] = 0.0f;
        result[1][0] = matrix[0][1]; result[1][1] = matrix[1][1]; result[1][2] = matrix[2][1]; result[1][3] = 0.0f;
        result[2][0] = matrix[0][2]; result[2][1] = matrix[1][2]; result[2][2] = matrix[2][2]; result[2][3] = 0.0f;
        result[3][0] = -(matrix[3][0] * result[0][0] + matrix[3][1] * result[1][0] + matrix[3][2] * result[2][0]);
        result[3][1] = -(matrix[3][0] * result[0][1] + matrix[3][1] * result[1][1] + matrix[3][2] * result[2][1]);
        result[3][2] = -(matrix[3][0] * result[0][2] + matrix[3][1] * result[1][2] + matrix[3][2] * result[2][2]);
        result[3][3] = 1.0f;
        matrix = result;
    }
}
