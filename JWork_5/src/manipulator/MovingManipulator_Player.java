package JWork_5.src.manipulator;

import JWork_5.lib.collisiondetection.DBT.IAABB;
import JWork_5.main.Main;
import JWork_5.src.creature.Creature;
import JWork_5.src.creature.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Vector;

public class MovingManipulator_Player implements BasicManipulator {
    private boolean available = true;
    private Player ce;

    public MovingManipulator_Player(Player pl){
        this.setCreature(pl);
    }

    @Override
    public void Disable() {
        available = false;
    }

    @Override
    public void setCreature(Creature ce) {
        if (ce instanceof Player) {
            this.ce = (Player) ce;
            this.ce.setManipulator(this, Main.manipulator.Move);
        }
    }

    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        var input = gc.getInput();
        var vecX = input.getMouseX() - ce.getLocationX();
        var vecY = input.getMouseY() - ce.getLocationY();
        ce.setRotation(vecX, vecY);
        if (available){

            float baseX = 0, baseY = 0;
            if (input.isKeyDown(Input.KEY_W))
                baseY += -1;
            if (input.isKeyDown(Input.KEY_D))
                baseX += 1;
            if (input.isKeyDown(Input.KEY_A))
                baseX += -1;
            if (input.isKeyDown(Input.KEY_S))
                baseY += 1;
            if (baseX != 0 && baseY != 0){
                baseX *= 0.707107f;
                baseY *= 0.707107f;
            }

            if (baseX != 0) {
                var x = ce.getLocationX() + baseX * ce.getSpeed() / Main.framesPerSec;
                ce.setLocationX(x);
            }
            if (baseY != 0){
                var y = ce.getLocationY() + baseY * ce.getSpeed() / Main.framesPerSec;
                ce.setLocationY(y);
            }
        }
        else {
            available = true;
        }
    }

    public void dealCollision(Vector<IAABB> collision){

    }

}
