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
package org.qeo.sample.gauge.android.writer;

/**
 * This class maintains the list of constants used in the application.
 */
public final class Constants
{

    private Constants()
    {

    }
    /**
     * File path name for to get interface details.
     */
    public static final String INTERFACE_FILE_PATH = "/sys/class/net/";
    /**
     * File path name for to get details for received data in bytes.
     */
    public static final String RX_BYTES_PATH = "/statistics/rx_bytes";
    /**
     * File path name for to get details for received data in packets.
     */
    public static final String RX_PACKETS_PATH = "/statistics/rx_packets";
    /**
     * File path name for to get details for transmitted data in bytes.
     */
    public static final String TX_BYTES_PATH = "/statistics/tx_bytes";
    /**
     * File path name for to get details for transmitted data in packets.
     */
    public static final String TX_PACKETS_PATH = "/statistics/tx_packets";

    /**
     * Bundle- Extra used to save current published state of Writer.
     */
    public static final String EXTRA_PUBLISH_STATE = "CurrentPublishState";

}
