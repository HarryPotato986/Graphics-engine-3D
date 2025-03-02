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

    public void draw(Graphics g, ProjectionMatrix matProj, float angleDegX, float angleDegZ) {
        float fScreenWidth = (float) GamePanel.SCREEN_WIDTH;
        float fScreenHeight = (float) GamePanel.SCREEN_HEIGHT;

        if (angleDegX > 360) {
            angleDegX -= 360;
        }
        if (angleDegZ > 360) {
            angleDegZ -= 360;
        }

        for (Triangle tri : mesh) {
            Triangle triRotZ = new Triangle(tri);
            triRotZ.rotate(angleDegZ, RotationAxis.Z);

            Triangle triRotZX = new Triangle(triRotZ);
            triRotZX.rotate(angleDegX, RotationAxis.X);
            triRotZX.translate(0.0f, 0.0f, 3.0f);

            //if (triRotZX.normal.Z < 0) {
            if(triRotZX.normal.X * (triRotZX.points[0].X - GamePanel.TEMP_CAMERA.X) +
               triRotZX.normal.Y * (triRotZX.points[0].Y - GamePanel.TEMP_CAMERA.Y) +
               triRotZX.normal.Z * (triRotZX.points[0].Z - GamePanel.TEMP_CAMERA.Z) < 0.0f) {

                //Light
                Vec3D lightDirection = new Vec3D(0.0f, 0.0f, -1.0f);
                float l = (float) Math.sqrt(lightDirection.X * lightDirection.X + lightDirection.Y * lightDirection.Y + lightDirection.Z * lightDirection.Z);
                lightDirection.X /= l; lightDirection.Y /= l; lightDirection.Z /= l;

                triRotZX.updateLighting(lightDirection);

                Triangle triProj = new Triangle(
                        matProj.projMultiply(triRotZX.points[0]),
                        matProj.projMultiply(triRotZX.points[1]),
                        matProj.projMultiply(triRotZX.points[2]));
                triProj.lum = triRotZX.lum;

                //Scale into view
                triProj.points[0].X += 1.0f;
                triProj.points[0].Y += 1.0f;
                triProj.points[1].X += 1.0f;
                triProj.points[1].Y += 1.0f;
                triProj.points[2].X += 1.0f;
                triProj.points[2].Y += 1.0f;

                triProj.points[0].X *= 0.5f * fScreenWidth;
                triProj.points[0].Y *= 0.5f * fScreenHeight;
                triProj.points[1].X *= 0.5f * fScreenWidth;
                triProj.points[1].Y *= 0.5f * fScreenHeight;
                triProj.points[2].X *= 0.5f * fScreenWidth;
                triProj.points[2].Y *= 0.5f * fScreenHeight;


                g.setColor(new Color(triProj.lum, 0.0f, 0.0f));
                int[] Xs = new int[]{(int) triProj.points[0].X, (int) triProj.points[1].X, (int) triProj.points[2].X};
                int[] Ys = new int[]{(int) triProj.points[0].Y, (int) triProj.points[1].Y, (int) triProj.points[2].Y};
                g.fillPolygon(Xs, Ys, 3);

                /*
                g.setColor(Color.black);
                g.drawLine((int) triProj.points[0].X, (int) triProj.points[0].Y, (int) triProj.points[1].X, (int) triProj.points[1].Y);
                g.drawLine((int) triProj.points[1].X, (int) triProj.points[1].Y, (int) triProj.points[2].X, (int) triProj.points[2].Y);
                g.drawLine((int) triProj.points[2].X, (int) triProj.points[2].Y, (int) triProj.points[0].X, (int) triProj.points[0].Y);
                */
            }
        }
    }
}
