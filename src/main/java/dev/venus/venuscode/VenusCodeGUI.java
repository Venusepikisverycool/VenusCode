package dev.venus.venuscode;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class VenusCodeGUI {

    private static BufferedImage currentImage;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            JFrame window =
                    new JFrame("VenusCode");

            window.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            window.setSize(700, 700);
            window.setMinimumSize(
                    new Dimension(500, 500)
            );

            JPanel mainPanel =
                    new JPanel(new BorderLayout(15, 15));

            mainPanel.setBorder(
                    new EmptyBorder(20, 20, 20, 20)
            );

            JLabel title =
                    new JLabel("VenusCode");

            title.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            28
                    )
            );

            mainPanel.add(
                    title,
                    BorderLayout.NORTH
            );

            JPanel content =
                    new JPanel();

            content.setLayout(
                    new BoxLayout(
                            content,
                            BoxLayout.Y_AXIS
                    )
            );

            JLabel messageLabel =
                    new JLabel("Message");

            messageLabel.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            16
                    )
            );

            JTextArea textArea =
                    new JTextArea(4, 40);

            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setFont(
                    new Font(
                            "SansSerif",
                            Font.PLAIN,
                            16
                    )
            );

            JScrollPane textScroll =
                    new JScrollPane(textArea);

            JButton encodeButton =
                    new JButton("Encode");

            encodeButton.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            JLabel previewLabel =
                    new JLabel("Preview");

            previewLabel.setFont(
                    new Font(
                            "SansSerif",
                            Font.BOLD,
                            16
                    )
            );

            JLabel imageLabel =
                    new JLabel();

            imageLabel.setHorizontalAlignment(
                    SwingConstants.CENTER
            );

            imageLabel.setVerticalAlignment(
                    SwingConstants.CENTER
            );

            JScrollPane imageScroll =
                    new JScrollPane(imageLabel);

            imageScroll.setPreferredSize(
                    new Dimension(600, 350)
            );

            JButton saveButton =
                    new JButton("Save PNG");

            saveButton.setEnabled(false);

            saveButton.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );

            encodeButton.addActionListener(event -> {

                String text =
                        textArea.getText();

                if (text.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            window,
                            "Please enter some text.",
                            "VenusCode",
                            JOptionPane.WARNING_MESSAGE
                    );

                    return;
                }

                try {

                    String binary =
                            BarcodeEncoder.encode(text);

                    currentImage =
                            BarcodeRenderer.render(
                                    binary
                            );

                    imageLabel.setIcon(
                            new ImageIcon(
                                    currentImage
                            )
                    );

                    saveButton.setEnabled(true);

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(
                            window,
                            e.getMessage(),
                            "VenusCode Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            saveButton.addActionListener(event -> {

                if (currentImage == null) {
                    return;
                }

                JFileChooser chooser =
                        new JFileChooser();

                chooser.setSelectedFile(
                        new File("venuscode.png")
                );

                int result =
                        chooser.showSaveDialog(window);

                if (result != JFileChooser.APPROVE_OPTION) {
                    return;
                }

                try {

                    ImageIO.write(
                            currentImage,
                            "png",
                            chooser.getSelectedFile()
                    );

                    JOptionPane.showMessageDialog(
                            window,
                            "Image saved successfully.",
                            "VenusCode",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (Exception e) {

                    JOptionPane.showMessageDialog(
                            window,
                            e.getMessage(),
                            "VenusCode Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

            content.add(messageLabel);
            content.add(Box.createVerticalStrut(8));
            content.add(textScroll);
            content.add(Box.createVerticalStrut(15));
            content.add(encodeButton);
            content.add(Box.createVerticalStrut(20));
            content.add(previewLabel);
            content.add(Box.createVerticalStrut(8));
            content.add(imageScroll);
            content.add(Box.createVerticalStrut(15));
            content.add(saveButton);

            mainPanel.add(
                    content,
                    BorderLayout.CENTER
            );

            window.setContentPane(mainPanel);

            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
