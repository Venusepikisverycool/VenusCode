package dev.venus.venuscode;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class VenusCodeGUI {

    private static final Color DARK_BACKGROUND = new Color(30, 30, 30);
    private static final Color DARK_PANEL = new Color(38, 38, 38);
    private static final Color DARK_INPUT = new Color(50, 50, 50);
    private static final Color DARK_FOREGROUND = new Color(235, 235, 235);

    private static final Color LIGHT_BACKGROUND = new Color(245, 245, 245);
    private static final Color LIGHT_PANEL = Color.WHITE;
    private static final Color LIGHT_INPUT = Color.WHITE;
    private static final Color LIGHT_FOREGROUND = new Color(30, 30, 30);

    private static boolean darkMode = true;

    private static BufferedImage currentImage;

    private static JFrame window;

    private static JPanel mainPanel;
    private static JPanel header;
    private static JPanel encodePanel;
    private static JPanel decodePanel;

    private static JLabel titleLabel;
    private static JLabel encodeMessageLabel;
    private static JLabel encodePreviewLabel;
    private static JLabel decodeResultLabel;

    private static JTextArea textArea;

    private static JLabel imageLabel;

    private static JButton encodeButton;
    private static JButton saveButton;
    private static JButton openButton;

    private static JTabbedPane tabs;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            window =
                    new JFrame("VenusCode");

            window.setDefaultCloseOperation(
                    JFrame.EXIT_ON_CLOSE
            );

            window.setSize(750, 700);

            window.setMinimumSize(
                    new Dimension(550, 550)
            );

            createInterface();

            applyTheme();

            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }

    private static void createInterface() {

        mainPanel =
                new JPanel(new BorderLayout(15, 15));

        mainPanel.setBorder(
                new EmptyBorder(20, 25, 25, 25)
        );

        /*
         * HEADER
         */

        header =
                new JPanel(new BorderLayout());

        titleLabel =
                new JLabel("VenusCode");

        titleLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        30
                )
        );

        JCheckBox themeSwitch =
                new JCheckBox("Dark mode");

        themeSwitch.setSelected(true);

        themeSwitch.addActionListener(event -> {

            darkMode =
                    themeSwitch.isSelected();

            applyTheme();
        });

        header.add(
                titleLabel,
                BorderLayout.WEST
        );

        header.add(
                themeSwitch,
                BorderLayout.EAST
        );

        mainPanel.add(
                header,
                BorderLayout.NORTH
        );

        /*
         * TABS
         */

        tabs =
                new JTabbedPane();

        createEncodeTab();
        createDecodeTab();

        tabs.addTab(
                "Encode",
                encodePanel
        );

        tabs.addTab(
                "Decode",
                decodePanel
        );

        mainPanel.add(
                tabs,
                BorderLayout.CENTER
        );

        window.setContentPane(mainPanel);
    }

    /*
     * ENCODE TAB
     */

    private static void createEncodeTab() {

        encodePanel =
                new JPanel();

        encodePanel.setLayout(
                new BoxLayout(
                        encodePanel,
                        BoxLayout.Y_AXIS
                )
        );

        encodePanel.setBorder(
                new EmptyBorder(20, 10, 10, 10)
        );

        encodeMessageLabel =
                new JLabel("Message");

        encodeMessageLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        encodePanel.add(
                encodeMessageLabel
        );

        encodePanel.add(
                Box.createVerticalStrut(8)
        );

        textArea =
                new JTextArea(5, 40);

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

        encodePanel.add(
                textScroll
        );

        encodePanel.add(
                Box.createVerticalStrut(15)
        );

        encodeButton =
                new JButton("Encode");

        encodeButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        encodeButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        encodeButton.addActionListener(
                event -> encode()
        );

        encodePanel.add(
                encodeButton
        );

        encodePanel.add(
                Box.createVerticalStrut(25)
        );

        encodePreviewLabel =
                new JLabel("Preview");

        encodePreviewLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        encodePanel.add(
                encodePreviewLabel
        );

        encodePanel.add(
                Box.createVerticalStrut(8)
        );

        imageLabel =
                new JLabel();

        imageLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        imageLabel.setOpaque(true);

        JScrollPane imageScroll =
                new JScrollPane(imageLabel);

        imageScroll.setPreferredSize(
                new Dimension(600, 300)
        );

        encodePanel.add(
                imageScroll
        );

        encodePanel.add(
                Box.createVerticalStrut(15)
        );

        saveButton =
                new JButton("Save PNG");

        saveButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        saveButton.setEnabled(false);

        saveButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        saveButton.addActionListener(
                event -> saveImage()
        );

        encodePanel.add(
                saveButton
        );
    }

    /*
     * DECODE TAB
     */

    private static void createDecodeTab() {

        decodePanel =
                new JPanel();

        decodePanel.setLayout(
                new BoxLayout(
                        decodePanel,
                        BoxLayout.Y_AXIS
                )
        );

        decodePanel.setBorder(
                new EmptyBorder(20, 10, 10, 10)
        );

        JLabel title =
                new JLabel("Decode a VenusCode image");

        title.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        16
                )
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        decodePanel.add(title);

        decodePanel.add(
                Box.createVerticalStrut(20)
        );

        openButton =
                new JButton("Open PNG");

        openButton.setFont(
                new Font(
                        "SansSerif",
                        Font.BOLD,
                        15
                )
        );

        openButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        openButton.addActionListener(
                event -> decodeImage()
        );

        decodePanel.add(
                openButton
        );

        decodePanel.add(
                Box.createVerticalStrut(25)
        );

        decodeResultLabel =
                new JLabel(
                        "Decoded text will appear here."
                );

        decodeResultLabel.setFont(
                new Font(
                        "SansSerif",
                        Font.PLAIN,
                        16
                )
        );

        decodeResultLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        decodePanel.add(
                decodeResultLabel
        );
    }

    /*
     * ENCODE
     */

    private static void encode() {

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
    }

    /*
     * SAVE IMAGE
     */

    private static void saveImage() {

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
    }

    /*
     * DECODE IMAGE
     */

    private static void decodeImage() {

        JFileChooser chooser =
                new JFileChooser();

        int result =
                chooser.showOpenDialog(window);

        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {

            BufferedImage image =
                    ImageIO.read(
                            chooser.getSelectedFile()
                    );

            if (image == null) {

                throw new IllegalArgumentException(
                        "The selected file is not a valid image."
                );
            }

            String decoded =
                    BarcodeDecoder.decode(image);

            decodeResultLabel.setText(
                    "<html>Decoded text:<br><br>"
                            + escapeHtml(decoded)
                            + "</html>"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    window,
                    e.getMessage(),
                    "VenusCode Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /*
     * THEME
     */

    private static void applyTheme() {

        Color background;
        Color panel;
        Color input;
        Color foreground;

        if (darkMode) {

            background =
                    DARK_BACKGROUND;

            panel =
                    DARK_PANEL;

            input =
                    DARK_INPUT;

            foreground =
                    DARK_FOREGROUND;

        } else {

            background =
                    LIGHT_BACKGROUND;

            panel =
                    LIGHT_PANEL;

            input =
                    LIGHT_INPUT;

            foreground =
                    LIGHT_FOREGROUND;
        }

        mainPanel.setBackground(
                background
        );

        header.setBackground(
                background
        );

        encodePanel.setBackground(
                panel
        );

        decodePanel.setBackground(
                panel
        );

        titleLabel.setForeground(
                foreground
        );

        encodeMessageLabel.setForeground(
                foreground
        );

        encodePreviewLabel.setForeground(
                foreground
        );

        decodeResultLabel.setForeground(
                foreground
        );

        textArea.setBackground(
                input
        );

        textArea.setForeground(
                foreground
        );

        textArea.setCaretColor(
                foreground
        );

        imageLabel.setBackground(
                Color.WHITE
        );

        tabs.setBackground(
                panel
        );

        tabs.setForeground(
                foreground
        );

        encodeButton.setBackground(
                darkMode
                        ? new Color(65, 65, 65)
                        : new Color(230, 230, 230)
        );

        encodeButton.setForeground(
                foreground
        );

        saveButton.setBackground(
                darkMode
                        ? new Color(65, 65, 65)
                        : new Color(230, 230, 230)
        );

        saveButton.setForeground(
                foreground
        );

        openButton.setBackground(
                darkMode
                        ? new Color(65, 65, 65)
                        : new Color(230, 230, 230)
        );

        openButton.setForeground(
                foreground
        );

        SwingUtilities.updateComponentTreeUI(
                window
        );

        window.repaint();
    }

    /*
     * BASIC HTML ESCAPING
     */

    private static String escapeHtml(
            String text
    ) {

        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\n", "<br>");
    }
}