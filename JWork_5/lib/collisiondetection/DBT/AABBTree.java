package JWork_5.lib.collisiondetection.DBT;

import JWork_5.lib.Ray;

import java.util.*;

public class AABBTree {
    Map<IAABB, Integer> _objectNodeIndexMap;
    Vector<AABBNode> _nodes;
    int _rootNodeIndex;
    int _allocatedNodeCount;
    int _nextFreeNodeIndex;
    int _nodeCapacity;
    int _growthSize;
    float _margin;

    float extraAABB = 8;
    AABB tempPassVal = new AABB();

    Vector<IAABB> overlaps;
    Stack<Integer> stack;

    public AABBTree(int initialSize){
        _rootNodeIndex = AABBNode.AABB_NULL_NODE;
        _allocatedNodeCount = 0;
        _nextFreeNodeIndex = 0;
        _nodeCapacity = initialSize;
        _growthSize = initialSize;
        _margin = 5;
        _objectNodeIndexMap = new HashMap<>();
        _nodes = new Vector<>();
        overlaps = new Vector<>();
        stack = new Stack<>();
        //_nodes.setSize(initialSize);
        for (int nodeIndex = 0; nodeIndex < initialSize; ++nodeIndex) {
                AABBNode node = new AABBNode();
                node.nextNodeIndex = nodeIndex + 1;
                _nodes.add(nodeIndex, node);
        }
        _nodes.get(initialSize-1).nextNodeIndex = AABBNode.AABB_NULL_NODE;
    }

    public int allocateNode(){
        if (_nextFreeNodeIndex == AABBNode.AABB_NULL_NODE) {
            assert(_allocatedNodeCount == _nodeCapacity);

            _nodeCapacity += _growthSize;
            //_nodes.setSize(_nodeCapacity);
            for (int nodeIndex = _allocatedNodeCount; nodeIndex < _nodeCapacity; nodeIndex++) {
                AABBNode node = new AABBNode();
                node.nextNodeIndex = nodeIndex + 1;
                _nodes.add(node);
            }
            _nodes.get(_nodeCapacity - 1).nextNodeIndex = AABBNode.AABB_NULL_NODE;
            _nextFreeNodeIndex = _allocatedNodeCount;
        }

        int nodeIndex = _nextFreeNodeIndex;
        AABBNode allocatedNode = _nodes.get(nodeIndex);
        allocatedNode.parentNodeIndex = AABBNode.AABB_NULL_NODE;
        allocatedNode.leftNodeIndex = AABBNode.AABB_NULL_NODE;
        allocatedNode.rightNodeIndex = AABBNode.AABB_NULL_NODE;
        _nextFreeNodeIndex = allocatedNode.nextNodeIndex;
        _allocatedNodeCount++;

        return nodeIndex;
    }

    public void deallocateNode(int nodeIndex){
        AABBNode deallocatedNode = _nodes.get(nodeIndex);
        deallocatedNode.nextNodeIndex = _nextFreeNodeIndex;
        _nextFreeNodeIndex = nodeIndex;
        _allocatedNodeCount--;
    }

    public void insertObject(IAABB object) {
        int nodeIndex = allocateNode();
        AABBNode node = _nodes.get(nodeIndex);

        // TODO: modification optimize
        object.getAABB(node.aabb);
        node.aabb.extend(this._margin);
        node.object = object;

        insertLeaf(nodeIndex);
        _objectNodeIndexMap.put(object, nodeIndex);
    }

    public void updateObject(IAABB object){
        int nodeIndex = _objectNodeIndexMap.get(object);
        object.getAABB(tempPassVal);
        updateLeaf(nodeIndex, tempPassVal);
    }

    public void insertLeaf(int leafNodeIndex){
        AABBNode temp = _nodes.get(leafNodeIndex);
        assert(temp.parentNodeIndex == AABBNode.AABB_NULL_NODE);
        assert(temp.leftNodeIndex == AABBNode.AABB_NULL_NODE);
        assert(temp.rightNodeIndex == AABBNode.AABB_NULL_NODE);

        // if the tree is empty then we make the root the leaf
        if (_rootNodeIndex == AABBNode.AABB_NULL_NODE) {
            _rootNodeIndex = leafNodeIndex;
            return;
        }

        // search for the best place to put the new leaf in the tree
        // we use surface area and depth as search heuristics
        int treeNodeIndex = _rootNodeIndex;
        AABBNode leafNode = _nodes.get(leafNodeIndex);
        while (!_nodes.get(treeNodeIndex).isLeaf()) {
            // because of the test in the while loop above we know we are never a leaf inside it
		    AABBNode treeNode = _nodes.get(treeNodeIndex);
            int leftNodeIndex = treeNode.leftNodeIndex;
            int rightNodeIndex = treeNode.rightNodeIndex;
		    AABBNode leftNode = _nodes.get(leftNodeIndex);
		    AABBNode rightNode = _nodes.get(rightNodeIndex);

            // TODO: modification
            this.tempPassVal.merge(treeNode.aabb, leafNode.aabb);
            //AABB combinedAabb = this.tempPassVal;
            double newParentNodeCost = 2.0 * tempPassVal.surfaceArea();
            double minimumPushDownCost = 2.0 * (tempPassVal.surfaceArea() - treeNode.aabb.surfaceArea());

            // use the costs to figure out whether to create a new parent here or descend
            double costLeft;
            double costRight;
            if (leftNode.isLeaf()) {
                tempPassVal.merge(leafNode.aabb, leftNode.aabb);
                costLeft = tempPassVal.surfaceArea() + minimumPushDownCost;
            }
            else {
                tempPassVal.merge(leafNode.aabb, leftNode.aabb);
                costLeft = (tempPassVal.surfaceArea() - leftNode.aabb.surfaceArea()) + minimumPushDownCost;
            }
            if (rightNode.isLeaf()) {
                tempPassVal.merge(leafNode.aabb, rightNode.aabb);
                costRight = tempPassVal.surfaceArea() + minimumPushDownCost;
            }
            else {
                tempPassVal.merge(leafNode.aabb, rightNode.aabb);
                costRight = (tempPassVal.surfaceArea() - rightNode.aabb.surfaceArea()) + minimumPushDownCost;
            }

            // if the cost of creating a new parent node here is less than descending in either direction then
            // we know we need to create a new parent node, errrr, here and attach the leaf to that
            if (newParentNodeCost < costLeft && newParentNodeCost < costRight)
                break;

            // otherwise descend in the cheapest direction
            if (costLeft < costRight)
                treeNodeIndex = leftNodeIndex;
            else
                treeNodeIndex = rightNodeIndex;
        }

        // the leafs sibling is going to be the node we found above and we are going to create a new
        // parent node and attach the leaf and this item
        int leafSiblingIndex = treeNodeIndex;
        AABBNode leafSibling = _nodes.get(leafSiblingIndex);
        int oldParentIndex = leafSibling.parentNodeIndex;
        int newParentIndex = allocateNode();
        AABBNode newParent = _nodes.get(newParentIndex);
        newParent.parentNodeIndex = oldParentIndex;
        newParent.aabb.merge(leafNode.aabb, leafSibling.aabb);
        // the new parents aabb is the leaf aabb combined with it's siblings aabb
        newParent.leftNodeIndex = leafSiblingIndex;
        newParent.rightNodeIndex = leafNodeIndex;
        leafNode.parentNodeIndex = newParentIndex;
        leafSibling.parentNodeIndex = newParentIndex;

        if (oldParentIndex == AABBNode.AABB_NULL_NODE) {
            // the old parent was the root and so this is now the root
            _rootNodeIndex = newParentIndex;
        }
        else {
            // the old parent was not the root and so we need to patch the left or right index to
            // point to the new node
            AABBNode oldParent = _nodes.get(oldParentIndex);
            if (oldParent.leftNodeIndex == leafSiblingIndex)
                oldParent.leftNodeIndex = newParentIndex;
            else
                oldParent.rightNodeIndex = newParentIndex;
        }

        // finally we need to walk back up the tree fixing heights and areas
        treeNodeIndex = leafNode.parentNodeIndex;
        fixUpwardsTree(treeNodeIndex);
    }

    public void removeLeaf(int leafNodeIndex){
        // TODO: no need to deAllocate ?
        if (leafNodeIndex == _rootNodeIndex) {
            _rootNodeIndex = AABBNode.AABB_NULL_NODE;
            return;
        }

        AABBNode leafNode = _nodes.get(leafNodeIndex);
        int parentNodeIndex = leafNode.parentNodeIndex;
	    AABBNode parentNode = _nodes.get(parentNodeIndex);
        int grandParentNodeIndex = parentNode.parentNodeIndex;
        int siblingNodeIndex = parentNode.leftNodeIndex == leafNodeIndex ? parentNode.rightNodeIndex : parentNode.leftNodeIndex;
        assert(siblingNodeIndex != AABBNode.AABB_NULL_NODE); // we must have a sibling
        AABBNode siblingNode = _nodes.get(siblingNodeIndex);

        if (grandParentNodeIndex != AABBNode.AABB_NULL_NODE) {
            // if we have a grand parent (i.e. the parent is not the root) then destroy the parent and connect the sibling to the grandparent in its
            // place
            AABBNode grandParentNode = _nodes.get(grandParentNodeIndex);
            if (grandParentNode.leftNodeIndex == parentNodeIndex) {
                grandParentNode.leftNodeIndex = siblingNodeIndex;
            }
            else {
                grandParentNode.rightNodeIndex = siblingNodeIndex;
            }
            siblingNode.parentNodeIndex = grandParentNodeIndex;
            deallocateNode(parentNodeIndex);

            fixUpwardsTree(grandParentNodeIndex);
        }
        else {
            // if we have no grandparent then the parent is the root and so our sibling becomes the root and has it's parent removed
            _rootNodeIndex = siblingNodeIndex;
            siblingNode.parentNodeIndex = AABBNode.AABB_NULL_NODE;
            deallocateNode(parentNodeIndex);
        }

        leafNode.parentNodeIndex = AABBNode.AABB_NULL_NODE;
    }

    public void updateLeaf(int leafNodeIndex, AABB newAaab){
        AABBNode node = _nodes.get(leafNodeIndex);

        // if the node contains the new aabb then we just leave things
        // TODO: when we add velocity this check should kick in as often an update will lie within the velocity fattened initial aabb
        // to support this we might need to differentiate between velocity fattened aabb and actual aabb
        //System.out.println("in query!");
        if (node.aabb.contains(newAaab)) {
            //System.out.println("in margin");
            return;
        }
        removeLeaf(leafNodeIndex);
        node.aabb.copy(newAaab);
        insertLeaf(leafNodeIndex);
    }

    public void fixUpwardsTree(int treeNodeIndex){
        while (treeNodeIndex != AABBNode.AABB_NULL_NODE) {
            AABBNode treeNode = _nodes.get(treeNodeIndex);

            // every node should be a parent
            assert(treeNode.leftNodeIndex != AABBNode.AABB_NULL_NODE &&
                    treeNode.rightNodeIndex != AABBNode.AABB_NULL_NODE);
            // fix height and area
		    AABBNode leftNode = _nodes.get(treeNode.leftNodeIndex);
		    AABBNode rightNode = _nodes.get(treeNode.rightNodeIndex);
            treeNode.aabb.merge(leftNode.aabb, rightNode.aabb);
            treeNodeIndex = treeNode.parentNodeIndex;
        }
    }

    public void removeObject(IAABB object){
        int nodeIndex = _objectNodeIndexMap.get(object);
        removeLeaf(nodeIndex);
        deallocateNode(nodeIndex);
        _objectNodeIndexMap.remove(object);
    }

    // forward list -> Vector
    public Vector<IAABB> queryOverlaps(IAABB object){
        this.overlaps.clear();
        this.stack.clear();

        //AABB testAabb = object.getAABB();
        object.getAABB(this.tempPassVal);
        stack.push(_rootNodeIndex);
        while(!stack.empty()) {
            int nodeIndex = stack.pop();

            if (nodeIndex == AABBNode.AABB_NULL_NODE)
                continue;

		    AABBNode node = _nodes.get(nodeIndex);
            if (node.aabb.overlaps(tempPassVal)) {
                if (node.isLeaf() && node.object != object) {
                    overlaps.add(node.object);
                }
                else {
                    stack.push(node.leftNodeIndex);
                    stack.push(node.rightNodeIndex);
                }
            }
        }
        return overlaps;
    }

    public Vector<IAABB> queryRayCast(Ray ray){
        this.overlaps.clear();
        this.stack.clear();

        if (_rootNodeIndex != AABBNode.AABB_NULL_NODE)
            stack.push(_rootNodeIndex);
        while (!stack.isEmpty()){
            int nodeIndex = stack.pop();
            AABBNode node = this._nodes.get(nodeIndex);
            if (ray.overLaps(node.aabb)){
                if (node.isLeaf())
                    overlaps.add(node.object);
                else {
                    if (node.leftNodeIndex != AABBNode.AABB_NULL_NODE)
                        stack.push(node.leftNodeIndex);
                    if (node.rightNodeIndex != AABBNode.AABB_NULL_NODE)
                        stack.push(node.rightNodeIndex);
                }
            }
        }
        return overlaps;
    }

}