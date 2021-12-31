package JWork_5.src.stateAlter.damage.bullet;

import JWork_5.main.Main;
import JWork_5.src.creature.Creature;
import JWork_5.src.manipulator.SetBackManipulator;
import JWork_5.src.stateAlter.Effect;

public class BulletDamage implements Effect {
    private float cx, cy, penetrateFactor;
    private int used, damage;

    public BulletDamage(float cx, float cy, int damage, int used, float penetrateFactor){
        this.cx = cx;
        this.cy = cy;
        this.damage = damage;
        this.used = used;
        this.penetrateFactor = penetrateFactor;
    }

    public void isUsed(){
        used -= 1;
    }
    public boolean usedUp(){
        return used <= 0;
    }
    // visitor
    public void accept(Creature ce){
        float armor = ce.getArmor();
        float hp = ce.getHp();
        if (armor > 0) {
            ce.setArmor(armor - penetrateFactor * damage);
            ce.setHp(hp - (1 - penetrateFactor) * damage);
        } else {
            ce.setHp(hp - damage);
        }
        ((SetBackManipulator) ce.getManipulator(Main.manipulator.SetBack)).setSetBack(
                ce.getLocationX() - cx, ce.getLocationY() - cy,
                180f / Main.framesPerSec, 3f/200
        );
    }

}
