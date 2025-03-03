public class Matrix {
    public float[][] matrix;
    public final int rows;
    public final int columns;

    Matrix(int rows, int columns) {
        matrix = new float[rows][columns];
        this.rows = rows;
        this.columns = columns;
    }

    Matrix(Matrix m) {
        matrix = new float[m.rows][m.columns];
        this.rows = m.rows;
        this.columns = m.columns;

        for (int i = 0; i < m.rows; i++) {
            for (int j = 0; j < m.columns; j++) {
                matrix[i][j] = m.matrix[i][j];
            }
        }
    }

    public static Matrix matrixMultiply(Matrix m1, Matrix m2) {
        if (m1.columns != m2.rows) {
            return new Matrix(1,1);
        }

        Matrix result = new Matrix(m1.rows, m2.columns);

        for (int r = 0; r < m1.rows; r++) {
            for (int c = 0; c < m2.columns; c++) {
                float sum = 0;
                for (int n = 0; n < m1.columns; n++) {
                    sum += m1.matrix[r][n] * m2.matrix[n][c];
                }
                result.matrix[r][c] = sum;
            }
        }
        return result;
    }

    public static Vec3D vectorMultiply(Vec3D v, Matrix m) {
        if (m.rows != 4 || m.columns != 4) {
            return new Vec3D(0,0,0);
        }

        float x = v.X * m.matrix[0][0] + v.Y * m.matrix[1][0] + v.Z * m.matrix[2][0] + v.W * m.matrix[3][0];
        float y = v.X * m.matrix[0][1] + v.Y * m.matrix[1][1] + v.Z * m.matrix[2][1] + v.W * m.matrix[3][1];
        float z = v.X * m.matrix[0][2] + v.Y * m.matrix[1][2] + v.Z * m.matrix[2][2] + v.W * m.matrix[3][2];
        float w = v.X * m.matrix[0][3] + v.Y * m.matrix[1][3] + v.Z * m.matrix[2][3] + v.W * m.matrix[3][3];
        return new Vec3D(x,y,z,w);
    }
}
