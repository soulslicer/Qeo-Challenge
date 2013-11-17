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

import org.qeo.sample.gauge.android.reader.interfaces.NetworkInterface;


/**
 *
 */
public class NetworkInterfaceModel
        implements NetworkInterface
{
    private final String mName;
    private final String mState;
    private final int mTxPackets;
    private final int mRxPackets;
    private final String mAddress;

    /**
     * Constructs an NetworkInterfaceModel object.
     * 
     * @param name Name of the interface
     * @param state Current state of interface
     * @param txPackets Transferred data in packets
     * @param rxPackets Received data in Packets
     * @param address IP address of interface
     */
    public NetworkInterfaceModel(String name, String state, int txPackets, int rxPackets, String address)
    {
        mName = name;
        mState = state;
        mTxPackets = txPackets;
        mRxPackets = rxPackets;
        mAddress = address;
    }

    @Override
    public String getName()
    {
        return mName;
    }

    @Override
    public String getState()
    {
        return mState;
    }

    @Override
    public int getTxPackets()
    {
        return mTxPackets;
    }

    @Override
    public int getRxPackets()
    {
        return mRxPackets;
    }

    @Override
    public String getIpAddress()
    {
        return mAddress;
    }

}
