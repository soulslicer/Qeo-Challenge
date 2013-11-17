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

import org.qeo.QeoFactory;
import org.qeo.android.QeoAndroid;
import org.qeo.android.QeoConnectionListener;
import org.qeo.exception.QeoException;
import org.qeo.sample.gauge.NetStatPublisher;
import org.qeo.system.DeviceId;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * This service starts the Qeo service as Android service and publishes the data.<br/>
 * For more information, check the Android Developer Guide
 */
public class QeoLocalService
    extends Service
{
    private static final String LOGGING_TAG = QeoLocalService.class.getSimpleName();
    private QeoFactory mQeo;
    private GaugeWriter mGaugeWriter;
    private NetStatPublisher mPublisher;
    private Intent mIntent;
    /**
     * 
     */
    public static final String QEO_SERVICE_STATUS = "org.qeo.sample.gauge.android.writer.SERVICE_STATUS";

    @Override
    public void onCreate()
    {
        mIntent = new Intent(QEO_SERVICE_STATUS);
        // Get a QeoFactory object to create Writer
        QeoAndroid.initQeo(getApplicationContext(), new QeoConnectionListener() {
            // Callback received once Qeo service is initialized successfully.
            @Override
            public void onQeoReady(QeoFactory qeo)
            {
                mQeo = qeo;
                DeviceId did = QeoAndroid.getDeviceId();
                if (did == null) {
                    Log.e(LOGGING_TAG, "Device id is not initialized");
                    stopSelf();
                }
                else {
                    Log.d(LOGGING_TAG, "onQeoReady" + did);
                    try {
                        mGaugeWriter = new GaugeWriter(did);
                        mPublisher = new NetStatPublisher(mGaugeWriter, 250, mQeo);
                    }
                    catch (QeoException e) {
                        Log.e(LOGGING_TAG, "Failure during initialisation", e);
                        stopSelf();
                    }
                }
            }

            @Override
            public void onQeoClosed(QeoFactory qeo)
            {
                super.onQeoClosed(qeo);
                if (mPublisher != null) {
                    mPublisher.close();
                }
                mPublisher = null;
                mQeo = null;
                mIntent.putExtra(getString(R.string.broadcast_qeo_force_stop), true);
                sendBroadcast(mIntent);
            }

            @Override
            public void onQeoError(QeoException ex)
            {
                Log.e(LOGGING_TAG, "Failure during initialisation", ex);

                mIntent.putExtra(getString(R.string.broadcast_qeo_force_stop), false);
                mIntent.putExtra(getString(R.string.broadcast_general_failure), false);
                mIntent.putExtra(getString(R.string.broadcast_qeo_fail_msg), ex.getMessage());
                stopSelf();
                sendBroadcast(mIntent);

            }
        });

    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        return false;
    }

    @Override
    public void onDestroy()
    {
        // Close the writer in onDestroy() as explained in document- Android Developer Guide
        if (mPublisher != null) {
            mPublisher.close();
        }
        // Close Qeo factory instance
        if (mQeo != null) {
            mQeo.close();
        }
    }

}
