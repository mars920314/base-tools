package heartbeat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartBeatService {
	Logger logger = LoggerFactory.getLogger(getClass());

	public static Map<String, HeartBeat<String>> serviceMap = new HashMap<String, HeartBeat<String>>();
    static int warningTime = 1;
    static int errorTime = 2;
	
	public static void setTime(String serviceName, long timestamp) {
		HeartBeat<String> heartbeat = null;
		if (serviceMap.get(serviceName)==null){
			heartbeat = new HeartBeat<String>(serviceName);
			heartbeat.setTimestamp(timestamp);
		}
		else{
			heartbeat = serviceMap.get(serviceName);
			heartbeat.setTimestamp(timestamp);
		}
		long diff = new Date().getTime()/1000 - timestamp;
		if (diff<warningTime) {
			heartbeat.setStatus(1);
			heartbeat.setMessage("OK");
		} else if (diff<errorTime && diff>=warningTime) {
			heartbeat.setStatus(2);
			heartbeat.setMessage("Warning: no update in 1 hour");
		} else if (diff>=errorTime) {
			heartbeat.setStatus(3);
			heartbeat.setMessage("Error: no update in 2 hours");
		}
		serviceMap.put(serviceName, heartbeat);
	}
	
	public static HeartBeat<ArrayList<HeartBeat<String>>> loadHeatBeat() {
		ArrayList<HeartBeat<String>> heartBeaservices = new ArrayList<HeartBeat<String>>(serviceMap.values());
		HeartBeat<ArrayList<HeartBeat<String>>> heartBeatModel = new HeartBeat<ArrayList<HeartBeat<String>>>(heartBeaservices);
		heartBeatModel.checkStatus();
		return heartBeatModel;
	}
	
}
