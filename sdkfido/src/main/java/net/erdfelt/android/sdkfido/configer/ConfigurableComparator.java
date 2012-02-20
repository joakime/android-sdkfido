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

import java.text.CollationKey;
import java.text.Collator;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

public class ConfigurableComparator implements Comparator<Configurable> {
    private Collator collator = Collator.getInstance();

    public int compare(Configurable c1, Configurable c2) {
        CollationKey key1 = asKey(c1);
        CollationKey key2 = asKey(c2);
        return key1.compareTo(key2);
    }

    private CollationKey asKey(Configurable c) {
        if (c == null) {
            return collator.getCollationKey("");
        }
        String key = c.getKey();
        if (StringUtils.isBlank(key)) {
            return collator.getCollationKey("");
        }
        return collator.getCollationKey(key.trim());
    }
}
