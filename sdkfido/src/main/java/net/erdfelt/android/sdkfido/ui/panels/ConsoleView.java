package net.erdfelt.android.sdkfido.ui.panels;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ConsoleView extends JPanel {
    private static final long serialVersionUID = -3641776139094178541L;
    private JScrollPane       scroller;
    private JTextArea         text;

    public ConsoleView() {
        super(true);

        text = new JTextArea();
        text.setText("");
        text.setFont(new Font("sansserif", 0, 10));

        scroller = new JScrollPane();
        scroller.setViewportView(text);
        scroller.setBorder(BorderFactory.createTitledBorder(null, "Console", 0, 0));
        scroller.setToolTipText("Console");
        scroller.setName("ConsoleView");

        this.setLayout(new BorderLayout());
        this.add(scroller, BorderLayout.CENTER);
    }

    public void attachLogger() {
        Logger root = Logger.getLogger("");

        boolean found = false;
        Handler handlers[] = root.getHandlers();
        for (Handler handler : handlers) {
            if (handler instanceof TextAreaLogHandler) {
                found = true;
                break;
            }
        }

        if (!found) {
            root.addHandler(new TextAreaLogHandler(text));
        }
    }

    class TextAreaLogHandler extends Handler {
        public JTextArea txt;

        public TextAreaLogHandler(JTextArea textarea) {
            this.txt = textarea;
        }

        @Override
        public void publish(LogRecord record) {
            StringBuilder buf = new StringBuilder();
            if(record.getLevel() != Level.INFO) {
                buf.append("[").append(record.getLevel().getName());
                buf.append("] ");
            }
            String name = record.getLoggerName();
            int idx = name.lastIndexOf('.');
            if(idx>0) {
                name= name.substring(idx+1);
            }
            buf.append(name);
            buf.append(": ").append(record.getMessage());
            buf.append("\n");

            txt.append(buf.toString());
            // FIXME: limit size of log window!
            if (record.getThrown() != null) {
                // TODO: show stacktrace in log window!
                record.getThrown().printStackTrace(System.out);
            }
        }

        @Override
        public void flush() {
            /* ignore */
        }

        @Override
        public void close() throws SecurityException {
            /* ignore */
        }

    }
}
