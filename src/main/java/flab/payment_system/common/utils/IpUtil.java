package flab.payment_system.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

public class IpUtil {

	private IpUtil() {
	}

	public static String getClientIp(HttpServletRequest request) {
		Optional<String> optionalIp = getIpReflectingHeaderXFF(request);
		String ip = optionalIp.orElse(request.getRemoteAddr());
		if(validIp(ip)) return ip;
		//else throw new UserIpInvalidException();
		return ip; //temp line
	}

	private static boolean validIp(String ip) {
		return !(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip));
	}

	private static Optional<String> getIpReflectingHeaderXFF(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		if (isMultipleIpWhenHeaderXFF(ip)) {
			ip = getClientIpIfMultipleIpWhenHeaderXFF(ip);
		}

		return Optional.ofNullable(ip);
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
