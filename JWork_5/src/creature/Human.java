package JWork_5.src.creature;

import org.newdawn.slick.geom.Circle;
import JWork_5.lib.collisiondetection.DBT.AABB;

public abstract class Human extends Creature{

    public Human(Circle bulk){
        super(bulk);
    }

    @Override
    public void getAABB(AABB result) {
        float x = bulk.getCenterX();
        float y = bulk.getCenterY();
        result.setValue(x - bulk.radius, y - bulk.radius,
                x + bulk.radius, y + bulk.radius);
    }
}
