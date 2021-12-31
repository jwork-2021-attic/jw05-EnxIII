package JWork_5.src.manager;

import JWork_5.lib.Ray;
import JWork_5.lib.collisiondetection.DBT.AABBTree;
import JWork_5.lib.collisiondetection.DBT.IAABB;
import JWork_5.lib.gridmap.GridMap;
import JWork_5.lib.polymesh.PolyMesh;
import JWork_5.lib.polymesh.fNode;
import JWork_5.main.GameZ;
import JWork_5.src.Item;
import JWork_5.src.creature.Monster;
import JWork_5.src.creature.Player;
import JWork_5.src.obstruction.BasicObstruction;
import JWork_5.src.obstruction.Wall;
import JWork_5.src.stateAlter.damage.Damage;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Line;

import java.util.HashSet;
import java.util.Vector;

public class GeneralManager {

    // for player interact (with Grande)
    public AABBTree tree;
    // for ambient items (no collision bulk)
    public AABBTree itemTree;
    // for the static obstruction
    public AABBTree mapTree;

    public GridMap map;
    public PolyMesh navigate;
    // avatar
    public Vector<Player> pList;
    public HashSet<Monster> mList;
    // bullet
    public HashSet<Damage> dmgList;
    public HashSet<Item> itemList;
    // obstruction
    public Vector<BasicObstruction> mapList;

    public GeneralManager() throws SlickException {
        tree = new AABBTree(300);
        itemTree = new AABBTree(300);
        mapTree = new AABBTree(400);
        map = new GridMap(GameZ.worldWidth, GameZ.worldHeight, GameZ.gridSize);
        navigate = new PolyMesh();
        pList = new Vector<>();
        mList  = new HashSet<>();
        dmgList = new HashSet<>();
        mapList = new Vector<>();
        loadMap();
        navigate.build(map, GameZ.gridSize);

    }

    public void GeneralUpdate(){
        updatePlayer();
        updateMonster();
        updateDamage();
    }

    // load grid map
    public void loadMap() throws SlickException {
        Image image = new Image("JWork_5/assets/image11.jpeg");
        Image subImage = image.getSubImage(image.getWidth() / 2, 0, 29, 29);
        var obj = new Wall(subImage, 210, 420);
        this.mapList.add(obj);
        obj = new Wall(subImage, 240, 420);
        this.mapList.add(obj);
        obj = new Wall(subImage, 270, 420);
        this.mapList.add(obj);
        obj = new Wall(subImage, 210, 450);
        this.mapList.add(obj);
        obj = new Wall(subImage, 270, 450);
        this.mapList.add(obj);

        for (var building : this.mapList) {
            this.mapTree.insertObject(building);
            this.map.setFilled(building.getLeft(), building.getTop(), building.getWidth(), building.getHeight());
        }
        //this.map.debugPrint();
    }

    public void AStar(float sx, float sy, float dx, float dy, Vector<fNode> path){
        navigate.AStar(sx, sy, dx, dy, path);
    }
    public void AStar(float sx, float sy, float dx, float dy, fNode dst){
        navigate.AStar(sx, sy, dx, dy, dst);
    }

    public int addPlayer(Player pl){
        pList.add(pl);
        tree.insertObject(pl);
        return pList.size() - 1;
    }

    public void removePlayer(Player pl){
        pList.remove(pl);
        tree.removeObject(pl);
    }

    public void removeMonster(Monster ms){
        mList.remove(ms);
        tree.removeObject(ms);
    }

    public int addMonster(Monster m){
        mList.add(m);
        tree.insertObject(m);
        return mList.size() - 1;
    }

    public void addDamage(Damage d){
        dmgList.add(d);
        if (d.hasCollision())
            tree.insertObject(d);
    }

    public Vector<IAABB> avatarCollisionQuery(IAABB object){ return tree.queryOverlaps(object);}
    public Vector<IAABB> rayAvatarCast(Ray ray){
        return tree.queryRayCast(ray);
    }
    public Vector<IAABB> mapCollisionQuery(IAABB object){ return mapTree.queryOverlaps(object);}
    public Vector<IAABB> rayMapCast(Ray ray){
        return mapTree.queryRayCast(ray);
    }

    public void updatePlayer(Player ce){
        tree.updateObject(ce);
    }
    public void updateMonster(Monster ms) { tree.updateObject(ms); }

    public void updatePlayer(){
        pList.removeIf(player -> {
            if (player.isAlive()) {
                player.update();
                tree.updateObject(player);
                return false;
            }
            else{
                tree.removeObject(player);
                return true;
            }
        });
    }
    public void updateMonster() {
        mList.removeIf(monster -> {
            if (monster.isAlive()) {
                monster.update();
                tree.updateObject(monster);
                return false;
            }
            else{
                tree.removeObject(monster);
                return true;
            }
        });
    }
    public void updateDamage(){
        dmgList.removeIf(damage -> {
            damage.update();
            return damage.usedUp() || damage.outOfWorld(GameZ.worldWidth, GameZ.worldHeight);
        });
    }
}
