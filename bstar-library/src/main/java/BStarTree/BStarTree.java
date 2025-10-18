package BStarTree;

import java.util.ArrayList;
import java.util.List;

public class BStarTree {
    private BStarNode root;
    private final int t;
    private final int maxKeys;

    public BStarTree(int t) {
        if (t < 2) throw new IllegalArgumentException("t must be >= 2");
        this.t = t;
        this.maxKeys = 2 * t - 1;
        this.root = new BStarNode(maxKeys, true);
    }

    public BStarNode getRoot() {
        return root;
    }

    public boolean search(int k) {
        return searchRecursive(root, k);
    }

    private boolean searchRecursive(BStarNode node, int k) {
        int i = 0;
        while (i < node.keyCount() && k > node.getKeys().get(i)) i++;
        if (i < node.keyCount() && node.getKeys().get(i) == k) return true;
        if (node.isLeaf()) return false;
        return searchRecursive(node.getChildren().get(i), k);
    }

    public void insert(int k) {
        if (search(k)) return;
        BStarNode r = root;
        if (r.keyCount() == maxKeys) {
            BStarNode s = new BStarNode(maxKeys, false);
            s.getChildren().add(r);
            root = s;
            splitChildWithRedistribute(s, 0);
            insertNonFull(s, k);
        } else {
            insertNonFull(r, k);
        }
    }

    private void insertNonFull(BStarNode node, int k) {
        int i = node.keyCount() - 1;
        if (node.isLeaf()) {
            node.getKeys().add(null);
            while (i >= 0 && k < node.getKeys().get(i)) {
                node.getKeys().set(i + 1, node.getKeys().get(i));
                i--;
            }
            node.getKeys().set(i + 1, k);
        } else {
            while (i >= 0 && k < node.getKeys().get(i)) i--;
            i++;
            BStarNode child = node.getChildren().get(i);
            if (child.keyCount() == maxKeys) {
                boolean redistributed = tryRedistribute(node, i);
                if (!redistributed) {
                    splitChildWithRedistribute(node, i);
                }
                if (i < node.keyCount() && k > node.getKeys().get(i)) i++;
            }
            insertNonFull(node.getChildren().get(i), k);
        }
    }

    private boolean tryRedistribute(BStarNode parent, int childIndex) {
        BStarNode child = parent.getChildren().get(childIndex);
        if (childIndex - 1 >= 0) {
            BStarNode left = parent.getChildren().get(childIndex - 1);
            if (left.keyCount() < maxKeys) {
                int parentKey = parent.getKeys().get(childIndex - 1);
                child.getKeys().add(0, parentKey);
                if (!left.isLeaf()) {
                    BStarNode moved = left.getChildren().remove(left.getChildren().size() - 1);
                    child.getChildren().add(0, moved);
                }
                int movedKeyFromLeft = left.getKeys().remove(left.getKeys().size() - 1);
                parent.getKeys().set(childIndex - 1, movedKeyFromLeft);
                return true;
            }
        }
        if (childIndex + 1 < parent.getChildren().size()) {
            BStarNode right = parent.getChildren().get(childIndex + 1);
            if (right.keyCount() < maxKeys) {
                int parentKey = parent.getKeys().get(childIndex);
                right.getKeys().add(0, parentKey);
                if (!right.isLeaf()) {
                    BStarNode moved = right.getChildren().remove(0);
                    child.getChildren().add(moved);
                }
                int movedKeyFromRight = right.getKeys().remove(1);
                parent.getKeys().set(childIndex, movedKeyFromRight);
                return true;
            }
        }
        return false;
    }

    private void splitChildWithRedistribute(BStarNode parent, int index) {
        BStarNode y = parent.getChildren().get(index);
        int medianIndex = y.keyCount() / 2;
        int medianKey = y.getKeys().get(medianIndex);

        BStarNode z = new BStarNode(maxKeys, y.isLeaf());
        for (int j = medianIndex + 1; j < y.keyCount(); j++) {
            z.getKeys().add(y.getKeys().get(j));
        }
        if (!y.isLeaf()) {
            for (int j = medianIndex + 1; j < y.getChildren().size(); j++) {
                z.getChildren().add(y.getChildren().get(j));
            }
        }
        for (int j = y.keyCount() - 1; j >= medianIndex; j--) y.getKeys().remove(j);
        if (!y.isLeaf()) {
            for (int j = y.getChildren().size() - 1; j >= medianIndex + 1; j--) y.getChildren().remove(j);
        }

        parent.getChildren().add(index + 1, z);
        parent.getKeys().add(index, medianKey);
    }

    public List<List<BStarNode>> getLevels() {
        List<List<BStarNode>> levels = new ArrayList<>();
        if (root == null) return levels;
        List<BStarNode> cur = new ArrayList<>();
        cur.add(root);
        while (!cur.isEmpty()) {
            levels.add(new ArrayList<>(cur));
            List<BStarNode> next = new ArrayList<>();
            for (BStarNode n : cur) {
                if (!n.isLeaf()) next.addAll(n.getChildren());
            }
            cur = next;
        }
        return levels;
    }
}