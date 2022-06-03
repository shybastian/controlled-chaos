package edu.seb.chaos.now.assault.components.annotation;

import java.lang.annotation.*;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Targetable {
}
