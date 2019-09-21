package users.influx;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

public class InfluxDataBase {
	String dataBaseHost;
	String dataBaseName;
	
    public InfluxDataBase(String dataBaseHost, String dataBaseName) {
    	this.dataBaseHost = dataBaseHost;
    	this.dataBaseName = dataBaseName;
    }

    
    public void insert(String log) {
    	
    	Log logPojo = new Log();
    	logPojo.setLog(log);
    	
    	
    	//this.log = log;
    	InfluxDB influxDB = InfluxDBFactory.connect(this.dataBaseHost);//("http://localhost:8086");//, "root", "root");
    	Point point = Point.measurementByPOJO(logPojo.getClass()).addFieldsFromPOJO(logPojo).build();
    	influxDB.write(this.dataBaseName, null, point);
    	influxDB.close();
    }

}
