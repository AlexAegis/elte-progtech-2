package com.alexaegis.progtech.window;

import com.alexaegis.progtech.database.Cache;
import com.alexaegis.progtech.database.MovieInserter;
import com.alexaegis.progtech.database.PersonTypes;
import com.alexaegis.progtech.misc.IllegalMovieException;
import com.alexaegis.progtech.models.Movie;
import com.alexaegis.progtech.models.Person;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Date;
import java.util.Arrays;

import static com.alexaegis.progtech.Main.connector;
import static com.alexaegis.progtech.window.MainWindow.displayProperties;

public class NewMovieWindow extends JDialog {
    private final JPanel contentPanel = new JPanel();

    public NewMovieWindow() {
        setTitle(displayProperties.getProperty("newmovieprompt.title"));
        setSize(Integer.parseInt(displayProperties.getProperty("newmovieprompt.width")), Integer.parseInt(displayProperties.getProperty("newmovieprompt.height")));
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

        JLabel titleLabel = new JLabel("Title:");
        GridBagConstraints gbc_titleLabel = new GridBagConstraints();
        gbc_titleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_titleLabel.anchor = GridBagConstraints.EAST;
        gbc_titleLabel.gridx = 0;
        gbc_titleLabel.gridy = 0;
        contentPanel.add(titleLabel, gbc_titleLabel);

        JFormattedTextField titleField = new JFormattedTextField();
        GridBagConstraints gbc_titleField = new GridBagConstraints();
        gbc_titleField.insets = new Insets(0, 0, 5, 5);
        gbc_titleField.fill = GridBagConstraints.HORIZONTAL;
        gbc_titleField.gridx = 1;
        gbc_titleField.gridy = 0;
        contentPanel.add(titleField, gbc_titleField);

        JLabel releaseLabel = new JLabel("Release:");
        GridBagConstraints gbc_releaseLabel = new GridBagConstraints();
        gbc_releaseLabel.anchor = GridBagConstraints.EAST;
        gbc_releaseLabel.insets = new Insets(0, 0, 5, 5);
        gbc_releaseLabel.gridx = 0;
        gbc_releaseLabel.gridy = 1;
        contentPanel.add(releaseLabel, gbc_releaseLabel);

        JFormattedTextField releaseField = new JFormattedTextField();
        GridBagConstraints gbc_releaseField = new GridBagConstraints();
        gbc_releaseField.insets = new Insets(0, 0, 5, 5);
        gbc_releaseField.fill = GridBagConstraints.HORIZONTAL;
        gbc_releaseField.gridx = 1;
        gbc_releaseField.gridy = 1;
        contentPanel.add(releaseField, gbc_releaseField);

        JLabel directorsLabel = new JLabel("Directors:");
        GridBagConstraints gbc_directorsLabel = new GridBagConstraints();
        gbc_directorsLabel.anchor = GridBagConstraints.EAST;
        gbc_directorsLabel.insets = new Insets(0, 0, 5, 5);
        gbc_directorsLabel.gridx = 0;
        gbc_directorsLabel.gridy = 2;
        contentPanel.add(directorsLabel, gbc_directorsLabel);

        JFormattedTextField directorsField = new JFormattedTextField();
        directorsField.setToolTipText("Use semicolons to separate multiple actors");
        GridBagConstraints gbc_directorsField = new GridBagConstraints();
        gbc_directorsField.insets = new Insets(0, 0, 5, 5);
        gbc_directorsField.fill = GridBagConstraints.HORIZONTAL;
        gbc_directorsField.gridx = 1;
        gbc_directorsField.gridy = 2;
        contentPanel.add(directorsField, gbc_directorsField);

        JLabel actorsLabel = new JLabel("Actors:");
        GridBagConstraints gbc_actorsLabel = new GridBagConstraints();
        gbc_actorsLabel.anchor = GridBagConstraints.EAST;
        gbc_actorsLabel.insets = new Insets(0, 0, 0, 5);
        gbc_actorsLabel.gridx = 0;
        gbc_actorsLabel.gridy = 3;
        contentPanel.add(actorsLabel, gbc_actorsLabel);

        JFormattedTextField actorsField = new JFormattedTextField();
        actorsField.setToolTipText("Use semicolons to separate multiple actors");
        GridBagConstraints gbc_actorsField = new GridBagConstraints();
        gbc_actorsField.insets = new Insets(0, 0, 0, 5);
        gbc_actorsField.fill = GridBagConstraints.HORIZONTAL;
        gbc_actorsField.gridx = 1;
        gbc_actorsField.gridy = 3;
        contentPanel.add(actorsField, gbc_actorsField);

        directorsField.setText("Christopher Nolan"); //TODO TAKE ME OUT
        actorsField.setText("Christian Bale; Michael Caine; Gary Oldman"); //TODO TAKE ME OUT
        titleField.setText("The Dark Knight Rises"); //TODO TAKE ME OUT
        releaseField.setText("2012-06-20"); //TODO TAKE ME OUT

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(e -> {
            try {
                MovieInserter movieInserter = new MovieInserter(connector);
                Movie movie = new Movie(0, titleField.getText().trim(), Date.valueOf(releaseField.getText()));
                java.util.List<String> directors = Arrays.asList(directorsField.getText().split(";"));
                java.util.List<String> actors = Arrays.asList(actorsField.getText().split(";"));
                directors.stream()
                        .map(String::trim)
                        .filter(s -> s.split(" ").length == 2)
                        .map(s -> new Person(0, s, null, PersonTypes.DIRECTOR))
                        .forEach(movie::addDirector);
                actors.stream()
                        .map(String::trim)
                        .filter(s -> s.split(" ").length == 2)
                        .map(s -> new Person(0, s, null, PersonTypes.ACTOR))
                        .forEach(movie::addActor);
                System.out.println(movie);
                movieInserter.evaluateNewMovie(movie);
                connector.refresh();
                dispose();
            } catch (IllegalArgumentException e1) {
                System.out.println("Wrong Date Format, use 'YYYY-MM-DD'"); // TODO make me a logger
                e1.printStackTrace();
            } catch (IllegalMovieException e1) {
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