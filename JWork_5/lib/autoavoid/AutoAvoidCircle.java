package JWork_5.lib.autoavoid;

import JWork_5.lib.collisiondetection.DBT.AABB;
import JWork_5.lib.collisiondetection.DBT.IAABB;
import JWork_5.src.Interactive;
import JWork_5.src.creature.Creature;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;


public class AutoAvoidCircle implements IAABB {
    protected Circle outer;
    protected Creature ce;
    protected float extra;

    public AutoAvoidCircle(Creature ce, float extraRadius) {
        extra = extraRadius;
        this.ce = ce;
        outer = new Circle(0, 0, ce.getBulk().getBoundingCircleRadius() + extraRadius);
    }

    @Override
    public void getAABB(AABB result) {
        ce.getAABB(result);
        result.setValue(
                result.getStartX() - extra, result.getStartY() - extra,
                result.getStartX() + result.getWidth() + extra,
                result.getStartY() + result.getHeight() + extra
        );
    }

    @Override
    public Interactive getObject() {
        return null;
    }

    public boolean intersects(Shape another){
        outer.setCenterX(ce.getLocationX());
        outer.setCenterY(ce.getLocationY());
        return outer.intersects(another);
    }

}
