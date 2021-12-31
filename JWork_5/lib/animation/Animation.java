package JWork_5.lib.animation;

import JWork_5.main.Main;
import JWork_5.main.Main.item;
import JWork_5.main.Main.Action;
import JWork_5.src.equippment.weapon.Pistol;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Animation {
    public static Image[] playerIdle = new Image[1];
    public static Image[] playerWalk = new Image[1];
    public static Image[] playerAttack = new Image[1];
    public static Image[] playerHandGunIdle = new Image[1];
    public static Image[] playerHandGunWalk = new Image[1];
    public static Image[] playerHandGunShoot = new Image[15];
    public static Image[] ZombieHandWalk = new Image[17];
    public static Image[] ZombieHandAttack = new Image[9];

    public static int[][] PlayFrameGap = new int[item.values().length][Action.values().length];
    public static Image[][][] PlayResourceMap = new Image[item.values().length][Action.values().length][];
    static {
        PlayFrameGap[item.BareHand.ordinal()][Action.Idle.ordinal()] = 10;
        PlayFrameGap[item.BareHand.ordinal()][Action.Walk.ordinal()] = 10;
        PlayFrameGap[item.BareHand.ordinal()][Action.Attack.ordinal()] = 10;
        PlayFrameGap[item.Pistol.ordinal()][Action.Idle.ordinal()] = 10;
        PlayFrameGap[item.Pistol.ordinal()][Action.Walk.ordinal()] = 10;
        PlayFrameGap[item.Pistol.ordinal()][Action.Attack.ordinal()] = Pistol.PistolInterval / (Animation.playerHandGunShoot.length - 1);
        PlayResourceMap[item.BareHand.ordinal()][Action.Idle.ordinal()] = playerIdle;
        PlayResourceMap[item.BareHand.ordinal()][Action.Walk.ordinal()] = playerWalk;
        PlayResourceMap[item.BareHand.ordinal()][Action.Attack.ordinal()] = playerAttack;
        PlayResourceMap[item.Pistol.ordinal()][Action.Idle.ordinal()] = playerHandGunIdle;
        PlayResourceMap[item.Pistol.ordinal()][Action.Walk.ordinal()] = playerHandGunWalk;
        PlayResourceMap[item.Pistol.ordinal()][Action.Attack.ordinal()] = playerHandGunShoot;
    }

    protected Image[] resources;
    protected int switchGap = 100;
    protected int accumulator = 0;
    protected int curFrames = 0;

    public Animation(){

    }

    // TODO: multi-thread deep copy
    public void setAnimation(Image[] frames, int gap){
        resources = frames;
        this.switchGap = gap;
    }

    public Image activate(){
        accumulator = 0;
        curFrames = 0;
        return resources[0];
    }

    public Image execute(int delay){
        accumulator += delay;
        if (accumulator >= switchGap){
            accumulator -= switchGap;
            curFrames++;
            if (curFrames >= resources.length)
                curFrames = 0;
        }
        return resources[curFrames];
    }

}
