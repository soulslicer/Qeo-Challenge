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

import java.util.List;

/**
 * SpeedHandler defines the interface that needs to be implemented to be used by
 * the SpeedCalculator class.
 *
 */
public interface SpeedHandler
{
    /**
     * Handles a new speed measurement as computed by the SpeedCalculator.
     *
     * @param ifaceSpeedDataList List of Interfaces speed data received from reader
     */
    void onSpeedAvailable(List<NetworkInterfaceSpeedData> ifaceSpeedDataList);

    /**
     * Handles the updates if publisher instance gets removed.
     *
     * @param ifNameList The list of network interface names to be removed from UI.
     * @param deviceId the device id of writer that has stopped publishing messages.
     */
    void onSpeedInterfaceRemoved(List<String> ifNameList, String deviceId);

    /**
     * Callback to handle if new writer is available.
     *
     * @param writerId device id of new writer
     */
    void onSpeedDeviceAvailable(String writerId);

    /**
     * Callback to handle if writer is removed.
     *
     * @param writerId device id of removed writer
     */
    void onSpeedDeviceRemoved(String writerId);
}
