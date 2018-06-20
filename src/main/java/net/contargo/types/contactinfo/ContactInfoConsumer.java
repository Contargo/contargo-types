package net.contargo.types.contactinfo;

import java.util.List;


public interface ContactInfoConsumer {

    void consume(final List<ContactInformation> allContactInformation);


    void consume(ContactInformation contactInformation);


    default void remove(ContactInformation contactInformation) {

        // OK
    }
}
