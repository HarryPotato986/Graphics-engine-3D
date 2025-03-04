public class TranslationMatrix extends SquareMatrix{

    TranslationMatrix(float x, float y, float z) {
        super(4);

        matrix[0][0] = 1.0f;
        matrix[1][1] = 1.0f;
        matrix[2][2] = 1.0f;
        matrix[3][3] = 1.0f;
        matrix[3][0] = x;
        matrix[3][1] = y;
        matrix[3][2] = z;
    }
}
