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
package net.erdfelt.android.sdkfido.configer;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.configuration.ConversionException;

public class EnumConverter implements Converter {
    private static final Logger LOG = Logger.getLogger(EnumConverter.class.getName());
    private Class<?>            enumclass;
    private Map<String, Object> values;

    public EnumConverter(Class<?> type) {
        this.enumclass = type;
        this.values = new HashMap<String, Object>();
        
        for(Field f: enumclass.getFields()) {
            if(f.isEnumConstant()) {
                String key = f.getName().toUpperCase();
                try {
                    Object val = f.get(null);
                    values.put(key, val);
                } catch (IllegalArgumentException e) {
                    LOG.log(Level.WARNING, "Unable to get value for field " + type.getName() + "#" + f.getName(), e);
                } catch (IllegalAccessException e) {
                    LOG.log(Level.WARNING, "Unable to get value for field " + type.getName() + "#" + f.getName(), e);
                }
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public Object convert(Class type, Object value) {
        if (type.isEnum()) {
            String key = String.valueOf(value).toUpperCase();
            return values.get(key);
        } else {
            throw new ConversionException("Not an Enum class");
        }
    }
}
