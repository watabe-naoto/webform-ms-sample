package webform.api.dto.billing_extension;

import java.io.Serializable;

public class VBillingExtensionMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    public VBillingExtensionMessage() {}

    /* ID */
    private Integer id;
    /* メッセージ項目コード */
    private String messageItemCode;
    /* 表示開始日時 */
    private java.util.Date displayStartDatetime;
    /* 表示終了日時 */
    private java.util.Date displayStopDatetime;
    /* 表示内容 */
    private String contents;
    /* 作成者 */
    private String createUser;
    /* 作成日時 */
    private java.util.Date createDatetime;

    public Integer getId() {
        return id;
    }

    public String getMessageItemCode() {
        return messageItemCode;
    }

    public java.util.Date getDisplayStartDatetime() {
        return displayStartDatetime;
    }

    public java.util.Date getDisplayStopDatetime() {
        return displayStopDatetime;
    }

    public String getContents() {
        return contents;
    }
    
    public String getCreateUser() {
        return createUser;
    }

    public java.util.Date getCreateDatetime() {
        return createDatetime;
    }
}
