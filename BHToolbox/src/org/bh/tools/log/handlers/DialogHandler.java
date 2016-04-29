package org.bh.tools.log.handlers;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import java.awt.Component;

import javax.swing.JOptionPane;



/**
 * DialogHandler, made for BHToolbox 2, is copyright Blue Husky Programming ©2015 BH-1-PS <hr>
 *
 * @author Kyli of Blue Husky Programming
 * @version 1.0.0
 * - 2015-08-29 (1.0.0) - Kyli created DialogHandler
 * @since 2015-08-29
 */
public class DialogHandler extends Handler {

    private Component parent;

    public DialogHandler(final Level lowestLevel) {
        setLevel(lowestLevel);
        setFilter((record) -> record.getLevel().intValue() >= getLevel().intValue());
    }

    @Override
    public void publish(LogRecord record) {
        if (!getFilter().isLoggable(record)) {
            return;
        }

        Level level = record.getLevel();
        int messageType, levelInt = level.intValue();

        if (levelInt >= Level.SEVERE.intValue()) {
            messageType = JOptionPane.ERROR_MESSAGE;
        } else if (levelInt >= Level.WARNING.intValue()) {
            messageType = JOptionPane.WARNING_MESSAGE;
        } else if (levelInt >= Level.INFO.intValue()) {
            messageType = JOptionPane.INFORMATION_MESSAGE;
        } else {
            messageType = JOptionPane.PLAIN_MESSAGE;
        }

        JOptionPane.showMessageDialog(parent, record.getMessage(), level.getLocalizedName(), messageType);
    }

    @Override public void flush() {
    }

    @Override public void close() throws SecurityException {
    }

}
