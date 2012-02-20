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
package net.erdfelt.android.sdkfido.util;

import java.text.CollationKey;
import java.text.Collator;

import org.apache.commons.lang.StringUtils;

public final class CompareUtil {
    private static Collator collator = Collator.getInstance();

    public static int compare(String str1, String str2) {
        CollationKey key1 = asKey(str1);
        CollationKey key2 = asKey(str2);
        return key1.compareTo(key2);
    }

    private static CollationKey asKey(String str) {
        return collator.getCollationKey(StringUtils.defaultString(str));
    }
}
