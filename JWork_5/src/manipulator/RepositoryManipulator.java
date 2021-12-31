package JWork_5.src.manipulator;

import JWork_5.main.Main;
import JWork_5.src.creature.Creature;
import JWork_5.src.equippment.weapon.Weapon;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class RepositoryManipulator implements BasicManipulator{
    protected boolean available = true;
    protected boolean pickUp = false;
    protected Creature ce;

    public RepositoryManipulator(Creature ce){
        this.setCreature(ce);
    }

    public boolean isPickUp(){ return this.pickUp; }
    public void setWeapon(Weapon wp){
        this.pickUp = false;

    }
    public void setItem(){

    }

    @Override
    public void Disable() {
        available = false;
    }

    @Override
    public void setCreature(Creature ce) {
        this.ce = ce;
        this.ce.setManipulator(this, Main.manipulator.Repository);
    }

    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        if (available){
            pickUp = gc.getInput().isKeyDown(Input.KEY_F);
        }
        else{
            available = true;
        }
    }
}
