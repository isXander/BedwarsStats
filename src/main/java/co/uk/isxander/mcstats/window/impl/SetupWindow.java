package co.uk.isxander.mcstats.window.impl;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import co.uk.isxander.mcstats.McStats;
import co.uk.isxander.mcstats.config.Configuration;
import co.uk.isxander.mcstats.utils.Multithreading;
import co.uk.isxander.mcstats.utils.ThreadCount;
import co.uk.isxander.mcstats.window.Window;

import javax.swing.*;

import com.bulenkov.darcula.DarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.*;

public class SetupWindow extends Window {
    @Override
    public void open() {
        initComponents();
        speedDropdown.setSelectedIndex(McStats.getConfig().speed.ordinal());
        this.setVisible(true);
    }

    private void finishedButtonActionPerformed(ActionEvent e) {
        McStats.getConfig().apiKey = apiKeyField.getText();
        McStats.getConfig().minecraftFolder = new File(mcFolderTextField.getText());
        McStats.getConfig().lunarFolder = new File(lcFolderTextField.getText());
        McStats.getConfig().trackLobbies = trackLobbiesCheckbox.isSelected();
        McStats.getConfig().speed = ThreadCount.values()[speedDropdown.getSelectedIndex()];
        McStats.getConfig().licenseKey = licenseKeyField.getText();
        McStats.getConfig().save();
        this.dispose();
        onClose();
    }

    protected void onClose() {

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        licenseKeyLabel = new JLabel();
        licenseKeyField = new JTextField();
        licenseKeyField.setText(McStats.getConfig().licenseKey);
        apiKeyLabel = new JLabel();
        apiKeyField = new JTextField();
        apiKeyField.setText(McStats.getConfig().apiKey);
        mcFolderLabel = new JLabel();
        mcFolderTextField = new JTextField();
        mcFolderTextField.setText(McStats.getConfig().minecraftFolder.getPath());
        lcFolderLabel = new JLabel();
        lcFolderTextField = new JTextField();
        lcFolderTextField.setText(McStats.getConfig().lunarFolder.getPath());
        label1 = new JLabel();
        trackLobbiesCheckbox = new JCheckBox();
        trackLobbiesCheckbox.setSelected(McStats.getConfig().trackLobbies);
        speedLabel = new JLabel();
        speedDropdown = new JComboBox<>();
        finishedButton = new JButton();

        //======== this ========
        setTitle("McStats Setup");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- licenseKeyLabel ----
        licenseKeyLabel.setText("License Key:");
        licenseKeyLabel.setFont(licenseKeyLabel.getFont().deriveFont(licenseKeyLabel.getFont().getSize() + 6f));
        contentPane.add(licenseKeyLabel, "cell 0 0");

        //---- licenseKeyField ----
        licenseKeyField.setFont(licenseKeyField.getFont().deriveFont(licenseKeyField.getFont().getSize() + 6f));
        contentPane.add(licenseKeyField, "cell 1 0");

        //---- apiKeyLabel ----
        apiKeyLabel.setText("API Key:");
        apiKeyLabel.setFont(apiKeyLabel.getFont().deriveFont(apiKeyLabel.getFont().getSize() + 6f));
        contentPane.add(apiKeyLabel, "cell 0 1");

        //---- apiKeyField ----
        apiKeyField.setFont(apiKeyField.getFont().deriveFont(apiKeyField.getFont().getSize() + 6f));
        contentPane.add(apiKeyField, "cell 1 1,width 300");

        //---- mcFolderLabel ----
        mcFolderLabel.setText("MC Folder: ");
        mcFolderLabel.setFont(mcFolderLabel.getFont().deriveFont(mcFolderLabel.getFont().getSize() + 6f));
        contentPane.add(mcFolderLabel, "cell 0 2");

        //---- mcFolderTextField ----
        mcFolderTextField.setFont(mcFolderTextField.getFont().deriveFont(mcFolderTextField.getFont().getSize() + 6f));
        contentPane.add(mcFolderTextField, "cell 1 2");

        //---- lcFolderLabel ----
        lcFolderLabel.setText("LC Folder:");
        lcFolderLabel.setFont(lcFolderLabel.getFont().deriveFont(lcFolderLabel.getFont().getSize() + 6f));
        contentPane.add(lcFolderLabel, "cell 0 3");

        //---- lcFolderTextField ----
        lcFolderTextField.setFont(lcFolderTextField.getFont().deriveFont(lcFolderTextField.getFont().getSize() + 6f));
        contentPane.add(lcFolderTextField, "cell 1 3");

        //---- label1 ----
        label1.setText("Track Lobbies: ");
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 6f));
        contentPane.add(label1, "cell 0 4");

        //---- trackLobbiesCheckbox ----
        trackLobbiesCheckbox.setToolTipText("Whether or not the program will track people in lobbies");
        contentPane.add(trackLobbiesCheckbox, "cell 1 4,alignx left,growx 0");

        //---- speedLabel ----
        speedLabel.setText("Speed:");
        speedLabel.setFont(speedLabel.getFont().deriveFont(speedLabel.getFont().getSize() + 6f));
        contentPane.add(speedLabel, "cell 0 5");

        //---- speedDropdown ----
        speedDropdown.setModel(new DefaultComboBoxModel<>(new String[] {
            "2 threads (very slow but performant)",
            "4 threads (slow but less performant)",
            "8 threads (default)",
            "12 threads (fast but expensive)",
            "16 threads (very fast but very expensive)"
        }));
        speedDropdown.setSelectedIndex(2);
        contentPane.add(speedDropdown, "cell 1 5");

        //---- finishedButton ----
        finishedButton.setText("Finished");
        finishedButton.addActionListener(e -> finishedButtonActionPerformed(e));
        contentPane.add(finishedButton, "cell 0 6 6 1");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel licenseKeyLabel;
    private JTextField licenseKeyField;
    private JLabel apiKeyLabel;
    private JTextField apiKeyField;
    private JLabel mcFolderLabel;
    private JTextField mcFolderTextField;
    private JLabel lcFolderLabel;
    private JTextField lcFolderTextField;
    private JLabel label1;
    private JCheckBox trackLobbiesCheckbox;
    private JLabel speedLabel;
    private JComboBox<String> speedDropdown;
    private JButton finishedButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
