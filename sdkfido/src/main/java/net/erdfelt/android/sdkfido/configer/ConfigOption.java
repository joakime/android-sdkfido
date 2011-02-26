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
