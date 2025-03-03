public class IdentityMatrix extends SquareMatrix{

    IdentityMatrix(int size) {
        super(size);

        for (int i = 0; i < size; i++) {
            matrix[i][i] = 1.0f;
        }
    }
}
