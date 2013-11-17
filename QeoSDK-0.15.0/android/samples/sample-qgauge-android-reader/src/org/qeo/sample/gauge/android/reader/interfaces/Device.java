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
 * Device Interface needs to be implemented to display the Device details viz. name, description, interfaces to
 * differentiate between different devices.
 *
 */
public interface Device
{
    /**
     * Gets the unique id of device.
     *
     * @return deviceId of device
     */
    String getId();

    /**
     * Gets the name of device.
     *
     * @return Display name of device
     */
    String getName();

    /**
     * Gets the description of device.
     *
     * @return description of device
     */
    String getDescription();

    /**
     * Returns the list of network interfaces for device.
     *
     * @return list if interfaces for device
     */
    NetworkInterface[] getInterfaces();
}
