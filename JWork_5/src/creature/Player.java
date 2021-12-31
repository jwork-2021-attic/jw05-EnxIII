package JWork_5.src.creature;

import JWork_5.main.GameZ;
import JWork_5.main.Main;
import JWork_5.src.Interactive;
import JWork_5.src.equippment.weapon.Weapon;
import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Player extends Human{

    public Player(Image image){
        super(new Circle(0, 0, GameZ.playerRadius));
        this.setImage(image);
        this.setHp(100);
        this.setArmor(100);
        this.setSpeed(280);
    }

    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        this.bmq.forEach((manipulator, basicManipulator) -> basicManipulator.accept(gc, sbg, delay));
    }

    @Override
    public void bumpWith(Interactive object) {
        Shape bk = object.getBulk();
        float x = bk.getCenterX(), y = bk.getCenterY();
        float xOff = 0, yOff = 0;
        if ((x - this.bulk.getCenterX()) > 0) xOff = -1;
        else xOff = 1;
        if ((y - this.bulk.getCenterY()) > 0) yOff = -1;
        else yOff = 1;
        this.bulk.setCenterX(this.bulk.getCenterX() + xOff);
        this.bulk.setCenterY(this.bulk.getCenterY() + yOff);
    }

    @Override
    public void draw(Graphics g) {
        var x = this.bulk.getCenterX();
        var y = this.bulk.getCenterY();
        this.image.setRotation(this.direction);
        this.image.draw(x - this.image.getWidth() / 2, y - this.image.getHeight() / 2);
    }

}
