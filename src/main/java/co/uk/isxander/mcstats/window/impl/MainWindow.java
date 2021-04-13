package co.uk.isxander.mcstats.window.impl;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import co.uk.isxander.mcstats.McStats;
import co.uk.isxander.mcstats.handlers.ChatListener;
import co.uk.isxander.mcstats.handlers.PlayerHandler;
import co.uk.isxander.mcstats.handlers.WindowManager;
import co.uk.isxander.mcstats.licensing.License;
import co.uk.isxander.mcstats.utils.*;
import co.uk.isxander.mcstats.window.Window;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import net.miginfocom.swing.*;

public class MainWindow extends Window {

    private static MainWindow instance;

    public MainWindow() {
        instance = this;
    }

    public static MainWindow getInstance() {
        return instance;
    }

    private List<String> messages = new ArrayList<>();
    private Map<Integer, Warning> warnings = new HashMap<>();

    private boolean autoscroll = false;

    @Override
    public void open() {
        License license = McStats.getLicense();
        if (license == null || !license.isValid()) {
//            McStats.getConfig().getConfigFile().delete();
//            int input = JOptionPane.showOptionDialog(null, "Your license key is invalid.\nRun the program again to enter your new license key.", "License Key", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, null, null);
//            System.exit(0);
        }

        setUndecorated(true);
        initComponents();
        //overwriteComponentProperties();
        textPane.addStyle("MinecraftStyle", null);
        this.setVisible(true);
        new ChatListener().start();
        updateDisplay();
    }

    private void closeButtonActionPerformed(ActionEvent e) {
        this.dispose();
        System.exit(0);
    }

    private boolean pressed;
    private int diffX;
    private int diffY;

    private void draggableAreaMouseDragged(MouseEvent e) {
        if (pressed) {
            this.setLocation(MouseInfo.getPointerInfo().getLocation().x - diffX, MouseInfo.getPointerInfo().getLocation().y - diffY);
        }
    }

    private void draggableAreaMousePressed(MouseEvent e) {
        pressed = true;
        diffX = MouseInfo.getPointerInfo().getLocation().x - this.getX();
        diffY = MouseInfo.getPointerInfo().getLocation().y - this.getY();
    }

    private void draggableAreaMouseReleased(MouseEvent e) {
        pressed = false;
    }

    private void hideButtonActionPerformed(ActionEvent e) {
        this.setState(Frame.ICONIFIED);
    }

    public void update() {
        AtomicBoolean needUpdate = new AtomicBoolean(false);
        new HashMap<>(warnings).forEach((id, w) -> {
            w.update();
            if (w.hasExpired()) {
                warnings.remove(id);
                needUpdate.set(true);
            }
        });
        if (needUpdate.get())
            updateDisplay();
    }

    public void updateDisplay() {
        clearText();
        PlayerHandler.getInstance().foreachPlayer((username, player) -> {
            if (player == null || !player.isSuccess()) {
                String prefix = "";
                if (player != null)
                    prefix = (!player.isSuccess() ? Formatting.MC_RED.toString() : "");
                addMessage(prefix
                        + InlineFormatter.get("...", 6, 8, "...")
                        + InlineFormatter.get(username, 14, 16, "...")
                        + InlineFormatter.get("...", 10, 11, "...")
                        + InlineFormatter.get("...", 6, 8, "...")
                        + InlineFormatter.get("...", 6, 8, "...")
                        + InlineFormatter.get("...", 6, 8, "...")
                );
            } else {
                addMessage(
                          InlineFormatter.get(StringUtils.formatStar(player.getStars()), 6, 8, "...")
                        + Formatting.MC_CHAR + player.getRank().getColorCode() + InlineFormatter.get(username, 14, 16, "...")
                        + InlineFormatter.get(StringUtils.formatTags(player), 10, 11, "...")
                        + InlineFormatter.get(StringUtils.formatFkdr(player.getFinalKillDeathRatio()), 6, 8, "...")
                        + InlineFormatter.get(StringUtils.formatFkdr(player.getWinLossRatio()), 6, 8, "...")
                        + InlineFormatter.get(StringUtils.formatFkdr(player.getBedBreakLossRatio()), 6, 8, "...")
                );
            }
        });

        for (String message : new ArrayList<>(messages)) {
            printLine(message);
        }
        printLine("");
        warnings.forEach((id, w) -> {
            printLine(Formatting.MC_YELLOW + w.getMessage());
        });
    }

    public void clearText() {
        textPane.setText("");
        messages.clear();
        addMessage(
                InlineFormatter.get("STARS", 6, 8, "...")
                        + InlineFormatter.get("NAME", 14, 16, "...")
                        + InlineFormatter.get("TAGS", 10, 11, "...")
                        + InlineFormatter.get("FKDR", 6, 8, "...")
                        + InlineFormatter.get("WLR", 6, 8, "...")
                        + InlineFormatter.get("BBLR", 6, 8, "...")
        );
    }

    public void printLine(String message) {
        char[] chars = message.toCharArray();
        StyledDocument doc = textPane.getStyledDocument();
        Style style = textPane.getStyle("MinecraftStyle");
        StyleConstants.setForeground(style, Color.white);
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == Formatting.MC_CHAR.toString().toCharArray()[0]) {
                switch (Character.toLowerCase(chars[i + 1])) {
                    case '0':
                        StyleConstants.setForeground(style, new Color(30, 30, 30));
                        break;
                    case '1':
                        StyleConstants.setForeground(style, new Color(0, 0, 170));
                        break;
                    case '2':
                        StyleConstants.setForeground(style, new Color(0, 170, 0));
                        break;
                    case '3':
                        StyleConstants.setForeground(style, new Color(0, 170, 170));
                        break;
                    case '4':
                        StyleConstants.setForeground(style, new Color(170, 0, 0));
                        break;
                    case '5':
                        StyleConstants.setForeground(style, new Color(170, 0, 170));
                        break;
                    case '6':
                        StyleConstants.setForeground(style, new Color(255, 170, 0));
                        break;
                    case '7':
                        StyleConstants.setForeground(style, new Color(170, 170, 170));
                        break;
                    case '8':
                        StyleConstants.setForeground(style, new Color(85, 85, 85));
                        break;
                    case '9':
                        StyleConstants.setForeground(style, new Color(85, 85, 255));
                        break;
                    case 'a':
                        StyleConstants.setForeground(style, new Color(85, 255, 85));
                        break;
                    case 'b':
                        StyleConstants.setForeground(style, new Color(85, 255, 255));
                        break;
                    case 'c':
                        StyleConstants.setForeground(style, new Color(255, 85, 85));
                        break;
                    case 'd':
                        StyleConstants.setForeground(style, new Color(255, 85, 255));
                        break;
                    case 'e':
                        StyleConstants.setForeground(style, new Color(255, 255, 85));
                        break;
                    case 'f':
                        StyleConstants.setForeground(style, new Color(255, 255, 255));
                        break;
                    case 'l':
                        StyleConstants.setBold(style, true);
                        break;
                    case 'o':
                        StyleConstants.setItalic(style, true);
                        break;
                    case 'n':
                        StyleConstants.setUnderline(style, true);
                        break;
                    case 'm':
                        StyleConstants.setStrikeThrough(style, true);
                        break;
                    case 'r':
                        resetStyle(style);
                        break;
                    case 'k':
                        break;
                    default:
                        continue;
                }
                i += 1;

            }
            else {
                try { doc.insertString(doc.getLength(), Character.toString(chars[i]), style); }
                catch (BadLocationException e) {}
                continue;
            }
        }
        try { doc.insertString(doc.getLength(), "\n", style); }
        catch (BadLocationException e) {}
        resetStyle(style);
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }

    public void addWarning(Warning warning) {
        if (!warnings.containsKey(warning.getId())) {
            warnings.put(warning.getId(), warning);
            this.updateDisplay();
        }
    }

    public Warning getWarning(int id) {
        return warnings.get(id);
    }

    public void removeWarning(int id) {
        warnings.remove(id);
        updateDisplay();
    }

    private void resetStyle(Style style) {
        StyleConstants.setForeground(style, Color.white);
        StyleConstants.setBold(style, false);
        StyleConstants.setItalic(style, false);
        StyleConstants.setUnderline(style, false);
        StyleConstants.setStrikeThrough(style, false);
    }

    private void fullscreenButtonActionPerformed(ActionEvent e) {
        if (OsCheck.getOperatingSystemType() == OsCheck.OSType.Windows)  {
            WindowManager.fullscreenMinecraft();
        } else {
            this.addWarning(new Warning(4, "You must be on Windows to use this feature at this time.", 5000));
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        draggableArea = new JTextField();
        hideButton = new JButton();
        fullscreenButton = new JButton();
        closeButton = new JButton();
        scrollPane1 = new JScrollPane();
        textPane = new JTextPane();

        //======== this ========
        setTitle("McStats");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setBackground(new Color(40, 40, 40));
        setForeground(SystemColor.text);
        setFont(new Font("Fixedsys Excelsior 3.01", Font.PLAIN, 12));
        setOpacity(0.75F);
        setUndecorated(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[18,fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- draggableArea ----
        draggableArea.setEnabled(false);
        draggableArea.setFont(draggableArea.getFont().deriveFont(draggableArea.getFont().getSize() + 4f));
        draggableArea.setForeground(Color.black);
        draggableArea.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                draggableAreaMouseDragged(e);
            }
        });
        draggableArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                draggableAreaMousePressed(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                draggableAreaMouseReleased(e);
            }
        });
        contentPane.add(draggableArea, "cell 0 0 20 1,alignx left,growx 0,width 500");

        //---- hideButton ----
        hideButton.setBackground(new Color(255, 131, 0));
        hideButton.setHorizontalAlignment(SwingConstants.RIGHT);
        hideButton.setToolTipText("Hide");
        hideButton.addActionListener(e -> hideButtonActionPerformed(e));
        contentPane.add(hideButton, "cell 20 0,width 30:30:30,height 30:30:30");

        //---- fullscreenButton ----
        fullscreenButton.setBackground(Color.green);
        fullscreenButton.setHorizontalAlignment(SwingConstants.RIGHT);
        fullscreenButton.setToolTipText("Fullscreen");
        fullscreenButton.addActionListener(e -> fullscreenButtonActionPerformed(e));
        contentPane.add(fullscreenButton, "cell 21 0,alignx center,growx 0,width 30:30:30,height 30:30:30");

        //---- closeButton ----
        closeButton.setBackground(new Color(197, 0, 0));
        closeButton.setPreferredSize(new Dimension(30, 30));
        closeButton.setToolTipText("Exit");
        closeButton.setHorizontalAlignment(SwingConstants.RIGHT);
        closeButton.setForeground(Color.white);
        closeButton.addActionListener(e -> closeButtonActionPerformed(e));
        contentPane.add(closeButton, "cell 22 0,alignx right,growx 0,width 30:30:30,height 30:30:30");

        //======== scrollPane1 ========
        {
            scrollPane1.setForeground(SystemColor.scrollbar);
            scrollPane1.setAutoscrolls(true);

            //---- textPane ----
            textPane.setFont(new Font("Consolas", Font.PLAIN, 14));
            textPane.setBackground(Color.black);
            textPane.setForeground(Color.white);
            textPane.setEditable(false);
            scrollPane1.setViewportView(textPane);
        }
        contentPane.add(scrollPane1, "cell 0 1 23 13,width 600,height 340");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JTextField draggableArea;
    private JButton hideButton;
    private JButton fullscreenButton;
    private JButton closeButton;
    private JScrollPane scrollPane1;
    private JTextPane textPane;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
