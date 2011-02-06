package net.erdfelt.android.sdkfido.logging;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class LeanConsoleHandler extends Handler {
    public static Handler createWithLevel(Level level) {
        LeanConsoleHandler handler = new LeanConsoleHandler();
        handler.setLevel(level);
        return handler;
    }

    @Override
    public void close() throws SecurityException {
        /* nothing to do here */
    }

    @Override
    public void flush() {
        /* nothing to do here */
    }

    @Override
    public void publish(LogRecord record) {
        StringBuilder buf = new StringBuilder();
        buf.append("[").append(record.getLevel().getName());
        buf.append("] ").append(record.getLoggerName());
        buf.append(" (").append(record.getSourceMethodName());
        buf.append("): ").append(record.getMessage());

        System.out.println(buf.toString());
        if (record.getThrown() != null) {
            record.getThrown().printStackTrace(System.out);
        }
    }
}
