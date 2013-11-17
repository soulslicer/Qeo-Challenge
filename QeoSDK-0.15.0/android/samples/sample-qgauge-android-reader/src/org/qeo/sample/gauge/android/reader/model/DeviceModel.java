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
package org.qeo.sample.gauge.android.reader.model;

import org.qeo.sample.gauge.android.reader.interfaces.Device;
import org.qeo.sample.gauge.android.reader.interfaces.NetworkInterface;

/**
 *
 * Device class implements the IDevice interface and acts as a model to fill with device data.
 *
 */
public class DeviceModel
        implements Device
{
    private final String mId;
    private final String mName;
    private final String mDescription;

    /**
     * Constructs a DeviceModel object with deviceId and other details.
     *
     * @param id deviceId
     * @param name device name
     * @param description detailed description of device
     */
    public DeviceModel(String id, String name, String description)
    {
        mId = id;
        mName = name;
        mDescription = description;
    }

    @Override
    public String getId()
    {
        return mId;
    }

    @Override
    public String getName()
    {
        return mName;
    }

    @Override
    public String getDescription()
    {
        return mDescription;
    }

    @Override
    public NetworkInterface[] getInterfaces()
    {
        return null;
    }
}
