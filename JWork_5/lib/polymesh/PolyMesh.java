package JWork_5.lib.polymesh;

import JWork_5.lib.gridmap.GridMap;
import org.lwjgl.Sys;
import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Vector;

import static java.lang.Math.*;

public class PolyMesh {
    public static int maxGridNum = 6;
    public static int naviDistance = 2;

    int row, col, gridSize;
    int[][] colorMap;
    public Vector<PolyGon> polygons;

    public PolyMesh(){
        polygons = new Vector<>();
    }

    public void build(GridMap gridMap, int gridSize){
        this.gridSize = gridSize;
        row = gridMap.map.length;
        col = gridMap.map[0].length;
        colorMap = new int[row][col];
        for (int i = 0; i < row; ++i){
            for (int j = 0; j < col; ++j){
                if (gridMap.map[i][j] != 0)
                    colorMap[i][j] = -1;
                else colorMap[i][j] = 0;
            }
        }
        this.putColor();
        this.buildMesh();
    }

    protected void buildMesh(){
        int ID = 0;
        for (int i = 0; i < row; ++i){
            for (int j = 0; j < col; ++j){
                if (ID < this.colorMap[i][j]){
                    ID = this.colorMap[i][j];
                    int w = getW(ID, i, j) * gridSize;
                    int h = getH(ID, i, j) * gridSize;
                    var p = new PolyGon(ID, gridSize * j, gridSize * i, w, h);
                    polygons.set(ID, p);
                    this.initiateNaviPoint(p);
                }
            }
        }
    }
    private int getW(int ID, int r, int c){
        int s = c;
        for (; s < col && colorMap[r][s] == ID; ++s);
        return s - c;
    }
    private int getH(int ID, int r, int c){
        int s = r;
        for (; s < row && colorMap[s][c] == ID; ++s);
        return s - r;
    }
    private void initiateNaviPoint(PolyGon p){
        int r = p.sy / gridSize;
        int c = p.sx / gridSize;
        int w = p.width / gridSize;
        int h = p.height / gridSize;
        if (r > 0 && c > 0 && colorMap[r - 1][c] > 0 && colorMap[r][c - 1] > 0 && colorMap[r - 1][c - 1] > 0){
            var poly = new fNode(p.sx, p.sy, 3);
            p.points.add(poly);
            poly.dir[0] = 4; poly.dir[1] = 5; poly.dir[2] = 6;
        }
        if (r > 0 && c + w < col && colorMap[r - 1][c + w] > 0 && colorMap[r][c + w] > 0 && colorMap[r - 1][c + w - 1] > 0){
            var poly = new fNode(p.sx + p.width, p.sy, 3);
            p.points.add(poly);
            poly.dir[0] = 6; poly.dir[1] = 7; poly.dir[2] = 0;
        }
        if (r + h < row && c > 0 && colorMap[r + h][c] > 0 && colorMap[r + h][c - 1] > 0 && colorMap[r + h - 1][c - 1] > 0){
            var poly = new fNode(p.sx, p.sy + p.height, 3);
            p.points.add(poly);
            poly.dir[0] = 2; poly.dir[1] = 3; poly.dir[2] = 4;
        }
        if (r + h < row && c + w < col && colorMap[r + h][c + w - 1] > 0 && colorMap[r + h - 1][c + w] > 0 && colorMap[r + h][c + w] > 0){
            var poly = new fNode(p.sx + p.width, p.sy + p.height, 3);
            p.points.add(poly);
            poly.dir[0] = 0; poly.dir[1] = 1; poly.dir[2] = 2;
        }
        // top
        int cc, sc = c;
        while (r > 0) {
            // find open start
            for (int lim = Math.min(col, c + w); sc < lim && colorMap[r - 1][sc] < 0; ++sc);
            // find close
            if (sc >= c + w) break;
            cc = sc + 1;
            for (int lim = Math.min(col, c + w); cc < lim && colorMap[r - 1][cc] > 0; ++cc);
            var point = new fNode(((float) sc + cc) * gridSize / 2, p.sy, 1);
            point.dir[0] = 6;
            p.points.add(point);
            cutLevel(6, p.sy, sc * gridSize, point.x, p);
            cutLevel(6, p.sy, point.x, cc * gridSize, p);
            sc = cc;
        }
        // bottom
        sc = c;
        while (r + h < row) {
            // find open start
            for (int lim = Math.min(col, c + w); sc < lim && colorMap[r + h][sc] < 0; ++sc);
            // find close
            if (sc >= c + w) break;
            cc = sc + 1;
            for (int lim = Math.min(col, c + w); cc < lim && colorMap[r + h][cc] > 0; ++cc);
            var point = new fNode(((float) sc + cc) * gridSize / 2, p.sy + p.height, 1);
            point.dir[0] = 2;
            p.points.add(point);
            cutLevel(2, p.sy + p.height, sc * gridSize, point.x, p);
            cutLevel(2, p.sy + p.height, point.x, cc * gridSize, p);
            sc = cc;
        }
        // left
        int cr, sr = r;
        while (c > 0) {
            // find open start
            for (int lim = Math.min(row, r + h); sr < lim && colorMap[sr][c - 1] < 0; ++sr);
            // find close
            if (sr >= r + h) break;
            cr = sr + 1;
            for (int lim = Math.min(row, r + h); cr < lim && colorMap[cr][c - 1] > 0; ++cr);
            var point = new fNode(p.sx, ((float) sr + cr) * gridSize / 2, 1);
            point.dir[0] = 4;
            p.points.add(point);
            cutVertical(4, p.sx, sr * gridSize, point.y, p);
            cutVertical(4, p.sx, point.y, cr * gridSize, p);
            sr = cr;
        }
        // right
        sr = r;
        while (c + w < col) {
            // find open start
            for (int lim = Math.min(row, r + h); sr < lim && colorMap[sr][c + w] < 0; ++sr);
            // find close
            if (sr >= r + h) break;
            cr = sr + 1;
            for (int lim = Math.min(row, r + h); cr < lim && colorMap[cr][c + w] > 0; ++cr);
            var point = new fNode(p.sx + p.width, ((float) sr + cr) * gridSize / 2, 1);
            point.dir[0] = 0;
            p.points.add(point);
            cutVertical(0, p.sx + p.width, sr * gridSize, point.y, p);
            cutVertical(0, p.sx + p.width, point.y, cr * gridSize, p);
            sr = cr;
        }
    }
    private void cutLevel(int d, float y, float left, float right, PolyGon p){
        if (right - left < naviDistance * gridSize)
            return;
        var poly = new fNode((left + right) / 2, y, 1);
        poly.dir[0] = d;
        p.points.add(poly);
        cutLevel(d, y, left, (left + right) / 2, p);
        cutLevel(d, y, (left + right) / 2, right, p);
    }
    private void cutVertical(int d, float x, float top, float bottom, PolyGon p){
        if (bottom - top < naviDistance * gridSize)
            return;
        var poly = new fNode(x, (top + bottom) / 2, 1);
        poly.dir[0] = d;
        p.points.add(poly);
        cutVertical(d, x, top, (top + bottom) / 2, p);
        cutVertical(d, x, (top + bottom) / 2, bottom, p);
    }

    protected void putColor(){
        int ID = 0;
        for (int i = 0; i < row; ++i){
            for (int j = 0; j < col; ++j){
                if (colorMap[i][j] == 0){
                    fillMap(++ID, i, j, maxGridNum - i % maxGridNum, maxGridNum);
                }
            }
        }
        this.polygons.setSize(ID + 1);
    }
    protected void fillMap(int ID, int r, int c, int rMax, int cMax){
        boolean dirX = true, dirY = true;
        int xSize = 1, ySize = 1;
        colorMap[r][c] = ID;
        while (xSize < cMax && ySize < rMax && (dirX | dirY)){
            if (dirX && (dirX = (c + xSize < col)) && (dirX = xExpandable(c, r, xSize, ySize))){
                for (int i = r; i < r + ySize; ++i)
                    colorMap[i][c + xSize] = ID;
                ++xSize;
            }
            if (dirY && (dirY = (r + ySize) < row) && (dirY = yExpandable(c, r, xSize, ySize))){
                for (int j = c; j < c + xSize; ++j)
                    colorMap[r + ySize][j] = ID;
                ++ySize;
            }
        }
    }
    private boolean xExpandable(int c, int r, int xSize, int ySize){
        for (int i = r; i < r + ySize; ++i)
            if (colorMap[i][c + xSize] != 0)
                return false;
        return true;
    }
    private boolean yExpandable(int c, int r, int xSize, int ySize){
        for (int j = c; j < c + xSize; ++j)
            if (colorMap[r + ySize][j] != 0)
                return false;
        return true;
    }

    private boolean isLegal(int r, int c){
        return (0 <= r && r < row && 0 <= c && c < col);
    }

    public void debugPrint(){
        for (int i = 0; i < row; ++i){
            for (int j = 0; j < col; ++j){
                System.out.print(colorMap[i][j]);
            }
            System.out.print('\n');
        }
    }

    public void debugPrint(Graphics g, int x, int y){
        for (var poly : this.polygons) {
            if (poly != null) {
                g.drawLine(poly.sx, poly.sy, poly.sx + poly.width, poly.sy);
                g.drawLine(poly.sx, poly.sy, poly.sx, poly.sy + poly.height);
                g.drawLine(poly.sx + poly.width, poly.sy, poly.sx + poly.width, poly.sy + poly.height);
                g.drawLine(poly.sx, poly.sy + poly.height, poly.sx + poly.width, poly.sy + poly.height);
                var temp = getPolyGon(x, y);
                if (temp != null){
                    g.drawString(Integer.toString(temp.points.size()), 30, 30);
                    for (var point : temp.points)
                        g.drawString("*", point.x, point.y);
                }
            }
        }
    }

    public void AStar(float sx, float sy, float dx, float dy, Vector<fNode> path){
        path.clear();
        var sp = getPolyGon(sx, sy);
        var dp = getPolyGon(dx, dy);
        if (sp == null || dp == null)
            return;
        if (sp == dp) {
            path.add(new fNode(dx, dy, 0));
            return;
        }
        HashSet<PolyGon> visit = new HashSet<>();
        HashMap<fNode, fNode> pre = new HashMap<>();
        HashMap<fNode, Float> cost = new HashMap<>();
        PriorityQueue<fNode> queue = new PriorityQueue<fNode>((fnode1, fnode2) -> {
            float d1 = (float) (cost.get(fnode1) + Math.sqrt((fnode1.x - dx) * (fnode1.x - dx) + (fnode1.y - dy) * (fnode1.y - dy)));
            float d2 = (float) (cost.get(fnode2) + Math.sqrt((fnode2.x - dx) * (fnode2.x - dx) + (fnode2.y - dy) * (fnode2.y - dy)));
            if (d1 <= d2) return -1;
            return 1;
        });
        PolyGon cur = sp;
        visit.add(cur);
        for (var node : cur.points) {
            pre.put(node, null);
            cost.put(node, (float) sqrt((node.x - sx) * (node.x - sx) + (node.y - sy) * (node.y - sy)));
            queue.offer(node);
        }
        while (!queue.isEmpty()){
            var curPoint = queue.remove();
            for (var d : curPoint.dir){
                float x = curPoint.x + fNode.Direction[d][0];
                float y = curPoint.y + fNode.Direction[d][1];
                var dpoly = getPolyGon(x, y);
                if (dpoly != null && visit.add(dpoly)){
                    if (dpoly.contains(dx, dy)){
                        path.add(new fNode(dx, dy, 0));
                        path.add(curPoint);
                        var lastOne = pre.get(curPoint);
                        while (lastOne != null){
                            path.add(lastOne);
                            curPoint = lastOne;
                            lastOne = pre.get(curPoint);
                        }
                        return;
                    }
                    float cCost = cost.get(curPoint);
                    for (var dpNode : dpoly.points) {
                        if (curPoint.x != dpNode.x || curPoint.y != dpNode.y) {
                            pre.put(dpNode, curPoint);
                            cost.put(dpNode, cCost + (float) sqrt(
                                    (dpNode.x - curPoint.x) * (dpNode.x - curPoint.x) +
                                            (dpNode.y - curPoint.y) * (dpNode.y - curPoint.y)));
                            queue.offer(dpNode);
                        }
                    }
                }
            }
        }
    }
    protected PolyGon getPolyGon(float x, float y){
        int r = (int) (y / gridSize);
        int c = (int) (x / gridSize);
        if (colorMap[r][c] < 0)
            return null;
        return polygons.get(colorMap[r][c]);
    }
    protected float threshold = 9;
    protected float[][] bulk = {{(gridSize - 1) / 2 , 0},{0, -(gridSize - 1) / 2},{-(gridSize - 1) / 2, 0},{0, (gridSize - 1) / 2}};
    public void AStar(float sx, float sy, float dx, float dy, fNode dst){
        var dp = getPolyGon(dx, dy);
        dst.x = -1;
        dst.y = -1;
        if (dp == null)
            return;
        HashSet<PolyGon> visit = new HashSet<>();
        HashMap<fNode, fNode> pre = new HashMap<>();
        HashMap<fNode, Float> cost = new HashMap<>();
        PriorityQueue<fNode> queue = new PriorityQueue<fNode>((fnode1, fnode2) -> {
            float d1 = (float) (cost.get(fnode1) + Math.sqrt((fnode1.x - dx) * (fnode1.x - dx) + (fnode1.y - dy) * (fnode1.y - dy)));
            float d2 = (float) (cost.get(fnode2) + Math.sqrt((fnode2.x - dx) * (fnode2.x - dx) + (fnode2.y - dy) * (fnode2.y - dy)));
            if (d1 <= d2) return -1;
            return 1;
        });
        PolyGon cur = null;
        for (int i = 0; i < 4; ++i){
            cur = getPolyGon(sx + bulk[i][0], sy + bulk[i][1]);
            if (cur != null) {
                if (cur == dp){
                    dst.x = dx;
                    dst.y = dy;
                    return;
                }
                visit.add(cur);
                for (var node : cur.points) {
                    pre.put(node, null);
                    cost.put(node, (float) sqrt((node.x - sx) * (node.x - sx) + (node.y - sy) * (node.y - sy)));
                    queue.offer(node);
                }
            }
        }
        while (!queue.isEmpty()){
            var curPoint = queue.remove();
            for (var d : curPoint.dir){
                float x = curPoint.x + fNode.Direction[d][0];
                float y = curPoint.y + fNode.Direction[d][1];
                var dpoly = getPolyGon(x, y);
                if (dpoly != null && visit.add(dpoly)){
                    if (dpoly.contains(dx, dy)){
                        var lastOne = pre.get(curPoint);
                        while (lastOne != null){
                            curPoint = lastOne;
                            lastOne = pre.get(curPoint);
                        }
                        dst.x = curPoint.x;
                        dst.y = curPoint.y;
                        return;
                    }
                    float cCost = cost.get(curPoint);
                    for (var dpNode : dpoly.points) {
                        if (curPoint.x != dpNode.x || curPoint.y != dpNode.y) {
                            pre.put(dpNode, curPoint);
                            cost.put(dpNode, cCost + (float) sqrt(
                                    (dpNode.x - curPoint.x) * (dpNode.x - curPoint.x) +
                                            (dpNode.y - curPoint.y) * (dpNode.y - curPoint.y)));
                            queue.offer(dpNode);
                        }
                    }
                }
            }
        }
    }

}
