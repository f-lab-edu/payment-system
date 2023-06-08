package flab.payment_system.core.interceptor;

import flab.payment_system.core.utils.IpUtil;
import flab.payment_system.domain.mail.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class IpAddressRangeCheckControllerInterceptor implements HandlerInterceptor {

	private final MailService mailService;

	@Override
	public boolean preHandle(@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull Object handler) {

		String userIpAddress = IpUtil.getClientIp(request);

		if (!IpUtil.isValidIp(userIpAddress)) {
			// TODO 인터셉터에서 body 접근을 위해 필터에서 body 래핑 및 캐싱
			//mailService.sendMail(request.getParameter("UserDto"));
		}

		return true;
	}


}
