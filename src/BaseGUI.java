import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class BaseGUI extends JFrame {
    protected static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    protected static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    protected static final Color SUCCESS_COLOR = new Color(39, 174, 96);
    protected static final Color DANGER_COLOR = new Color(231, 76, 60);
    protected static final Color WARNING_COLOR = new Color(241, 196, 15);
    protected static final Color INFO_COLOR = new Color(52, 152, 219);
    protected static final Color BG_COLOR = new Color(236, 240, 241);
    protected static final Color PURPLE_COLOR = new Color(155, 89, 182);
    protected static final Color TEAL_COLOR = new Color(26, 188, 156);

    protected JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    protected JButton createStyledButton(String text, Color bgColor, Dimension size) {
        JButton button = createStyledButton(text, bgColor);
        button.setPreferredSize(size);
        return button;
    }

    protected void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    protected void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    protected int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    protected void addFormField(JPanel panel, GridBagConstraints gbc, int row,
                                String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }

    protected void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    protected void centerWindow() {
        setLocationRelativeTo(null);
    }

    protected abstract void initializeComponents();
}