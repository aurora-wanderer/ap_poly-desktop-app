package main.java.ui;

import main.java.member.Account;
import main.java.ui.customized.*;
import main.java.controller.*;
import main.java.COLORS;
import main.java.FONTS;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;
import main.java.IMAGES;

public class LoginUI extends JFrame {

    private final Dimension FULLSCREEN
            = Toolkit.getDefaultToolkit().getScreenSize();

    private final BackgroundImage background = new BackgroundImage(IMAGES.LOGIN_BACKGROUND);

    private final Validator validator = new Validator();
    static volatile Connector connector = new Connector("sa", "123456", "FPL_DaoTao");

    public LoginUI() throws HeadlessException {
        super("Login");
        JFrame.setDefaultLookAndFeelDecorated(false);
        this.setBackgroundImage();
        this.initUI();
        this.setAttributeComponents();
        this.setControls();
        this.setListeners();
    }

    public LoginUI(
            final String user,
            final String pwd,
            final boolean isChecked
    ) throws HeadlessException {
        this();
        username.setText(user);
        password.setText(pwd);
        remember.setSelected(isChecked);

        username.setForeground(COLORS.LOGIN_TEXT);
        password.setForeground(COLORS.LOGIN_TEXT);
    }

    private void setBackgroundImage() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = Window.getWindows()[0].getWidth();
                int height = Window.getWindows()[0].getHeight();
                background.getScaledImage(width, height, Image.SCALE_FAST);
                int GAP = (height - formPanel.getHeight()) / 2;

                background.setBorder(new EmptyBorder(GAP, 0, 0, 0));
            }
        });
    }

    private void initUI() {
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.setMinimumSize(new Dimension(800, 800));
        this.setMaximumSize(FULLSCREEN);

        this.setContentPane(background);
        this.setLocationRelativeTo(null);

        formPanel = new RoundedPanel(20);
        formLayout = new GroupLayout(formPanel);

        title = new JLabel();

        userPane = new JPanel();
        userLayout = new GroupLayout(userPane);
        userLabel = new JLabel();
        username = new JTextField();
        username_error_msg = new JLabel();

        passwordPane = new JPanel();
        passwordLayout = new GroupLayout(passwordPane);
        passwordLabel = new JLabel();
        password = new JPasswordField();
        password_error_msg = new JLabel();

        remember = new JCheckBox();

        loginBtn = new GradientButton("LOGIN");
    }

    private void setAttributeComponents() {
        this.setFocusable(true);
        // set attribute for all components
        formPanel.setBackground(COLORS.GLOBAL_BACKGROUND);
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(65, 55, 54, 55));
        // <editor-fold defaultstate="collapsed" desc="Tilte Form">
        title.setFont(FONTS.GLOBAL_TITLE);
        title.setForeground(COLORS.LOGIN_TEXT);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setText("Login");
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 49, 0));
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Username">
        userPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 23, 0));
        userPane.setBackground(COLORS.GLOBAL_BACKGROUND);

        userLabel.setFont(FONTS.LOGIN_LABEL);
        userLabel.setForeground(COLORS.LOGIN_TEXT);
        userLabel.setLabelFor(username);
        userLabel.setText("Username");

        username.setForeground(Color.lightGray);
        username.setFocusable(false);
        username.setPreferredSize(new Dimension(380, 40));
        username.setFont(FONTS.LOGIN_INPUT);

        username_error_msg.setFont(FONTS.LOGIN_ERROR_MESSAGE);
        username_error_msg.setForeground(COLORS.LOGIN_ERROR_MESSAGE);
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Password">
        passwordPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 23, 0));
        passwordPane.setBackground(COLORS.GLOBAL_BACKGROUND);

        passwordLabel.setFont(FONTS.LOGIN_LABEL);
        passwordLabel.setForeground(COLORS.LOGIN_TEXT);
        passwordLabel.setLabelFor(password);
        passwordLabel.setText("Password");

        password_error_msg.setPreferredSize(new Dimension(300, 40));
        password_error_msg.setFont(FONTS.LOGIN_ERROR_MESSAGE);
        password_error_msg.setForeground(COLORS.LOGIN_ERROR_MESSAGE);

        password.setFont(FONTS.LOGIN_INPUT);
        password.setFocusable(false);
        password.setForeground(Color.lightGray);

        password.setLayout(new FlowLayout(FlowLayout.RIGHT));
        password.add(showHiddenText(password));

        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Checkbox remember">
        remember.setFont(FONTS.LOGIN_LABEL);
        remember.setText("Remember");
        remember.setSelected(false);
        remember.setFocusable(false);
        remember.setBorder(new EmptyBorder(0, 0, 35, 12));
        remember.setHorizontalAlignment(SwingConstants.RIGHT);
        remember.setBackground(COLORS.GLOBAL_BACKGROUND);
        remember.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="LoginUI button">
        loginBtn.setFont(FONTS.LOGIN_BUTTON);
        loginBtn.setFocusable(false);
        loginBtn.setStartColor(COLORS.LOGIN_LEFT_GRADIENT_BUTTON);
        loginBtn.setEndColor(COLORS.LOGIN_RIGHT_GRADIENT_BUTTON);
        loginBtn.setGradientFocus(500);
        loginBtn.setForeground(Color.white);
        loginBtn.setCursor(new Cursor(java.awt.Cursor.HAND_CURSOR));
        // </editor-fold>
        background.add(formPanel);
    }

    private JButton showHiddenText(JPasswordField pwd) {
        JButton show = new JButton();
        ImageIcon showIcon = new ImageIcon(IMAGES.LOGIN_SHOW_ICON);
        ImageIcon hiddenIcon = new ImageIcon(IMAGES.LOGIN_HIDDEN_ICON);

        show.setIcon(showIcon);
        show.setBorder(null);
        show.setFocusPainted(false);
        show.setOpaque(false);
        show.setFocusable(false);
        show.setCursor(new Cursor(Cursor.HAND_CURSOR));
        show.setFont(FONTS.LOGIN_LABEL);

        final char defaultEchoChar = pwd.getEchoChar();

        show.addActionListener((act) -> {
            boolean echoCharIsSet = pwd.echoCharIsSet();
            if (echoCharIsSet) {
                pwd.setEchoChar((char) 0);
                show.setIcon(hiddenIcon);
            } else {
                pwd.setEchoChar(defaultEchoChar);
                show.setIcon(showIcon);
            }
        });

        return show;
    }

    private void setListeners() {
        username.addMouseListener(new MouseAdapterImpl());
        username.addFocusListener(new FocusAdapterImpl());
        username.addKeyListener(new KeyAdapterImpl());

        password.addMouseListener(new MouseAdapterImpl());
        password.addFocusListener(new FocusAdapterImpl());
        password.addKeyListener(new KeyAdapterImpl());

        this.addMouseListener(new MouseAdapterImpl());
        this.addKeyListener(new KeyAdapterImpl());

        formPanel.addMouseListener(new MouseAdapterImpl());

        loginBtn.addActionListener((act) -> login());
        loginBtn.addMouseListener(new MouseAdapterImpl());
    }

    private void setControls() {
        userNameControl();
        passWordControl();
        formControl();
    }

    /* Put components with group by GroupLayout*/
    // <editor-fold defaultstate="collapsed" desc="Username Controller">
    private void userNameControl() {
        userPane.setLayout(userLayout);
        userLayout.setHorizontalGroup(
                userLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(userLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(userLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        )
                        .addGroup(userLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(username)
                                .addContainerGap()
                        )
                        .addGroup(userLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(username_error_msg)
                                .addContainerGap()
                        )
        );

        userLayout.setVerticalGroup(
                userLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(userLayout.createSequentialGroup()
                                .addComponent(userLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(username, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(username_error_msg, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
        );
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Password Controller">
    private void passWordControl() {
        passwordPane.setLayout(passwordLayout);
        passwordLayout.setHorizontalGroup(
                passwordLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(passwordLayout.createSequentialGroup()
                                .addGap(7, 7, 7)
                                .addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(passwordLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(password)
                                .addContainerGap())
                        .addGroup(passwordLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(password_error_msg)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        passwordLayout.setVerticalGroup(
                passwordLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(passwordLayout.createSequentialGroup()
                                .addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(password, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(password_error_msg, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE))
        );
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Form Controller">
    private void formControl() {
        formPanel.setLayout(formLayout);
        formLayout.setHorizontalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(title, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(userPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(passwordPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(GroupLayout.Alignment.TRAILING,
                                formLayout.createSequentialGroup()
                                        .addComponent(remember))
                        .addComponent(loginBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        formLayout.setVerticalGroup(
                formLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(formLayout.createSequentialGroup()
                                .addComponent(title, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(userPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(passwordPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(remember, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(loginBtn, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
    }
    // </editor-fold>

    private JPanel formPanel, userPane, passwordPane;
    private JLabel title, userLabel, passwordLabel;
    private JLabel password_error_msg, username_error_msg;
    private JTextField username;
    private JPasswordField password;
    private JCheckBox remember;
    private GradientButton loginBtn;
    private GroupLayout formLayout, userLayout, passwordLayout;

    private boolean isValidate(final JTextField text) {
        if (validator.isRequired(text, "Please enter username!")) {
            text.setBackground(COLORS.LOGIN_WARNING);
            username_error_msg.setText(validator.getMessage());
            return false;
        }

        if (!(validator.isID(text, "Username is invalid",
                Pattern.compile("^((\\w){2}){1}\\d{5}$"))
                || validator.isEmail(text.getText(), "", "fpt.edu.vn"))) {
            text.setBackground(COLORS.LOGIN_WARNING);
            username_error_msg.setText(validator.getMessage());
            return false;
        }

        return true;
    }

    private boolean isValidate(final JPasswordField pass) {
        if (validator.isRequired(pass, "Please enter password!")) {
            pass.setBackground(COLORS.LOGIN_WARNING);
            password_error_msg.setText(validator.getMessage());
            return false;
        }

        if (!validator.isPassword(pass, "Password must be 6 - 12 characters!",
                Pattern.compile("^.{6,12}$"))) {
            pass.setBackground(COLORS.LOGIN_WARNING);
            password_error_msg.setText(validator.getMessage());
            return false;
        }

        return true;
    }

    private boolean validation() {
        return isValidate(username) && isValidate(password);
    }

    private void rememberCheck() {
        if (remember.isSelected()) {
            Account.usernameSaved = username.getText();
            Account.passwordSaved = String.valueOf(password.getPassword());
        }
        Account.isRemembered = remember.isSelected();
    }

    private void login() {
        // connect to user and password and your database
        if (validation() && connector.isConnected()) {
            System.out.println("Connect Server Successfully");

            var accounts = connector.callProc("find_account");

            String usernameValue = username.getText();
            String passwordValue = String.valueOf(password.getPassword());

            //check username and password 
            boolean accountValid = accounts.stream().anyMatch(row
                    -> (row.get(0).equalsIgnoreCase(usernameValue)
                            || row.get(3).equalsIgnoreCase(usernameValue))
                    && row.get(1).equalsIgnoreCase(passwordValue)
            );

            if (accountValid) {
                System.out.println("Login Successfully");
                rememberCheck(); // if remember's checkbox has ticked?

                // save role of that account
                Account.roleSaved = accounts
                        .stream()
                        .filter(row -> row.get(0).equalsIgnoreCase(usernameValue) 
                            || row.get(3).equalsIgnoreCase(usernameValue))
                        .flatMap(plist -> plist.stream())
                        .collect(Collectors.toList())
                        .get(2);

                SwingUtilities.invokeLater(() -> {
                    new Home().setVisible(true);
                    this.dispose();
                });
            } else {
                System.out.println("Login Failed");
                JOptionPane.showMessageDialog(this, "Username or password incorrectly!");
            }
        }
    }

    private class MouseAdapterImpl extends MouseAdapter {

        public MouseAdapterImpl() {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mouseClicked(e);
            username.setFocusable(true);
            password.setFocusable(true);

            if (e.getSource() == username) {
                username.requestFocus();
            } else if (e.getSource() == password) {
                password.requestFocus();
            } else {
                username.setFocusable(false);
                password.setFocusable(false);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() == loginBtn) {
                loginBtn.setStartColor(COLORS.LOGIN_RIGHT_GRADIENT_BUTTON);
                loginBtn.setEndColor(COLORS.LOGIN_LEFT_GRADIENT_BUTTON);
            }
            super.mouseEntered(e);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() == loginBtn) {
                loginBtn.setStartColor(COLORS.LOGIN_LEFT_GRADIENT_BUTTON);
                loginBtn.setEndColor(COLORS.LOGIN_RIGHT_GRADIENT_BUTTON);
            }
            super.mouseExited(e);
        }
    }

    private class FocusAdapterImpl extends FocusAdapter {

        public FocusAdapterImpl() {
        }

        @Override
        public void focusGained(FocusEvent e) {
            if (e.getSource() == username) {
                username.setBackground(COLORS.GLOBAL_BACKGROUND);
                username.setForeground(COLORS.LOGIN_TEXT);
                username.setSelectionStart(0);
                username.setSelectionEnd(username.getText().length());
                username_error_msg.setText("");
            }

            if (e.getSource() == password) {
                password.setBackground(COLORS.GLOBAL_BACKGROUND);
                password.setForeground(COLORS.LOGIN_TEXT);
                password.setSelectionStart(0);
                password.setSelectionEnd(Arrays.toString(password.getPassword()).length());
                password_error_msg.setText("");
            }
        }

        @Override
        public void focusLost(FocusEvent e) {
            if (e.getSource() == username && !isValidate(username)) {
                username_error_msg.setText(validator.getMessage());
            }

            if (e.getSource() == password && !isValidate(password)) {
                password_error_msg.setText(validator.getMessage());
            }
        }
    }

    private class KeyAdapterImpl extends KeyAdapter {

        public KeyAdapterImpl() {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                login();
            }
        }
    }
}
