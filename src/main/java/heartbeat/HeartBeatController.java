package heartbeat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HeartBeatController {
	Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/first")
    public String index() {
        return "This is my first spring boot!";
    }

    @RequestMapping(value="/heartbeat", method = RequestMethod.GET)
    public Object getHeartBeat() {
        return HeartBeatService.loadHeartBeat();
    }

}
