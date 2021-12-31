package JWork_5.src;

import JWork_5.main.Main;
import JWork_5.src.stateAlter.Effect;
import JWork_5.src.stateAlter.StateAlter;
import JWork_5.src.manipulator.BasicManipulator;
import JWork_5.src.stateAlter.damage.Damage;
import JWork_5.src.util.Contact;
import org.newdawn.slick.geom.Shape;

public interface Interactive {

    public int penetrateLevel();
    public void isAttacked(Effect effect);
    public void addState(Effect effect);

    public void bumpWith(Interactive object);

    public void setManipulator(BasicManipulator bm, Main.manipulator index);
    public BasicManipulator getManipulator(Main.manipulator type);
    public Shape getBulk();
    public void setRotation(float x, float y);
    public float getRotation();
    public void setLocation(float x, float y);
    public void setLocationX(float x);
    public void setLocationY(float y);
    public float getLocationX();
    public float getLocationY();

    public void setSpeed(float speed);
    public void setHp(float hp);
    public void setArmor(float armor);
    public float getSpeed();
    public float getHp();
    public float getArmor();


    public void update();
}
