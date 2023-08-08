package main.java.member;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class Student {

    public Student(String ID, String name) {
        this.ID = ID;
        this.name = name;
    }

    public Student() {
    }

    private String ID;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String address;
    private String avatar;

    public String getID() {
        return ID;
    }

    @Override
    public String toString() {
        return "Student{" + "ID=" + ID + ", name=" + name + ", email=" + email + ", phone=" + phone + ", gender=" + gender + ", address=" + address + ", avatar=" + avatar + '}';
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return createEmail(name);
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = createEmail(name);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<String> toList() {
        ArrayList<String> temp = new ArrayList<>();

        temp.add(ID);
        temp.add(name);
        temp.add(email);
        temp.add(phone);
        temp.add(gender);
        temp.add(address);
        temp.add(avatar);
        
        return temp;
    }

    public Object[] toArray() {
        return new Object[]{
            getID().toUpperCase(),
            getName(),
            createEmail(name),
            getPhone(),
            getGender().equals("0") ? "Nam" : "Nữ",
            getAddress(),
            avatar == null || avatar.isEmpty() ? "No-avatar" : getAvatar()
        };
    }

    public void setValues(String... values) {
        if (values.length < 6) {
            return;
        }
        ID = values[0];
        name = values[1];
        email = values[2];
        phone = values[3];
        gender = values[4];
        address = values[5];
    }

    public String createEmail(String fullname) {
        String[] words = fullname.split(" ");

        StringBuilder sb = new StringBuilder();

        sb.append(words[words.length - 1]);

        for (int i = 0; i < words.length - 1; i++) {
            String word = words[i];
            sb.append(word.substring(0, 1));
        }

        sb.append(ID);
        sb.append("@fpt.edu.vn");

        return Normalizer.normalize(sb.toString(),
                Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .replaceAll("Đ|đ", "D").toLowerCase();
    }
}
