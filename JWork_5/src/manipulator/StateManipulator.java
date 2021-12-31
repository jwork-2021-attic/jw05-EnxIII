package JWork_5.src.manipulator;

import JWork_5.main.Main;
import JWork_5.src.creature.Creature;
import JWork_5.src.stateAlter.Effect;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

import java.util.LinkedList;

public class StateManipulator implements BasicManipulator{
    protected boolean available = true;
    protected Creature ce;
    // thread unsafe, but efficient in insertion and delete
    protected LinkedList<Effect> stateObject;
    protected LinkedList<Effect> incomingList;

    public StateManipulator(Creature ce){
        this.setCreature(ce);
        stateObject = new LinkedList<>();
        incomingList = new LinkedList<>();
    }

    @Override
    public void Disable() {
        available = false;
    }

    @Override
    public void setCreature(Creature ce) {
        this.ce = ce;
        this.ce.setManipulator(this, Main.manipulator.State);
    }

    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        if (available && !stateObject.isEmpty()){
            // TODO: add synchronizing lock, contention with 'addState' method
            stateObject.forEach(effect -> {
                effect.accept(ce);
                effect.isUsed();
                if (!effect.usedUp())
                    this.addState(effect);
            });
            stateObject.clear();
            if (!ce.isAlive()) {
                //if (ce instanceof Player)
                //    GameZ.gm.removePlayer((Player) ce);
                //else if (ce instanceof Monster)
                //    GameZ.gm.removeMonster((Monster) ce);
            }
        }
        else {
            available = true;
            if (!stateObject.isEmpty())
                stateObject.clear();
        }
    }

    @Override
    public void flip(){
        var temp = stateObject;
        stateObject = incomingList;
        incomingList = temp;
        incomingList.clear();
    }

    public void addState(Effect effect){
        incomingList.add(effect);
    }

}
