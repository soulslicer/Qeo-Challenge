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
package org.qeo.sample.gauge.android.reader.helper;

import java.util.List;

import org.qeo.sample.gauge.android.reader.model.DeviceModel;

/**
 * Interface to handle the callback to update Device list screen based on updates from DeviceInfoReader.
 */

public interface DeviceInfoCallBackHandler
{

    /**
     * @param deviceList List of Qeo writer devices to be updated at UI level.
     */
    void onDeviceInfoUpdated(List<DeviceModel> deviceList);
}
