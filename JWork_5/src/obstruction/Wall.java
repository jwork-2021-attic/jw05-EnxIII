package JWork_5.src.obstruction;


import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Wall extends BasicObstruction{

    public Wall(Image image, float x, float y){
        super(image, new Rectangle(x, y, image.getWidth(), image.getHeight()));
    }

}
