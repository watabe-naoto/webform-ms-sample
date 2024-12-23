/*
 * ===============================================================================
 * All right reserved.
 * Copyright(C) 2010 NTT Communications Corporation.
 *
 * ユースケースID : UOAP000
 * ユースケース名 : 新規申込
 *
 * Ver.  日付       作成／変更者       コメント
 * ----- ---------- ------------------ -------------------------------------------
 * 1.0   2011/07/15 SOPIA             新規作成
 * 1.1   2013/01/22 ACN               保守運用タスクID1149対応
 *
 * ===============================================================================
 */
package webform.api.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;


public class SecureUtil {

	private static int TOKEN_LENGTH = 16;  //16*2=32バイト


    /**
     * <p>ランダムなトークン文字列を取得。</p>
     * @param sei
     * @param mei
     * @return
     */
    public static String getRandomToken(){
    	byte token[] = new byte[TOKEN_LENGTH];
    	StringBuffer buf = new StringBuffer();
    	String tokenString = null;

    	SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
        random.nextBytes(token);

        for(int i = 0; i < token.length; i++) {
            buf.append(String.format("%02x", token[i]));
        }
        tokenString = buf.toString();
        return tokenString;
    }


    /**
     * 指定桁数の数値文字列を得る
     * @param codeLong
     * @return
     */
    public static String getRandomNo(int codeLong){
    	return RandomStringUtils.randomNumeric(codeLong);
    }

}
