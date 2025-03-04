import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class Mesh {
    public ArrayList<Triangle> mesh;

    Mesh(ArrayList<Triangle> triangles) {
        mesh = triangles;
    }

    Mesh(String objFilePath) {
        mesh = new ArrayList<>();
        loadMeshFromFile(objFilePath);
    }

    private void loadMeshFromFile(String objFilePath) {
        ArrayList<Vec3D> verts = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(objFilePath))) {

            String line;
            String name = "Name not found :(";
            while ((line = reader.readLine()) != null){
                String[] chunks = line.split(" ");
                //System.out.println(chunks[0]);

                if (Objects.equals(chunks[0], "v")) {
                    //System.out.println("WE HAVE A V!!!");
                    verts.add(new Vec3D(Float.parseFloat(chunks[1]), Float.parseFloat(chunks[2]), Float.parseFloat(chunks[3])));
                } else if (Objects.equals(chunks[0], "f")) {
                    mesh.add(new Triangle(verts.get(Integer.parseInt(chunks[1]) - 1),
                                          verts.get(Integer.parseInt(chunks[2]) - 1),
                                          verts.get(Integer.parseInt(chunks[3]) - 1)));
                } else if (Objects.equals(chunks[0], "o")) {
                    name = chunks[1];
                }
            }
            System.out.println(name);
        }
        catch(FileNotFoundException e) {
            System.out.println("obj file not found!");
        }
        catch(IOException e) {
            System.out.println("Something went wrong");
        }
        System.out.println(mesh.size());
    }

    public ArrayList<Triangle> prepareTrisToDraw(Graphics g, ProjectionMatrix matProj, PointAtMatrix matView, Vec3D vCamera, float angleDegX, float angleDegZ, Color baseColor) {
        if (angleDegX > 360) {
            angleDegX -= 360;
        }
        if (angleDegZ > 360) {
            angleDegZ -= 360;
        }

        float fScreenWidth = (float) GamePanel.SCREEN_WIDTH;
        float fScreenHeight = (float) GamePanel.SCREEN_HEIGHT;
        RotationMatrix matRotX = new RotationMatrix(angleDegX, RotationAxis.X);
        RotationMatrix matRotZ = new RotationMatrix(angleDegZ, RotationAxis.Z);
        TranslationMatrix matTrans = new TranslationMatrix(0.0f, 0.0f, 3.0f);
        IdentityMatrix matI = new IdentityMatrix(4);

        Matrix matWorld;
        matWorld = new Matrix(Matrix.matrixMultiply(matRotZ, matRotX));
        matWorld = new Matrix(Matrix.matrixMultiply(matWorld, matTrans));


        ArrayList<Triangle> trisToDraw = new ArrayList<>();


        for (Triangle tri : mesh) {

            Triangle triTransformed = new Triangle(Matrix.vectorMultiply(tri.points[0], matWorld),
                                                   Matrix.vectorMultiply(tri.points[1], matWorld),
                                                   Matrix.vectorMultiply(tri.points[2], matWorld));

            Vec3D vCameraRay = Vec3D.subtract(triTransformed.points[0], vCamera);

            if(Vec3D.dotProduct(triTransformed.normal, vCameraRay) < 0.0f) {

                //Light
                Vec3D lightDirection = new Vec3D(-1.0f, 0.0f, -1.0f);
                lightDirection.normalize();

                triTransformed.updateLighting(lightDirection);
                //System.out.println(triTransformed.lum);

                Triangle triViewed = new Triangle(
                        Matrix.vectorMultiply(triTransformed.points[0], matView),
                        Matrix.vectorMultiply(triTransformed.points[1], matView),
                        Matrix.vectorMultiply(triTransformed.points[2], matView));
                triViewed.lum = triTransformed.lum;

                int nClippedTriangles = 0;
                Triangle[] clipped = new Triangle[]{new Triangle(triViewed), new Triangle(triViewed)};
                nClippedTriangles = Triangle.clipAgainstPlane(new Vec3D(0.0f, 0.0f, 0.1f), new Vec3D(0.0f, 0.0f, 1.0f), triViewed, clipped[0], clipped[1]);

                for (int n = 0; n < nClippedTriangles; n++) {


                    Triangle triProjected = new Triangle(
                            Matrix.vectorMultiply(clipped[n].points[0], matProj),
                            Matrix.vectorMultiply(clipped[n].points[1], matProj),
                            Matrix.vectorMultiply(clipped[n].points[2], matProj));
                    triProjected.lum = clipped[n].lum;
                    //triProjected.updateColor(baseColor);
                    triProjected.color = clipped[n].color;

                    triProjected.points[0] = Vec3D.divide(triProjected.points[0], triProjected.points[0].W);
                    triProjected.points[1] = Vec3D.divide(triProjected.points[1], triProjected.points[1].W);
                    triProjected.points[2] = Vec3D.divide(triProjected.points[2], triProjected.points[2].W);

                    triProjected.points[0].X *= -1.0f;
                    triProjected.points[1].X *= -1.0f;
                    triProjected.points[2].X *= -1.0f;
                    triProjected.points[0].Y *= -1.0f;
                    triProjected.points[1].Y *= -1.0f;
                    triProjected.points[2].Y *= -1.0f;

                    //Scale into view
                    Vec3D vOffsetView = new Vec3D(1.0f, 1.0f, 0.0f);
                    triProjected.points[0] = Vec3D.add(triProjected.points[0], vOffsetView);
                    triProjected.points[1] = Vec3D.add(triProjected.points[1], vOffsetView);
                    triProjected.points[2] = Vec3D.add(triProjected.points[2], vOffsetView);

                    triProjected.points[0].X *= 0.5f * fScreenWidth;
                    triProjected.points[0].Y *= 0.5f * fScreenHeight;
                    triProjected.points[1].X *= 0.5f * fScreenWidth;
                    triProjected.points[1].Y *= 0.5f * fScreenHeight;
                    triProjected.points[2].X *= 0.5f * fScreenWidth;
                    triProjected.points[2].Y *= 0.5f * fScreenHeight;


                    trisToDraw.add(triProjected);


                    /*
                    g.setColor(triProjected.color);
                    int[] Xs = new int[]{(int) triProjected.points[0].X, (int) triProjected.points[1].X, (int) triProjected.points[2].X};
                    int[] Ys = new int[]{(int) triProjected.points[0].Y, (int) triProjected.points[1].Y, (int) triProjected.points[2].Y};
                    g.fillPolygon(Xs, Ys, 3);
                    */
                    /*
                    g.setColor(Color.black);
                    g.drawLine((int) triProjected.points[0].X, (int) triProjected.points[0].Y, (int) triProjected.points[1].X, (int) triProjected.points[1].Y);
                    g.drawLine((int) triProjected.points[1].X, (int) triProjected.points[1].Y, (int) triProjected.points[2].X, (int) triProjected.points[2].Y);
                    g.drawLine((int) triProjected.points[2].X, (int) triProjected.points[2].Y, (int) triProjected.points[0].X, (int) triProjected.points[0].Y);
                    */
                }
            }
        }
        return trisToDraw;
    }
}
