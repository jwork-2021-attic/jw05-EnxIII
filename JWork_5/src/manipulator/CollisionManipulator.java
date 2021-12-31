package JWork_5.src.manipulator;

import JWork_5.main.GameZ;
import JWork_5.main.Main;
import JWork_5.src.Interactive;
import JWork_5.src.creature.Creature;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Vector;

public class CollisionManipulator implements BasicManipulator{
    protected boolean available = true;
    protected Creature ce;
    protected Vector<Interactive> collisionObject;

    public CollisionManipulator(Creature ce){
        collisionObject = new Vector<>();
        this.setCreature(ce);
    }

    @Override
    public void Disable() {
        available = false;
    }

    @Override
    public void setCreature(Creature ce) {
        this.ce = ce;
        this.ce.setManipulator(this, Main.manipulator.Collision);
    }

    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        if (available) {
            for (var i : GameZ.gm.mapCollisionQuery(ce))
                if (i.getObject().getBulk().intersects(ce.getBulk()))
                    collisionObject.add(i.getObject());
            for (var i : GameZ.gm.avatarCollisionQuery(ce))
                if (i.getObject().getBulk().intersects(ce.getBulk()))
                    collisionObject.add(i.getObject());
            if (!collisionObject.isEmpty()) {
                float dirX = 0, dirY = 0, velocity = ce.getSpeed() / Main.framesPerSec, acceleration = 3.0f / 30;
                for (int i = 0; i < collisionObject.size(); ++i) {
                    var obj = collisionObject.get(i);
                    var x = ce.getLocationX() - obj.getLocationX();
                    var y = ce.getLocationY() - obj.getLocationY();
                    var l = (float) Math.sqrt(x * x + y * y);
                    dirX += x / l;
                    dirY += y / l;
                }
                collisionObject.clear();
                var mani = (SetBackManipulator) ce.getManipulator(Main.manipulator.SetBack);
                mani.setSetBack(dirX, dirY, velocity / 10, acceleration);
            }
        }
        else{
            if (!collisionObject.isEmpty())
                collisionObject.clear();
            available = true;
        }
    }
}
