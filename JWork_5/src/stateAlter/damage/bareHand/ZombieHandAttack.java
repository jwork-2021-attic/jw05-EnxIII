package JWork_5.src.stateAlter.damage.bareHand;

import JWork_5.lib.collisiondetection.DBT.AABB;
import JWork_5.src.Interactive;
import JWork_5.src.stateAlter.damage.Damage;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import java.util.concurrent.atomic.AtomicInteger;

public class ZombieHandAttack extends Circle implements Damage {
    float sx, sy;

    public ZombieHandAttack(float sx, float sy, float cX, float cY, int damage, float radius) {
        super(cX, cY, radius);
        this.sx = sx;
        this.sy = sy;
    }

    @Override
    public void getAABB(AABB result) {

    }
    @Override
    public Interactive getObject() {return null;}
    @Override
    public void isUsed(int times) {}

    @Override
    public boolean usedUp() {
        return false;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean canPenetrate() {
        return false;
    }

    @Override
    public boolean contacts(Shape sp) {
        return false;
    }

    @Override
    public boolean outOfWorld(int width, int height) {
        return false;
    }

    @Override
    public Shape getBulk() {
        return null;
    }

    @Override
    public void genDamage(Interactive target, float cx, float cy) {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics g) {

    }
}
