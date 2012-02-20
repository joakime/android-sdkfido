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

import org.apache.commons.lang.StringUtils;

public class Configurable {
    private String  key;
    private String  name;
    private Field   field;
    private String  type;
    private String  description;
    private String  scope;

    public Configurable(Field field, ConfigOption option, String key, String scope) {
        this.field = field;
        this.field.setAccessible(true);
        this.name = field.getName();
        if (StringUtils.isNotBlank(scope)) {
            this.scope = scope;
        } else {
            this.scope = option.scope();
            if (StringUtils.isBlank(this.scope)) {
                this.scope = "options";
            }
        }
        this.key = key;

        if (StringUtils.isNotBlank(option.type())) {
            this.type = option.type();
        } else if ((this.field != null) && (this.field.getType() != null)) {
            this.type = this.field.getType().getSimpleName();
        }

        this.description = option.description();
    }

    public Configurable(String key, String type, String description) {
        this.key = key;
        this.type = type;
        this.description = description;
        this.scope = "misc";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Configurable other = (Configurable) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    public String getScope() {
        return scope;
    }

    public String getDescription() {
        return description;
    }

    public Field getField() {
        return field;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    public boolean hasType() {
        return (StringUtils.isNotBlank(this.type));
    }

    public boolean isInternal() {
        return (field == null);
    }
}
