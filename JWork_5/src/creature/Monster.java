package JWork_5.src.creature;

import JWork_5.lib.collisiondetection.DBT.AABB;
import org.newdawn.slick.geom.Circle;

public abstract class Monster extends Creature{
    public Monster(Circle bulk){
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
