package main.java;

import java.awt.Image;
import java.awt.Toolkit;

public class IMAGES {

    static final String RESOURECES = "main/java/resources";
    static final ClassLoader LOADER = IMAGES.class.getClassLoader();
    static final Toolkit TOOLER = Toolkit.getDefaultToolkit();

    //LOGIN IMAGE
    public static final Image LOGIN_BACKGROUND = getImage("/login-bg.jpg");

    public static final Image LOGIN_SHOW_ICON = getImage("/show_password-icon.png");

    public static final Image LOGIN_HIDDEN_ICON = getImage("/hidden_password-icon.png");

    public static final Image HOME_BACKGROUND = getImage("/Banner.png");

    //HOME
    public static final Image HOME_LOGO = getImage("/logo_fpoly.png")
            .getScaledInstance(200, 100, Image.SCALE_SMOOTH);

    public static final Image HOME_LOGOUT = getImage("/logout-icon.png");

    // NAVIGATION
    public static final Image NAV_OPEN_FILE = getIconMenuBar("/open_file-icon.png");
    public static final Image NAV_SAVE_FILE = getIconMenuBar("/save_file-icon.png");
    public static final Image NAV_SAVE_AS_FILE = getIconMenuBar("/save_as_file-icon.png");
    public static final Image NAV_PRINT_FILE = getIconMenuBar("/print_file-icon.png");
    public static final Image NAV_CLOSE_EDIT = getIconMenuBar("/close_file-icon.png");
    public static final Image NAV_EXIT_EDIT = getIconMenuBar("/exit_file-icon.png");

    public static final Image NAV_UNDO_EDIT = getIconMenuBar("/undo_edit-icon.png");
    public static final Image NAV_REDO_EDIT = getIconMenuBar("/redo_edit-icon.png");
    public static final Image NAV_COPY_EDIT = getIconMenuBar("/copy_edit-icon.png");
    public static final Image NAV_CUT_EDIT = getIconMenuBar("/cut_edit-icon.png");
    public static final Image NAV_PASTE_EDIT = getIconMenuBar("/paste_edit-icon.png");
    public static final Image NAV_DELETE_EDIT = getIconMenuBar("/delete_edit-icon.png");

    public static final Image NAV_HOME_PAGE = getIconMenuBar("/home_help-icon.png");
    public static final Image NAV_CONTACT = getIconMenuBar("/contact_help-icon.png");
    public static final Image NAV_ABOUT = getIconMenuBar("/about_help-icon.png");

    //  - FORM
    public static final Image FORM_HEADER_SEARCH = getIcon("/search-icon.png").getScaledInstance(25, 25, Image.SCALE_SMOOTH);
    public static final Image FORM_NEW_BUTTON = getIcon("/add_button-icon.png");
    public static final Image FORM_SAVE_BUTTON = getIcon("/save_button-icon.png");
    public static final Image FORM_DELETE_BUTTON = getIcon("/delete_button-icon.png");
    public static final Image FORM_UPDATE_BUTTON = getIcon("/update_button-icon.png");
    public static final Image FORM_FIRST = getIcon("/first_item-icon.png");
    public static final Image FORM_LAST = getIcon("/last_item-icon.png");
    public static final Image FORM_PREVIOUS = getIcon("/previous_item-icon.png");
    public static final Image FORM_NEXT = getIcon("/next_item-icon.png");
    
    // MANAGER
    public static final Image MANAGER_USER_AVATAR = getImage("/user_avatar.png");

    public static Image getImage(String name) {
        return TOOLER.getImage(LOADER.getResource(RESOURECES + name));
    }

    public static Image getIconMenuBar(String name) {
        return TOOLER.getImage(LOADER.getResource(RESOURECES + name))
                .getScaledInstance(25, 25, Image.SCALE_SMOOTH);
    }

    public static Image getIcon(String name) {
        return TOOLER.getImage(LOADER.getResource(RESOURECES + name));
    }
}
