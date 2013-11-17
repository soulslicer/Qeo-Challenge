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

import org.qeo.sample.gauge.NetworkInterfaceSpeedData;
import org.qeo.sample.gauge.SpeedHandler;
import org.qeo.sample.gauge.android.reader.GaugeReaderApplication;
import org.qeo.sample.gauge.android.reader.model.DeviceModel;

/**
 * CallbackHandler to provide the updates to UI based on data received from readers.
 */
public class ReaderCallbackHandler
        implements SpeedHandler, DeviceInfoCallBackHandler
{

    private static ReaderCallbackHandler sInstance;
    private RefreshUIListener mListener;

    /**
     * Provides the instance of ReaderCallbackHandler.
     * 
     * @return returns the object of ReaderCallbackHandler class.
     */
    public static ReaderCallbackHandler getInstance()
    {
        if (sInstance == null) {
            sInstance = new ReaderCallbackHandler();
        }

        return sInstance;
    }

    /**
     * Registers the listener to get the callbacks.
     * 
     * @param listener - Listener to be registered
     */
    public void registerUiListener(RefreshUIListener listener)
    {
        this.mListener = listener;
    }

    /**
     * Unregisters the listener to stop listening the callbacks.
     * 
     * @param listener Listener to be unregistered
     */
    public void unRegisterUiListener(RefreshUIListener listener)
    {
        if (listener == this.mListener) {
            this.mListener = null;
        }
    }

    /**
     * Callback from the SpeedCalculator that indicates a new speed has been received.
     * 
     * @param ifaceSpeedDataList List of interface speed data received from reader
     */
    @Override
    public void onSpeedAvailable(List<NetworkInterfaceSpeedData> ifaceSpeedDataList)
    {
        if (mListener != null) {
            mListener.onPublish(ifaceSpeedDataList);
        }
    }

    /**
     * Callback from SpeedCalculator to update UI and cleanup if writer is removed.
     * 
     * @param ifNameList -list of interfaces to be removed from UI for removed writer
     * @param deviceId -device id of writer that has stopped publishing data
     */
    @Override
    public void onSpeedInterfaceRemoved(List<String> ifNameList, String deviceId)
    {
        if (mListener != null) {
            mListener.onRemove(ifNameList);
        }
    }

    /**
     * Interface to handle the callback to update UI based on message received from reader.
     */
    public interface RefreshUIListener
    {
        /**
         * @param ifaceSpeedDataList List of calculated network interface speed data to be updated at UI level.
         */
        void onPublish(List<NetworkInterfaceSpeedData> ifaceSpeedDataList);

        /**
         * @param ifName list of interface names to be removed form UI.
         */
        void onRemove(List<String> ifName);

        /**
         * @param deviceList list of Qeo writer devices to be updated on UI.
         */
        void onupdateDeviceList(List<DeviceModel> deviceList);

    }

    @Override
    public void onDeviceInfoUpdated(final List<DeviceModel> deviceList)
    {
        GaugeReaderApplication.sQeoDeviceList = deviceList;
        if (mListener != null) {
            mListener.onupdateDeviceList(deviceList);
        }
    }

    /**
     * Callback from the SpeedCalculator that indicates a new device writer is available.
     * 
     * @param writerId - device id of new device writer
     */
    @Override
    public void onSpeedDeviceAvailable(String writerId)
    {
        GaugeReaderApplication.sDeviceIdList.add(writerId);
    }

    /**
     * Callback from the SpeedCalculator that indicates device writer is removed.
     * 
     * @param writerId - device id of removed device writer
     */
    @Override
    public void onSpeedDeviceRemoved(String writerId)
    {
        GaugeReaderApplication.sDeviceIdList.remove(writerId);
    }

}
