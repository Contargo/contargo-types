package net.contargo.types.contactinfo;

import java.util.List;


public interface ContactInfoConsumer {


    default boolean consumesRegistrations() {

        return true;
    }


    void consume(final List<ContactInformation> allContactInformation);


    void consume(ContactInformation contactInformation);


    default void remove(ContactInformation contactInformation) {

        // OK
    }


    default void reset() {

        // OK
    }
}
