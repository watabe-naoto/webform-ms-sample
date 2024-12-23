package webform.ms.exception;

import java.io.Serializable;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

import webform.ms.util.ExceptionUtil;


/**
 *
 * @author d_yoshioka
 *
 */
public class AppException extends RuntimeException implements Serializable {
    final static String SYSTEM_ERR = "systemError";
    final static String MESSAGE_PATH = "apperrors";	// エラーメッセージを定義するプロパティファイル

    // defaults
    final static String DEFAULT_ERR_TITLE = "システムエラー";
    final static String DEFAULT_ERR_MSG = "システムエラーが発生しました。";

    String errorCd;

    String args[];

    String customMessage;

	public void setArgs(String[] args) {
		this.args = args;
	}
	public String getCustomMessage() {
		return customMessage;
	}
	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}
	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}

	public AppException(){
        this.errorCd = null;
    }
    public AppException(String errorCd){
        this.errorCd = errorCd;
    }
    public AppException(String errorCd, String[] args){
        this.errorCd = errorCd;
        this.args = args;
    }

    /**
     * エラーコードを取得する
     * @return
     */
    public String getErrorCd(){
        if(StringUtils.isEmpty(errorCd)){
        	// エラーコードがセットされていない場合はデフォルトのエラーコードを返す
            return SYSTEM_ERR;
        }
        return errorCd;
    }

    /**
     * エラーメッセージを取得する
     * @return
     */
    public String getErrorMsg() {
        if(!StringUtils.isEmpty(customMessage)){
        	// カスタムエラーメッセージがセットされていればそれを返す
            return customMessage;
        }

     // カスタムされていなければプロパティファイルから取得
        String message = ExceptionUtil.getFromProp(MESSAGE_PATH, getErrorCd() + ".error", DEFAULT_ERR_MSG);

        if(args != null){
        	return MessageFormat.format(message, (Object[])args);
        }

        return message;
    }

    /**
     * エラーメッセージを取得する
     * @return
     */
    @Override
    public String getMessage() {
    	return getErrorMsg();
    }

    /**
     * エラータイトルを取得
     * @return
     */
    public String getErrorTitle(){
        return ExceptionUtil.getFromProp(MESSAGE_PATH, getErrorCd() + ".title", DEFAULT_ERR_TITLE);
    }
}