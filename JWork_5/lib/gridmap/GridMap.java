package JWork_5.lib.gridmap;

import java.util.*;

public class GridMap {

    protected int row, col;
    public int [][] map;
    protected int gridSize;

    public GridMap (int width, int height, int gridSize){
        this.col = width / gridSize;
        if (width % gridSize != 0)
            this.col += 1;
        this.row = height / gridSize;
        if (height % gridSize != 0)
            this.row += 1;
        this.gridSize = gridSize;
        this.map = new int[this.row][this.col];
        for (int i = 0; i < this.row; ++i)
            for (int j = 0; j < this.col; ++j)
                map[i][j] = 0;
    }

    public boolean isBlank(int c, int r){
        return map[r][c] == 0;
    }

    public void setFilled(float x, float y, float width, float height){
        for (int i = Math.max(0, (int) (y / gridSize)), limy = Math.min((int) ((y + height - 0.001) / gridSize), row); i <= limy; ++i){
            for (int j = Math.max(0, (int) (x / gridSize)), limx = Math.min((int) ((x + width - 0.001) / gridSize), col); j <= limx; ++j){
                map[i][j] = 1;
            }
        }
    }

    public boolean legal(int x, int y){
        return (0 <= x && x < col && 0 <= y && y < row);
    }

    public void debugPrint(){
        for (int i = 0; i < row; ++i){
            for (int j = 0; j < col; ++j)
                System.out.print(map[i][j]);
            System.out.print('\n');
        }
    }

    private int[][] Direction = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public void AStar(float srcX, float srcY, float dstX, float dstY, Vector<Node> path){
        int sx = (int) (srcX / gridSize), sy = (int) (srcY / gridSize);
        int dx = (int) (dstX / gridSize), dy = (int) (dstY / gridSize);
        path.clear();
        if (!isBlank(sx, sy) || !isBlank(dx, dy))
            return;
        HashMap<Node, Integer> visit = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<Node>((node1, node2) -> {
            int d1 = Math.abs(node1.x - dx) + Math.abs(node1.y - dy);
            int d2 = Math.abs(node2.x - dx) + Math.abs(node2.y - dy);
            if (d1 > d2) return -1;
            else return 0;
        });
        queue.add(new Node(sx, sy));
        int time = -1;
        while (!queue.isEmpty()){
            var cur = queue.poll();
            visit.put(cur, ++time);
            var newPos = new Node(cur.x, cur.y);
            for (int dir = 0; dir < 4; ++dir) {
                newPos.x = cur.x + Direction[dir][0];
                newPos.y = cur.y + Direction[dir][1];
                if (legal(newPos.x, newPos.y) && !visit.containsKey(newPos)){
                    queue.add(newPos);
                    newPos = new Node(cur.x, cur.y);
                }
            }
        }

        // give the path
        var dest = new Node(dx, dy);
        if (!visit.containsKey(dest))
            return;
        var cur = dest;
        int curTime = visit.get(cur);
        path.add(cur);
        while (cur.x != sx || cur.y != sy){
            Node mark = null;
            Node temp = new Node(0, 0);
            for (int dir = 0; dir < 4; ++dir){
                temp.x = cur.x + Direction[dir][0];
                temp.y = cur.y + Direction[dir][1];
                if (visit.containsKey(temp)){
                    int t = visit.get(temp);
                    if (t < curTime){
                        curTime = t;
                        mark = temp;
                    }
                }
            }
            cur = mark;
            path.add(cur);
        }
    }

}
