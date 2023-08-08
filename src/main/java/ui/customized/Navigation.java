package main.java.ui.customized;

import java.awt.event.*;

import javax.swing.*;
import main.java.IMAGES;

public class Navigation extends JMenuBar implements ActionListener {

    private JMenu fileMenu, editMenu, helpMenu;
    private JMenuItem openFile, saveFile, saveAsFile, printFile, closeFile, exitFile;
    private JMenuItem undoEdit, redoEdit, copyEdit, cutEdit, pasteEdit, deleteEdit;
    private JMenuItem startPage, aboutMe, contactMe;

    public Navigation() {
        this.initMenuBar();
        this.setAltMenuItem();
        this.putIcons();
    }

    private void setAltMenuItem() {
        // file menu
        openFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                InputEvent.CTRL_DOWN_MASK));
        saveFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK));
        saveAsFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                InputEvent.CTRL_DOWN_MASK + InputEvent.SHIFT_DOWN_MASK));
        printFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
                InputEvent.CTRL_DOWN_MASK));
        closeFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
                InputEvent.CTRL_DOWN_MASK));
        exitFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_DOWN_MASK));
        // edit menu
        undoEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
                InputEvent.CTRL_DOWN_MASK));
        redoEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
                InputEvent.CTRL_DOWN_MASK));
        copyEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
                InputEvent.CTRL_DOWN_MASK));
        cutEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
                InputEvent.CTRL_DOWN_MASK));
        pasteEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                InputEvent.CTRL_DOWN_MASK));
        deleteEdit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,
                InputEvent.SHIFT_DOWN_MASK));
    }

    private void putIcons() {
        openFile.setIcon(new ImageIcon(IMAGES.NAV_OPEN_FILE));
        saveFile.setIcon(new ImageIcon(IMAGES.NAV_SAVE_FILE));
        saveAsFile.setIcon(new ImageIcon(IMAGES.NAV_SAVE_AS_FILE));
        printFile.setIcon(new ImageIcon(IMAGES.NAV_PRINT_FILE));
        closeFile.setIcon(new ImageIcon(IMAGES.NAV_CLOSE_EDIT));
        exitFile.setIcon(new ImageIcon(IMAGES.NAV_EXIT_EDIT));
        
        undoEdit.setIcon(new ImageIcon(IMAGES.NAV_UNDO_EDIT));
        redoEdit.setIcon(new ImageIcon(IMAGES.NAV_REDO_EDIT));
        copyEdit.setIcon(new ImageIcon(IMAGES.NAV_COPY_EDIT));
        cutEdit.setIcon(new ImageIcon(IMAGES.NAV_CUT_EDIT));
        pasteEdit.setIcon(new ImageIcon(IMAGES.NAV_PASTE_EDIT));
        deleteEdit.setIcon(new ImageIcon(IMAGES.NAV_DELETE_EDIT));
        
        startPage.setIcon(new ImageIcon(IMAGES.NAV_HOME_PAGE));
        contactMe.setIcon(new ImageIcon(IMAGES.NAV_CONTACT));
        aboutMe.setIcon(new ImageIcon(IMAGES.NAV_ABOUT));
    }

    private void initMenuBar() {
        fileMenu = new JMenu("File");
        openFile = new JMenuItem("Open");
        saveFile = new JMenuItem("Save");
        saveAsFile = new JMenuItem("Save As...");
        printFile = new JMenuItem("Print");
        closeFile = new JMenuItem("Close");
        exitFile = new JMenuItem("Exit");

        fileMenu.add(openFile);
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.add(printFile);
        fileMenu.add(closeFile);
        fileMenu.add(exitFile);
        fileMenu.setMnemonic(KeyEvent.VK_F);
        this.add(fileMenu);

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        undoEdit = new JMenuItem("Undo");
        redoEdit = new JMenuItem("Redo");
        copyEdit = new JMenuItem("Copy");
        cutEdit = new JMenuItem("Cut");
        pasteEdit = new JMenuItem("Paste");
        deleteEdit = new JMenuItem("Delete");

        editMenu.add(undoEdit);
        editMenu.add(redoEdit);
        editMenu.add(copyEdit);
        editMenu.add(cutEdit);
        editMenu.add(pasteEdit);
        editMenu.add(deleteEdit);
        this.add(editMenu);

        helpMenu = new JMenu("Help");
        startPage = new JMenuItem("Start Page");
        aboutMe = new JMenuItem("About Me");
        contactMe = new JMenuItem("Contact Me");
        helpMenu.add(startPage);
        helpMenu.add(aboutMe);
        helpMenu.add(contactMe);
        this.add(helpMenu);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String item = e.getActionCommand();

//        switch (item) {
//            case "Open" ->
//                openFile();
//            case "Save" ->
//                saveFile();
//            case "Save As..." ->
//                saveAsFile();
//            case "Print" ->
//                printFile();
//            case "Close" ->
//                closesFile();
//            case "Exit" ->
//                exit();
//            default ->
//                throw new AssertionError();
//        }
    }

    private void exit() {
        System.exit(0);
    }
}