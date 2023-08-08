package main.java.ui.customized;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundImage extends JPanel {

    private String source;
    private File file;
    private Image image;

    public BackgroundImage() {
    }

    public BackgroundImage(String source) throws IOException {
        this.image = ImageIO.read(new File(source));
    }

    public BackgroundImage(File file) throws IOException {
        this.image = ImageIO.read(file);
    }

    public BackgroundImage(Image image) {
        this.image = image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void getScaledImage(int width, int height, int scaled) {
        this.image = this.image
                .getScaledInstance(width, height, scaled);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.image, 0, 0, this);
    }
}
