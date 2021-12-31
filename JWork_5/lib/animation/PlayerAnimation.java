package JWork_5.lib.animation;

import JWork_5.main.Main;

public class PlayerAnimation extends Animation{
    public PlayerAnimation(){

    }

    public void setNewAnimation(Main.item it, Main.Action act){
        this.setAnimation(
                PlayResourceMap[it.ordinal()][act.ordinal()],
                PlayFrameGap[it.ordinal()][act.ordinal()]);
    }
}
