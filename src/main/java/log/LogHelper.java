package log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogHelper {

	public final static SimpleDateFormat FORMAT_DATE_EN = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");

	public static int PrintLevel = 2;

	public enum LogLevel {
		DEBUG("Debug", 1), INFO("Info", 2), WARN("Warn", 3), ERROR("Error", 4);
		private final String msg;
		private final int level;

		private LogLevel(String msg, int level) {
			this.msg = msg;
			this.level = level;
		}
	}

	public static void log(String thread, LogLevel logLevel, String msg) {
		if (thread == null)
			thread = "thread-1";
		if (logLevel == null)
			logLevel = LogLevel.INFO;
		if (logLevel.level >= PrintLevel)
			System.out.printf("%s - [%s] - [%s] - %s\r\n", FORMAT_DATE_EN.format(new Date()), thread, logLevel.msg, msg);
	}

}
