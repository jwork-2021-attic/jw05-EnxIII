package JWork_5.lib.polymesh;

public class fNode {
    public float x;
    public float y;
    public int[] dir;
    public static final int[][] Direction = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};

    public fNode(float x, float y, int num){
        this.x = x;
        this.y = y;
        this.dir = new int[num];
    }

    public void debugPrint(){
        System.out.print("cord:");
        System.out.print(x);
        System.out.print(' ');
        System.out.println(y);
    }

}
