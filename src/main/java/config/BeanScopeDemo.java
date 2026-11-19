
package config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class BeanScopeDemo {

    public BeanScopeDemo() {
        System.out.println("Singleton Bean Created");
    }
}
