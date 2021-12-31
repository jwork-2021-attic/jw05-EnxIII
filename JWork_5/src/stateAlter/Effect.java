package JWork_5.src.stateAlter;

import JWork_5.src.creature.Creature;

public interface Effect {
    public void isUsed();
    public boolean usedUp();
    // visitor
    public void accept(Creature ce);
}
