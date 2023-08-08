package main.java.ui;

import main.java.*;
import main.java.controller.Connector;
import main.java.member.Student;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import main.java.controller.Validator;
import main.java.member.Grades;

public class TeacherUI extends JFrame implements ActionListener {

    private final Dimension FULLSCREEN = Toolkit.getDefaultToolkit()
            .getScreenSize();

    private final Object[] OPTIONS_SEARCH = new Object[]{
        "Search by", "ID", "Name"
    };

    private final Object[] OPTIONS_SORT = new Object[]{
        "Sort by ", "ID", "Name", "ENScore", "ITScore", "PEScore", "AVGScore"
    };

    private final Object[] VIEWS_SORT = new Object[]{
        "View: ", 3, 5, 10, "All", "..."
    };

    private final Object[] MORE_SORT = new Object[]{
        "More ", "A_Z", "Z_A"
    };

    private java.util.List<Grades> rows;

    private final Connector connector = LoginUI.connector;
    private final Validator validator = new Validator();

    private int indexOnTable = 0;

    public TeacherUI() throws HeadlessException {
        super("Teacher");
        this.rows = new ArrayList<>();
        JFrame.setDefaultLookAndFeelDecorated(false);
        this.initUI();
        this.setupAttribute();
        this.getDefaultData();
        this.setupControls();
        this.setListeners();
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ((JFrame) e.getSource()).requestFocus();
                super.mouseClicked(e);
            }
        });
    }

    public TeacherUI(boolean isTeacher) throws HeadlessException {
        this();
        if (!isTeacher) {
            disableController(buttonsControl);
            disableController(infoControl);
            disableController(editorControl);
        }
    }

    private void disableController(JPanel pane) {
        Arrays.asList(pane.getComponents())
                .stream()
                .filter(cmp -> cmp instanceof JButton
                || cmp instanceof JTextField)
                .forEach(item -> item.setEnabled(false));
    }

    private void initUI() {
        rows = new ArrayList<>();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);

        this.setMinimumSize(new Dimension(900, FULLSCREEN.height));
        this.setMaximumSize(FULLSCREEN);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);

        main = new JPanel();
        header = new JPanel();
        content = new JPanel();

        searchPanel = new JPanel();
        infoControl = new JPanel();
        editorControl = new JPanel();
        buttonsControl = new JPanel();
        tablePanel = new JPanel();
        headerTablePane = new JPanel();

        titleForm = new JLabel();

        searchLabel = new JLabel();
        search_input = new JTextField();
        place_holder_search_input = new JLabel();
        searchBtn = new JButton();

        searchOptions = new JComboBox<>();
        tableOptions = new JComboBox<>();
        sortOptions = new JComboBox<>();
        more_options = new JComboBox<>();

        nameLabel = new JLabel();
        IDLabel = new JLabel();
        englishMark = new JLabel();
        ITMark = new JLabel();
        PELabel = new JLabel();
        tableOptionLabel = new JLabel();
        AVGDisplay = new JLabel();

        name_input = new JTextField();
        ID_input = new JTextField();
        enMark_input = new JTextField();
        ITMark_input = new JTextField();
        PEMark_input = new JTextField();

        newBtn = new JButton();
        saveBtn = new JButton();
        deleteBtn = new JButton();
        updateBtn = new JButton();

        firstBtn = new JButton();
        nextBtn = new JButton();
        previousBtn = new JButton();
        lastBtn = new JButton();

        studentTable = new JTable();
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        jsp = new JScrollPane(studentTable,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    private void setupAttribute() {
        this.add(main);

        main.setLayout(new BorderLayout(20, 20));
        main.setBackground(Color.white);
        main.add(header, BorderLayout.PAGE_START);
        main.add(content, BorderLayout.CENTER);

        main.setBorder(new EmptyBorder(50, 50, 50, 50));

        header.setLayout(new BorderLayout(20, 20));
        header.setOpaque(false);

        header.add(titleForm, BorderLayout.CENTER);
        header.add(searchPanel, BorderLayout.SOUTH);

        titleForm.setText("Student Score Management");
        titleForm.setFont(FONTS.GLOBAL_TITLE);
        titleForm.setForeground(Color.red);
        titleForm.setHorizontalAlignment(JLabel.CENTER);

        content.setLayout(new FlowLayout(FlowLayout.LEFT, 100, 20));
        content.setOpaque(false);
        content.add(infoControl);
        content.add(tablePanel);
        content.add(editorControl);

        //<editor-fold defaultstate="collapsed" desc=" Header search bar ">
        searchPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        searchPanel.setOpaque(false);
        searchPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        searchPanel.add(searchLabel);
        searchPanel.add(search_input);
        searchPanel.add(searchOptions);

        searchLabel.setFont(FONTS.TEACHER_LABEL);
        searchLabel.setText("Seacrh: ");
        searchLabel.setForeground(COLORS.TEACHER_TEXT);

        search_input.setLayout(new FlowLayout(FlowLayout.LEFT, 0, -2));
        search_input.setMinimumSize(new Dimension(100, 30));
        search_input.setPreferredSize(new Dimension(300, 30));
        search_input.setFont(FONTS.TEACHER_INPUT);

        place_holder_search_input.setText("Type something...");
        place_holder_search_input.setOpaque(false);
        place_holder_search_input.setFont(new Font("Open Sans", Font.ITALIC, 18));
        place_holder_search_input.setPreferredSize(new Dimension(260, 30));
        place_holder_search_input.setForeground(Color.lightGray);

        search_input.add(place_holder_search_input);
        search_input.add(searchBtn);

        searchBtn.setIcon(new ImageIcon(IMAGES.FORM_HEADER_SEARCH));
        searchBtn.setFocusable(false);
        searchBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        searchBtn.setFocusPainted(false);
        searchBtn.setBorder(null);
        searchBtn.setOpaque(false);

        searchOptions.setPreferredSize(new Dimension(120, 30));
        searchOptions.setFocusable(false);
        searchOptions.setFont(FONTS.TEACHER_LABEL);
        for (Object item : OPTIONS_SEARCH) {
            searchOptions.addItem(item);
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" Main content ">
        infoControl.setOpaque(false);
        infoControl.setBorder(new CompoundBorder(
                new TitledBorder(
                        null, "INFOMATION",
                        TitledBorder.LEADING,
                        TitledBorder.ABOVE_TOP,
                        new Font("Consolas", Font.PLAIN, 15),
                        COLORS.TEACHER_TEXT
                ),
                new EmptyBorder(5, 5, 5, 5))
        );

        nameLabel.setText("Name: ");
        nameLabel.setFont(FONTS.TEACHER_LABEL);
        nameLabel.setForeground(COLORS.TEACHER_TEXT);
        nameLabel.setHorizontalAlignment(JLabel.LEFT);

        name_input.setText("");
        name_input.setPreferredSize(new Dimension(370, 30));
        name_input.setForeground(COLORS.TEACHER_TEXT);
        name_input.setFont(FONTS.TEACHER_INPUT);
        name_input.setBorder(null);
        name_input.setOpaque(false);
        name_input.setEditable(false);
        name_input.setFocusable(false);

        IDLabel.setText("ID: ");
        IDLabel.setFont(FONTS.TEACHER_LABEL);
        IDLabel.setForeground(COLORS.TEACHER_TEXT);
        IDLabel.setHorizontalAlignment(JLabel.LEFT);

        ID_input.setText("");
        ID_input.setPreferredSize(new Dimension(370, 30));
        ID_input.setForeground(COLORS.TEACHER_TEXT);
        ID_input.setFont(FONTS.TEACHER_INPUT);

        englishMark.setText("English score: ");
        englishMark.setFont(FONTS.TEACHER_LABEL);
        englishMark.setForeground(COLORS.TEACHER_TEXT);
        englishMark.setHorizontalAlignment(JLabel.LEFT);

        enMark_input.setText("");
        enMark_input.setPreferredSize(new Dimension(370, 30));

        enMark_input.setForeground(COLORS.TEACHER_TEXT);
        enMark_input.setFont(FONTS.TEACHER_INPUT);

        ITMark.setText("IT score: ");
        ITMark.setFont(FONTS.TEACHER_LABEL);
        ITMark.setForeground(COLORS.TEACHER_TEXT);
        ITMark.setHorizontalAlignment(JLabel.LEFT);

        ITMark_input.setText("");
        ITMark_input.setPreferredSize(new Dimension(370, 30));

        ITMark_input.setForeground(COLORS.TEACHER_TEXT);
        ITMark_input.setFont(FONTS.TEACHER_INPUT);

        PELabel.setText("PE score: ");
        PELabel.setFont(FONTS.TEACHER_LABEL);
        PELabel.setForeground(COLORS.TEACHER_TEXT);
        PELabel.setHorizontalAlignment(JLabel.LEFT);

        PEMark_input.setText("");
        PEMark_input.setPreferredSize(new Dimension(370, 30));
        PEMark_input.setForeground(COLORS.TEACHER_TEXT);
        PEMark_input.setFont(FONTS.TEACHER_INPUT);

        AVGDisplay.setText("10");
        AVGDisplay.setHorizontalAlignment(JLabel.CENTER);
        AVGDisplay.setForeground(COLORS.TEACHER_AVG_DISPLAY);
        AVGDisplay.setFont(FONTS.TEACHER_AVG_DISPLAY);

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc=" Editor Control ">
        editorControl.setLayout(new FlowLayout(FlowLayout.LEFT, 35, 20));
        editorControl.add(newBtn);
        editorControl.add(saveBtn);
        editorControl.add(deleteBtn);
        editorControl.add(updateBtn);

        editorControl.setOpaque(false);
        editorControl.setBorder(new CompoundBorder(
                new TitledBorder(
                        null, "Edit",
                        TitledBorder.LEFT,
                        TitledBorder.ABOVE_TOP,
                        new Font("Consolas", Font.PLAIN, 15),
                        COLORS.TEACHER_TEXT
                ),
                new EmptyBorder(0, 0, 0, 0))
        );

        newBtn.setText("New");

        newBtn.setPreferredSize(new Dimension(100, 40));
        newBtn.setFont(FONTS.TEACHER_BUTTON);
        newBtn.setFocusable(false);
        newBtn.setFocusPainted(false);
        newBtn.setBorder(null);
        newBtn.setBackground(new Color(0xf6f7f8));
        newBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        saveBtn.setText("Save");
        saveBtn.setPreferredSize(new Dimension(100, 40));
        saveBtn.setFont(FONTS.TEACHER_BUTTON);
        saveBtn.setFocusable(false);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorder(null);
        saveBtn.setBackground(new Color(0xf6f7f8));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        deleteBtn.setText("Delete");
        deleteBtn.setPreferredSize(new Dimension(100, 40));
        deleteBtn.setFont(FONTS.TEACHER_BUTTON);
        deleteBtn.setFocusable(false);
        deleteBtn.setFocusPainted(false);
        deleteBtn.setBorder(null);
        deleteBtn.setBackground(new Color(0xf6f7f8));
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        updateBtn.setText("Update");
        updateBtn.setPreferredSize(new Dimension(100, 40));
        updateBtn.setFont(FONTS.TEACHER_BUTTON);
        updateBtn.setFocusable(false);
        updateBtn.setFocusPainted(false);
        updateBtn.setBorder(null);
        updateBtn.setBackground(new Color(0xf6f7f8));
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc=" Button FUNC Control ">
        buttonsControl.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 15));
        buttonsControl.setOpaque(false);

        firstBtn.setIcon(new ImageIcon(IMAGES.FORM_FIRST));
        firstBtn.setFont(FONTS.TEACHER_BUTTON);
        firstBtn.setFocusable(false);
        firstBtn.setFocusPainted(false);
        firstBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        firstBtn.setBorder(null);
        firstBtn.setOpaque(false);

        previousBtn.setIcon(new ImageIcon(IMAGES.FORM_PREVIOUS));
        previousBtn.setFont(FONTS.TEACHER_BUTTON);
        previousBtn.setFocusable(false);
        previousBtn.setFocusPainted(false);
        previousBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        previousBtn.setBorder(null);
        previousBtn.setOpaque(false);

        nextBtn.setIcon(new ImageIcon(IMAGES.FORM_NEXT));
        nextBtn.setFont(FONTS.TEACHER_BUTTON);
        nextBtn.setFocusable(false);
        nextBtn.setFocusPainted(false);
        nextBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextBtn.setBorder(null);
        nextBtn.setOpaque(false);
        nextBtn.setMargin(new Insets(0, 0, 0, 15));

        lastBtn.setIcon(new ImageIcon(IMAGES.FORM_LAST));
        lastBtn.setFont(FONTS.TEACHER_BUTTON);
        lastBtn.setFocusable(false);
        lastBtn.setFocusPainted(false);
        lastBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lastBtn.setBorder(null);
        lastBtn.setOpaque(false);
        lastBtn.setMargin(new Insets(0, 0, 0, 15));

        firstBtn.setActionCommand("first");
        previousBtn.setActionCommand("previous");
        nextBtn.setActionCommand("next");
        lastBtn.setActionCommand("last");

        buttonsControl.add(firstBtn);
        buttonsControl.add(previousBtn);
        buttonsControl.add(nextBtn);
        buttonsControl.add(lastBtn);
        //</editor-fold>

        tablePanel.setLayout(new BorderLayout(0, 25));
        tablePanel.setBackground(COLORS.GLOBAL_BACKGROUND);

        JPanel leftPane = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPane.setBackground(COLORS.GLOBAL_BACKGROUND);
        leftPane.add(tableOptionLabel);
        tableOptionLabel.setFont(FONTS.TEACHER_LABEL);
        tableOptionLabel.setText("Option: ");

        tableOptions.setFont(FONTS.TEACHER_LABEL);
        for (Object option_view : VIEWS_SORT) {
            tableOptions.addItem(option_view);
        }
        leftPane.add(tableOptions);

        more_options.setFont(FONTS.TEACHER_LABEL);
        for (Object more : MORE_SORT) {
            more_options.addItem(more);
        }
        leftPane.add(more_options);

        JPanel rightPane = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPane.setBackground(COLORS.GLOBAL_BACKGROUND);
        sortOptions.setFont(FONTS.TEACHER_LABEL);
        sortOptions.setFocusable(false);
        for (Object option : OPTIONS_SORT) {
            sortOptions.addItem(option);
        }

        rightPane.add(sortOptions);

        headerTablePane.setLayout(new GridLayout(1, 2));
        headerTablePane.add(leftPane);
        headerTablePane.add(rightPane);

        tablePanel.add(headerTablePane, BorderLayout.NORTH);
        tablePanel.add(jsp, BorderLayout.CENTER);

        studentTable.setForeground(COLORS.TEACHER_TEXT);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        studentTable.getTableHeader().setFont(new Font("Calibri", Font.BOLD, 16));
        studentTable.getTableHeader().setReorderingAllowed(false);
        studentTable.setBorder(BorderFactory.createLineBorder(COLORS.TEACHER_TEXT));
        studentTable.setRowHeight(25);
        studentTable.setPreferredSize(new Dimension(600, 300));
        studentTable.setFocusable(false);
        studentTable.setFillsViewportHeight(true);
        studentTable.setPreferredScrollableViewportSize(studentTable.getPreferredSize());
    }

    private void getDefaultData() {
        String[] columns = connector.getColumnsName("SCORES_INFO()")
                .toArray(String[]::new);
        model.setColumnIdentifiers(columns);

        getRowsData();
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        studentTable.setModel(model);
        fillTable(rows);
    }

    private void getRowsData() {
        var database = connector.getDataFrom("SCORES_INFO()");

        database.forEach(row -> {
            Grades grades = new Grades();

            grades.setStudent(new Student(row.get(0), row.get(1)));
            grades.setENScore(Double.valueOf(row.get(2)));
            grades.setITScore(Double.valueOf(row.get(3)));
            grades.setPEScore(Double.valueOf(row.get(4)));
            grades.setAVGScore(grades.getAVGScore());

            rows.add(grades);
        });
    }

    private void setupControls() {
        infoControl.setLayout(new GridBagLayout());
        GridBagConstraints infoGBC = new GridBagConstraints();
        infoGBC.ipadx = 5;
        infoGBC.fill = GridBagConstraints.HORIZONTAL;
        infoGBC.insets = new Insets(10, 10, 10, 10);

        infoGBC.gridx = 0;
        infoGBC.gridy = 0;
        infoControl.add(nameLabel, infoGBC);
        infoGBC.gridx = 1;
        infoControl.add(name_input, infoGBC);

        infoGBC.gridx = 0;
        infoGBC.gridy = 1;
        infoControl.add(IDLabel, infoGBC);
        infoGBC.gridx = 1;
        infoControl.add(ID_input, infoGBC);

        infoGBC.gridx = 0;
        infoGBC.gridy = 2;
        infoControl.add(englishMark, infoGBC);
        infoGBC.gridx = 1;
        infoControl.add(enMark_input, infoGBC);

        infoGBC.gridx = 0;
        infoGBC.gridy = 3;
        infoControl.add(ITMark, infoGBC);
        infoGBC.gridx = 1;
        infoControl.add(ITMark_input, infoGBC);

        infoGBC.gridx = 0;
        infoGBC.gridy = 4;
        infoControl.add(PELabel, infoGBC);
        infoGBC.gridx = 1;
        infoControl.add(PEMark_input, infoGBC);

        infoGBC.gridx = 0;
        infoGBC.gridy = 5;
        infoControl.add(AVGDisplay, infoGBC);

        infoGBC.gridx = 1;
        infoGBC.gridy = 5;
        infoControl.add(buttonsControl, infoGBC);
    }

    private void setListeners() {
        search_input.addKeyListener(new KeyAdapterImpl("Type something..."));
        searchBtn.addActionListener((act) -> search());
        newBtn.addActionListener((act) -> cleanForm());
        saveBtn.addActionListener((act) -> saveForm());
        deleteBtn.addActionListener((act) -> deleteRow());
        updateBtn.addActionListener((act) -> updateTable());
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                fillForm(rows);
                super.mouseClicked(e);
            }
        });
        firstBtn.addActionListener(this);
        previousBtn.addActionListener(this);
        nextBtn.addActionListener(this);
        lastBtn.addActionListener(this);
        tableOptions.addActionListener((act) -> sortTable());
        more_options.addActionListener((act) -> sortTable());
        sortOptions.addActionListener((act) -> sortTable());
    }

    private JPanel main, header, content, searchPanel,
            infoControl, editorControl, buttonsControl,
            tablePanel, headerTablePane;
    private JLabel titleForm, searchLabel, place_holder_search_input,
            nameLabel, IDLabel, englishMark, ITMark, PELabel,
            tableOptionLabel, AVGDisplay;
    private JButton searchBtn, newBtn, saveBtn, deleteBtn, updateBtn,
            firstBtn, nextBtn, previousBtn, lastBtn;
    private JTextField search_input, name_input, ID_input,
            enMark_input, ITMark_input, PEMark_input;
    private JComboBox<Object> searchOptions, tableOptions, more_options, sortOptions;
    private JTable studentTable;
    private JScrollPane jsp;
    private DefaultTableModel model;

    private void cleanForm() {
        name_input.setText(null);
        ID_input.setText(null);
        enMark_input.setText(null);
        ITMark_input.setText(null);
        PEMark_input.setText(null);
        AVGDisplay.setText(null);
        studentTable.clearSelection();
    }

    private void saveForm() {
        if (!validation()) {
            JOptionPane.showMessageDialog(this, "Sai thong tin!");
            return;
        }
        rows.add(new Grades(
                new Student(ID_input.getText(), name_input.getText()),
                Double.valueOf(enMark_input.getText()),
                Double.valueOf(ITMark_input.getText()),
                Double.valueOf(ITMark_input.getText()),
                Double.valueOf(PEMark_input.getText()))
        );
        fillTable(rows);
    }

    private void deleteRow() {
        int[] selectedRows = studentTable.getSelectedRows();
        ArrayList<Grades> temps = new ArrayList<>();

        if (selectedRows.length <= 0) {
            return;
        }

        for (int selectedRow : selectedRows) {
            temps.add(rows.get(selectedRow));
        }

        rows.removeAll(temps);
        fillTable(rows);
        cleanForm();
    }

    private void updateTable() {
        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow < 0) {
            return;
        }
        indexOnTable = selectedRow;
        if (!validation()) {
            return;
        }

        Grades grades = new Grades(
                new Student(ID_input.getText(), name_input.getText()),
                Double.valueOf(enMark_input.getText()),
                Double.valueOf(ITMark_input.getText()),
                Double.valueOf(ITMark_input.getText()),
                Double.valueOf(PEMark_input.getText())
        );

        rows.remove(selectedRow);
        rows.add(selectedRow, grades);
        fillTable(rows);
        AVGDisplay.setText(grades.getAVGScore().toString());
    }

    private void fillForm(int index) {
        if (index >= rows.size() || index < 0) {
            return;
        }

        Grades gr = rows.get(index);

        fillInput(gr);
    }

    private void fillForm(java.util.List<Grades> rows) {
        int selectedRow = studentTable.getSelectedRow();
        indexOnTable = selectedRow;

        if (rows.isEmpty()) {
            return;
        }

        Grades gr;
        if (selectedRow < 0 && rows.size() == 1) {
            gr = rows.get(0);
        } else {
            gr = rows.get(selectedRow);
        }
        fillInput(gr);
    }

    private void fillInput(Grades gr) {
        name_input.setText(gr.getStudentName());
        ID_input.setText(gr.getStudentID());
        enMark_input.setText(gr.getENScore().toString());
        ITMark_input.setText(gr.getITScore().toString());
        PEMark_input.setText(gr.getPEScore().toString());
        AVGDisplay.setText(gr.getAVGScore().toString());
    }

    private void fillTable(java.util.List<Grades> list) {
        model.setRowCount(0);

        list.forEach(grade -> {
            model.addRow(grade.toArray());
        });
    }

    private void search() {

        if (search_input.getText().isEmpty()) {
            return;
        }

        String value_need_compare = search_input.getText();
        findAndFill(value_need_compare);
    }

    private void findAndFill(String value) {
        ArrayList<Grades> search_table = new ArrayList<>();
        search_table.addAll(rows);

        java.util.List<Grades> collect = search_table.stream()
                .filter(gr -> gr.getStudentID().equalsIgnoreCase(value)
                || gr.getStudentName().equalsIgnoreCase(value)
                )
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

        studentTable.getSelectionModel().clearSelection();

        int indexOf = rows.indexOf(collect.get(0));
        fillForm(collect);
        setHightLight(indexOf);
    }

    private void setHightLight(int index) {
        studentTable.getSelectionModel().setSelectionInterval(index, index);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String nameBtn = e.getActionCommand();

        switch (nameBtn) {
            case "first" -> {
                indexOnTable = 0;
                fillForm(indexOnTable);
            }

            case "previous" -> {
                --indexOnTable;
                if (indexOnTable < 0) {
                    indexOnTable = rows.size() - 1;
                }
                fillForm(indexOnTable);
            }

            case "next" -> {
                ++indexOnTable;
                if (indexOnTable > rows.size() - 1) {
                    indexOnTable = 0;
                }
                fillForm(indexOnTable);
            }

            case "last" -> {
                indexOnTable = rows.size() - 1;
                fillForm(indexOnTable);
            }
            default ->
                throw new AssertionError();
        }
        setHightLight(indexOnTable);
    }

    private String getTextOptions() {
        StringBuilder sb = new StringBuilder();

        String text1 = !sortOptions.getSelectedItem()
                .equals(OPTIONS_SORT[0])
                ? sortOptions.getSelectedItem().toString() + " "
                : "";

        String text2 = !tableOptions.getSelectedItem()
                .equals(VIEWS_SORT[0])
                ? tableOptions.getSelectedItem().toString() + " "
                : "All";

        String text3 = !more_options.getSelectedItem()
                .equals(MORE_SORT[0])
                ? more_options.getSelectedItem().toString()
                : "";

        sb.append(text1);
        sb.append(text2);
        sb.append(text3);

        return sb.toString();
    }

    private void sortTable() {
        String[] values = getTextOptions().split(" ");
        if (values.length == 3) {
            sortBy(values[0], values[1], values[2]);
        }
    }

    private void sortBy(String value, String limited, String filter) {
        long limit = !limited.equals("All") && limited.matches("^[0-9]+$")
                ? Long.parseLong(limited)
                : rows.size();
        java.util.List<Grades> toList = new ArrayList<>();
        switch (value) {
            case "ID" -> {
                toList = rows.stream()
                        .sorted(filter.equalsIgnoreCase("A_Z")
                                ? Comparator.comparing(Grades::getStudentID)
                                : Comparator.comparing(Grades::getStudentID)
                                        .reversed())
                        .limit(limit)
                        .toList();
            }
            case "Name" -> {
                toList = rows.stream()
                        .sorted(filter.equalsIgnoreCase("A_Z")
                                ? Comparator.comparing(Grades::getStudentName)
                                : Comparator.comparing(Grades::getStudentName)
                                        .reversed())
                        .limit(limit)
                        .toList();
            }

            case "ITScore" -> {
                toList = rows.stream()
                        .sorted(filter.equalsIgnoreCase("A_Z")
                                ? Comparator.comparing(Grades::getITScore)
                                : Comparator.comparing(Grades::getITScore)
                                        .reversed())
                        .limit(limit)
                        .toList();
            }

            case "ENScore" -> {
                toList = rows.stream()
                        .sorted(filter.equalsIgnoreCase("A_Z")
                                ? Comparator.comparing(Grades::getENScore)
                                : Comparator.comparing(Grades::getENScore)
                                        .reversed())
                        .limit(limit)
                        .toList();
            }

            case "PEScore" -> {
                toList = rows.stream()
                        .sorted(filter.equalsIgnoreCase("A_Z")
                                ? Comparator.comparing(Grades::getPEScore)
                                : Comparator.comparing(Grades::getPEScore)
                                        .reversed())
                        .limit(limit)
                        .toList();
            }

            case "AVGScore" -> {
                toList = rows.stream()
                        .sorted(filter.equalsIgnoreCase("A_Z")
                                ? Comparator.comparing(Grades::getAVGScore)
                                : Comparator.comparing(Grades::getAVGScore)
                                        .reversed())
                        .limit(limit)
                        .toList();
            }
            default ->
                throw new AssertionError();
        }
        fillTable(toList);
    }

    private boolean validation() {
        if (!validator.isID(ID_input, "",
                Pattern.compile("(SV|sv){1}\\d{5}"))) {
            return false;
        }

        if (!checkMark(enMark_input.getText(), ITMark_input.getText(),
                PEMark_input.getText())) {
            return false;
        }

        return !validator.isRequired(ID_input,
                enMark_input, ITMark_input, PEMark_input);
    }

    private boolean checkMark(String... marks) {
        for (String mark : marks) {
            Double numberMark = Double.parseDouble(mark);
            if (!validator.isMark(mark) || !(numberMark >= 0 || numberMark <= 10)) {
                return false;
            }
        }
        return true;
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
            if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                search();
            }
            String value = search_input.getText();
            if (value.isEmpty()) {
                place_holder_search_input.setText(this.pl_text);
                return;
            }
            place_holder_search_input.setText(null);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            super.keyReleased(e);
            String value = search_input.getText();
            if (value.isEmpty()) {
                place_holder_search_input.setText(this.pl_text);
                return;
            }
            place_holder_search_input.setText(null);
        }
    }
}
