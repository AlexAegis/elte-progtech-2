package com.alexaegis.progtech.window;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static com.alexaegis.progtech.Main.connector;
import static com.alexaegis.progtech.Main.dbProperties;
import static com.alexaegis.progtech.Main.sshProperties;
import static com.alexaegis.progtech.window.MainWindow.displayProperties;

public class ConnectionWindow extends JFrame {

    private JPanel connectionPane;
    private int width = Integer.parseInt(displayProperties.getProperty("connectprompt.width"));
    private int height = Integer.parseInt(displayProperties.getProperty("connectprompt.height"));

    private String title = displayProperties.getProperty("connectprompt.title");
    private boolean sshEnabled = false;
    private JFormattedTextField dbHostField;
    private JFormattedTextField dbPortField;

    private JFormattedTextField dbDatabaseField;
    private JFormattedTextField dbUsernameField;
    private JPasswordField dbPasswordField;

    private JFormattedTextField sshServerField;
    private JFormattedTextField sshPortField;
    private JFormattedTextField sshLocalHostField;
    private JFormattedTextField sshLocalPortField;
    private JFormattedTextField sshUsernameField;
    private JPasswordField sshPasswordField;

    public ConnectionWindow() {
        setTitle(title);
		setSize(width, height);
		setLocationRelativeTo(null);
		connectionPane = new JPanel();
		connectionPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(connectionPane);
		connectionPane.setLayout(new BoxLayout(connectionPane, BoxLayout.Y_AXIS));
        addDBPanel();
        addSSHPanel();
        addButtonPanel();
        setSshElements(false);
		setVisible(true);
		revalidate();
		repaint();
    }

    private void addDBPanel() {
        JPanel databasePanel = new JPanel();
        databasePanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Database Connection", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        connectionPane.add(databasePanel);
        GridBagLayout gbl_databasePanel = new GridBagLayout();
        gbl_databasePanel.columnWidths = new int[]{64, 180, 64, 40, 0};
        gbl_databasePanel.rowHeights = new int[]{23, 23, 23, 23, 23, 0};
        gbl_databasePanel.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_databasePanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        databasePanel.setLayout(gbl_databasePanel);

        JLabel dbHostLabel = new JLabel("host:");
        GridBagConstraints gbc_dbHostLabel = new GridBagConstraints();
        gbc_dbHostLabel.anchor = GridBagConstraints.EAST;
        gbc_dbHostLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dbHostLabel.gridx = 0;
        gbc_dbHostLabel.gridy = 0;
        databasePanel.add(dbHostLabel, gbc_dbHostLabel);

        dbHostField = new JFormattedTextField();
        dbHostField.setText(dbProperties.getProperty("rhost"));
        GridBagConstraints gbc_dbHostField = new GridBagConstraints();
        gbc_dbHostField.fill = GridBagConstraints.BOTH;
        gbc_dbHostField.insets = new Insets(0, 0, 5, 5);
        gbc_dbHostField.gridx = 1;
        gbc_dbHostField.gridy = 0;
        databasePanel.add(dbHostField, gbc_dbHostField);

        JLabel dbPortLabel = new JLabel("port:");
        GridBagConstraints gbc_dbPortLabel = new GridBagConstraints();
        gbc_dbPortLabel.anchor = GridBagConstraints.EAST;
        gbc_dbPortLabel.fill = GridBagConstraints.VERTICAL;
        gbc_dbPortLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dbPortLabel.gridx = 2;
        gbc_dbPortLabel.gridy = 0;
        databasePanel.add(dbPortLabel, gbc_dbPortLabel);

        dbPortField = new JFormattedTextField();
        dbPortField.setText(dbProperties.getProperty("rport"));
        GridBagConstraints gbc_dbPortField = new GridBagConstraints();
        gbc_dbPortField.fill = GridBagConstraints.BOTH;
        gbc_dbPortField.insets = new Insets(0, 0, 5, 0);
        gbc_dbPortField.gridx = 3;
        gbc_dbPortField.gridy = 0;
        databasePanel.add(dbPortField, gbc_dbPortField);

        JLabel dbDatabaseLabel = new JLabel("database:");
        GridBagConstraints gbc_dbDatabaseLabel = new GridBagConstraints();
        gbc_dbDatabaseLabel.anchor = GridBagConstraints.EAST;
        gbc_dbDatabaseLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dbDatabaseLabel.gridx = 0;
        gbc_dbDatabaseLabel.gridy = 1;
        databasePanel.add(dbDatabaseLabel, gbc_dbDatabaseLabel);

        dbDatabaseField = new JFormattedTextField();
        dbDatabaseField.setText(dbProperties.getProperty("schema"));
        GridBagConstraints gbc_dbDatabaseField = new GridBagConstraints();
        gbc_dbDatabaseField.fill = GridBagConstraints.BOTH;
        gbc_dbDatabaseField.insets = new Insets(0, 0, 5, 5);
        gbc_dbDatabaseField.gridx = 1;
        gbc_dbDatabaseField.gridy = 1;
        databasePanel.add(dbDatabaseField, gbc_dbDatabaseField);

        JLabel dbUsernameLabel = new JLabel("username:");
        GridBagConstraints gbc_dbUsernameLabel = new GridBagConstraints();
        gbc_dbUsernameLabel.anchor = GridBagConstraints.EAST;
        gbc_dbUsernameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dbUsernameLabel.gridx = 0;
        gbc_dbUsernameLabel.gridy = 2;
        databasePanel.add(dbUsernameLabel, gbc_dbUsernameLabel);

        dbUsernameField = new JFormattedTextField();
        dbUsernameField.setToolTipText("Username for the database connecton");
        dbUsernameField.setText("alexaegis");
        GridBagConstraints gbc_dbUsernameField = new GridBagConstraints();
        gbc_dbUsernameField.fill = GridBagConstraints.BOTH;
        gbc_dbUsernameField.insets = new Insets(0, 0, 5, 5);
        gbc_dbUsernameField.gridx = 1;
        gbc_dbUsernameField.gridy = 2;
        databasePanel.add(dbUsernameField, gbc_dbUsernameField);

        JLabel dbPasswordLabel = new JLabel("password:");
        GridBagConstraints gbc_dbPasswordLabel = new GridBagConstraints();
        gbc_dbPasswordLabel.anchor = GridBagConstraints.EAST;
        gbc_dbPasswordLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dbPasswordLabel.gridx = 0;
        gbc_dbPasswordLabel.gridy = 3;
        databasePanel.add(dbPasswordLabel, gbc_dbPasswordLabel);

        dbPasswordField = new JPasswordField();
        dbPasswordField.setToolTipText("Password for the database connecton");
        GridBagConstraints gbc_dbPasswordField = new GridBagConstraints();
        gbc_dbPasswordField.fill = GridBagConstraints.BOTH;
        gbc_dbPasswordField.insets = new Insets(0, 0, 5, 5);
        gbc_dbPasswordField.gridx = 1;
        gbc_dbPasswordField.gridy = 3;
        databasePanel.add(dbPasswordField, gbc_dbPasswordField);

        JCheckBox sshSwitch = new JCheckBox("Use SSH Portforwarding");
        GridBagConstraints gbc_sshSwitch = new GridBagConstraints();
        gbc_sshSwitch.fill = GridBagConstraints.BOTH;
        gbc_sshSwitch.insets = new Insets(0, 0, 0, 5);
        gbc_sshSwitch.gridx = 1;
        gbc_sshSwitch.gridy = 4;
        databasePanel.add(sshSwitch, gbc_sshSwitch);

        sshSwitch.addActionListener(e -> {
            sshEnabled = !sshEnabled;
            setSshElements(sshEnabled);
        });
    }

    private void setSshElements(boolean sshEnabled) {
        sshLocalHostField.setEnabled(sshEnabled);
        sshLocalPortField.setEnabled(sshEnabled);
        sshPortField.setEnabled(sshEnabled);
        sshPasswordField.setEnabled(sshEnabled);
        sshServerField.setEnabled(sshEnabled);
        sshUsernameField.setEnabled(sshEnabled);
    }

    private void addSSHPanel() {
        JPanel ssh = new JPanel();
        ssh.setBorder(new TitledBorder(null, "SSH Connection", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        connectionPane.add(ssh);
        GridBagLayout gbl_ssh = new GridBagLayout();
        gbl_ssh.columnWidths = new int[]{64, 180, 64, 40, 0};
        gbl_ssh.rowHeights = new int[]{20, 14, 20, 20, 20, 0};
        gbl_ssh.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_ssh.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        ssh.setLayout(gbl_ssh);

        JLabel sshServerLabel = new JLabel("ssh server:");
        GridBagConstraints gbc_sshServerLabel = new GridBagConstraints();
        gbc_sshServerLabel.anchor = GridBagConstraints.EAST;
        gbc_sshServerLabel.insets = new Insets(0, 0, 5, 5);
        gbc_sshServerLabel.gridx = 0;
        gbc_sshServerLabel.gridy = 0;
        ssh.add(sshServerLabel, gbc_sshServerLabel);

        sshServerField = new JFormattedTextField();
        sshServerField.setText("caesar.elte.hu");
        GridBagConstraints gbc_sshServerField = new GridBagConstraints();
        gbc_sshServerField.fill = GridBagConstraints.HORIZONTAL;
        gbc_sshServerField.insets = new Insets(0, 0, 5, 5);
        gbc_sshServerField.gridx = 1;
        gbc_sshServerField.gridy = 0;
        ssh.add(sshServerField, gbc_sshServerField);

        JLabel sshPortLabel = new JLabel("port:");
        GridBagConstraints gbc_sshPortLabel = new GridBagConstraints();
        gbc_sshPortLabel.anchor = GridBagConstraints.EAST;
        gbc_sshPortLabel.insets = new Insets(0, 0, 5, 5);
        gbc_sshPortLabel.gridx = 2;
        gbc_sshPortLabel.gridy = 0;
        ssh.add(sshPortLabel, gbc_sshPortLabel);

        sshPortField = new JFormattedTextField();
        sshPortField.setText("22");
        GridBagConstraints gbc_sshPortField = new GridBagConstraints();
        gbc_sshPortField.anchor = GridBagConstraints.NORTH;
        gbc_sshPortField.fill = GridBagConstraints.HORIZONTAL;
        gbc_sshPortField.insets = new Insets(0, 0, 5, 0);
        gbc_sshPortField.gridx = 3;
        gbc_sshPortField.gridy = 0;
        ssh.add(sshPortField, gbc_sshPortField);

        JLabel sshForwardLabel = new JLabel("Forward to:");
        GridBagConstraints gbc_sshForwardLabel = new GridBagConstraints();
        gbc_sshForwardLabel.anchor = GridBagConstraints.WEST;
        gbc_sshForwardLabel.insets = new Insets(0, 0, 5, 5);
        gbc_sshForwardLabel.gridx = 1;
        gbc_sshForwardLabel.gridy = 1;
        ssh.add(sshForwardLabel, gbc_sshForwardLabel);

        JLabel sshLocalHostLabel = new JLabel("local host:");
        GridBagConstraints gbc_sshLocalHostLable = new GridBagConstraints();
        gbc_sshLocalHostLable.anchor = GridBagConstraints.EAST;
        gbc_sshLocalHostLable.insets = new Insets(0, 0, 5, 5);
        gbc_sshLocalHostLable.gridx = 0;
        gbc_sshLocalHostLable.gridy = 2;
        ssh.add(sshLocalHostLabel, gbc_sshLocalHostLable);

        sshLocalHostField = new JFormattedTextField();
        sshLocalHostField.setText("127.0.0.1");
        GridBagConstraints gbc_sshLocalHostField = new GridBagConstraints();
        gbc_sshLocalHostField.fill = GridBagConstraints.HORIZONTAL;
        gbc_sshLocalHostField.insets = new Insets(0, 0, 5, 5);
        gbc_sshLocalHostField.gridx = 1;
        gbc_sshLocalHostField.gridy = 2;
        ssh.add(sshLocalHostField, gbc_sshLocalHostField);

        JLabel sshLocalPortLabel = new JLabel("local port:");
        GridBagConstraints gbc_sshLocalPortLabel = new GridBagConstraints();
        gbc_sshLocalPortLabel.anchor = GridBagConstraints.EAST;
        gbc_sshLocalPortLabel.insets = new Insets(0, 0, 5, 5);
        gbc_sshLocalPortLabel.gridx = 2;
        gbc_sshLocalPortLabel.gridy = 2;
        ssh.add(sshLocalPortLabel, gbc_sshLocalPortLabel);

        sshLocalPortField = new JFormattedTextField();
        sshLocalPortField.setText("22");
        GridBagConstraints gbc_sshLocalPortField = new GridBagConstraints();
        gbc_sshLocalPortField.anchor = GridBagConstraints.NORTH;
        gbc_sshLocalPortField.fill = GridBagConstraints.HORIZONTAL;
        gbc_sshLocalPortField.insets = new Insets(0, 0, 5, 0);
        gbc_sshLocalPortField.gridx = 3;
        gbc_sshLocalPortField.gridy = 2;
        ssh.add(sshLocalPortField, gbc_sshLocalPortField);

        JLabel sshUsernameLabel = new JLabel("username:");
        GridBagConstraints gbc_sshUsernameLabel = new GridBagConstraints();
        gbc_sshUsernameLabel.anchor = GridBagConstraints.EAST;
        gbc_sshUsernameLabel.insets = new Insets(0, 0, 5, 5);
        gbc_sshUsernameLabel.gridx = 0;
        gbc_sshUsernameLabel.gridy = 3;
        ssh.add(sshUsernameLabel, gbc_sshUsernameLabel);

        sshUsernameField = new JFormattedTextField();
        sshUsernameField.setToolTipText("Username for the database connecton");
        sshUsernameField.setText("alexaegis");
        GridBagConstraints gbc_sshUsernameField = new GridBagConstraints();
        gbc_sshUsernameField.fill = GridBagConstraints.HORIZONTAL;
        gbc_sshUsernameField.insets = new Insets(0, 0, 5, 5);
        gbc_sshUsernameField.gridx = 1;
        gbc_sshUsernameField.gridy = 3;
        ssh.add(sshUsernameField, gbc_sshUsernameField);

        JLabel sshPasswordLabel = new JLabel("password:");
        GridBagConstraints gbc_sshPasswordLabel = new GridBagConstraints();
        gbc_sshPasswordLabel.anchor = GridBagConstraints.EAST;
        gbc_sshPasswordLabel.insets = new Insets(0, 0, 0, 5);
        gbc_sshPasswordLabel.gridx = 0;
        gbc_sshPasswordLabel.gridy = 4;
        ssh.add(sshPasswordLabel, gbc_sshPasswordLabel);

        sshPasswordField = new JPasswordField();
        sshPasswordField.setToolTipText("Password for the database connecton");
        GridBagConstraints gbc_sshPasswordField = new GridBagConstraints();
        gbc_sshPasswordField.fill = GridBagConstraints.HORIZONTAL;
        gbc_sshPasswordField.insets = new Insets(0, 0, 0, 5);
        gbc_sshPasswordField.gridx = 1;
        gbc_sshPasswordField.gridy = 4;
        ssh.add(sshPasswordField, gbc_sshPasswordField);
    }

    private void addButtonPanel() {
        JPanel buttonPanel = new JPanel();
        connectionPane.add(buttonPanel);
        buttonPanel.setLayout(new BorderLayout(0, 0));

        JButton dbConnectButton = new JButton("Connect");
        buttonPanel.add(dbConnectButton, BorderLayout.EAST);
        dbConnectButton.setHorizontalAlignment(SwingConstants.RIGHT);
        dbConnectButton.addActionListener(e -> {
            if((!sshEnabled && (dbHostField.getText().isEmpty() ||
                        dbPortField.getText().isEmpty() ||
                        dbDatabaseField.getText().isEmpty() ||
                        dbUsernameField.getText().isEmpty() ||
                        dbPasswordField.getPassword().length == 0))
                    || (sshEnabled && ( sshLocalHostField.getText().isEmpty() ||
                        sshLocalPortField.getText().isEmpty() ||
                        sshServerField.getText().isEmpty() ||
                        sshPortField.getText().isEmpty() ||
                        sshUsernameField.getText().isEmpty() ||
                        sshPasswordField.getPassword().length == 0))) {
                JOptionPane.showMessageDialog(this, "Empty fields!");
            } else {
                dbProperties.setProperty("rhost", dbHostField.getText());
                dbProperties.setProperty("rport", dbPortField.getText());
                dbProperties.setProperty("lhost", sshLocalHostField.getText());
                dbProperties.setProperty("lport", sshLocalPortField.getText());
                dbProperties.setProperty("schema", dbDatabaseField.getText());
                dbProperties.setProperty("username", dbUsernameField.getText());
                dbProperties.setProperty("password", dbPasswordField.getPassword());
                sshProperties.setProperty("host", sshServerField.getText());
                sshProperties.setProperty("port", sshPortField.getText());
                sshProperties.setProperty("username", sshUsernameField.getText());
                sshProperties.setProperty("password", sshPasswordField.getPassword());
                sshProperties.setProperty("usessh", Boolean.toString(sshEnabled));
                connector.disconnect();
                new Thread(() -> connector.connect()).start();
                dispose();
            }
        });
    }
}