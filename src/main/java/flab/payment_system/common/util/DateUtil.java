package flab.payment_system.common.util;

import java.time.OffsetDateTime;

public class DateUtil {
	public static String getYesterdayDate() {
		OffsetDateTime yesterday = OffsetDateTime.now().minusDays(1);
		return yesterday.toLocalDate().toString();
	}
}
