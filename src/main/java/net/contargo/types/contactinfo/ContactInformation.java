package net.contargo.types.contactinfo;

import net.contargo.types.telephony.PhoneNumber;


/**
 * Contact options for a user in the COLA environment.
 *
 * @author  jayasinghe@synyx.de
 */
public class ContactInformation {

    private final String userUUID;
    private final PhoneNumber mobile;
    private final PhoneNumber phone;
    private final String email;

    public ContactInformation(String userUUID, String mobile, String phone, String email) {

        this.userUUID = userUUID;
        this.mobile = new PhoneNumber(mobile);
        this.phone = new PhoneNumber(phone);
        this.email = email;
    }

    public String getUserUUID() {

        return userUUID;
    }


    public PhoneNumber getMobile() {

        return mobile;
    }


    public PhoneNumber getPhone() {

        return phone;
    }


    public String getEmail() {

        return email;
    }
}
