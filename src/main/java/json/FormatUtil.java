package json;

import java.math.BigDecimal;

public class FormatUtil {

	public static Double scale(double d, int size) {
		BigDecimal b = new BigDecimal(d);
		d = b.setScale(size, BigDecimal.ROUND_HALF_UP).doubleValue();
		return d;
	}

}
