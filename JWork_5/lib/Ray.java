package JWork_5.lib;

import JWork_5.lib.collisiondetection.DBT.AABB;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;

public class Ray extends Line{

    static Rectangle rect = new Rectangle(0, 0, 0, 0);

    public Ray(float sx, float sy, float ex, float ey){
        super(sx, sy, ex, ey);
    }

    public boolean overLaps(AABB aabb){
        rect.setBounds(aabb.getStartX(), aabb.getStartY(), aabb.getWidth(), aabb.getHeight());
        return this.intersects(rect) || rect.contains(getCenterX(), getCenterY());
    }
}
