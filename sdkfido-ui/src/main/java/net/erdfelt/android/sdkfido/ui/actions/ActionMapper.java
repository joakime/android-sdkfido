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
package net.erdfelt.android.sdkfido.ui.actions;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;

/**
 * ActionMapper
 */
public class ActionMapper extends AbstractAction {
    private static final long   serialVersionUID = -2169218195440300644L;

    private static final Logger LOG              = Logger.getLogger(ActionMapper.class.getName());
    private Map<String, Method> commandMap;
    private Object              commandObject;

    /**
     * Create an Application Commands processor.
     */
    public ActionMapper(Object commandObj) {
        LOG.fine("Finding action methods for " + commandObj.getClass().getName());
        commandMap = new HashMap<String, Method>();
        commandObject = commandObj;

        Method methods[] = commandObject.getClass().getMethods();
        for (Method method : methods) {
            String name = method.getName();

            ActionTarget target = method.getAnnotation(ActionTarget.class);
            if (target == null) {
                LOG.finest("Method " + name + " not an @ActionTarget");
                continue; // skip
            }

            int modifiers = method.getModifiers();
            Object params[] = method.getParameterTypes();

            if (!Modifier.isPublic(modifiers) || Modifier.isAbstract(modifiers) || Modifier.isNative(modifiers)
                    || Modifier.isStatic(modifiers)) {
                LOG.warning("Method " + name + " is has wrong modifiers.");
                continue;
            }

            if ((params == null) || (params.length < 1) || (params.length > 1)) {
                LOG.warning("Method " + name + " has wrong number of parameters.");
                continue;
            }

            Object param = params[0];

            if (param instanceof ActionEvent) {
                commandMap.put(target.name(), method);
            } else if (param instanceof Class) {
                Class<?> pclass = (Class<?>) param;
                if (pclass.isAssignableFrom(ActionEvent.class)) {
                    commandMap.put(target.name(), method);
                } else {
                    LOG.warning("Method " + name + " has wrong parameter type class.  Expected "
                            + ActionEvent.class.getName() + " but found " + pclass.getName() + " instead.");
                }
            } else {
                LOG.warning("Method " + name + " has wrong parameter type.  Expected " + ActionEvent.class.getName()
                        + " but found " + param.getClass().getName() + " instead.");
            }
        }

        LOG.finest("Done with ActionMapper()");
    }

    public void actionPerformed(ActionEvent evt) {
        String actionCommand = evt.getActionCommand();

        if (evt.getSource() instanceof JButton) {
            JButton btn = (JButton) evt.getSource();
            actionCommand = btn.getActionCommand();
        }

        if (actionCommand == null) {
            LOG.warning("Encountered null action command. " + evt);
            return;
        }

        Method method = commandMap.get(actionCommand.toLowerCase());
        if (method == null) {
            LOG.warning("Action command " + actionCommand + " does not have a corresponding command method.");
            return;
        }

        try {
            method.invoke(commandObject, new Object[] { evt });
        } catch (IllegalArgumentException e) {
            LOG.log(Level.WARNING, "Unable to call method " + method + ".", e);
        } catch (IllegalAccessException e) {
            LOG.log(Level.WARNING, "Unable to call method " + method + ".", e);
        } catch (InvocationTargetException e) {
            LOG.log(Level.WARNING, "Unable to call method " + method + ".", e);
        }
    }
}
