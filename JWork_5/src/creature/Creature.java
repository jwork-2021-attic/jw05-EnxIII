package JWork_5.src.creature;

import JWork_5.lib.collisiondetection.DBT.IAABB;
import JWork_5.main.Main;
import JWork_5.src.Drawable;
import JWork_5.src.Interactive;
import JWork_5.src.manipulator.BasicManipulator;
import JWork_5.src.manipulator.StateManipulator;
import JWork_5.src.stateAlter.Effect;
import JWork_5.src.stateAlter.StateAlter;
import JWork_5.src.stateAlter.damage.Damage;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

import java.util.HashMap;

public abstract class Creature implements IAABB, Interactive, Drawable {
    // bulk contains cords and radius
    protected Circle bulk;
    protected Image image;
    protected HashMap<Main.manipulator, BasicManipulator> bmq;
    protected boolean[] stateVec;
    protected float hp;
    protected float armor;
    protected float speed;
    protected float direction = 0;

    public Creature(Circle bulk){
        this.bulk = bulk;
        this.bmq = new HashMap<>();
    }

    public boolean isAlive(){ return this.hp > 0; }
    public abstract void accept(GameContainer gc, StateBasedGame sbg, int delay);

    @Override
    public void draw(Graphics g) {
        var x = this.bulk.getCenterX();
        var y = this.bulk.getCenterY();
        this.image.setRotation(this.direction);
        this.image.draw(x - this.image.getWidth() / 2, y - this.image.getHeight() / 2);
    }

    @Override
    public Interactive getObject(){
        return this;
    }
    @Override
    public Shape getBulk(){
        return this.bulk;
    }
    @Override
    public void setManipulator(BasicManipulator bm, Main.manipulator index){
        this.bmq.put(index, bm);
    }
    @Override
    public BasicManipulator getManipulator(Main.manipulator type) {
        if (this.bmq.containsKey(type))
            return this.bmq.get(type);
        else return null;
    }

    @Override
    public void setRotation(float x, float y){
        this.direction = (float) Math.toDegrees(Math.atan2(y, x));
        //this.image.setRotation(degrees);
    }
    @Override
    public float getRotation(){ return this.direction; }
    @Override
    public void setLocation(float x, float y){
        this.bulk.setCenterX(x);
        this.bulk.setCenterY(y);
    }
    @Override
    public void setLocationX(float x){ this.bulk.setCenterX(x);}
    @Override
    public void setLocationY(float y){ this.bulk.setCenterY(y);}
    @Override
    public float getLocationX(){return this.bulk.getCenterX();}
    @Override
    public float getLocationY(){return this.bulk.getCenterY();}
    @Override
    public void setSpeed(float speed){
        this.speed = speed;
    }
    @Override
    public void setHp(float hp) { this.hp = hp; }
    @Override
    public void setArmor(float armor) {
        if (armor < 0)
            this.armor = 0;
        else this.armor = armor;
    }

    public void setImage(Image image) {
        //this.image = image.getScaledCopy(80, 80);
        this.image = image;
        //this.image = image.getSubImage(0, 0,
        //    (int) (this.bulk.radius * 2), (int) (this.bulk.radius * 2));
    }

    @Override
    public float getSpeed(){
        return this.speed;
    }
    @Override
    public float getHp() { return this.hp; }
    @Override
    public float getArmor() { return this.armor; }
    public Image getImage(){
        return this.image;
    }

    @Override
    public int penetrateLevel(){ return 1;}
    @Override
    public void isAttacked(Effect effect){ ((StateManipulator)this.bmq.get(Main.manipulator.State)).addState(effect); }
    @Override
    public void addState(Effect effect) {
        ((StateManipulator)this.bmq.get(Main.manipulator.State)).addState(effect);
    }
    @Override
    public void update(){
        this.bmq.forEach((manipulator, basicManipulator) -> basicManipulator.flip());
    }
}
