package heartbeat;

import java.util.Date;

public class HeartBeatIte<T> {
	
    public static final long warningTime = 1;
    public static final long errorTime = 2;

    private int status;
    private String message;
    private long timestamp;
    public T services;

    public HeartBeatIte (T services) {
        this.services = services;
        status = CodeEnum.success.getCode();
        message = "";
        timestamp = 0;
    }
    
    public HeartBeatIte(T services, int status, String message, long timestamp) {
		this.services = services;
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}

    public void checkStatus() {
    	if(this.services instanceof Iterable){
            // update heartBeat status
        	for(HeartBeatIte<String> service : ((Iterable<HeartBeatIte<String>>) this.services)){
        		service.checkStatus();
            	if(service.getStatus()>this.status)
                	this.status = service.getStatus();
            }
            // update message
            if (this.status == CodeEnum.success.getCode()) {
            	this.message = "OK";
            } else if (this.status == CodeEnum.warning.getCode()) {
            	this.message = "WARN: program has risk";
            } else if (this.status == CodeEnum.error.getCode()) {
            	this.message = "ERROR: program is down";
            }
    		this.timestamp = new Date().getTime();
    	}
    	else if(this.services instanceof String){
    		long diff = new Date().getTime()/1000 - timestamp;
    		if (diff<warningTime) {
    			this.status = CodeEnum.success.getCode();
    			this.message = "OK";
    		} else if (diff<errorTime && diff>=warningTime) {
    			this.status = CodeEnum.warning.getCode();
    			this.message = "Warning: no update in " + warningTime + "s";
    		} else if (diff>=errorTime) {
    			this.status = CodeEnum.error.getCode();
    			this.message = "Error: no update in " + errorTime + "s";
    		}
    		this.timestamp = new Date().getTime();
    	}
    	else{
    		this.status = CodeEnum.RunTimeError.getCode();
    		this.timestamp = new Date().getTime();
    		this.message = "parse error";
    	}
    }

    public T getServices() {
        return this.services;
    }

    public void setServices(T services) {
        this.services = services;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
