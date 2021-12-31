package JWork_5.lib.collisiondetection.DBT;

public class AABBNode {
        final static int AABB_NULL_NODE = 0xffffffff;

        public AABB aabb;
        public IAABB object;
        // tree links
        public int parentNodeIndex;
        public int leftNodeIndex;
        public int rightNodeIndex;
        // node linked list link
        public int nextNodeIndex;
        public boolean isLeaf() { return leftNodeIndex == AABB_NULL_NODE; }

        public AABBNode(){
            aabb = new AABB();
            object = null;
            parentNodeIndex = AABB_NULL_NODE;
            leftNodeIndex = AABB_NULL_NODE;
            rightNodeIndex = AABB_NULL_NODE;
            nextNodeIndex = AABB_NULL_NODE;
        }
};