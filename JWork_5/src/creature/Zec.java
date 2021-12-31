package JWork_5.src.creature;

import JWork_5.main.GameZ;
import JWork_5.main.Main;
import JWork_5.src.Interactive;
import JWork_5.src.manipulator.SetBackManipulator;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.state.StateBasedGame;

public class Zec extends Monster{

    public Zec(Image image) {
        super(new Circle(0, 0, GameZ.zecRadius));
        this.setImage(image);
        this.setHp(100);
        this.setArmor(100);
        this.setSpeed(180);
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

        var mani = object.getManipulator(Main.manipulator.SetBack);
        if (mani != null){
            var setBack = (SetBackManipulator)mani;
            setBack.setSetBack(x - this.getLocationX(), y - this.getLocationY(),
                    3, 3.0f/50);
        }
    }

}
