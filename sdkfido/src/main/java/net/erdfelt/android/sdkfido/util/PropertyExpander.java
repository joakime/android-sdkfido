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

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

public class PropertyExpander {
    private Map<String, String> props;
    Pattern                     pat = Pattern.compile("(@([A-Z][A-Z0-9_]*)@)");

    public PropertyExpander(Map<String, String> props) {
        this.props = props;
    }

    public String expand(String str) {
        if (StringUtils.isEmpty(str)) {
            // Empty string. Fail fast.
            return str;
        }

        if (str.indexOf("@") < 0) {
            // Contains no potential expressions. Fail fast.
            return str;
        }

        Matcher mat = pat.matcher(str);
        int offset = 0;
        String expression;
        String value;
        StringBuffer expanded = new StringBuffer();

        while (mat.find(offset)) {
            expression = mat.group(1);

            expanded.append(str.substring(offset, mat.start(1)));
            value = props.get(mat.group(2));
            if (value != null) {
                expanded.append(value);
            } else {
                expanded.append(expression);
            }
            offset = mat.end(1);
        }

        expanded.append(str.substring(offset));

        if (expanded.indexOf("@@") >= 0) {
            // Special case for escaped content.
            return expanded.toString().replaceAll("\\@\\@", "\\@");
        } else {
            // return expanded
            return expanded.toString();
        }
    }
}
