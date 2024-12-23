package webform.api.dto.external_system;

import java.io.Serializable;

public class VExternalSystemInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public VExternalSystemInfo() {
	}

	/*  */
	private Integer id;
	/*  */
	private Integer externalSystemId;
	/*  */
	private String externalSystemName;
	/*  */
	private Integer externalSystemStatus;
	/*  */
	private java.util.Date updateDatetime;
	/*  */
	private String updateUser;
	/*  */
	private java.util.Date stopDatetime;
	/*  */
	private java.util.Date rebootDatetime;
	/*  */
	private String message;

	public Integer getId() {
		return id;
	}

	public Integer getExternalSystemId() {
		return externalSystemId;
	}

	public String getExternalSystemName() {
		return externalSystemName;
	}

	public Integer getExternalSystemStatus() {
		return externalSystemStatus;
	}

	public java.util.Date getUpdateDatetime() {
		return updateDatetime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public java.util.Date getStopDatetime() {
		return stopDatetime;
	}

	public java.util.Date getRebootDatetime() {
		return rebootDatetime;
	}

	public String getMessage() {
		return message;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setExternalSystemId(Integer externalSystemId) {
		this.externalSystemId = externalSystemId;
	}

	public void setExternalSystemName(String externalSystemName) {
		this.externalSystemName = externalSystemName;
	}

	public void setExternalSystemStatus(Integer externalSystemStatus) {
		this.externalSystemStatus = externalSystemStatus;
	}

	public void setUpdateDatetime(java.util.Date updateDatetime) {
		this.updateDatetime = updateDatetime;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public void setStopDatetime(java.util.Date stopDatetime) {
		this.stopDatetime = stopDatetime;
	}

	public void setRebootDatetime(java.util.Date rebootDatetime) {
		this.rebootDatetime = rebootDatetime;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
