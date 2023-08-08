package main.java.ui;

import main.java.member.Account;
import main.java.ui.customized.BackgroundImage;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import main.java.IMAGES;

public class Home extends JFrame implements ActionListener {

    private final BackgroundImage background = new BackgroundImage(IMAGES.HOME_BACKGROUND);

    final Icon logoFpoly = new ImageIcon(IMAGES.HOME_LOGO);
    final Icon logout_icon = new ImageIcon(IMAGES.HOME_LOGOUT);

    final Dimension fullscreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    final Font myFont = new Font("Tahoma", Font.BOLD, 18);

    public Home() throws HeadlessException {
        super("Home");
        this.setBackgroundImage();
        this.initUI();
    }

    private void setBackgroundImage() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = (int) fullscreenSize.getWidth();
                int height = (int) fullscreenSize.getHeight();
                background.getScaledImage(width, height, Image.SCALE_SMOOTH);
            }
        });
    }

    private void initUI() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setType(Type.NORMAL);

        this.setMaximumSize(fullscreenSize);
        this.setMinimumSize(new Dimension(800, 800));
        this.setResizable(false);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        background.setBackground(Color.white);

        header = new JPanel(new BorderLayout(0, 0));
        header.setBackground(Color.white);

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 50, 20));

        leftPanel.setOpaque(false);
        logoLabel = new JLabel(logoFpoly);
        logoLabel.setFont(myFont);
        leftPanel.add(logoLabel);

        CBDT = new JButton("Manager");
        CBDT.setFont(myFont);
        CBDT.setFocusPainted(false);
        CBDT.setBorder(null);
        CBDT.setOpaque(false);
        CBDT.setCursor(new Cursor(Cursor.HAND_CURSOR));
        CBDT.addActionListener(this);
        leftPanel.add(CBDT);

        SV = new JButton("Student");
        SV.setFont(myFont);
        SV.setFocusPainted(false);
        SV.setBorder(null);
        SV.setOpaque(false);
        SV.setCursor(new Cursor(Cursor.HAND_CURSOR));
        SV.addActionListener(this);
        leftPanel.add(SV);

        GV = new JButton("Teacher");
        GV.setFont(myFont);
        GV.setFocusPainted(false);
        GV.setBorder(null);
        GV.setOpaque(false);
        GV.setCursor(new Cursor(Cursor.HAND_CURSOR));
        GV.addActionListener(this);
        leftPanel.add(GV);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 50, 45));
        rightPanel.setOpaque(false);

        JButton logoutBtn = new JButton("Log out");

        logoutBtn.setIcon(logout_icon);
        logoutBtn.setFont(myFont);
        logoutBtn.setForeground(Color.blue);
        logoutBtn.setBorder(null);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setOpaque(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        logoutBtn.setVerticalTextPosition(JButton.CENTER);
        logoutBtn.setHorizontalTextPosition(JButton.LEFT);
        logoutBtn.setIconTextGap(20);

        logoutBtn.addActionListener((act) -> logout());

        rightPanel.add(logoutBtn);
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        this.add(header, BorderLayout.NORTH);
        this.add(background, BorderLayout.CENTER);
    }

    private JPanel header;
    private JLabel logoLabel;
    private JButton CBDT, SV, GV;

    @Override
    public void actionPerformed(ActionEvent e) {
        String roleChoose = e.getActionCommand();

        boolean checkRole = roleChoose.equalsIgnoreCase(Account.roleSaved);

        switch (roleChoose) {
            case "Manager" -> {
                SwingUtilities.invokeLater(() -> {
                    new ManagerUI(checkRole).setVisible(true);
                });
            }

            case "Student" -> {
                if (QnA() == 0) {
                    SwingUtilities.invokeLater(() -> {
                        new TeacherUI(!checkRole).setVisible(true);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        new ManagerUI(checkRole).setVisible(true);
                    });
                }
            }

            case "Teacher" -> {
                SwingUtilities.invokeLater(() -> {
                    new TeacherUI(checkRole).setVisible(true);
                });
            }

            default ->
                throw new AssertionError();
        }
//        this.dispose();
    }

    private void logout() {
        if (Account.isRemembered) {
            EventQueue.invokeLater(() -> {
                new LoginUI(
                        Account.usernameSaved,
                        Account.passwordSaved,
                        Account.isRemembered
                ).setVisible(true);
            });
        } else {
            EventQueue.invokeLater(() -> {
                new LoginUI().setVisible(true);
            });
        }

        this.dispose();
    }

    private int QnA() {
        Object[] options = {"Xem điểm", "Xem thông tin"};
        int x = JOptionPane.showOptionDialog(null,
                "What do you want ?",
                "Click a button",
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);
        return x;
    }
}
