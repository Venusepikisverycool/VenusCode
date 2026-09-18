package dev.venus.venuscode;

public class BarcodeEncoder {

    public static String encode(String text) {

        StringBuilder binary = new StringBuilder();

        // Store the number of characters as a 32-bit integer.
        int length = text.length();

        String lengthBits = String.format(
                "%32s",
                Integer.toBinaryString(length)
        ).replace(' ', '0');

        binary.append(lengthBits);

        // Store the actual text.
        for (int i = 0; i < text.length(); i++) {

            char character = text.charAt(i);

            String bits = String.format(
                    "%8s",
                    Integer.toBinaryString(character)
            ).replace(' ', '0');

            binary.append(bits);
        }

        return binary.toString();
    }
}