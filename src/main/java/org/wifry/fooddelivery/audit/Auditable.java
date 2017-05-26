package org.wifry.fooddelivery.audit;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to mark JPA entities that need to be audited by the
 * audit trail system.
 *
 * @author Fredy Tuco
 * @since: 4/4/14
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

}
