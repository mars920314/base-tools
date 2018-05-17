package file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import thread.BaseCache;

public class MyFilePropertiesController extends BaseCache {

	private final static long recycle = 60000L;
	private Map<String, Properties> dbPropertyMap = new HashMap<String, Properties>();

	private MyFilePropertiesController(long updateInterval, long offset) {
		super(updateInterval, offset);
	}

    private static class SingletonHolder {
    	private static final MyFilePropertiesController INSTANCE = new MyFilePropertiesController(recycle, 0);
    }
    
    public static final MyFilePropertiesController getInstance() {
    	return SingletonHolder.INSTANCE;
    }
	
	public void writeProperty(String propertyFile, String propertyName, String propertyVal) {
		Properties dbProperty = this.dbPropertyMap.get(propertyFile);
		if(dbProperty==null){
			dbProperty = getProperty(propertyFile);
			this.dbPropertyMap.put(propertyFile, dbProperty);
		}
		dbProperty.setProperty(propertyName, propertyVal);
	}
	
	public String readProperty(String propertyFile, String propertyName) {
		Properties dbProperty = this.dbPropertyMap.get(propertyFile);
		if(dbProperty==null){
			dbProperty = getProperty(propertyFile);
			this.dbPropertyMap.put(propertyFile, dbProperty);
		}
		return dbProperty.getProperty(propertyName);
	}
	
	private Properties getProperty(String propertyFile) {
		Properties dbProperty = new Properties();
		try {
			FileInputStream fi = new FileInputStream(propertyFile);
			InputStreamReader isr = new InputStreamReader(fi, "UTF-8");
			dbProperty.load(isr);
			isr.close();
			fi.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dbProperty;
	}

	@Override
	public void quickInit() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updCache() {
		for(Map.Entry<String, Properties> entry : dbPropertyMap.entrySet()){
			try {
				FileOutputStream fo = new FileOutputStream(entry.getKey());
				OutputStreamWriter ofw = new OutputStreamWriter(fo, "UTF-8");
				entry.getValue().store(ofw, "auto-gen properties");
				ofw.close();
				fo.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public Class getRealClass() {
		// TODO Auto-generated method stub
		return MyFilePropertiesController.class;
	}

}
