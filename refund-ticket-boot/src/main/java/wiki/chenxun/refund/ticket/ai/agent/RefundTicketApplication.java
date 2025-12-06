package wiki.chenxun.refund.ticket.ai.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 退票AI代理系统启动类
 * 
 * @author refund-ticket
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"wiki.chenxun.refund.ticket.ai.agent"})
public class RefundTicketApplication {

    public static void main(String[] args) throws UnknownHostException {
        SpringApplication app = new SpringApplication(RefundTicketApplication.class);
        Environment env = app.run(args).getEnvironment();
        
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        
        log.info("""
                
                ----------------------------------------------------------
                Application '{}' is running! Access URLs:
                Local:      {}://localhost:{}
                External:   {}://{}:{}
                Profile(s): {}
                ----------------------------------------------------------
                """,
                env.getProperty("spring.application.name"),
                protocol,
                env.getProperty("server.port"),
                protocol,
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getActiveProfiles().length == 0 ? 
                    env.getDefaultProfiles() : env.getActiveProfiles()
        );
    }
}
