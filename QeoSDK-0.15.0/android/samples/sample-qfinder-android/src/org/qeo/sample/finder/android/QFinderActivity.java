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

package org.qeo.sample.finder.android;

import java.util.ArrayList;
import java.util.UUID;

import org.qeo.QeoFactory;
import org.qeo.StateReader;
import org.qeo.StateReaderListener;
import org.qeo.android.QeoAndroid;
import org.qeo.android.QeoConnectionListener;
import org.qeo.android.exception.QeoSecurityInitFailedException;
import org.qeo.android.exception.QeoServiceNotFoundException;
import org.qeo.exception.QeoException;
import org.qeo.system.DeviceId;
import org.qeo.system.DeviceInfo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main activity of the QFinder app that will display a list of discovered Qeo devices.
 */
public class QFinderActivity
        extends Activity
{
    private static final String TAG = "QFinder";
    private QeoFactory mQeo = null;
    private StateReader<DeviceInfo> mDevices = null;
    private ArrayAdapter<String> mAdapter = null;
    private ListView mListview = null;
    private DeviceId mThisDeviceId = null;
    private boolean mQeoClosed = false;

    /**
     * Implementation of a StateReaderListener interface that will be used in triggering updates to the device list.
     */
    private class DeviceIdListener
            implements StateReaderListener
    {
        /**
         * Whenever anything changes with respect to discovered devices this method will be called. This can be either
         * due to the discovery of a new device, the disappearance of an existing device or the update of the data of an
         * existing device. In response to this notification we will update our device list in the UI.
         */
        @Override
        public void onUpdate()
        {
            Log.d(TAG, "refreshing list");
            /* First remove all items from the list. */
            mAdapter.clear();
            /* Then add all devices that are still available. */
            for (final DeviceInfo device : mDevices) {
                /*
                 * Each line starts with the device identifier to later be able to change the display color depending on
                 * this.
                 */
                String devideId = new UUID(device.deviceId.upper, device.deviceId.lower).toString();
                mAdapter.add(devideId + "\n  manufacturer = " + device.manufacturer + "\n  modelName = "
                        + device.modelName + "\n  productClass = " + device.productClass + "\n  serialNumber = "
                        + device.serialNumber + "\n  hardwareVersion = " + device.hardwareVersion
                        + "\n  softwareVersion = " + device.softwareVersion + "\n  userFriendlyName = "
                        + device.userFriendlyName + "\n  configURL = " + device.configURL);
            }

        }
    }

    /**
     * A method for connecting to the Qeo service.
     */
    private void qeoInit()
    {
        Log.d(TAG, "Initializing qeo connection");
        /*
         * Initialize Qeo and provide a callback for handling the notification of being ready and disconnected.
         */
        QeoAndroid.initQeo(getApplicationContext(), new QeoConnectionListener() {
            /**
             * When the connection with the Qeo service is ready we can create our reader.
             */
            @Override
            public void onQeoReady(QeoFactory qeo)
            {
                mQeo = qeo;
                Log.d(TAG, "onQeoReady");
                try {
                    /*
                     * Retrieve the identifier of the device on which the app is running. This identifier will be used
                     * later to change the determine which color to use for displaying the discovered devices.
                     */
                    mThisDeviceId = QeoAndroid.getDeviceId();
                    /*
                     * create a state reader for DeviceInfo and listen for updates using the StateReaderListener
                     * interface implemented by this activity (see the onUpdate method above).
                     */
                    mDevices = mQeo.createStateReader(DeviceInfo.class, new DeviceIdListener());
                }
                catch (final QeoException e) {
                    Log.e(TAG, "Error creating state reader", e);
                }
                mQeoClosed = false;
            }

            @Override
            public void onQeoError(QeoException ex)
            {
                if (ex instanceof QeoServiceNotFoundException) {
                    Toast.makeText(QFinderActivity.this, "QeoService not installed, exiting!", Toast.LENGTH_LONG)
                            .show();
                }
                else if (ex instanceof QeoSecurityInitFailedException) {
                    Toast.makeText(QFinderActivity.this, "Qeo Service failed due to " + ex.getMessage(),
                        Toast.LENGTH_LONG).show();
                }
                Log.e(TAG, "Error initializing Qeo", ex);

                finish();
            }

            @Override
            public void onQeoClosed(QeoFactory qeo)
            {
                Log.w(TAG, "Connection to qeo service lost");
                mQeoClosed = true;
                mDevices = null;
                mQeo = null;
            }
        });
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        /* Initialize Qeo. */
        qeoInit();
        /* Initialize the UI. */
        setContentView(R.layout.activity_main);
        /* Link the list view to an adapter that will contain the device list. */
        final ArrayList<String> list = new ArrayList<String>();
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, list) {
            /*
             * We subclass the default ArrayAdapter to be able to change the color of the lines in the ListView
             * depending on whether it is the device running the app or some other device.
             */
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                TextView textView = (TextView) super.getView(position, convertView, parent);

                if (textView.getText().toString().startsWith(mThisDeviceId.toString())) {
                    textView.setTextColor(Color.YELLOW); /* this device */
                }
                else {
                    textView.setTextColor(Color.WHITE); /* another device */
                }
                return textView;
            }

        };
        mListview = (ListView) findViewById(R.id.deviceList);
        mListview.setAdapter(mAdapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if (mQeoClosed) {
            // if the connection to the service was lost while we're not active, restore it again.
            qeoInit();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mQeo != null) {
            /* Close our reader. */
            if (mDevices != null) {
                mDevices.close();
                mDevices = null;
            }
            /* Disconnect from the service. */
            mQeo.close();
            mQeo = null;
        }
    }
}
