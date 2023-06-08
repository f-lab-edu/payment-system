package flab.payment_system.core.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

public class IpUtil {

	private IpUtil() {
	}

	private static final String[] IP_HEADER_LIST = {
		"Proxy-Client-IP",
		"WL-Proxy-Client-IP",
		"HTTP_X_FORWARDED_FOR",
		"HTTP_X_FORWARDED",
		"HTTP_X_CLUSTER_CLIENT_IP",
		"HTTP_CLIENT_IP",
		"HTTP_FORWARDED_FOR",
		"HTTP_FORWARDED",
		"HTTP_VIA",
		"X-Real-IP",
		"X-RealIP",
		"REMOTE_ADDR"
	};

	public static String getClientIp(HttpServletRequest request) {
		String ip = getIpReflectingHeaderXFF(request);

		for (String ipHeader : List.of(IP_HEADER_LIST)) {
			ip = request.getHeader(ipHeader);
			if (validIp(ip)) {
				return ip;
			}
		}

		return ip != null ? ip : request.getRemoteAddr();
	}

	private static boolean validIp(String ip) {
		return !(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip));
	}

	@Nullable
	private static String getIpReflectingHeaderXFF(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		if (isMultipleIpWhenHeaderXFF(ip)) {
			ip = getClientIpIfMultipleIpWhenHeaderXFF(ip);
		}

		return ip;
	}

	private static boolean isMultipleIpWhenHeaderXFF(String ip) {
		return ip.contains(",");
	}

	private static String getClientIpIfMultipleIpWhenHeaderXFF(String ipList) {
		return ipList.split(",")[0];
	}

	public static boolean isValidIp(String ipAddress) {
		// TODO 한국 ip 대역 리스트 넣기
		List<IpAddressMatcher> ipAddressMatcherList = Arrays.asList(
			new IpAddressMatcher("192.168.0.0/24"), // temp
			new IpAddressMatcher("192.168.0.0/24") // temp
		);

		return ipAddressMatcherList.stream().anyMatch(matcher -> matcher.matches(ipAddress));
	}

}
