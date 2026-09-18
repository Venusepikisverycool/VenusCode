package dev.venus.venuscode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {

        String binary =
                BarcodeEncoder.encode("https://www.wikipedia.org/");

        BufferedImage image =
                BarcodeRenderer.render(binary);

        ImageIO.write(
                image,
                "png",
                new File("venuscode.png")
        );

        System.out.println("Created venuscode.png");
    }
}