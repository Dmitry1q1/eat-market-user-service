package userservice;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EatMarketUserService {

    static Logger logger = Logger.getLogger(EatMarketUserService.class.getName());

    public static void main(String[] args) {
        logger.info("Start app");
        logger.debug("Start app");
        logger.error("Start app");
        SpringApplication.run(EatMarketUserService.class, args);
    }
}
