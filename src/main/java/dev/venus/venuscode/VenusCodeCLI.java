package dev.venus.venuscode;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VenusCodeCLI {

    public static void main(String[] args) {

        if (args.length == 0) {
            printUsage();
            return;
        }

        String command = args[0];

        try {

            switch (command) {

                case "encode":
                    encode(args);
                    break;

                case "decode":
                    decode(args);
                    break;

                default:
                    System.out.println(
                            "Unknown command: " + command
                    );

                    System.out.println();

                    printUsage();
            }

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }

    private static void encode(
            String[] args
    ) throws Exception {

        if (args.length < 2) {

            System.out.println(
                    "Error: missing text to encode."
            );

            System.out.println();

            System.out.println(
                    "Usage: venuscode encode \"text\" [output.png]"
            );

            return;
        }

        String text =
                args[1];

        String outputFilename =
                args.length >= 3
                        ? args[2]
                        : "venuscode.png";

        String binary =
                BarcodeEncoder.encode(text);

        BufferedImage image =
                BarcodeRenderer.render(binary);

        File outputFile =
                new File(outputFilename);

        ImageIO.write(
                image,
                "png",
                outputFile
        );

        System.out.println(
                "Created " + outputFilename
        );
    }

    private static void decode(
            String[] args
    ) throws Exception {

        if (args.length < 2) {

            System.out.println(
                    "Error: missing image filename."
            );

            System.out.println();

            System.out.println(
                    "Usage: venuscode decode <image.png>"
            );

            return;
        }

        String filename =
                args[1];

        File inputFile =
                new File(filename);

        if (!inputFile.exists()) {

            System.out.println(
                    "Error: file does not exist: "
                            + filename
            );

            return;
        }

        BufferedImage image =
                ImageIO.read(inputFile);

        if (image == null) {

            System.out.println(
                    "Error: file is not a valid image."
            );

            return;
        }

        String decoded =
                BarcodeDecoder.decode(image);

        System.out.println(decoded);
    }

    private static void printUsage() {

        System.out.println(
                "VenusCode"
        );

        System.out.println(
                "Custom barcode encoder and decoder."
        );

        System.out.println();

        System.out.println(
                "Usage:"
        );

        System.out.println(
                "  venuscode encode \"text\" [output.png]"
        );

        System.out.println(
                "  venuscode decode <image.png>"
        );

        System.out.println();

        System.out.println(
                "Examples:"
        );

        System.out.println(
                "  venuscode encode \"Hello world\""
        );

        System.out.println(
                "  venuscode encode \"Hello world\" hello.png"
        );

        System.out.println(
                "  venuscode decode hello.png"
        );
    }
}