package heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackdoorController {
	Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="/backdoor/addID", method = RequestMethod.GET)
    public String addID(@RequestParam("id") String id) {
        logger.info(id);
        return "add id " + id + " successfull!";
    }

}
