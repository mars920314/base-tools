package heartbeat;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.datayes.framework.WebApplicationBase;

@Configuration
@ComponentScan(basePackages={"com.datayes.service.springboot"})
@EnableAutoConfiguration
//@SpringBootApplication
public class WebApplicationHeartbeat {

    public static void main(String[] args) throws Exception {
        WebApplicationBase.run(WebApplicationHeartbeat.class, args);
    }

}
