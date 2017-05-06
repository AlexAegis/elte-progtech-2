package com.alexaegis.progtech.window;

import com.alexaegis.progtech.database.MovieHandler;
import com.alexaegis.progtech.misc.IllegalLeaseException;
import com.alexaegis.progtech.misc.IllegalMovieException;
import com.alexaegis.progtech.misc.IllegalPersonException;
import com.alexaegis.progtech.models.movies.Movie;
import com.alexaegis.progtech.models.people.PersonFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.util.Arrays;

import static com.alexaegis.progtech.Main.connector;
import static com.alexaegis.progtech.window.MainWindow.displayProperties;



public class NewLeaseWindow extends JDialog {
    private final JPanel contentPanel = new JPanel();

    public NewLeaseWindow(Movie movie) {
        setTitle(displayProperties.getProperty("newleaseprompt.title"));
        setSize(Integer.parseInt(displayProperties.getProperty("newleaseprompt.width")), Integer.parseInt(displayProperties.getProperty("newleaseprompt.height")));
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[]{65, 307, 0, 0};
        gbl_contentPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
        gbl_contentPanel.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        contentPanel.setLayout(gbl_contentPanel);

        JLabel titleLabel = new JLabel("You want to lend the movie " + movie.getTitle());
        GridBagConstraints gbc_titleLabel = new GridBagConstraints();
        gbc_titleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_titleLabel.anchor = GridBagConstraints.EAST;
        gbc_titleLabel.gridx = 0;
        gbc_titleLabel.gridy = 0;
        contentPanel.add(titleLabel, gbc_titleLabel);


        JLabel recipentLabel = new JLabel("Recients Name:");
        GridBagConstraints gbc_recipentLabel = new GridBagConstraints();
        gbc_recipentLabel.anchor = GridBagConstraints.EAST;
        gbc_recipentLabel.insets = new Insets(0, 0, 5, 5);
        gbc_recipentLabel.gridx = 0;
        gbc_recipentLabel.gridy = 1;
        contentPanel.add(recipentLabel, gbc_recipentLabel);

        JFormattedTextField recipentField = new JFormattedTextField();
        GridBagConstraints gbc_recipentField = new GridBagConstraints();
        gbc_recipentField.insets = new Insets(0, 0, 5, 5);
        gbc_recipentField.fill = GridBagConstraints.HORIZONTAL;
        gbc_recipentField.gridx = 1;
        gbc_recipentField.gridy = 1;
        contentPanel.add(recipentField, gbc_recipentField);

        JLabel dateLabel = new JLabel("Date:");
        GridBagConstraints gbc_dateLabel = new GridBagConstraints();
        gbc_dateLabel.anchor = GridBagConstraints.EAST;
        gbc_dateLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dateLabel.gridx = 0;
        gbc_dateLabel.gridy = 2;
        contentPanel.add(dateLabel, gbc_dateLabel);

        JFormattedTextField dateField = new JFormattedTextField();
        dateField.setValue(new java.sql.Date(new java.util.Date().getTime()));
        GridBagConstraints gbc_dateField = new GridBagConstraints();
        gbc_dateField.insets = new Insets(0, 0, 5, 5);
        gbc_dateField.fill = GridBagConstraints.HORIZONTAL;
        gbc_dateField.gridx = 1;
        gbc_dateField.gridy = 2;
        contentPanel.add(dateField, gbc_dateField);





        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");

        okButton.addActionListener(e -> {
            try {
                MovieHandler movieHandler = new MovieHandler(connector);
                String leaser = recipentField.getText();
                java.sql.Date date =  new java.sql.Date(((java.util.Date) dateField.getValue()).getTime());
                if(!movie.getLegality()) {
                    int dialogResult = JOptionPane.showConfirmDialog(this, "This movie is not original. Are you sure that you want to lend it to someone?");

                    if(dialogResult == JOptionPane.YES_OPTION) {
                        movieHandler.evaluateNewLease(movie, leaser, date);
                        dispose();
                    } else if(dialogResult == JOptionPane.NO_OPTION) {
                        dispose();
                    } else if(dialogResult == JOptionPane.CANCEL_OPTION) {

                    }
                } else {
                    movieHandler.evaluateNewLease(movie, leaser, date);
                    dispose();
                }
                connector.refresh();
            } catch (IllegalLeaseException e1) {
                e1.printStackTrace();
            }
        });


        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(e -> dispose());
        buttonPane.add(cancelButton);
        setVisible(true);
        revalidate();
        repaint();
    }
}