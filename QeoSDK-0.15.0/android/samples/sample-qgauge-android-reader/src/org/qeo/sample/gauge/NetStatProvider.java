/************** COPYRIGHT AND CONFIDENTIALITY INFORMATION *********************
 **                                                                          **
 ** Copyright (c) 2013 Technicolor                                           **
 ** All Rights Reserved                                                      **
 **                                                                          **
 ** This program contains proprietary information which is a trade           **
 ** secret of TECHNICOLOR and/or its affiliates and also is protected as     **
 ** an unpublished work under applicable Copyright laws. Recipient is        **
 ** to retain this program in confidence and is not permitted to use or      **
 ** make copies thereof other than as permitted in a written agreement       **
 ** with TECHNICOLOR, UNLESS OTHERWISE EXPRESSLY ALLOWED BY APPLICABLE LAWS. **
 **                                                                          **
 ******************************************************************************/
package org.qeo.sample.gauge;

/**
 * NetStatProvider needs to be implemented to create a NetStatPublisher. It
 * abstracts the platform specific implementation of collecting network
 * statistics from the logic to publish these statistics over Qeo.
 */

public interface NetStatProvider
{
    /**
     * Retrieves the current counter values of all the interfaces that need to
     * be published by a NetStatPublisher.
     *
     * @return An array of NetStatmMessages that will be published by the
     *         NetStatPublisher.
     */
    NetStatMessage[] getCurrentStats();

    /**
     * Callback method when NetStatPublisher is stopped to clean up any resources used by this NetStatProvider.
     *
     * @param netStatPublisher object publishing the data
     * @param cause object contains the error details
     */
    void publisherStopped(NetStatPublisher netStatPublisher, Throwable cause);
}
