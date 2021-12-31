package JWork_5.src.stateAlter;

import JWork_5.lib.collisiondetection.DBT.IAABB;
import JWork_5.src.Item;
import JWork_5.src.creature.Creature;

public interface StateAlter extends IAABB{

    public void isUsed(int times);
    public boolean usedUp();
    // side effect of using weapon
    public void accept(Creature ce);
}
