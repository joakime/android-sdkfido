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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigOption {
    /**
     * Does option have a parameter after it?
     */
    String scope() default "";

    /**
     * The human readable description of the option.
     */
    String description();

    /**
     * To allow overriding of the option parameter type to something more human friendly / readable.
     * <p>
     * Has no effect on logic, parsing, etc. Purely for display purposes
     */
    String type() default "";

    /**
     * To indicate that this flagged option is really a suboption for a deeper sub-class
     */
    boolean suboption() default false;
}
