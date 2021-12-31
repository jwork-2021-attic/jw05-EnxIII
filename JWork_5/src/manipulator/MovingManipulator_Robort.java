package JWork_5.src.manipulator;

import JWork_5.lib.autoavoid.AutoAvoidCircle;
import JWork_5.lib.polymesh.fNode;
import JWork_5.main.GameZ;
import JWork_5.main.Main;
import JWork_5.src.creature.Creature;
import JWork_5.src.creature.Player;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

import static java.lang.Math.*;

public class MovingManipulator_Robort implements BasicManipulator{
    protected boolean available = true, navigating = false;
    protected int threshold = 30, accumulator = 0;
    protected Creature ce, target;
    protected fNode dst = new fNode(0,0,0);

    public MovingManipulator_Robort(Creature ce){
        this.setCreature(ce);
        this.autoAvoid = new AutoAvoidCircle(ce, 6);
    }

    @Override
    public void Disable() {
        available = false;
    }

    @Override
    public void setCreature(Creature ce) {
        this.ce = ce;
        this.ce.setManipulator(this, Main.manipulator.Move);
    }

    protected AutoAvoidCircle autoAvoid;
    protected float minFreshDist = 2;
    @Override
    public void accept(GameContainer gc, StateBasedGame sbg, int delay) {
        if (available && navigating){
            accumulator += delay;
            if (accumulator > threshold) {
                accumulator -= threshold;
                GameZ.gm.AStar(ce.getLocationX(), ce.getLocationY(),
                        target.getLocationX(), target.getLocationY(), this.dst);
                if (dst.x < 0 || dst.y < 0) {
                    deactivate();
                    return;
                }
            } else if (abs(dst.x - ce.getLocationX()) + abs(dst.y - ce.getLocationY()) < minFreshDist){
                accumulator = 0;
                GameZ.gm.AStar(ce.getLocationX(), ce.getLocationY(),
                        target.getLocationX(), target.getLocationY(), this.dst);
                if (dst.x < 0 || dst.y < 0) {
                    deactivate();
                    return;
                }
            }

            // Direction adjustment
            float dirX = dst.x - ce.getLocationX(), dirY = dst.y - ce.getLocationY();
            float length = (float) sqrt(dirX * dirX + dirY * dirY);
            if (length != 0){
                dirX /= length;
                dirY /= length;
            }
            //float old = ce.getRotation();
            ce.setRotation(dirX, dirY);
            //if (min(abs(old - ce.getRotation()), abs(360 - abs(old - ce.getRotation()))) > 150)
            //    old = 4;
            // if (length < attackRange)

            float cx, cy, cl, nl, nx = -dirY, ny = dirX;
            for (var iab : GameZ.gm.mapCollisionQuery(autoAvoid)){
                var obj = iab.getObject();
                if (autoAvoid.intersects(obj.getBulk())){
                    cx = ce.getLocationX() - obj.getLocationX();
                    cy = ce.getLocationY() - obj.getLocationY();
                    if (cx * dirX + cy * dirY < 0) {
                        nl = nx * cx + ny * cy;
                        if (nl >= 0) {
                            dirX += nx;
                            dirY += ny;
                        } else {
                            dirX -= nx;
                            dirY -= ny;
                        }
                    }
                }
            }
            var x = ce.getLocationX() + dirX * ce.getSpeed() / Main.framesPerSec;
            var y = ce.getLocationY() + dirY * ce.getSpeed() / Main.framesPerSec;
            ce.setLocation(x, y);
        }
        else{
            available = true;
            for (var pl : GameZ.gm.pList) {
                autoLockDown(pl);
                break;
            }
        }
    }

    public void initiateTarget(Creature ce){
        target = ce;
        navigating = true;
        accumulator = threshold;
    }

    public void autoLockDown(Creature ce){
        initiateTarget(ce);
    }

    public void deactivate(){
        navigating = false;
    }

}

    /*
    HashSet<PolyGon> visit = new HashSet<>();
    HashMap<fNode, fNode> pre = new HashMap<>();
    HashMap<fNode, Float> cost = new HashMap<>();
    PriorityQueue<fNode> queue = new PriorityQueue<fNode>((fnode1, fnode2) -> {
        float d1 = (float) (cost.get(fnode1) + Math.sqrt((fnode1.x - targetX) * (fnode1.x - targetX) + (fnode1.y - targetY) * (fnode1.y - targetY)));
        float d2 = (float) (cost.get(fnode2) + Math.sqrt((fnode2.x - targetX) * (fnode2.x - targetX) + (fnode2.y - targetY) * (fnode2.y - targetY)));
        if (d1 <= d2) return -1;
        return 1;
    });
     */