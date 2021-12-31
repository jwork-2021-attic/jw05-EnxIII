package JWork_5.main;

import JWork_5.lib.animation.Animation;
import org.newdawn.slick.*;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class Main extends StateBasedGame {

    public static final int windowWidth = 1200;
    public static final int windowHeight = 800;
    public static final int framesPerSec = 144;


    public enum manipulator{
        Attack, Move, Collision, State, Repository, SetBack;
    }

    public enum item{
        BareHand, Pistol
    }

    public enum Action{
        Idle, Walk, Attack
    }

    private GameState gamez;

    public Main(){
        super("Main");
    }

    public static void main(String[] args) throws SlickException {
        AppGameContainer app = new AppGameContainer(new Main());
        app.setDisplayMode(windowWidth, windowHeight, false);
        app.setAlwaysRender(true);
        app.setTargetFrameRate(framesPerSec);
        app.start();
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        for (int i = 0; i <= 14; ++i) {
            Animation.playerHandGunShoot[i] = new Image("JWork_5/assets/Survivor_Spine/handGunShoot/survivor-shoot_handgun_" +
                    String.format("%02d.PNG", i)).getScaledCopy(50, 50);
        }



        gamez = new GameZ(0);
        this.addState(gamez);
    }

    //public static final Animation playerWalkHandGun = new Animation(playerHandGunShoot, 1000/Main.framesPerSec);
}
