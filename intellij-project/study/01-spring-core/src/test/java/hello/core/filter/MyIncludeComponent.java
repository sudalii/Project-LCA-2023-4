package hello.core.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)   // TYPE: class 레벨레 붙음
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {
}
