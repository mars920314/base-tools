package heartbeat;

import java.util.ArrayList;

public class HeartBeat<T> {
    private int status;
    private String message;
    private long timestamp;
    public T services;

    public HeartBeat (T services) {
        this.services = services;
        status = -1;
        message = "";
        timestamp = 0;
    }

    public void checkStatus() {
    	ArrayList<HeartBeat<String>> services;
    	try{
        	services = (ArrayList<HeartBeat<String>>) this.services;
    	} catch(Exception e){
    		e.printStackTrace();
    		return;
    	}
        // update heartBeat status
        for (int i = 0; i < services.size(); i++) {
        	HeartBeat<String> service = services.get(i);
            status = Math.max(status, service.getStatus());
        }
        // update message
        if (status == 1) {
            message = "OK";
        } else if (status == 2) {
            message = "WARN: program has risk";
        } else if (status == 3) {
            message = "ERROR: program is down";
        }
    }

    public T getServices() {
        return services;
    }

    public void setServices(T services) {
        this.services = services;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
