package JWork_5.src.manipulator;

import JWork_5.src.creature.Creature;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public interface BasicManipulator {

    public void Disable();

    public void setCreature(Creature ce);

    // ready for the next loop
    default public void flip(){}

    public void accept(GameContainer gc, StateBasedGame sbg, int delay);

}
