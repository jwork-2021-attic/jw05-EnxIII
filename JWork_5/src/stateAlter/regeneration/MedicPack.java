package JWork_5.src.stateAlter.regeneration;

import JWork_5.lib.collisiondetection.DBT.AABB;
import JWork_5.src.Interactive;
import JWork_5.src.creature.Creature;
import JWork_5.src.stateAlter.StateAlter;

public class MedicPack implements StateAlter {


    @Override
    public void getAABB(AABB result) {

    }

    @Override
    public Interactive getObject() {
        return null;
    }

    @Override
    public void isUsed(int times) {

    }

    @Override
    public boolean usedUp() {
        return false;
    }

    @Override
    public void accept(Creature ce) {

    }
}
