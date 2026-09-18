package dev.venus.venuscode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class DecodeMain {

    public static void main(String[] args) throws Exception {

        File file = new File("venuscode.png");

        BufferedImage image = ImageIO.read(file);

        String decoded =
                BarcodeDecoder.decode(image);

        System.out.println("Decoded: " + decoded);
    }
}