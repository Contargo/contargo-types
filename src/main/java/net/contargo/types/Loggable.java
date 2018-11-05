package net.contargo.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public interface Loggable {


    default Logger logger() {

        return LoggerFactory.getLogger(this.getClass());
    }
}
