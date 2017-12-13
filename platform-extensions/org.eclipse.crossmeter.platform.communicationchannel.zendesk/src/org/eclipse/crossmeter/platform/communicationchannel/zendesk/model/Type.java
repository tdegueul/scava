package org.eclipse.crossmeter.platform.communicationchannel.zendesk.model;

/**
 * @author stephenc
 * @since 05/04/2013 08:57
 */
public enum Type {
    PROBLEM,
    INCIDENT,
    QUESTION,
    TASK;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}