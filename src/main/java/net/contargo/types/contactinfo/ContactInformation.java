package net.contargo.types.contactinfo;

/**
 * Contact options for a user in the COLA environment.
 *
 * @author  jayasinghe@synyx.de
 */
public class ContactInformation {

    private final String userUUID;
    private final String mobile;
    private final String phone;
    private final String email;
    private final String communicationEmail;

    public ContactInformation(String userUUID, String mobile, String phone, String email, String communicationEmail) {

        this.userUUID = userUUID;
        this.mobile = mobile;
        this.phone = phone;
        this.email = email;
        this.communicationEmail = communicationEmail;
    }

    public String getUserUUID() {

        return userUUID;
    }


    public String getMobile() {

        return mobile;
    }


    public String getPhone() {

        return phone;
    }


    public String getCommunicationEmail() {

        return communicationEmail;
    }


    public String getEmail() {

        return email;
    }
}
