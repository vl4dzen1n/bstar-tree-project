package GUI;

import BStarTree.BStarNode;
import BStarTree.BStarTree;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BStarTreePanel extends JPanel {
    private final BStarTree tree;

    public BStarTreePanel(BStarTree tree) {
        this.tree = tree;
        setPreferredSize(new Dimension(900, 500));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        List<List<BStarNode>> levels = tree.getLevels();
        if (levels.isEmpty()) return;

        int panelW = getWidth();
        int panelH = getHeight();
        int levelsCount = levels.size();
        int vGap = Math.max(40, panelH / (levelsCount + 1));

        for (int lvl = 0; lvl < levelsCount; lvl++) {
            List<BStarNode> row = levels.get(lvl);
            int n = row.size();
            int y = 30 + lvl * vGap;

            for (int i = 0; i < n; i++) {
                BStarNode node = row.get(i);
                int nodeW = Math.max(40 + node.keyCount() * 30, 60);
                int x = (int) ((i + 1) * panelW / (n + 1) - nodeW / 2.0);

                g.setColor(new Color(240, 240, 255));
                g.fillRoundRect(x, y, nodeW, 30, 8, 8);
                g.setColor(Color.BLACK);
                g.drawRoundRect(x, y, nodeW, 30, 8, 8);

                int segW = nodeW / Math.max(1, node.keyCount());
                for (int k = 0; k < node.keyCount(); k++) {
                    int tx = x + k * segW + 4;
                    String txt = node.getKeys().get(k).toString();
                    g.drawString(txt, tx, y + 20);
                    if (k > 0) g.drawLine(x + k * segW, y, x + k * segW, y + 30);
                }

                if (!node.isLeaf()) {
                    int childY = y + vGap;
                    int childCount = node.getChildren().size();
                    for (int c = 0; c < childCount; c++) {
                        int childX = (int) ((c + 1) * panelW / (Math.max(1, childCount) + 1));
                        int fromX = x + nodeW / 2;
                        int fromY = y + 30;
                        g.drawLine(fromX, fromY, childX, childY);
                    }
                }
            }
        }
    }
}