package GUI;
import BStarTree.BStarTree;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class BStarTreeDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String tInput = JOptionPane.showInputDialog(
                    null,
                    "Введите параметр t (min степень дерева, >= 2):",
                    "Параметр B*-дерева",
                    JOptionPane.QUESTION_MESSAGE
            );

            int t = 3;
            try {
                if (tInput != null && !tInput.trim().isEmpty()) {
                    t = Integer.parseInt(tInput.trim());
                    if (t < 2) {
                        JOptionPane.showMessageDialog(null, "t должно быть >= 2. Используется t=3.");
                        t = 3;
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Неверный формат числа. Используется t=3.");
                t = 3;
            }

            createAndShowGUI(t);
        });
    }

    private static void createAndShowGUI(int t) {
        BStarTree tree = new BStarTree(t);

        JFrame frame = new JFrame("B*-дерево — demo (t=" + t + ")");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        BStarTreePanel panel = new BStarTreePanel(tree);
        frame.add(panel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JTextField input = new JTextField(10);
        JButton insertBtn = new JButton("Insert");
        JButton searchBtn = new JButton("Search");
        JButton clearBtn = new JButton("Clear");
        JLabel status = new JLabel(" ");

        insertBtn.addActionListener((ActionEvent e) -> {
            String s = input.getText().trim();
            try {
                int v = Integer.parseInt(s);
                tree.insert(v);
                panel.repaint();
                status.setText("Inserted " + v);
            } catch (NumberFormatException ex) {
                status.setText("Bad number");
            }
        });

        searchBtn.addActionListener((ActionEvent e) -> {
            String s = input.getText().trim();
            try {
                int v = Integer.parseInt(s);
                boolean found = tree.search(v);
                status.setText(found ? "Found " + v : "Not found " + v);
            } catch (NumberFormatException ex) {
                status.setText("Bad number");
            }
        });

        clearBtn.addActionListener(e -> {
            BStarTree newTree = new BStarTree(t);
            frame.getContentPane().remove(panel);
            BStarTreePanel newPanel = new BStarTreePanel(newTree);
            frame.add(newPanel, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
        });

        controls.add(new JLabel("Key:"));
        controls.add(input);
        controls.add(insertBtn);
        controls.add(searchBtn);
        controls.add(clearBtn);
        controls.add(status);

        frame.add(controls, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
