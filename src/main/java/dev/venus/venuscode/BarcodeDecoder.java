package dev.venus.venuscode;

import java.awt.image.BufferedImage;

public class BarcodeDecoder {

    private static final int SCALE = 10;
    private static final int MARKER_WIDTH = 3;
    private static final int LENGTH_BITS = 32;

    public static String decode(BufferedImage image) {

        /*
         * First figure out which way the VenusCode
         * barcode is facing.
         */
        BufferedImage oriented =
                orientImage(image);

        /*
         * From this point onward, the barcode is
         * guaranteed to be facing the normal direction.
         */
        return decodeOriented(oriented);
    }

    private static BufferedImage decodeImageOrientation(
            BufferedImage image
    ) {

        return orientImage(image);
    }

    private static BufferedImage orientImage(
            BufferedImage image
    ) {

        /*
         * Try all four possible rotations.
         *
         * 0   degrees
         * 90  degrees clockwise
         * 180 degrees
         * 270 degrees clockwise
         *
         * Whichever rotation produces valid markers
         * is the correct orientation.
         */

        BufferedImage current = image;

        for (int rotation = 0;
             rotation < 4;
             rotation++) {

            if (hasValidMarkers(current)) {
                return current;
            }

            current =
                    rotate90(current);
        }

        throw new IllegalArgumentException(
                "Could not determine VenusCode orientation."
        );
    }

    private static boolean hasValidMarkers(
            BufferedImage image
    ) {

        if (image.getWidth() % SCALE != 0
                || image.getHeight() % SCALE != 0) {

            return false;
        }

        int width =
                image.getWidth() / SCALE;

        int height =
                image.getHeight() / SCALE;

        if (width < 8 || height < 1) {
            return false;
        }

        try {

            validateMarkers(
                    image,
                    width,
                    height
            );

            return true;

        } catch (IllegalArgumentException e) {

            return false;
        }
    }

    private static BufferedImage rotate90(
            BufferedImage original
    ) {

        int width =
                original.getWidth();

        int height =
                original.getHeight();

        BufferedImage rotated =
                new BufferedImage(
                        height,
                        width,
                        original.getType()
                );

        for (int x = 0;
             x < width;
             x++) {

            for (int y = 0;
                 y < height;
                 y++) {

                rotated.setRGB(
                        height - 1 - y,
                        x,
                        original.getRGB(x, y)
                );
            }
        }

        return rotated;
    }

    private static String decodeOriented(
            BufferedImage image
    ) {

        int width =
                image.getWidth() / SCALE;

        int height =
                image.getHeight() / SCALE;

        if (image.getWidth() % SCALE != 0
                || image.getHeight() % SCALE != 0) {

            throw new IllegalArgumentException(
                    "Image dimensions are not valid for VenusCode."
            );
        }

        if (width < 8 || height < 1) {

            throw new IllegalArgumentException(
                    "Image is too small to be a VenusCode barcode."
            );
        }

        validateMarkers(
                image,
                width,
                height
        );

        /*
         * Normal orientation:
         *
         * LEFT MARKER | DATA | TERMINATOR | RIGHT MARKER
         */

        int dataStartX =
                MARKER_WIDTH;

        int rightMarkerStart =
                width - MARKER_WIDTH;

        int terminatorX =
                rightMarkerStart - 1;

        StringBuilder binary =
                new StringBuilder();

        /*
         * Read:
         *
         * top → bottom
         * then next column
         */
        for (int x = dataStartX;
             x < terminatorX;
             x++) {

            for (int y = 0;
                 y < height;
                 y++) {

                if (isBlack(image, x, y)) {
                    binary.append('1');
                } else {
                    binary.append('0');
                }
            }
        }

        /*
         * Check the terminator.
         */
        if (!isBlack(
                image,
                terminatorX,
                height - 1
        )) {

            throw new IllegalArgumentException(
                    "VenusCode terminator was not found."
            );
        }

        /*
         * Need the 32-bit character count.
         */
        if (binary.length() < LENGTH_BITS) {

            throw new IllegalArgumentException(
                    "VenusCode does not contain a character count."
            );
        }

        /*
         * Read character count.
         */
        String lengthBits =
                binary.substring(
                        0,
                        LENGTH_BITS
                );

        long characterCountLong =
                Long.parseLong(
                        lengthBits,
                        2
                );

        if (characterCountLong > Integer.MAX_VALUE) {

            throw new IllegalArgumentException(
                    "VenusCode character count is too large."
            );
        }

        int characterCount =
                (int) characterCountLong;

        int requiredBits =
                LENGTH_BITS
                        + characterCount * 8;

        if (requiredBits > binary.length()) {

            throw new IllegalArgumentException(
                    "VenusCode does not contain enough data."
            );
        }

        /*
         * Ignore padding.
         */
        String textBits =
                binary.substring(
                        LENGTH_BITS,
                        requiredBits
                );

        return binaryToText(textBits);
    }

    private static String binaryToText(
            String binary
    ) {

        if (binary.length() % 8 != 0) {

            throw new IllegalArgumentException(
                    "VenusCode data does not contain a complete byte."
            );
        }

        StringBuilder text =
                new StringBuilder();

        for (int i = 0;
             i < binary.length();
             i += 8) {

            String byteString =
                    binary.substring(
                            i,
                            i + 8
                    );

            int value =
                    Integer.parseInt(
                            byteString,
                            2
                    );

            text.append(
                    (char) value
            );
        }

        return text.toString();
    }

    private static boolean isBlack(
            BufferedImage image,
            int moduleX,
            int moduleY
    ) {

        int pixelX =
                moduleX * SCALE
                        + SCALE / 2;

        int pixelY =
                moduleY * SCALE
                        + SCALE / 2;

        int rgb =
                image.getRGB(
                        pixelX,
                        pixelY
                );

        int red =
                (rgb >> 16) & 0xFF;

        int green =
                (rgb >> 8) & 0xFF;

        int blue =
                rgb & 0xFF;

        return red < 128
                && green < 128
                && blue < 128;
    }

    private static void validateMarkers(
            BufferedImage image,
            int width,
            int height
    ) {

        /*
         * LEFT MARKER
         *
         * Black everywhere except
         * the middle row.
         */
        int middleRow =
                height / 2;

        for (int y = 0;
             y < height;
             y++) {

            boolean expectedBlack =
                    y != middleRow;

            for (int x = 0;
                 x < MARKER_WIDTH;
                 x++) {

                boolean actualBlack =
                        isBlack(
                                image,
                                x,
                                y
                        );

                if (actualBlack
                        != expectedBlack) {

                    throw new IllegalArgumentException(
                            "Invalid VenusCode left orientation marker."
                    );
                }
            }
        }

        /*
         * RIGHT MARKER
         *
         * Black everywhere except
         * the bottom row.
         */
        int rightMarkerStart =
                width - MARKER_WIDTH;

        for (int y = 0;
             y < height;
             y++) {

            boolean expectedBlack =
                    y != height - 1;

            for (int x = rightMarkerStart;
                 x < width;
                 x++) {

                boolean actualBlack =
                        isBlack(
                                image,
                                x,
                                y
                        );

                if (actualBlack
                        != expectedBlack) {

                    throw new IllegalArgumentException(
                            "Invalid VenusCode right orientation marker."
                    );
                }
            }
        }
    }
}