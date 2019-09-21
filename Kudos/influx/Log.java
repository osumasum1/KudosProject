package users.influx;

import java.time.Instant;


import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

@Measurement(name="logs")
public class Log {
	
	
	
   // @Column(name = "time")
	//private Instant time;
    @Column(name = "log")
	private String log;

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}
    
    
    

}
