package BStarTree;

import java.util.ArrayList;
import java.util.List;

public class BStarNode {
    private boolean leaf;
    private List<Integer> keys;
    private List<BStarNode> children;
    private int maxKeys;

    public BStarNode(int maxKeys, boolean leaf) {
        this.maxKeys = maxKeys;
        this.leaf = leaf;
        this.keys = new ArrayList<>();
        this.children = new ArrayList<>();
    }

    public int keyCount() {
        return keys.size();
    }

    public boolean isFull() {
        return keyCount() > maxKeys;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public List<Integer> getKeys() {
        return keys;
    }

    public List<BStarNode> getChildren() {
        return children;
    }

    public int getMaxKeys() {
        return maxKeys;
    }
}