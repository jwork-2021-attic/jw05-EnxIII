package JWork_5.src.stateAlter.damage;

import JWork_5.lib.collisiondetection.DBT.IAABB;
import JWork_5.src.Interactive;
import JWork_5.src.creature.Creature;
import JWork_5.src.stateAlter.Effect;
import JWork_5.src.stateAlter.StateAlter;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Shape;

public interface Damage extends StateAlter {

    // whether it has collision bulk
    public boolean hasCollision();
    public boolean canPenetrate();
    public boolean contacts(Shape sp);
    public boolean outOfWorld(int width, int height);
    public Shape getBulk();
    public void genDamage(Interactive target, float cx, float cy);

    public void update();
    public void draw(Graphics g);

    @Override
    default public void accept(Creature ce){}
}
