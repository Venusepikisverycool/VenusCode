package dev.venus.venuscode;

import java.awt.image.BufferedImage;

public class BarcodeRenderer {

    private static final int SCALE = 10;
    private static final int MARKER_WIDTH = 3;
    private static final int TERMINATOR_WIDTH = 1;

    public static BufferedImage render(String binary) {

        int dataBits = binary.length();

        /*
         * Each column holds dataHeight bits.
         *
         * The format grows like this:
         *
         * 24 x 8
         * 24 x 24
         * 48 x 24
         * 48 x 48
         * 96 x 48
         * 96 x 96
         * ...
         */

        int dataHeight = 8;
        int maxDataWidth = 24;

        // Grow the height when the current stage can't contain the data.
        while (dataBits > maxDataWidth * dataHeight) {

            if (dataHeight < maxDataWidth) {
                dataHeight = maxDataWidth;
            } else {
                maxDataWidth *= 2;
            }
        }

        // Only use as many data columns as needed.
        int dataWidth =
                (int) Math.ceil((double) dataBits / dataHeight);

        /*
         * Layout:
         *
         * LEFT MARKER | DATA | TERMINATOR | RIGHT MARKER
         *
         * Left marker  = 3 modules
         * Terminator   = 1 module
         * Right marker = 3 modules
         */
        int totalWidth =
                MARKER_WIDTH
                        + dataWidth
                        + TERMINATOR_WIDTH
                        + MARKER_WIDTH;

        int totalHeight = dataHeight;

        BufferedImage image = new BufferedImage(
                totalWidth * SCALE,
                totalHeight * SCALE,
                BufferedImage.TYPE_INT_RGB
        );

        // Draw data.
        for (int bit = 0; bit < dataBits; bit++) {

            int x = bit / dataHeight;
            int y = bit % dataHeight;

            int color =
                    binary.charAt(bit) == '1'
                            ? 0x000000
                            : 0xFFFFFF;

            drawModule(
                    image,
                    x + MARKER_WIDTH,
                    y,
                    SCALE,
                    color
            );
        }

        // Fill unused data modules with white.
        for (int x = 0; x < dataWidth; x++) {
            for (int y = 0; y < dataHeight; y++) {

                int bit = x * dataHeight + y;

                if (bit >= dataBits) {
                    drawModule(
                            image,
                            x + MARKER_WIDTH,
                            y,
                            SCALE,
                            0xFFFFFF
                    );
                }
            }
        }

        /*
         * TERMINATOR
         *
         * Every module is normally white.
         *
         * The bottom module is black.
         * This tells the decoder:
         *
         * "STOP READING."
         */
        int terminatorX = MARKER_WIDTH + dataWidth;

        for (int y = 0; y < dataHeight; y++) {

            int color =
                    y == dataHeight - 1
                            ? 0x000000
                            : 0xFFFFFF;

            drawModule(
                    image,
                    terminatorX,
                    y,
                    SCALE,
                    color
            );
        }

        // Left marker: white stripe in the middle.
        int middleRow = dataHeight / 2;

        for (int y = 0; y < dataHeight; y++) {

            int color =
                    y == middleRow
                            ? 0xFFFFFF
                            : 0x000000;

            for (int x = 0; x < MARKER_WIDTH; x++) {

                drawModule(
                        image,
                        x,
                        y,
                        SCALE,
                        color
                );
            }
        }

        /*
         * Right marker: white stripe at the bottom.
         */
        int rightMarkerX =
                MARKER_WIDTH
                        + dataWidth
                        + TERMINATOR_WIDTH;

        for (int y = 0; y < dataHeight; y++) {

            int color =
                    y == dataHeight - 1
                            ? 0xFFFFFF
                            : 0x000000;

            for (int x = 0; x < MARKER_WIDTH; x++) {

                drawModule(
                        image,
                        rightMarkerX + x,
                        y,
                        SCALE,
                        color
                );
            }
        }

        return image;
    }

    private static void drawModule(
            BufferedImage image,
            int moduleX,
            int moduleY,
            int scale,
            int color
    ) {

        for (int x = 0; x < scale; x++) {
            for (int y = 0; y < scale; y++) {

                image.setRGB(
                        moduleX * scale + x,
                        moduleY * scale + y,
                        color
                );
            }
        }
    }
}