package webform.api.dto.external_system;

import java.io.Serializable;

public class VExternalSystemRelationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public VExternalSystemRelationInfo() {}

    /*  */
    private Integer id;
    /*  */
    private String orderformType;
    /*  */
    private Integer externalSystemId;
    /*  */
    private String addUser;
    /*  */
    private java.util.Date addDate;

    public Integer getId() {
        return id;
    }

    public String getOrderformType() {
        return orderformType;
    }

    public Integer getExternalSystemId() {
        return externalSystemId;
    }

    public String getAddUser() {
        return addUser;
    }

    public java.util.Date getAddDate() {
        return addDate;
    }

}
