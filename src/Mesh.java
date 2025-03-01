import java.awt.*;
import java.util.ArrayList;

public class Mesh {
    public ArrayList<Triangle> mesh;

    Mesh(ArrayList<Triangle> triangles) {
        mesh = triangles;
    }

    public void draw(Graphics g, ProjectionMatrix matProj, float angleDegX, float angleDegZ) {
        float fScreenWidth = (float) GamePanel.SCREEN_WIDTH;
        float fScreenHeight = (float) GamePanel.SCREEN_HEIGHT;

        for (Triangle tri : mesh) {
            Triangle triRotZ = new Triangle(tri);
            triRotZ.rotate(angleDegZ, RotationAxis.Z);

            Triangle triRotZX = new Triangle(triRotZ);
            triRotZX.rotate(angleDegX, RotationAxis.X);
            triRotZX.translate(0.0f,0.0f,3.0f);

            Triangle triProj = new Triangle(
                    matProj.projMultiply(triRotZX.points[0]),
                    matProj.projMultiply(triRotZX.points[1]),
                    matProj.projMultiply(triRotZX.points[2]));

            //Scale into view
            triProj.points[0].X += 1.0f; triProj.points[0].Y += 1.0f;
            triProj.points[1].X += 1.0f; triProj.points[1].Y += 1.0f;
            triProj.points[2].X += 1.0f; triProj.points[2].Y += 1.0f;

            triProj.points[0].X *= 0.5f * fScreenWidth;
            triProj.points[0].Y *= 0.5f * fScreenHeight;
            triProj.points[1].X *= 0.5f * fScreenWidth;
            triProj.points[1].Y *= 0.5f * fScreenHeight;
            triProj.points[2].X *= 0.5f * fScreenWidth;
            triProj.points[2].Y *= 0.5f * fScreenHeight;

            g.setColor(Color.black);
            g.drawLine((int) triProj.points[0].X, (int) triProj.points[0].Y, (int) triProj.points[1].X, (int) triProj.points[1].Y);
            g.drawLine((int) triProj.points[1].X, (int) triProj.points[1].Y, (int) triProj.points[2].X, (int) triProj.points[2].Y);
            g.drawLine((int) triProj.points[2].X, (int) triProj.points[2].Y, (int) triProj.points[0].X, (int) triProj.points[0].Y);
        }

    }
}
