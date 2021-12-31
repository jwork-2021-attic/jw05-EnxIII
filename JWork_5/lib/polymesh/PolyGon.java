package JWork_5.lib.polymesh;

import org.lwjgl.Sys;

import java.util.Vector;

public class PolyGon {
    public int ID;
    public int sx, sy;
    public int width, height;
    public Vector<fNode> points;

    public PolyGon(int ID, int x, int y, int w, int h){
        this.ID = ID;
        sx = x;
        sy = y;
        width = w;
        height = h;
        points = new Vector<>();
    }

    public float getCentreX(){ return sx + width / 2f; }
    public float getCentreY(){ return sy + height / 2f; }
    public float distanceSquare(float dx, float dy){
        float x = getCentreX() - dx;
        float y = getCentreY() - dy;
        return x * x + y * y;
    }

    public boolean contains(float x, float y){
        return sx <= x && x <= sx + width && sy <= y && y <= sy + height;
    }

    public void debugPrint(){
        for (var p : points) {
            System.out.print(p.x);
            System.out.print(' ');
            System.out.println(p.y);
        }
    }

}
