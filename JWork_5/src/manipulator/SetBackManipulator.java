package JWork_5.src.manipulator;

import JWork_5.main.Main;
import JWork_5.src.creature.Creature;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class SetBackManipulator implements BasicManipulator{
    protected boolean available = true;
    protected Creature ce;
    protected float x = 0, y = 0;
    // TODO: speed should be pixels per sec
    protected float speed = 0, decline = 0;

    public SetBackManipulator(Creature ce){
        this.setCreature(ce);
    }

    @Override
    public void Disable() {
        available = false;
    }

    @Override
    public void setCreature(Creature ce) {
        this.ce = ce;
        ce.setManipulator(this, Main.manipulator.SetBack);
    }

    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        if (available && speed != 0){
            var move = ce.getManipulator(Main.manipulator.Move);
            if (move != null)
                move.Disable();
            float lx = ce.getLocationX(), ly = ce.getLocationY();
            lx += x * speed;
            ly += y * speed;
            ce.setLocation(lx, ly);
            speed -= decline;
            if (speed < 0)
                speed = 0;
        }
        else {
            available = true;
        }
    }

    public void setSetBack(float dirX, float dirY, float speed, float decline){
        if (dirX == 0 && dirY == 0)
            dirX = 1;
        var l = (float)Math.sqrt(dirX * dirX + dirY * dirY);
        x = dirX / l;
        y = dirY / l;
        this.speed = speed;
        this.decline = decline;
    }

    public float getDirX(){ return x; }
    public float getDirY(){ return y; }
    public float getSpeed(){ return speed; }
    public float getDecline(){ return decline;}

}
