package tn.vortrix5.wizard.command;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();

    String description() default "Same description";

    ExecutorType type() default ExecutorType.ALL;

    enum ExecutorType {
        ALL, User, CONSOLE
    }
}
