package main.java.controller;

import java.util.*;
import java.util.regex.*;
import javax.swing.*;

public class Validator {

    private Object object;
    private String message;

    public Validator() {
    }

    public Validator(Object object) {
        this.object = object;
    }

    public Validator(Object object, String message) {
        this.object = object;
        this.message = message;
    }

    public boolean isRequired(Object obj) {
        if (this.message == null) {
            this.message = "This field is required";
        }

        if (Objects.isNull(obj)) {
            this.object = null;
            return false;
        }

        if (obj instanceof JTextField jTextField) {
            this.object = jTextField;

            String value = jTextField.getText();
            return value.isEmpty();
        }

        if (obj instanceof JPasswordField jPasswordField) {
            this.object = jPasswordField;

            String value = String.valueOf(jPasswordField.getPassword());
            return value.isEmpty();
        }

        if (obj instanceof JTextArea jTextArea) {
            this.object = jTextArea;

            String value = String.valueOf(jTextArea.getText());
            return value.isEmpty();
        }

        if (obj instanceof JComboBox jComboBox) {
            this.object = jComboBox;

            JComboBox cbx = jComboBox;
            return cbx.getSelectedItem().equals(cbx.getItemAt(0));
        }

        if (obj instanceof ButtonGroup buttonGroup) {
            this.object = buttonGroup;

            ButtonGroup buttonsGroup = buttonGroup;
            for (var buttons = buttonsGroup.getElements(); buttons.hasMoreElements();) {
                JRadioButton temp = (JRadioButton) buttons.nextElement();
                if (temp.isSelected()) {
                    return false;
                }
            }
            return true;
        }

        if (obj instanceof JCheckBox jCheckBox) {
            this.object = jCheckBox;

            JCheckBox ticked = jCheckBox;
            return !ticked.isSelected();
        }

        return false;
    }

    public boolean isRequired(Object obj, String message) {
        this.message = (message == null || message.isEmpty())
                ? "Trường này không được để trống" : message;
        return isRequired(obj);
    }

    public boolean isRequired(Object... objs) {
        this.message = "Trường này không được để trống";
        List<Boolean> isTrue = new ArrayList<>();

        Arrays.asList(objs).forEach(obj -> {
            isTrue.add(isRequired(obj));
        });

        return isTrue.stream().anyMatch(t -> t);
    }

//    public boolean isID(JTextField input, String message, String compareID) {
//        this.message = (message == null || message.isEmpty())
//                ? (this.message == null || this.message.isEmpty()
//                ? "This ID is invalid"
//                : this.message)
//                : message;
//
//        return input.getText().equalsIgnoreCase(compareID);
//    }
    public boolean isID(JTextField input, String message, Pattern pattern) {
        this.message = (message == null || message.isEmpty())
                ? (this.message == null || this.message.isEmpty()
                ? "This ID is invalid"
                : this.message)
                : message;

        return pattern.matcher(input.getText()).find();
    }

//    public boolean isPassword(JPasswordField password, String message,
//            String comparePassword) {
//        this.message = (message == null || message.isEmpty())
//                ? this.message != null ? this.message : "This password is invalid"
//                : message;
//
//        String password_value = String.valueOf(password.getPassword());
//
//        return password_value.equalsIgnoreCase(comparePassword);
//    }
    public boolean isPassword(JPasswordField password, String message, Pattern pattern) {
        this.message = (message == null || message.isEmpty())
                ? (this.message == null || this.message.isEmpty()
                ? "This ID is invalid"
                : this.message)
                : message;

        return pattern.matcher(String.valueOf(password.getPassword())).find();
    }

    public boolean isName(JTextField input, String message) {
        this.message = (message == null || message.isEmpty())
                ? "This name is invalid" : message;
        final Pattern regex = Pattern.compile("^[a-z\\D_-]{2,}$");
        return regex.matcher(input.getText()).find();
    }

//    public boolean isName(JTextField input, String message, String compareName) {
//        this.message = (message == null || message.isEmpty())
//                ? "This name is invalid" : message;
//
//        return isName(input, message)
//                && input.getText().equalsIgnoreCase(compareName);
//    }
    public boolean isEmail(String email, String message, String domain) {
        this.message = message == null || message.isEmpty()
                ? "Email is invalid"
                : message;
        return email.matches("^\\w+([\\.-]?\\w+)*@(" + domain + "){1}$");
    }

    public boolean isEmail(String email, String message) {
        this.message = message == null || message.isEmpty()
                ? "Email is invalid"
                : message;
        return email.matches("^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$");
    }

    public boolean isPhone(String phone, String message) {
        this.message = message == null || message.isEmpty()
                ? "Email is invalid"
                : message;
        String numbers = "^[0-9]+$";

        if (!phone.matches(numbers) || phone.length() != 10) {
            return false;
        }

        String first = phone.substring(0, 3);

        final String[] FIRST_NUMBERS_ACCEPTED = new String[]{
            "096", "097", "098", "089", "090", "093", "088", "091", "094", "092",
            "056", "058", "032", "033", "034", "035", "036", "037", "038", "039",
            "070", "076", "077", "078", "079", "081", "082", "083", "084", "085",
            "086", "056", "058", "059", "099"
        };

        return Arrays.asList(FIRST_NUMBERS_ACCEPTED).stream()
                .anyMatch(first_numbers -> first_numbers.equalsIgnoreCase(first));
    }

//    public boolean isEmail(
//            String email, 
//            String message, 
//            String domain, 
//            String compareEmail) {
//        this.message = message == null || message.isEmpty()
//                ? "Email is invalid"
//                : message;
//        return isEmail(email, message, domain)
//                && compareEmail.equals(email);
//    }
    public boolean comparing(String value, String comparatorValue) {
        return value.equalsIgnoreCase(comparatorValue);
    }
    
    public boolean isMark(String mark) {
        return mark.matches("^[0-9]+?.[0-9]+$|^[0-9]+$|^[0-9]+.$");
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
