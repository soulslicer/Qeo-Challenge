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

package org.qeo.sample.gauge.android.reader.interfaces;

/**
 *
 */
public interface NetworkInterface
{
    /**
     * Returns the interface name.
     *
     * @return Display name of interface
     */
    String getName();

    /**
     * Returns the interface state.
     *
     * @return current state of interface
     */
    String getState();

    /**
     * Returns packet data transferred over the interface.
     *
     * @return number of packets transferred
     */
    int getTxPackets();

    /**
     * Returns packet data received over the interface.
     *
     * @return number of packets received
     */
    int getRxPackets();

    /**
     * Returns the IP address of interface.
     *
     * @return string representing the IP address
     */
    String getIpAddress();

}
