package JWork_5.src.manipulator;

import JWork_5.lib.animation.Animation;
import JWork_5.lib.animation.PlayerAnimation;
import JWork_5.lib.animation.ZombieAnimation;
import JWork_5.main.GameZ;
import JWork_5.main.Main;
import JWork_5.src.creature.Creature;
import JWork_5.src.creature.Player;
import JWork_5.src.equippment.weapon.Weapon;
import JWork_5.src.equippment.weapon.ZombieHand;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class AttackManipulator_Robort implements BasicManipulator{
    protected boolean available = true;
    protected float tX, tY;
    protected int delay = 0;
    protected Creature ce;
    protected Weapon weapon;
    protected Animation pAttack = new Animation();

    public AttackManipulator_Robort(Creature ce){ this.setCreature(ce);}

    public void setWeapon(){
        this.weapon = new ZombieHand(Main.item.BareHand);
        this.pAttack.setAnimation(Animation.ZombieHandAttack, weapon.getAttackInterval() / Animation.ZombieHandAttack.length - 1);
    }

    @Override
    public void Disable() {
        available = false;
    }

    @Override
    public void setCreature(Creature ce) {
        this.ce = ce;
        this.ce.setManipulator(this, Main.manipulator.Attack);
    }

    public void setTarget(float x, float y){
        this.available = true;
        this.tX = x;
        this.tY = y;
    }

    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        if (this.delay > 0) {
            this.delay -= delay;
            ce.setImage(pAttack.execute(delay));
            if (this.delay <= 0) this.delay = 0;
        }
        if (this.available){
            this.available = false;
            if (this.delay <= 0) {
                float dirX = tX - ce.getLocationX();
                float dirY = tY - ce.getLocationY();
                if (dirX == 0 && dirY == 0)
                    dirY = 1;
                float l = (float) Math.sqrt(dirX * dirX + dirY * dirY);
                dirX /= l;
                dirY /= l;
                weapon.genDamage(ce.getLocationX() + 7 * dirX, ce.getLocationY() + 7 * dirY,
                        dirX, dirY, GameZ.gm);
                this.delay = weapon.getAttackInterval();
                ce.setImage(pAttack.activate());
            }
        }
    }
}
