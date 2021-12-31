package JWork_5.src.obstruction;

import JWork_5.lib.collisiondetection.DBT.AABB;
import JWork_5.lib.collisiondetection.DBT.IAABB;
import JWork_5.main.Main;
import JWork_5.src.Interactive;
import JWork_5.src.manipulator.BasicManipulator;
import JWork_5.src.stateAlter.Effect;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Shape;


public class BasicObstruction implements Interactive, IAABB {

    protected Image image;
    protected Shape bulk;

    public BasicObstruction(Image image, Shape bulk){
        this.image = image;
        this.bulk = bulk;
    }

    public float getLeft(){ return this.bulk.getMinX();}
    public float getTop(){ return this.bulk.getMinY();}
    public float getWidth(){ return this.bulk.getWidth();}
    public float getHeight(){ return this.bulk.getHeight();}

    @Override
    public int penetrateLevel() {return 100;}
    @Override
    public void isAttacked(Effect effect) {}
    @Override
    public void addState(Effect effect) {}
    @Override
    public void bumpWith(Interactive object) {}
    @Override
    public void setManipulator(BasicManipulator bm, Main.manipulator index) {}
    @Override
    public BasicManipulator getManipulator(Main.manipulator type) {return null;}
    @Override
    public Shape getBulk() {return this.bulk;}
    @Override
    public void setRotation(float x, float y) {}
    @Override
    public float getRotation() {return image.getRotation();}
    @Override
    public void setLocation(float x, float y) {
        this.bulk.setCenterX(x);
        this.bulk.setCenterY(y);
    }
    @Override
    public void setLocationX(float x) {this.bulk.setCenterX(x);}
    @Override
    public void setLocationY(float y) {this.bulk.setCenterY(y);}
    @Override
    public float getLocationX() {return this.bulk.getCenterX();}
    @Override
    public float getLocationY() {return this.bulk.getCenterY();}
    @Override
    public void setSpeed(float speed) {}
    @Override
    public void setHp(float hp) {}
    @Override
    public void setArmor(float armor) {}
    @Override
    public float getSpeed() {return 0;}
    @Override
    public float getHp() {return 0;}
    @Override
    public float getArmor() {return 0;}
    @Override
    public void update() {

    }

    public void draw(Graphics g){
        this.image.draw(this.bulk.getMinX(), this.bulk.getMinY());
    }

    @Override
    public void getAABB(AABB result) {
        result.setValue(
                this.bulk.getMinX(), this.bulk.getMinY(),
                this.bulk.getMaxX(), this.bulk.getMaxY()
        );
    }
    @Override
    public Interactive getObject() {
        return this;
    }


}
