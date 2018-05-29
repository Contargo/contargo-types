package net.contargo.types.contactinfo;

/**
 * Contact options for a user in the COLA environment.
 *
 * @author  jayasinghe@synyx.de
 */
public class ContactInformation {

    private final String userUuid;
    private final String mobile;
    private final String phone;
    private final String email;

    public ContactInformation(String userUuid, String mobile, String phone, String email) {

        this.userUuid = userUuid;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
    }

    public String getUserUuid() {

        return userUuid;
    }


    public String getMobile() {

        return mobile;
    }


    public String getPhone() {

        return phone;
    }


    public String getEmail() {

        return email;
    }
}
