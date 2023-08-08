package main.java.ui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import main.java.COLORS;
import main.java.FONTS;
import main.java.IMAGES;
import main.java.controller.Connector;
import main.java.controller.Validator;
import main.java.member.Student;

public class ManagerUI extends JFrame {

    private final Dimension FULLSCREEN = Toolkit.getDefaultToolkit()
            .getScreenSize();

    private final String[] OPTIONS_SEARCH = new String[]{
        "Search by", "MASV", "Name", "Email"
    };

    private String value_search = OPTIONS_SEARCH[0];

    private final Connector connector = LoginUI.connector;
    private final Validator validator = new Validator();
    private Student student;

    private int indexOnTable = 0;

    public ManagerUI() throws HeadlessException {
        super("Manager");
        this.student = new Student();
        JFrame.setDefaultLookAndFeelDecorated(false);
        this.initUI();
        this.setupFrame();
        this.setLocationRelativeTo(null);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
                super.windowClosing(e);
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((JFrame) e.getSource()).requestFocus();
                super.mouseClicked(e);
            }
        });
    }

    public ManagerUI(boolean isManager) throws HeadlessException {
        this();
        this.student = new Student();
        if (!isManager) {
            content.disabled();
        }
    }

    private void initUI() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);
        this.setMinimumSize(new Dimension(900, FULLSCREEN.height));
        this.setMaximumSize(FULLSCREEN);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);

        main = new JPanel();
        header = new Header();
        content = new Content();
        content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
    }

    private void setupFrame() {
        this.add(main);

        main.setLayout(new BorderLayout(0, 0));
        main.setBackground(Color.white);
        main.add(header, BorderLayout.NORTH);
        main.add(content, BorderLayout.CENTER);
    }

    private JPanel main;
    private Header header;
    private Content content;

    class Header extends JPanel {

        public Header() {
            this.initHeader();
            this.setup();
            this.listen();
        }

        private void initHeader() {
            title = new JLabel();
            search_label = new JLabel();
            place_holder_search = new JLabel();
            search_pane = new JPanel();
            search_input = new JTextField();
            searchBtn = new JButton();
            searchOptions = new JComboBox<>();
        }

        private void setup() {
            this.setLayout(new BorderLayout(10, 10));
            this.setBorder(new EmptyBorder(20, 0, 20, 0));
            title.setHorizontalAlignment(JLabel.CENTER);
            title.setText("Student Management");
            title.setFont(FONTS.GLOBAL_TITLE);
            title.setForeground(Color.red);
            title.setHorizontalAlignment(JLabel.CENTER);

            search_pane.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
            search_pane.setOpaque(false);
            search_pane.setBorder(new EmptyBorder(5, 5, 5, 5));

            search_label.setFont(FONTS.MANAGER_LABEL_HEADER);
            search_label.setText("Seacrh: ");
            search_label.setForeground(COLORS.MANAGER_TEXT);

            search_input.setLayout(new FlowLayout(FlowLayout.LEFT, 0, -2));
            search_input.setMinimumSize(new Dimension(100, 30));
            search_input.setPreferredSize(new Dimension(300, 30));
            search_input.setFont(FONTS.MANAGER_INPUT_HEADER);

            place_holder_search.setText("Type something...");
            place_holder_search.setOpaque(false);
            place_holder_search.setFont(FONTS.MANAGER_INPUT_HEADER);
            place_holder_search.setPreferredSize(new Dimension(260, 30));
            place_holder_search.setForeground(Color.lightGray);

            search_input.add(place_holder_search);
            search_input.add(searchBtn);

            searchBtn.setIcon(new ImageIcon(IMAGES.FORM_HEADER_SEARCH));
            searchBtn.setFocusable(false);
            searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            searchBtn.setFocusPainted(false);
            searchBtn.setBorder(null);
            searchBtn.setOpaque(false);

            searchOptions.setPreferredSize(new Dimension(120, 30));
            searchOptions.setFocusable(false);
            searchOptions.setFont(FONTS.MANAGER_LABEL_HEADER);
            for (String item : OPTIONS_SEARCH) {
                searchOptions.addItem(item);
            }

            error_options = new JLabel(" <- Chưa chọn nè");
            error_options.setFont(FONTS.MANAGER_LABEL_HEADER);
            error_options.setForeground(Color.red);
            error_options.setVisible(false);

            search_pane.add(search_label);
            search_pane.add(search_input);
            search_pane.add(searchOptions);
            search_pane.add(error_options);

            this.add(title, BorderLayout.NORTH);
            this.add(search_pane, BorderLayout.CENTER);

        }

        private void listen() {
            search_input.addKeyListener(new KeyAdapterImpl("Type anything..."));
            searchOptions.addItemListener((ItemEvent e) -> {
                value_search = e.getItem().toString();
                if (!value_search.equalsIgnoreCase(OPTIONS_SEARCH[0])) {
                    error_options.setVisible(false);
                }
            });
            searchBtn.addActionListener((act) -> search());
            search_input.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                        search();
                    }
                }
            });
        }

        private void search() {
            if (search_input.getText().isEmpty()) {
                return;
            }

            if (value_search.equalsIgnoreCase(OPTIONS_SEARCH[0])) {
                error_options.setVisible(true);
                return;
            }

            error_options.setVisible(false);
            String id_need_compare = search_input.getText();
            findAndFill(id_need_compare);
            validation();
        }

        private void findAndFill(String value) {
            ArrayList<Student> search_table = new ArrayList<>();
            search_table.addAll(content.tableManager.rows);

            java.util.List<Student> collect = search_table.stream()
                    .filter(student -> student.getID().equalsIgnoreCase(value)
                    || student.getName().equalsIgnoreCase(value)
                    || student.getEmail().equalsIgnoreCase(value))
                    .collect(Collectors.toList());

            if (collect.isEmpty()) {
                JOptionPane.showMessageDialog(content.getParent(), "Không tìm thấy!");
                return;
            }

            search_input.setText(null);
            searchOptions.setSelectedIndex(0);
            try {
                Robot rb = new Robot();
                rb.keyPress(KeyEvent.VK_BACK_SPACE);
            } catch (AWTException ex) {
                Logger.getLogger(ManagerUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            content.tableManager.studentTable
                    .getSelectionModel().clearSelection();

            int indexOf = content.tableManager.rows.indexOf(collect.get(0));
            content.tableManager.fillForm(collect);
            content.tableManager.setHighLight(indexOf);
        }

        private class KeyAdapterImpl extends KeyAdapter {

            String pl_text;

            public KeyAdapterImpl() {
            }

            public KeyAdapterImpl(String pl_text) {
                this.pl_text = pl_text;
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                String value = search_input.getText();
                if (value.isEmpty()) {
                    place_holder_search.setText(this.pl_text);
                    return;
                }
                place_holder_search.setText(null);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String value = search_input.getText();
                if (value.isEmpty()) {
                    place_holder_search.setText(this.pl_text);
                    return;
                }
                place_holder_search.setText(null);
            }
        }

        private JLabel title, search_label, place_holder_search, error_options;
        private JPanel search_pane;
        private JTextField search_input;
        private JButton searchBtn;
        private JComboBox<String> searchOptions;
    }

    class Content extends JPanel {

        public Content() {
            this.createContent();
            this.setListeners();
        }

        public void setEnabledComponent(boolean isEnabled) {
            Arrays.asList(eastButtons.getComponents()).stream()
                    .filter(cmp -> cmp instanceof JButton)
                    .forEach(item -> item.setEnabled(false));
        }

        private void createContent() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 50));
            form = new Form();
            east = new JPanel();
            tableManager = new Table();

            imgPane = new JPanel();
            imgPane.setLayout(new BorderLayout(20, 20));
            imgPane.setBorder(new EmptyBorder(0, 50, 0, 50));
            avatar = new JLabel();
            avatar.setBorder(new EmptyBorder(20, 0, 20, 0));
            avatar.setPreferredSize(new Dimension(350, 300));

            JButton choseImage = new JButton("Chose Image");
            choseImage.setFont(FONTS.MANAGER_BUTTON_CONTENT);
            choseImage.setHorizontalAlignment(JButton.CENTER);
            choseImage.setFocusPainted(false);
            choseImage.setMinimumSize(new Dimension(200, 30));
            choseImage.setMaximumSize(new Dimension(300, 50));
            choseImage.setPreferredSize(new Dimension(200, 30));

            choseImage.addActionListener((act) -> openChoseFile());

            imgPane.add(avatar, BorderLayout.NORTH);
            imgPane.add(choseImage, BorderLayout.SOUTH);
            east.setLayout(new GridLayout(2, 1, 0, 10));
            east.setBorder(new EmptyBorder(0, 0, 100, 100));

            east.setPreferredSize(new Dimension(400, 200));
            buttonFunc();

            east.add(imgPane, BorderLayout.NORTH);
            east.add(eastButtons, BorderLayout.CENTER);

            this.add(form);
            this.add(east);
            this.add(tableManager);
        }

        private void buttonFunc() {
            eastButtons = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.fill = GridBagConstraints.BOTH;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(15, 15, 15, 15);

            newBtn = new JButton("New", new ImageIcon(IMAGES.FORM_NEW_BUTTON));
            saveBtn = new JButton("Save", new ImageIcon(IMAGES.FORM_SAVE_BUTTON));
            updateBtn = new JButton("Update", new ImageIcon(IMAGES.FORM_UPDATE_BUTTON));
            deletebtn = new JButton("Delete", new ImageIcon(IMAGES.FORM_DELETE_BUTTON));

            newBtn.setFocusable(false);
            saveBtn.setFocusable(false);
            updateBtn.setFocusable(false);
            deletebtn.setFocusable(false);

            newBtn.setFont(FONTS.MANAGER_BUTTON_CONTENT);
            saveBtn.setFont(FONTS.MANAGER_BUTTON_CONTENT);
            updateBtn.setFont(FONTS.MANAGER_BUTTON_CONTENT);
            deletebtn.setFont(FONTS.MANAGER_BUTTON_CONTENT);

            gbc.gridx = 0;
            gbc.gridy = 0;

            eastButtons.add(newBtn, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            eastButtons.add(saveBtn, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            eastButtons.add(updateBtn, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            eastButtons.add(deletebtn, gbc);
        }

        private Form form;
        private JPanel east, eastButtons, imgPane;
        private JLabel avatar;
        private JButton newBtn, saveBtn, updateBtn, deletebtn;
        private Table tableManager;

        private void disabled() {
            content.disabledElements(eastButtons.getComponents());
            content.disabledElements(form.getComponents());
            content.form.taAddress.setEnabled(false);
        }

        private void disabledElements(Component[] components) {
            for (Component component : components) {
                if (component != null) {
                    if (component instanceof JPanel pane) {
                        disabledElements(pane.getComponents());
                    }
                    component.setEnabled(false);
                }
            }
        }

        private void setListeners() {
            newBtn.addActionListener((act) -> cleanForm());
            saveBtn.addActionListener((act) -> addStudent());
            updateBtn.addActionListener((act) -> updateForm());
            deletebtn.addActionListener((act) -> deleteStudent());
        }

        private void addStudent() {
            if (idExisted()) {
                return;
            }

            if (validation()) {
                student.setID(this.form.txtID.getText());
                student.setName(this.form.txtName.getText());
                student.setPhone(this.form.txtPhone.getText());
                student.setEmail(this.form.txtEmail.getText());
                student.setGender(this.form.maleBtn.isSelected() ? "0" : "1");
                student.setAddress(this.form.taAddress.getText());
                student.setAvatar(this.avatar.getName());
                this.tableManager.rows.add(student);
                this.tableManager.model.addRow(student.toArray());
                cleanForm();
            }
        }

        private boolean idExisted() throws HeadlessException {
            boolean idValid = this.tableManager.rows
                    .stream()
                    .anyMatch(std -> std.getID()
                    .equalsIgnoreCase(form.txtID.getText())
                    );
            if (idValid) {
                JOptionPane.showMessageDialog(this.getParent(), "ID đã tồn tại!");
                return true;
            }
            return false;
        }

        private void cleanForm() {
            indexOnTable = 0;
            student = new Student();
            this.form.txtID.setText("");
            this.form.txtName.setText("");
            this.form.txtPhone.setText("");
            this.form.txtEmail.setText("");
            this.form.taAddress.setText("");
            this.form.genderGroup.clearSelection();
            this.tableManager.studentTable.getSelectionModel().clearSelection();
            this.avatar.setIcon(null);
            content.form.clearFocus();
        }

        private void updateForm() {
            java.util.List<Student> students = content.tableManager.rows;

            JTable tbl = content.tableManager.studentTable;
            int index = tbl.getSelectedRow();

            if (index < 0) {
                return;
            }

            Student student = students.get(index);

            student.setID(this.form.txtID.getText());
            student.setName(this.form.txtName.getText());
            student.setPhone(this.form.txtPhone.getText());
            student.setEmail(this.form.txtEmail.getText());
            student.setGender(this.form.maleBtn.isSelected() ? "0" : "1");
            student.setAddress(this.form.taAddress.getText());
            student.setAvatar(this.avatar.getName());
            students.remove(index);
            students.add(index, student);
            content.tableManager.fillTable();
            cleanForm();

        }

        private void deleteStudent() {
            ArrayList<Student> students = content.tableManager.rows;

            JTable tbl = content.tableManager.studentTable;
            int[] index = tbl.getSelectedRows();

            ArrayList<Student> temp = new ArrayList<>();
            for (int i : index) {
                temp.add(students.get(i));
            }

            students.removeAll(temp);
            content.tableManager.fillTable();
            cleanForm();
            content.form.clearFocus();
        }

        private void openChoseFile() {
            JFileChooser fc = new JFileChooser(new File("."));

            int res = fc.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsoluteFile().toString();
                String extension = path.substring(path.lastIndexOf(".") + 1, path.length());
                ImageIcon imageCreated
                        = new ImageIcon(Toolkit.getDefaultToolkit()
                                .createImage(path)
                                .getScaledInstance(150, 150, Image.SCALE_SMOOTH));

                if (extension.equals("jpg") || extension.equals("png")) {
                    avatar.setIcon(imageCreated);
                    String srcAvatar = path.substring(path.lastIndexOf("\\"), path.length())
                            .replace("\\", "/");
//                    student.setAvatar(srcAvatar);
                    avatar.setName(srcAvatar);
                } else {
                    JOptionPane.showMessageDialog(this, "This is not image file!");
                }
            }
        }
    }

    final class Form extends JPanel implements ActionListener {

        public Form() {
            this.initForm();
            this.setAttributes();
            this.setListeners();
        }

        private void initForm() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(new EmptyBorder(20, 20, 20, 20));

            this.setPreferredSize(new Dimension(500, 300));
            pnlID = new JPanel();
            lblID = new JLabel();
            txtID = new JTextField();

            pnlName = new JPanel();
            lblName = new JLabel();
            txtName = new JTextField();

            pnlEmail = new JPanel();
            lblEmail = new JLabel();
            txtEmail = new JTextField();

            pnlPhone = new JPanel();
            lblPhone = new JLabel();
            txtPhone = new JTextField();

            pnlGender = new JPanel();
            lblGender = new JLabel();
            genderGroup = new ButtonGroup();
            maleBtn = new JRadioButton();
            femaleBtn = new JRadioButton();

            pnlAddress = new JPanel();
            lblAddress = new JLabel();
            taAddress = new JTextArea();

            lblErrorID = new JLabel();
            lblErrorName = new JLabel();
            lblErrorEmail = new JLabel();
            lblErrorPhone = new JLabel();
            lblErrorGender = new JLabel();
            lblErrorAddress = new JLabel();

            lblErrorID.setFont(FONTS.MANAGER_ERROR_LABEL_FORM);
            lblErrorID.setForeground(Color.red);

            lblErrorName.setFont(FONTS.MANAGER_ERROR_LABEL_FORM);
            lblErrorName.setForeground(Color.red);

            lblErrorEmail.setFont(FONTS.MANAGER_ERROR_LABEL_FORM);
            lblErrorEmail.setForeground(Color.red);

            lblErrorPhone.setFont(FONTS.MANAGER_ERROR_LABEL_FORM);
            lblErrorPhone.setForeground(Color.red);

            lblErrorGender.setFont(FONTS.MANAGER_ERROR_LABEL_FORM);
            lblErrorGender.setForeground(Color.red);

            lblErrorAddress.setFont(FONTS.MANAGER_ERROR_LABEL_FORM);
            lblErrorAddress.setForeground(Color.red);

            footer = new JPanel();

            firstBtn = new JButton();
            previousBtn = new JButton();
            nextBtn = new JButton();
            lastBtn = new JButton();

            jsp = new JScrollPane();

            formControl(pnlID, lblID, txtID, lblErrorID);
            formControl(pnlName, lblName, txtName, lblErrorName);
            formControl(pnlEmail, lblEmail, txtEmail, lblErrorEmail);
            formControl(pnlPhone, lblPhone, txtPhone, lblErrorPhone);
            formControl(pnlGender, lblGender, maleBtn, femaleBtn);
            formControl(pnlAddress, lblAddress, jsp, lblErrorAddress);

            this.add(pnlID);
            this.add(pnlName);
            this.add(pnlPhone);
            this.add(pnlEmail);
            this.add(pnlGender);
            this.add(lblErrorGender);
            this.add(pnlAddress);
            this.add(footer);
        }

        private void setAttributes() {
            lblID.setText("ID");
            lblID.setFont(FONTS.MANAGER_LABEL_FORM);
            txtID.setFont(FONTS.MANAGER_INPUT_FORM);
            txtID.setMinimumSize(new Dimension(100, 30));
            txtID.setPreferredSize(new Dimension(100, 30));

            lblName.setText("Name");
            lblName.setFont(FONTS.MANAGER_LABEL_FORM);
            txtName.setFont(FONTS.MANAGER_INPUT_FORM);
            txtName.setMinimumSize(new Dimension(100, 30));
            txtName.setPreferredSize(new Dimension(100, 30));

            lblEmail.setText("Email");
            lblEmail.setFont(FONTS.MANAGER_LABEL_FORM);
            txtEmail.setFont(FONTS.MANAGER_INPUT_FORM);
            txtEmail.setMinimumSize(new Dimension(100, 30));
            txtEmail.setPreferredSize(new Dimension(100, 30));

            lblPhone.setText("Phone");
            lblPhone.setFont(FONTS.MANAGER_LABEL_FORM);
            txtPhone.setFont(FONTS.MANAGER_INPUT_FORM);
            txtPhone.setMinimumSize(new Dimension(100, 30));
            txtPhone.setPreferredSize(new Dimension(100, 30));

            lblGender.setText("Gender");
            lblGender.setFont(FONTS.MANAGER_LABEL_FORM);
            lblGender.setPreferredSize(new Dimension(60, 30));
            lblGender.setBorder(new EmptyBorder(0, 10, 0, 0));
            maleBtn.setText("Male");
            maleBtn.setFont(FONTS.MANAGER_LABEL_FORM);
            maleBtn.setBorder(new EmptyBorder(0, 25, 0, 0));
            maleBtn.setFocusable(false);
            femaleBtn.setText("Female");
            femaleBtn.setFont(FONTS.MANAGER_LABEL_FORM);
            femaleBtn.setBorder(new EmptyBorder(0, 50, 0, 0));
            femaleBtn.setFocusable(false);
            genderGroup.add(maleBtn);
            genderGroup.add(femaleBtn);

            lblAddress.setText("Address");
            lblAddress.setFont(FONTS.MANAGER_LABEL_FORM);
            lblAddress.setPreferredSize(new Dimension(60, 150));
            lblAddress.setVerticalAlignment(JLabel.TOP);
            taAddress.setPreferredSize(new Dimension(100, 150));
            jsp.setViewportView(taAddress);
            taAddress.setColumns(20);
            taAddress.setRows(8);
            taAddress.setFont(FONTS.MANAGER_INPUT_FORM);

            footer.setLayout(new FlowLayout(FlowLayout.LEFT, 35, 0));
            footer.setBorder(new EmptyBorder(0, 40, 50, 0));
            firstBtn = new JButton(new ImageIcon(IMAGES.FORM_FIRST));
            firstBtn.setBorder(null);
            previousBtn = new JButton(new ImageIcon(IMAGES.FORM_PREVIOUS));
            previousBtn.setBorder(null);
            nextBtn = new JButton(new ImageIcon(IMAGES.FORM_NEXT));
            nextBtn.setBorder(null);
            lastBtn = new JButton(new ImageIcon(IMAGES.FORM_LAST));
            lastBtn.setBorder(null);

            firstBtn.setFocusable(false);
            previousBtn.setFocusable(false);
            nextBtn.setFocusable(false);
            lastBtn.setFocusable(false);

            firstBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            previousBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            lastBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            firstBtn.setActionCommand("first");
            previousBtn.setActionCommand("previous");
            nextBtn.setActionCommand("next");
            lastBtn.setActionCommand("last");

            footer.add(firstBtn);
            footer.add(previousBtn);
            footer.add(nextBtn);
            footer.add(lastBtn);
        }

        private void setListeners() {
            firstBtn.addActionListener(this);
            previousBtn.addActionListener(this);
            nextBtn.addActionListener(this);
            lastBtn.addActionListener(this);
            txtID.addFocusListener(new FocusAdapterImpl());
            txtName.addFocusListener(new FocusAdapterImpl());
            txtPhone.addFocusListener(new FocusAdapterImpl());
            txtEmail.addFocusListener(new FocusAdapterImpl());
            taAddress.addFocusListener(new FocusAdapterImpl());
            maleBtn.addFocusListener(new FocusAdapterImpl());
            femaleBtn.addFocusListener(new FocusAdapterImpl());
            txtID.addActionListener(new ActionListenerImpl());
            txtID.addActionListener(new ActionListenerImpl());
            txtPhone.addActionListener(new ActionListenerImpl());
            txtEmail.addActionListener(new ActionListenerImpl());
        }

        private void clearFocus() {
            txtID.setFocusable(true);
            txtName.setFocusable(true);
            txtPhone.setFocusable(true);
            txtEmail.setFocusable(true);
            taAddress.setFocusable(true);
        }

        private void formControl(JPanel panel, JLabel label, JTextField input, JLabel errorlbl) {
            GroupLayout controller = new GroupLayout(panel);
            panel.setLayout(controller);
            controller.setHorizontalGroup(
                    controller.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(controller.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(controller.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(errorlbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(controller.createSequentialGroup()
                                                    .addComponent(label, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(input, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))))
            );
            controller.setVerticalGroup(
                    controller.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, controller.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(controller.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(input, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    )
                                    .addComponent(errorlbl, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addContainerGap())
            );
        }

        private void formControl(JPanel panel, JLabel label, JRadioButton... buttons) {
            FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 5);
            panel.setLayout(flowLayout);
            panel.add(label);

            Arrays.asList(buttons).forEach(button -> panel.add(button));
        }

        private void formControl(JPanel panel, JLabel label, JScrollPane jsp, JLabel lblerror) {
            GroupLayout controller = new GroupLayout(panel);
            panel.setLayout(controller);
            controller.setHorizontalGroup(
                    controller.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(controller.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(controller.createParallelGroup(GroupLayout.Alignment.LEADING)
                                            .addComponent(lblerror, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(controller.createSequentialGroup()
                                                    .addComponent(label, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(jsp, GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))))
            );
            controller.setVerticalGroup(
                    controller.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addGroup(GroupLayout.Alignment.TRAILING, controller.createSequentialGroup()
                                    .addGap(15, 15, 15)
                                    .addGroup(controller.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                            .addComponent(label, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jsp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    )
                                    .addComponent(lblerror, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addContainerGap())
            );
        }

        public JPanel pnlID, pnlName, pnlEmail, pnlPhone, pnlGender, pnlAddress, footer;
        private JLabel lblID, lblName, lblEmail, lblPhone, lblGender, lblAddress;
        private JLabel lblErrorID, lblErrorName, lblErrorEmail, lblErrorPhone, lblErrorGender, lblErrorAddress;
        private JTextField txtID, txtName, txtEmail, txtPhone;
        private JButton firstBtn, previousBtn, nextBtn, lastBtn;
        private JRadioButton maleBtn, femaleBtn;
        private ButtonGroup genderGroup;
        private JScrollPane jsp;
        private JTextArea taAddress;

        @Override
        public void actionPerformed(ActionEvent e) {
            String nameBtn = e.getActionCommand();

            Table tbl = content.tableManager;

            switch (nameBtn) {
                case "first" -> {
                    indexOnTable = 0;
                    tbl.fillForm(indexOnTable);
                }

                case "previous" -> {
                    --indexOnTable;
                    if (indexOnTable < 0) {
                        indexOnTable = tbl.rows.size() - 1;
                    }
                    tbl.fillForm(indexOnTable);
                }

                case "next" -> {
                    ++indexOnTable;
                    if (indexOnTable > tbl.rows.size() - 1) {
                        indexOnTable = 0;
                    }
                    tbl.fillForm(indexOnTable);
                }

                case "last" -> {
                    indexOnTable = tbl.rows.size() - 1;
                    tbl.fillForm(indexOnTable);
                }
                default ->
                    throw new AssertionError();
            }
            tbl.setHighLight(indexOnTable);
        }

        private class FocusAdapterImpl extends FocusAdapter {

            public FocusAdapterImpl() {
            }

            @Override
            public void focusGained(FocusEvent e) {
                e.getComponent().setBackground(COLORS.GLOBAL_BACKGROUND);
                if (e.getComponent() == txtID) {
                    txtID.setSelectionStart(0);
                    txtID.setSelectionEnd(txtID.getText().length());
                    lblErrorID.setText("");
                }

                if (e.getComponent() == txtName) {
                    txtName.setSelectionStart(0);
                    txtName.setSelectionEnd(txtName.getText().length());
                    lblErrorName.setText("");
                }

                if (e.getComponent() == txtPhone) {
                    txtPhone.setSelectionStart(0);
                    txtPhone.setSelectionEnd(txtPhone.getText().length());
                    lblErrorPhone.setText("");
                }

                if (e.getComponent() == txtEmail) {
                    txtEmail.setSelectionStart(0);
                    txtEmail.setSelectionEnd(txtEmail.getText().length());
                    lblErrorEmail.setText("");
                }

                if (e.getComponent() == maleBtn || e.getComponent() == femaleBtn) {
                    lblErrorGender.setText("");
                }

                if (e.getComponent() == taAddress) {
                    taAddress.setSelectionStart(0);
                    taAddress.setSelectionEnd(taAddress.getText().length());
                    lblErrorID.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                validation();
            }
        }

        private class ActionListenerImpl implements ActionListener {

            public ActionListenerImpl() {
            }

            @Override
            public void actionPerformed(ActionEvent act) {
                ((Component) act.getSource()).setFocusable(true);
            }
        }
    }

    final class Table extends JPanel {

        final ArrayList<Student> rows;
        final Object[] OPTIONS_SORT;
        final Object[] VIEWS_SORT;
        final Object[] MORE_OPTIONS;
        String value_sort;

        public Table() {
            this.OPTIONS_SORT = new Object[]{
                "Sort by: ", "Name", "ID"
            };
            this.VIEWS_SORT = new Object[]{
                "View: ", 3, 5, 10, "..."
            };
            this.MORE_OPTIONS = new Object[]{
                "More", "A-Z", "Z-A"
            };
            this.rows = new ArrayList<>();
            this.createInitTable();
            this.getDefaultData();
            this.setListeners();
        }

        private void createInitTable() {
            this.setLayout(new BorderLayout(0, 25));
            this.setPreferredSize(new Dimension(200, 200));
            this.setBorder(new EmptyBorder(0, 15, 50, 20));
            this.setBackground(COLORS.GLOBAL_BACKGROUND);
            this.setOpaque(false);

            tableOptionLabel = new JLabel();
            sortOptions = new JComboBox<>();
            headerTablePane = new JPanel();
            more_options = new JComboBox<>();

            model = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            studentTable = new JTable();
            jsp = new JScrollPane(studentTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            JPanel leftPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
            leftPane.setOpaque(false);

            text = new JLabel();
            text.setFont(FONTS.MANAGER_LABEL_TABLE);
            addOptions(more_options, MORE_OPTIONS);

            leftPane.add(tableOptionLabel);
            leftPane.add(text);
            leftPane.add(more_options);

            more_options.addActionListener((act) -> {
                if (more_options.getSelectedItem().equals(MORE_OPTIONS[0])) {
                    more_text = "";
                } else {
                    more_text = " " + more_options.getSelectedItem().toString();
                }

                text.setText(getTextOptions());
                sortTable();
                fillTable();
            });

            JPanel rightPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            tableOptionLabel.setFont(FONTS.MANAGER_LABEL_TABLE);
            tableOptionLabel.setText("Option: ");

            rightPane.setOpaque(false);

            addOptions(sortOptions, OPTIONS_SORT);

            sortOptions.addActionListener((act) -> {
                if (sortOptions.getSelectedItem().equals(OPTIONS_SORT[0])) {
                    options_text = "";
                } else {
                    options_text = "Sort " + sortOptions.getSelectedItem().toString();
                }

                text.setText(getTextOptions());
                sortTable();
                fillTable();
            });
            rightPane.add(sortOptions);

            headerTablePane.setLayout(new GridLayout(1, 2));
            headerTablePane.add(leftPane);
            headerTablePane.add(rightPane);

            this.add(headerTablePane, BorderLayout.NORTH);
            this.add(jsp, BorderLayout.CENTER);

            studentTable.setForeground(COLORS.MANAGER_TEXT);
            studentTable.setFont(FONTS.MANAGER_CONTENT_TABLE);
            studentTable.getTableHeader().setFont(FONTS.MANAGER_HEADER_TABLE);
            studentTable.getTableHeader().setReorderingAllowed(false);
            studentTable.setBorder(BorderFactory.createLineBorder(COLORS.MANAGER_TEXT));
            studentTable.setRowHeight(25);
            studentTable.setPreferredSize(new Dimension(600, 300));
            studentTable.setFocusable(false);
            studentTable.setFillsViewportHeight(true);
            studentTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            studentTable.setPreferredScrollableViewportSize(studentTable.getPreferredSize());
        }

        private void addOptions(JComboBox<Object> options_component,
                Object[] options_value) {
            options_component.setFocusable(false);
            options_component.setFont(FONTS.MANAGER_LABEL_TABLE);
            for (Object option : options_value) {
                options_component.addItem(option);
            }
        }

        private void getDefaultData() {
            String[] columns = connector.getColumnsName("Students")
                    .toArray(String[]::new);
            model.setColumnIdentifiers(columns);

            setRowsData();
            studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            studentTable.setModel(model);
        }

        private void setListeners() {
            studentTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    fillForm(rows);
                }
            });
        }

        private String getTextOptions() {
            StringBuilder sb = new StringBuilder();

            if (options_text == null) {
                options_text = "";
            }

            if (more_text == null) {
                more_text = "";
            }

            sb.append(options_text);
            sb.append(more_text);

            return sb.toString();
        }

        private JLabel tableOptionLabel, text;
        private JPanel headerTablePane;
        private JComboBox<Object> sortOptions, more_options;
        private JTable studentTable;
        private JScrollPane jsp;
        private DefaultTableModel model;

        String options_text, more_text;

        private void setRowsData() {
            var database = connector.getDataFrom("Students");

            database.forEach(row -> {
                Student student = new Student();

                student.setID(row.get(0));
                student.setName(row.get(1));
                student.setEmail(row.get(2));
                student.setPhone(row.get(3));
                student.setGender(row.get(4));
                student.setAddress(row.get(5));
                student.setAvatar(row.get(6));
                rows.add(student);
            });

            rows.forEach(row -> model.addRow(row.toArray()));
            fillTable();
        }

        private void fillTable() {
            model.setRowCount(0);
            rows.forEach(row -> model.addRow(row.toArray()));
        }

        private void fillForm(java.util.List<Student> stds) {
            int selectedRow = studentTable.getSelectedRow();
            indexOnTable = selectedRow;
            Form iForm = content.form;

            if (stds.isEmpty()) {
                return;
            }

            Student student;

            if (selectedRow < 0 && stds.size() == 1) {
                student = stds.get(0);
            } else {
                student = stds.get(selectedRow);
            }

            iForm.txtID.setText(student.getID());
            iForm.txtName.setText(student.getName());
            iForm.txtEmail.setText(student.getEmail());
            iForm.txtPhone.setText(student.getPhone());
            (student.getGender().equals("0") ? iForm.maleBtn : iForm.femaleBtn)
                    .setSelected(true);
            iForm.taAddress.setText(student.getAddress());
            content.avatar.setIcon(student.getAvatar() != null ? new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(getClass()
                            .getClassLoader()
                            .getResource("main/java/resources" + student.getAvatar()
                            )
                    )
            ) : null);
        }

        private void fillForm(int index) {
            if (index >= rows.size() || index < 0) {
                return;
            }

            Student student = rows.get(index);
            Form iForm = content.form;

            iForm.txtID.setText(student.getID());
            iForm.txtName.setText(student.getName());
            iForm.txtEmail.setText(student.getEmail());
            iForm.txtPhone.setText(student.getPhone());
            (student.getGender().equals("0") ? iForm.maleBtn : iForm.femaleBtn)
                    .setSelected(true);
            iForm.taAddress.setText(student.getAddress());
            content.avatar.setIcon(student.getAvatar() == null ? null : new ImageIcon(Toolkit.getDefaultToolkit()
                    .getImage(getClass()
                            .getClassLoader()
                            .getResource("main/java/resources" + student.getAvatar()
                            )
                    )
            ));
        }

        private void setHighLight(int row) {
            content.tableManager.studentTable
                    .getSelectionModel()
                    .setSelectionInterval(row, row);
        }

        private void sortTable() {
            String[] values = getTextOptions().split(" ");
            if (values.length == 3) {
                sortBy(values[1], values[2]);
            }
        }

        private void sortBy(String value, String filter) {
            switch (value) {
                case "ID" -> {
                    java.util.List<Student> toList
                            = rows.stream()
                                    .sorted(filter.equalsIgnoreCase("A-Z")
                                            ? Comparator.comparing(Student::getID)
                                            : Comparator.comparing(Student::getID)
                                                    .reversed())
                                    .toList();
                    rows.clear();
                    rows.addAll(toList);
                }
                case "Name" -> {
                    java.util.List<Student> toList
                            = rows.stream()
                                    .sorted(filter.equalsIgnoreCase("A-Z")
                                            ? Comparator.comparing(Student::getName)
                                            : Comparator.comparing(Student::getName)
                                                    .reversed())
                                    .toList();
                    rows.clear();
                    rows.addAll(toList);
                }
                default ->
                    throw new AssertionError();
            }
        }
    }

    private boolean validation() {
        Form form = content.form;

        if (validator.isRequired(form.txtID, "Please input your ID!")
                || !validator.isID(form.txtID, "ID is invalid",
                        Pattern.compile("^(DT|dt|SV|sv|GV|gv){1}\\d{5}$"))) {
            form.txtID.setBackground(COLORS.LOGIN_WARNING);
            form.lblErrorID.setText(validator.getMessage());
            return false;
        }

        if (validator.isRequired(form.txtName, "Name hasn't empty!")
                || !validator.isName(form.txtName, "Name is invalid")) {
            form.txtName.setBackground(COLORS.LOGIN_WARNING);
            form.lblErrorName.setText(validator.getMessage());
            return false;
        }

        if (validator.isRequired(form.txtPhone, "Phone hasn't empty!")
                || !validator.isPhone(form.txtPhone.getText(), "Phone is invalid")) {
            form.txtPhone.setBackground(COLORS.LOGIN_WARNING);
            form.lblErrorPhone.setText(validator.getMessage());
            return false;
        }

        if (validator.isRequired(form.txtEmail, "Email hasn't empty!")
                || !validator.isEmail(form.txtEmail.getText(), "Email is invalid!")) {
            form.txtEmail.setBackground(COLORS.LOGIN_WARNING);
            form.lblErrorEmail.setText(validator.getMessage());
            return false;
        }

        if (validator.isRequired(form.genderGroup, "Gender hasn't empty!")) {
            form.lblErrorGender.setText(validator.getMessage());
            return false;
        }

        if (validator.isRequired(form.taAddress, "Address hasn't empty!")) {
            form.taAddress.setBackground(COLORS.LOGIN_WARNING);
            form.lblErrorAddress.setText(validator.getMessage());
            return false;
        }

        return true;
    }

    private void exit() {
        // this function only called when want to closed application 
        // if close this window then show the dialog ask do u want to save changed?
        // or if the data on table in frame difference with sql data on server
    }
}
