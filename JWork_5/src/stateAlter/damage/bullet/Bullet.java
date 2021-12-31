package JWork_5.src.stateAlter.damage.bullet;

import JWork_5.lib.Ray;
import JWork_5.lib.collisiondetection.DBT.AABB;
import JWork_5.main.Main;
import JWork_5.src.Interactive;
import JWork_5.src.stateAlter.damage.Damage;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

import java.util.concurrent.atomic.AtomicInteger;

public class Bullet extends Ray implements Damage {
    protected AtomicInteger used, damage;
    protected Interactive target;
    protected boolean engage = false;
    protected float speed, penetrateFactor = 0.7f;
    protected int decline;

    public Bullet(float sx, float sy, float dirX, float dirY,
                  int damage, int declinePerPenetrate, int penetrate, float speed){
        super(sx, sy, sx + dirX * 3, sy + dirY * 3);
        this.speed = speed;
        this.decline = declinePerPenetrate;
        this.used = new AtomicInteger(penetrate);
        this.damage = new AtomicInteger(damage);
    }

    @Override
    public void getAABB(AABB result) {
        result.setValue(
                this.getMinX(), this.getMaxX(),
                this.getMinY(), this.getMaxY()
        );
    }

    public void setDamage(int damage) { this.damage.set(damage); }
    public void setSpeed(float speed){ this.speed = speed; }
    public void setUsed(int times) { this.used.set(times); }
    public void setDirection(float dirX, float dirY){ this.set(getX1(), getY1(), getX1() + dirX, getY1() + dirY);}

    @Override
    public void isUsed(int times){ used.addAndGet(-times); damage.addAndGet(-decline * times); }

    @Override
    public boolean usedUp(){ return used.get() <= 0;}

    @Override
    public Interactive getObject() {
        return null;
    }

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public boolean canPenetrate() { return false; }

    @Override
    public boolean contacts(Shape sp) { return this.intersects(sp);}

    @Override
    public boolean outOfWorld(int width, int height) {
        float sx = getX1(), sy = getY1();
        float ex = getX2(), ey = getY2();
        return ((sx < 0 && ex < 0) || (sx > width && ex > width) || (sy < 0 && ey < 0) || (sy > height && ey > height));
    }

    @Override
    public void genDamage(Interactive target, float cx, float cy) {
        this.engage = true;
        if (this.target == null || this.target != target) {
            this.target = target;
            this.isUsed(target.penetrateLevel());
            target.isAttacked(new BulletDamage(cx, cy, this.damage.get(), 1, penetrateFactor));
        }
    }

    @Override
    public Shape getBulk() {
        return this;
    }

    @Override
    public void update() {
        float x2 = getX2(), y2 = getY2();
        float factor = this.speed / (length() * Main.framesPerSec);
        this.set(x2, y2, x2 + (x2 - getX1()) * factor, y2 + (y2 - getY1()) * factor);

        if (!this.engage)
            this.target = null;
        else this.engage = false;
    }

    @Override
    public void draw(Graphics g) {
        var color = g.getColor();
        g.setColor(Color.black);
        g.drawLine(this.getX1(), this.getY1(), this.getX2(), this.getY2());
        g.setColor(color);
    }

}
