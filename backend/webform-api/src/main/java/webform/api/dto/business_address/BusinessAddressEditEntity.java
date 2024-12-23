package webform.api.dto.business_address;

/** 請求書送付先変更（複数請求番号一括用） */
public class BusinessAddressEditEntity {
	/** ご請求番号1 */
	private String billNumber01;
	/** ご請求番号2 */
	private String billNumber02;
	/** ご請求番号3 */
	private String billNumber03;
	/** ご請求番号4 */
	private String billNumber04;
	/** ご請求番号5 */
	private String billNumber05;
	/** ご請求番号6 */
	private String billNumber06;
	/** ご請求番号7 */
	private String billNumber07;
	/** ご請求番号8 */
	private String billNumber08;
	/** ご請求番号9 */
	private String billNumber09;
	/** ご請求番号10 */
	private String billNumber10;

	/** 変更後の送付先名１ */
	private String destinationName;
	/** 変更後の送付先名２（部課名） */
	private String destinationSectionName;
	/** 変更後の送付先名（カナ） */
	private String destinationKana;

	/** 郵便番号 */
	private String post;
	/** 住所コード */
	private String addressCode;
	/** 都道府県市区町村 */
	private String address1;
	/** 番地有無 */
	private String address2Select;
	/** 番地号 */
	private String address2;
	/** ビル名・部屋番号 */
	private String address3;

	/** 変更希望月 */
	private String dateofchange;
	/** 申し込み日 */
	private String dateOfRequest;
	/** 申し込み時刻 */
	private String timeOfRequest;

	/** 会社名 */
	private String applyCorpname;
	/** お申し込み者名 */
	private String applyName;
	/** ご連絡先電話番号 */
	private String tel;
	/** ご連絡先メールアドレス */
	private String mail1;
	/** ご連絡先メールアドレス 確認 */
	private String mail2;
	/** 重要事項の同意 */
	private String agree;

	/**
	 * @return String ご請求番号1をゲットする
	 */
	public String getBillNumber01() {
		return billNumber01;
	}

	/**
	 * @param billNumber01 ご請求番号1をセットする
	 */
	public void setBillNumber01(String billNumber01) {
		this.billNumber01 = billNumber01;
	}

	/**
	 * @return String ご請求番号2をゲットする
	 */
	public String getBillNumber02() {
		return billNumber02;
	}

	/**
	 * @param billNumber02 ご請求番号2をセットする
	 */
	public void setBillNumber02(String billNumber02) {
		this.billNumber02 = billNumber02;
	}

	/**
	 * @return String ご請求番号3をゲットする
	 */
	public String getBillNumber03() {
		return billNumber03;
	}

	/**
	 * @param billNumber03 ご請求番号3をセットする
	 */
	public void setBillNumber03(String billNumber03) {
		this.billNumber03 = billNumber03;
	}

	/**
	 * @return String ご請求番号4をゲットする
	 */
	public String getBillNumber04() {
		return billNumber04;
	}

	/**
	 * @param billNumber04 ご請求番号4をセットする
	 */
	public void setBillNumber04(String billNumber04) {
		this.billNumber04 = billNumber04;
	}

	/**
	 * @return String ご請求番号5をゲットする
	 */
	public String getBillNumber05() {
		return billNumber05;
	}

	/**
	 * @param billNumber05 ご請求番号5をセットする
	 */
	public void setBillNumber05(String billNumber05) {
		this.billNumber05 = billNumber05;
	}

	/**
	 * @return String ご請求番号6をゲットする
	 */
	public String getBillNumber06() {
		return billNumber06;
	}

	/**
	 * @param billNumber06 ご請求番号6をセットする
	 */
	public void setBillNumber06(String billNumber06) {
		this.billNumber06 = billNumber06;
	}

	/**
	 * @return String ご請求番号7をゲットする
	 */
	public String getBillNumber07() {
		return billNumber07;
	}

	/**
	 * @param billNumber07 ご請求番号7をセットする
	 */
	public void setBillNumber07(String billNumber07) {
		this.billNumber07 = billNumber07;
	}

	/**
	 * @return String ご請求番号8をゲットする
	 */
	public String getBillNumber08() {
		return billNumber08;
	}

	/**
	 * @param billNumber08 ご請求番号8をセットする
	 */
	public void setBillNumber08(String billNumber08) {
		this.billNumber08 = billNumber08;
	}

	/**
	 * @return String ご請求番号9をゲットする
	 */
	public String getBillNumber09() {
		return billNumber09;
	}

	/**
	 * @param billNumber09 ご請求番号9をセットする
	 */
	public void setBillNumber09(String billNumber09) {
		this.billNumber09 = billNumber09;
	}

	/**
	 * @return String ご請求番号10をゲットする
	 */
	public String getBillNumber10() {
		return billNumber10;
	}

	/**
	 * @param billNumber10 ご請求番号10をセットする
	 */
	public void setBillNumber10(String billNumber10) {
		this.billNumber10 = billNumber10;
	}

	/**
	 * @return String 変更後の送付先名（カナ）をゲットする
	 */
	public String getDestinationName() {
		return destinationName;
	}

	/**
	 * @param destinationName 変更後の送付先名（カナ）をセットする
	 */
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	/**
	 * @return String 変更後の送付先名２（部課名）をゲットする
	 */
	public String getDestinationSectionName() {
		return destinationSectionName;
	}

	/**
	 * @param destinationSectionName 変更後の送付先名２（部課名）をセットする
	 */
	public void setDestinationSectionName(String destinationSectionName) {
		this.destinationSectionName = destinationSectionName;
	}

	/**
	 * @return String 変更後の送付先名（カナ）をゲットする
	 */
	public String getDestinationKana() {
		return destinationKana;
	}

	/**
	 * @param destinationKana 変更後の送付先名（カナ）をセットする
	 */
	public void setDestinationKana(String destinationKana) {
		this.destinationKana = destinationKana;
	}

	/**
	 * @return String 変更後の送付先名（カナ）をゲットする
	 */
	public String getPost() {
		return post;
	}

	/**
	 * @param post 変更後の送付先名（カナ）をセットする
	 */
	public void setPost(String post) {
		this.post = post;
	}

	/**
	 * @return String 住所コードをゲットする
	 */
	public String getAddressCode() {
		return addressCode;
	}

	/**
	 * @param addressCode 住所コードをセットする
	 */
	public void setAddressCode(String addressCode) {
		this.addressCode = addressCode;
	}

	/**
	 * @return String 都道府県市区町村をゲットする
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1 都道府県市区町村をセットする
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return String 番地有無をゲットする
	 */
	public String getAddress2Select() {
		return address2Select;
	}

	/**
	 * @param address2Select 番地有無をセットする
	 */
	public void setAddress2Select(String address2Select) {
		this.address2Select = address2Select;
	}

	/**
	 * @return String 番地号をゲットする
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2 番地号をセットする
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return String ビル名・部屋番号をゲットする
	 */
	public String getAddress3() {
		return address3;
	}

	/**
	 * @param address3 ビル名・部屋番号をセットする
	 */
	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	/**
	 * @return String 変更希望月をゲットする
	 */
	public String getDateofchange() {
		return dateofchange;
	}

	/**
	 * @param dateofchange 変更希望月をセットする
	 */
	public void setDateofchange(String dateofchange) {
		this.dateofchange = dateofchange;
	}

	/**
	 * @return String 申し込み日をゲットする
	 */
	public String getDateOfRequest() {
		return dateOfRequest;
	}

	/**
	 * @param dateOfRequest 申し込み日をセットする
	 */
	public void setDateOfRequest(String dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	/**
	 * @return String 申し込み時刻をゲットする
	 */
	public String getTimeOfRequest() {
		return timeOfRequest;
	}

	/**
	 * @param timeOfRequest 申し込み時刻をセットする
	 */
	public void setTimeOfRequest(String timeOfRequest) {
		this.timeOfRequest = timeOfRequest;
	}

	/**
	 * @return String 会社名をゲットする
	 */
	public String getApplyCorpname() {
		return applyCorpname;
	}

	/**
	 * @param applyCorpname 会社名をセットする
	 */
	public void setApplyCorpname(String applyCorpname) {
		this.applyCorpname = applyCorpname;
	}

	/**
	 * @return String お申し込み者名をゲットする
	 */
	public String getApplyName() {
		return applyName;
	}

	/**
	 * @param applyName お申し込み者名をセットする
	 */
	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	/**
	 * @return String ご連絡先電話番号をゲットする
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param tel ご連絡先電話番号をセットする
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return String ご連絡先メールアドレスをゲットする
	 */
	public String getMail1() {
		return mail1;
	}

	/**
	 * @param mail1 ご連絡先メールアドレスをセットする
	 */
	public void setMail1(String mail1) {
		this.mail1 = mail1;
	}

	/**
	 * @return String ご連絡先メールアドレス 確認をゲットする
	 */
	public String getMail2() {
		return mail2;
	}

	/**
	 * @param mail2 ご連絡先メールアドレス 確認をセットする
	 */
	public void setMail2(String mail2) {
		this.mail2 = mail2;
	}

	/**
	 * @return String 重要事項の同意をゲットする
	 */
	public String getAgree() {
		return agree;
	}

	/**
	 * @param agree 重要事項の同意をセットする
	 */
	public void setAgree(String agree) {
		this.agree = agree;
	}


}
