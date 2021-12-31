package JWork_5.src.manipulator;

import JWork_5.lib.animation.Animation;
import JWork_5.lib.animation.PlayerAnimation;
import JWork_5.main.GameZ;
import JWork_5.main.Main;
import JWork_5.src.creature.Creature;
import JWork_5.src.creature.Player;
import JWork_5.src.equippment.weapon.Weapon;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

public class AttackManipulator_Player implements BasicManipulator {
    protected boolean available = true;
    protected int delay = 0;
    protected Player player;
    protected Weapon weapon;
    protected PlayerAnimation pAttack = new PlayerAnimation();

    public AttackManipulator_Player(Player pl){ this.setCreature(pl);}

    public void setWeapon(Weapon wp){
        this.weapon = wp;
        //this.handGunAni.setAnimation(Animation.playerHandGunShoot,
        //        wp.getAttackInterval() / (Animation.playerHandGunShoot.length - 1));
        this.pAttack.setNewAnimation(wp.getID(), Main.Action.Attack);

    }

    @Override
    public void Disable() {
        available = false;
    }

    @Override
    public void setCreature(Creature ce) {
        if (ce instanceof Player){
            this.player = (Player) ce;
            this.player.setManipulator(this, Main.manipulator.Attack);
        }
    }

    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        if (this.delay > 0) {
            this.delay -= delay;
            player.setImage(pAttack.execute(delay));
            if (this.delay <= 0) this.delay = 0;
        }
        if (this.available){
            if (this.weapon != null && gc.getInput().isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
                if (this.delay <= 0) {
                    float dirX = gc.getInput().getMouseX() - player.getLocationX();
                    float dirY = gc.getInput().getMouseY() - player.getLocationY();
                    if (dirX == 0 && dirY == 0)
                        dirY = 1;
                    float l = (float) Math.sqrt(dirX * dirX + dirY * dirY);
                    dirX /= l; dirY /= l;
                    weapon.genDamage(player.getLocationX() + (GameZ.playerRadius + 3) * dirX,
                                    player.getLocationY() + (GameZ.playerRadius + 3) * dirY,
                                        dirX, dirY, GameZ.gm);
                    this.delay = weapon.getAttackInterval();
                    player.setImage(pAttack.activate());
                }
            }
        }
        else {
            this.available = true;
        }
    }

}
