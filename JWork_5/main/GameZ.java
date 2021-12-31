package JWork_5.main;

import JWork_5.lib.Ray;
import JWork_5.lib.animation.Animation;
import JWork_5.src.creature.Player;
import JWork_5.src.creature.Zec;
import JWork_5.src.equippment.weapon.Pistol;
import JWork_5.src.manager.GeneralManager;
import JWork_5.src.manipulator.*;
import JWork_5.src.stateAlter.damage.Damage;
import JWork_5.src.util.Contact;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


import java.util.Vector;


import static java.lang.Math.abs;

public class GameZ extends BasicGameState {

    public static final float playerRadius = 15;
    public static final float zecRadius = 15;
    public static final int gameSpeed = 7;
    public static int worldWidth = 1200 ;
    public static int worldHeight = 800;
    public static int gridSize = 30;

    private int ID;
    private int speedControl = 0;
    static public GeneralManager gm;
    static public GeomUtil gu = new GeomUtil();

    public GameZ(int ID) throws SlickException {
        this.ID = ID;
    }


    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public void init(GameContainer gc, StateBasedGame sbg) throws SlickException {
        gm = new GeneralManager();
        var img = new Image("JWork_5/assets/image11.jpeg");

        var pl = new Player(Animation.playerHandGunShoot[0].copy());
        pl.setLocation(100, 100);
        gm.addPlayer(pl);

        var att = new AttackManipulator_Player(pl);
        var wp = new Pistol();
        att.setWeapon(wp);
        wp.isHold(pl);
        new MovingManipulator_Player(pl);
        new SetBackManipulator(pl);
        new CollisionManipulator(pl);


        var zec = new Zec(img.getSubImage(40, 0, (int) (zecRadius * 2),
                (int) (zecRadius * 2)));
        zec.setLocation(200, 200);
        gm.addMonster(zec);
        new SetBackManipulator(zec);
        new CollisionManipulator(zec);
        new StateManipulator(zec);
        new MovingManipulator_Robort(zec);

        var zec2 = new Zec(img.getSubImage(40, 0, (int) (zecRadius * 2),
                (int) (zecRadius * 2)));
        zec2.setLocation(280, 200);
        gm.addMonster(zec2);
        new SetBackManipulator(zec2);
        new CollisionManipulator(zec2);
        new StateManipulator(zec2);
        new MovingManipulator_Robort(zec2);

    }

    @Override
    public void update(GameContainer gc, StateBasedGame sbg, int delay) throws SlickException {
        this.speedControl += delay;
        if (this.speedControl >= gameSpeed) {
            this.speedControl -= gameSpeed;
            gm.GeneralUpdate();
            for (var damage : gm.dmgList){
                // collision
                if (damage instanceof Ray)
                    identifyRayCast(Precise, damage);
                else
                    identifyCollision(Precise, damage);
                for (var ct : Precise) {
                    damage.genDamage(ct.object, ct.contactX, ct.contactY);
                    // can't penetrate anymore
                    if (damage.usedUp()) break;
                }
            }
            // player
            for (var p : gm.pList)
                p.accept(gc, sbg, delay);
            // monster
            for (var m : gm.mList)
                m.accept(gc, sbg, delay);
        }
    }

    private Image whiteBackGround = new Image("JWork_5/assets/Map/forTest/WhiteBackGround.PNG");
    @Override
    public void render(GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        whiteBackGround.draw();
        whiteBackGround.draw(800, 0);
        for (var o : gm.mapList)
            o.draw(g);
        for (var d : gm.dmgList)
            d.draw(g);
        for (var p : gm.pList)
            p.draw(g);
        for (var m : gm.mList)
            m.draw(g);
    }


    protected Vector<Contact> Precise = new Vector<>();
    protected void identifyCollision(Vector<Contact> precise, Damage damage){
        precise.clear();
        var sp = (Shape) damage;
        for (var aabb : gm.avatarCollisionQuery(damage))
            if (aabb.getObject().getBulk().intersects(sp))
                precise.add(new Contact(aabb.getObject(), sp.getCenterX(), sp.getCenterY()));
    }

    protected void identifyRayCast(Vector<Contact> precise, Damage damage){
        precise.clear();
        var line = (Line) damage;
        // map obstruction collision
        for (var aabb : gm.rayMapCast((Ray) damage)) {
            if (line.intersects(aabb.getObject().getBulk())) {
                var res = gu.intersect(aabb.getObject().getBulk(), line);
                Precise.add(new Contact(aabb.getObject(), res.line.getX1(), res.line.getY1()));
            }
            else if (aabb.getObject().getBulk().contains(line)){
                Precise.add(new Contact(aabb.getObject(), line.getX1(), line.getX1()));
            }
        }
        // avatar collision
        for (var aabb : gm.rayAvatarCast((Ray) damage)) {
            if (line.intersects(aabb.getObject().getBulk())) {
                var res = gu.intersect(aabb.getObject().getBulk(), line);
                Precise.add(new Contact(aabb.getObject(), res.line.getX1(), res.line.getY1()));
            }
            else if (aabb.getObject().getBulk().contains(line)){
                Precise.add(new Contact(aabb.getObject(), line.getX1(), line.getX1()));
            }
        }
        Precise.sort((o1, o2) -> {
            float minx1 = abs(o1.contactX - line.getX1()), miny1 = abs(o1.contactY - line.getY1());
            float minx2 = abs(o2.contactX - line.getX1()), miny2 = abs(o2.contactY - line.getY1());
            if (minx2 < minx1 || miny2 < miny1)
                return -1;
            else
                return 0;
        });
    }

}

/* test code
        //tx = 50;
        //ty = 50;
        int dx = gc.getInput().getMouseX();
        int dy = gc.getInput().getMouseY();
        if (gc.getInput().isMousePressed(Input.MOUSE_LEFT_BUTTON)){
            tx = gc.getInput().getMouseX();
            ty = gc.getInput().getMouseY();
        }

        for (var o : gm.mapList)
            o.draw(g);
        var path = new Vector<fNode>();
        gm.AStar(tx, ty, dx, dy, path);
        if (path.size() > 1) {
            var cur = new fNode(tx, ty, 0);
            for (int l = path.size() - 1; l >= 0; --l) {
                var nxt = path.get(l);
                g.drawLine(cur.x, cur.y, nxt.x, nxt.y);
                cur = nxt;
            }
        }
        int k = 0;
         gm.navigate.debugPrint(g, dx, dy);

         protected int tx = 0, ty = 0;
 */
