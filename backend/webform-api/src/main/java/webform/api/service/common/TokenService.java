package webform.api.service.common;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.server.ContainerRequest;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import webform.api.exception.TokenException;
import webform.api.util.ResourceUtil;

/**
 * トークンServiceクラス
 *
 */
public class TokenService {
	/** Log */
	private static final Logger logger = LogManager.getLogger(TokenService.class);
	/** Access Token Label Name */
	private static final String[] ACCESS_TOKEN_LABEL = { "access-token", "access_token", "accessToken" };
	/** SUBJECT */
	private static final String SUBJECT = "reqform";

	private static final String USER_TOKEN_KEY = "user_token";

	/**
	 * トークンの生成
	 *
	 * @param userId ユーザID
	 * @return トークン文字列
	 */
	public static String createToken(final String userToken) {
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
				.subject(ResourceUtil.getString("claim.subject", SUBJECT))
				.audience(ResourceUtil.getString("claim.audience", ""))
				.expirationTime(getExpireDatetime())
				.claim(USER_TOKEN_KEY, userToken)
				.build();
		return createToken(claimsSet);
	}

	/**
	 * トークンの生成
	 *
	 * @return トークン文字列
	 */
	public static String createToken(final JWTClaimsSet claimsSet) {
		try {
			// Create an HMAC-protected JWS object
			SignedJWT signedJWT = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build(), claimsSet);

			// Apply the HMAC to the JWS object
			signedJWT.sign(new MACSigner(getSharedKey()));

			// Output to URL-safe format
			return signedJWT.serialize();
		} catch (JOSEException ex) {
			throw new TokenException(ex);
		}
	}

	/**
	 * ヘッダの取得
	 *
	 * @param token トークン
	 * @return ヘッダ
	 */
	public static JWSHeader getHeader(final String token) {
		if (!StringUtils.isEmpty(token)) {
			try {
				SignedJWT signedJWT = SignedJWT.parse(token);
				return signedJWT.getHeader();
			} catch (ParseException ex) {
				throw new TokenException(ex);
			}
		}
		return null;
	}

	/**
	 * ClaimsSetの取得
	 *
	 * @param token トークン
	 * @return ClaimsSet
	 */
	public static JWTClaimsSet getClaimsSet(final String token) {
		if (!StringUtils.isEmpty(token)) {
			try {
				// parse
				SignedJWT signedJWT = SignedJWT.parse(token);
				// verify
				if (signedJWT.verify(new MACVerifier(getSharedKey()))) {
					return signedJWT.getJWTClaimsSet();
				}
				throw new TokenException("verify error");
			} catch (ParseException | JOSEException ex) {
				throw new TokenException(ex);
			}
		}
		return null;
	}

	/**
	 * トークンの妥当性チェック
	 *
	 * @return チェック結果
	 */
	public static boolean isTokenValid(final HttpHeaders headers) {
		if (null != headers) {
			// ヘッダから取得
			for (String label : ACCESS_TOKEN_LABEL) {
				String accessToken = headers.getHeaderString(label);
				if (!StringUtils.isEmpty(accessToken) && isValid(accessToken)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * token文字列を取得
	 * @param headers
	 * @return
	 */
	public static String getToken(final HttpHeaders headers) {
		if (null != headers) {
			// ヘッダから取得
			for (String label : ACCESS_TOKEN_LABEL) {
				String accessToken = headers.getHeaderString(label);
				if (!StringUtils.isEmpty(accessToken) && isValid(accessToken)) {
					return accessToken;
				}
			}
		}
		return null;
	}

	/**
	 * <b>アクセストークンの有効性確認</b>
	 * <br/><br/>
	 * 取得した認証トークンを分解し、以下情報が含まれている場合のみ有効とする。
	 * <ul><li>SUBJECT:mypage</li>
	 * <li>有効期限： 有効期限内</li>
	 * <li>UserId: ユーザID</li></ul>
	 *
	 * @param accessToken アクセストークン
	 * @return 有効性
	 */
	public static boolean isValid(final String accessToken) {
		JWTClaimsSet claimsSet = getClaimsSet(accessToken);
		if ((null != claimsSet) && SUBJECT.equals(claimsSet.getSubject())) {
			final Date expireDatetime = claimsSet.getExpirationTime();
			if ((null != expireDatetime) && (System.currentTimeMillis() < expireDatetime.getTime())) {
				final String userId = (String) claimsSet.getClaim(USER_TOKEN_KEY);
				if (!StringUtils.isEmpty(userId)/* && AuthenticateService.isUserValid(userId)*/) {
					logger.info("UserId:[" + userId + "] accepted.");
					return true;
				}
			} else {
				logger.debug("token expired.");
			}
		} else if (null != claimsSet) {
			logger.warn("未知のSubject:" + claimsSet.getSubject());
		} else {
			logger.warn("no claims");
		}
		return false;
	}

	/**
	 * ユーザIDの取得
	 *
	 * @param accessToken アクセストークン
	 * @return ユーザID
	 */
	public static String getUserToken(final String accessToken) {
		JWTClaimsSet claimsSet = getClaimsSet(accessToken);
		if ((null != claimsSet) && SUBJECT.equals(claimsSet.getSubject())) {
			logger.info("match subject.");
			return (String) claimsSet.getClaim(USER_TOKEN_KEY);
		}
		logger.info("subject failed.");
		return null;
	}

	public static String getUserToken(final HttpHeaders headers) {
		if (null != headers) {
			// ヘッダから取得
			for (String label : ACCESS_TOKEN_LABEL) {
				String accessToken = headers.getHeaderString(label);
				logger.info("raw-token header:" + accessToken);
				if (!StringUtils.isEmpty(accessToken)) {
					return getUserToken(accessToken);
				}
			}
			// Cookieから取得
			//			Map<String, Cookie> cookieMap = headers.getCookies();
			//			if (!Util.isEmpty(cookieMap)) {
			//				for (String label: ACCESS_TOKEN_LABEL) {
			//					Cookie accessTokenCookie = cookieMap.get(label);
			//					System.out.println("raw-token cookie:" + accessTokenCookie);
			//					if (null != accessTokenCookie) {
			//						String accessToken = accessTokenCookie.getValue();
			//						if (!Util.isEmpty(accessToken)) {
			//							return getUserToken(accessToken);
			//						}
			//					}
			//				}
			//			}
		}
		return null;
	}

	public static String getUserTokenFromParam(ContainerRequestContext requestContext) {

		String q = getPostInfo(requestContext);

		//		log.info("post: " + q);
		String[] params = q.split("&");
		for (String param : params) {
			for (String label : ACCESS_TOKEN_LABEL) {
				if (label.equals(param.split("=")[0])) {
					String accessToken = param.split("=")[1];
					//log.info("raw-token post:" + accessToken);
					if (!StringUtils.isEmpty(accessToken)) {
						return getUserToken(accessToken);
					}
				}
			}
		}
		return null;
	}

	private static String getPostInfo(final ContainerRequestContext requestContext) {
		ContainerRequest request = (ContainerRequest) requestContext;
		StringBuilder builder = new StringBuilder("");
		if (request.bufferEntity()) {
			ByteArrayOutputStream byteStream = null;
			try {
				InputStream iStream = request.getEntityStream();
				byteStream = new ByteArrayOutputStream();
				byteStream.write(iStream);
				byte[] byt = byteStream.toByteArray();
				builder.append(new String(byt));
				requestContext.setEntityStream(new ByteArrayInputStream(byt));
			} catch (Exception ex) {
				return null;
			} finally {
				if (null != byteStream) {
					try {
						byteStream.close();
					} catch (IOException ex) {
					}
				}
			}
		}
		return builder.toString();
	}

	/**
	 * 共有キーの取得
	 *
	 * @return 共有キー
	 */
	private static byte[] getSharedKey() {
		return ResourceUtil.getPrimitiveByte("token.shared.key");
	}

	/**
	 * 有効期限の取得
	 *
	 * @return 有効期限
	 */
	private static Date getExpireDatetime() {
		Calendar nowTime = Calendar.getInstance();
		nowTime.add(Calendar.MINUTE, ResourceUtil.getInteger("token.expire.min", 120));
		return nowTime.getTime();
	}
}
