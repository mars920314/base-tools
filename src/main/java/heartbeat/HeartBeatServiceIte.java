package heartbeat;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeartBeatServiceIte {
	Logger logger = LoggerFactory.getLogger(getClass());

	public static Map<String, HeartBeatIte<?>> serviceMap = new HashMap<String, HeartBeatIte<?>>();
	
	public static void updateServiceTime(String serviceName, long timestamp) {
		serviceMap.put(serviceName, new HeartBeatIte<String>(serviceName, CodeEnum.success.getCode(), "", timestamp));
	}
	
	public static void updateServiceTime(HeartBeatIte<?> heartbeat) {
		serviceMap.put(heartbeat.getServices().toString(), heartbeat);
	}
	
	public static HeartBeatIte<Collection<HeartBeatIte<?>>> loadHeartBeat() {
		HeartBeatIte<Collection<HeartBeatIte<?>>> heartBeatModel = new HeartBeatIte<Collection<HeartBeatIte<?>>>(serviceMap.values());
		heartBeatModel.checkStatus();
		return heartBeatModel;
	}
	
}
