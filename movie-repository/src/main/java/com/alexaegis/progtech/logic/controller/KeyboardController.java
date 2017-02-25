package com.alexaegis.progtech.logic.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Logger;

public class KeyboardController implements KeyListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private boolean enabled = false;

    public KeyboardController() {
        enable();
    }


    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(enabled) {
            logger.info(e.getKeyCode() + " pressed");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}