package JWork_5.src.equippment.weapon;

import JWork_5.main.Main;
import JWork_5.src.Interactive;
import JWork_5.src.manager.GeneralManager;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public abstract class Weapon {
    protected Main.item ID;
    protected int attackInterval;
    // TODO: should be pixels per sec, right new pixels per frame
    protected float bulletSpeed;
    protected Image image;
    protected Interactive holder;
    protected float initialRotation;
    protected float posX, posY;

    public Weapon(Main.item ID){
        this.ID = ID;
        initialRotation = 0;
        holder = null;
        posX = posY = 0;
    }

    // functionality
    public void setAttackInterval(int interval){ this.attackInterval = interval; }
    public void setBulletSpeed(float speed){ this.bulletSpeed = speed; }
    public void setInitialRotation(float rotation){ this.initialRotation = rotation; }
    public void setLocationX(float x){ this.posX = x; }
    public void setLocationY(float y){ this.posY = y; }
    public void setImage(Image image){ this.image = image; }
    public int getAttackInterval(){ return this.attackInterval; }
    public float getBulletSpeed(){ return this.bulletSpeed; }
    public float getInitialRotation(){ return this.initialRotation; }
    public float setLocationX(){ return this.posX; }
    public float setLocationY(){ return this.posY; }

    // interactive
    public void isDiscarded(){
        posX = holder.getLocationX();
        posY = holder.getLocationY();
        holder = null;
    }
    public void isHold(Interactive holder){
        this.holder = holder;
    }

    // rendering (probably not useful now)
    public void draw(Graphics g){
        if (holder != null){
            image.setRotation(holder.getRotation() + this.initialRotation);
            image.draw(holder.getLocationX(), holder.getLocationY());
        }
        else{
            image.draw(posX, posY);
        }
    }

    public Main.item getID(){return this.ID;}
    public abstract void genDamage(float sx, float sy, float dirX, float dirY, GeneralManager gm);
}
