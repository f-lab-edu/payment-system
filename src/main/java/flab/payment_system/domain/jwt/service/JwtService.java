package flab.payment_system.domain.jwt.service;

import flab.payment_system.core.utils.CookieUtil;
import flab.payment_system.core.utils.Sha256Util;
import flab.payment_system.domain.jwt.enums.Token;
import flab.payment_system.domain.user.exception.JwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import jakarta.servlet.http.Cookie;
import java.security.Key;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;

@Service
@RequiredArgsConstructor
public class JwtService {

	@Value("${secret-key}")
	private String secretKey;


	public String createToken(String userId, Token tokenType) {
		Calendar cal = Calendar.getInstance();

		byte[] keyBytes = Decoders.BASE64.decode(secretKey);

		Key hashingSecretKey = Keys.hmacShaKeyFor(keyBytes);

		Claims claims = Jwts.claims()
			.setSubject(tokenType.getTokenType())
			.setIssuedAt(new Date())
			.setExpiration(
				new Date(cal.getTimeInMillis() + 1000L * tokenType.getMaxExpireSecond()));

		claims.put("userId", userId);

		if (tokenType == Token.RefreshToken) {
			claims.put("tokenId", Sha256Util.encrypt(userId + OffsetDateTime.now()));
		}

		return Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims)
			.signWith(hashingSecretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public Claims getJwtContents(String jwt) {
		try {
			return Jwts.parserBuilder()
				.setSigningKey(Decoders.BASE64.decode(secretKey)).build()
				.parseClaimsJws(jwt).getBody();

		} catch (Exception exception) {
			throw new JwtException();
		}

	}

	public String getUserIdFromJwt(String jwt) {
		try {
			Claims claims = getJwtContents(jwt);
			return (String) Jwts.parserBuilder()
				.setSigningKey(Decoders.BASE64.decode(secretKey)).build()
				.parseClaimsJws(jwt)
				.getBody()
				.get("userId");
		} catch (Exception exception) {
			throw new JwtException();
		}
	}

	public Map<Token, ResponseCookie> reissueTokenCookies(Cookie refreshTokenCookie) {
		String userId = getUserIdFromJwt(refreshTokenCookie.getValue());
		return issueTokenCookies(userId);
	}

	public Map<Token, ResponseCookie> issueTokenCookies(String userId) {
		Map<Token, ResponseCookie> tokenCookies = new HashMap<>();

		ResponseCookie accessTokenCookie = CookieUtil.makeCookie(Token.AccessToken.getTokenType(),
			createToken(userId, Token.AccessToken),
			Token.AccessToken.getMaxExpireSecond());
		ResponseCookie refreshTokenCookie = CookieUtil.makeCookie(Token.RefreshToken.getTokenType(),
			createToken(userId, Token.RefreshToken),
			Token.RefreshToken.getMaxExpireSecond());

		tokenCookies.put(Token.AccessToken, accessTokenCookie);
		tokenCookies.put(Token.RefreshToken, refreshTokenCookie);

		return tokenCookies;
	}
}
