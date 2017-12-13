package org.eclipse.crossmeter.platform.communicationchannel.zendesk;

/**
 * @author stephenc
 * @since 04/04/2013 14:24
 */
public class ZendeskException extends RuntimeException {
    public ZendeskException(String message) {
        super(message);
    }

    public ZendeskException() {
    }

    public ZendeskException(Throwable cause) {
        super(cause);
    }

    public ZendeskException(String message, Throwable cause) {
        super(message, cause);
    }
}