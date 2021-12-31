package JWork_5.src.equippment.weapon;

import JWork_5.main.Main;
import JWork_5.src.manager.GeneralManager;
import JWork_5.src.stateAlter.damage.bullet.Bullet;

public class Pistol extends Weapon{
    public static int PistolInterval = 300;
    public int damage = 100;
    public int decline = 4;
    public int penetrate = 2;

    public Pistol(){
        super(Main.item.Pistol);
        this.setAttackInterval(PistolInterval);
        this.setBulletSpeed(1000);
    }

    @Override
    public void genDamage(float sx, float sy, float ex, float ey, GeneralManager gm) {
        gm.addDamage(new Bullet(sx, sy, ex, ey, damage, decline, penetrate, bulletSpeed));
    }

}
