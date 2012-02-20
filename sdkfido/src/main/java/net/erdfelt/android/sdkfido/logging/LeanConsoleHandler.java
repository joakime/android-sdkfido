/*******************************************************************************
 *    Copyright 2012 - Joakim Erdfelt
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
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
        buf.append("[").append(record.getLevel().getName()).append("] ");
        String logname = record.getLoggerName();
        int idx = logname.lastIndexOf('.');
        if (idx > 0) {
            logname = logname.substring(idx + 1);
        }
        buf.append(logname);
        buf.append(": ");
        buf.append(record.getMessage());

        System.out.println(buf.toString());
        if (record.getThrown() != null) {
            record.getThrown().printStackTrace(System.out);
        }
    }
}
