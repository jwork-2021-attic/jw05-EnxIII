package JWork_5.src.equippment.weapon;

import JWork_5.main.Main;
import JWork_5.src.manager.GeneralManager;
import JWork_5.src.stateAlter.damage.bareHand.ZombieHandAttack;
import JWork_5.src.stateAlter.damage.bullet.Bullet;

public class ZombieHand extends Weapon{
    public static int ZombieHandInterval = 300;
    public int damage = 20;
    public int decline = 100;
    public int penetrate = 1;

    public ZombieHand(Main.item ID) {
        super(ID);
    }

    @Override
    public void genDamage(float sx, float sy, float ex, float ey, GeneralManager gm) {
        gm.addDamage(new ZombieHandAttack(sx, sy, ex, ey, damage, 10));
    }

}
