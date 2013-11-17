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
package org.qeo.sample.gauge.android.writer.ui;

import org.qeo.sample.gauge.android.writer.Constants;
import org.qeo.sample.gauge.android.writer.QeoLocalService;
import org.qeo.sample.gauge.android.writer.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Fragment class to display the UI for GaugeWriter application. We are using Fragments based on the Best Practice
 * Approaches explained in Android Developer Guide.
 */
public class GaugeWriterFragment
    extends Fragment
{
    private Button mStartPubBtn;
    private Button mStopPubBtn;
    private Intent mQeoserviceIntent;
    private Context mCtx;
    private boolean mStartState = true;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mCtx = this.getActivity();
        mCtx.registerReceiver(mReceiver, new IntentFilter(QeoLocalService.QEO_SERVICE_STATUS));
        View v = inflater.inflate(R.layout.gauge_writer_fragment, container, false);

        mStartPubBtn = (Button) v.findViewById(R.id.startPubBtn);
        mStopPubBtn = (Button) v.findViewById(R.id.stopPubBtn);

        if (savedInstanceState != null) {
            mStartState = savedInstanceState.getBoolean(Constants.EXTRA_PUBLISH_STATE);
        }
        if (mStartState) {
            mStartPubBtn.setEnabled(true);
            mStopPubBtn.setEnabled(false);
        }
        else {
            mStartPubBtn.setEnabled(false);
            mStopPubBtn.setEnabled(true);
        }

        mStartPubBtn.setOnClickListener(new View.OnClickListener() {
            /*
             * This button listener initializes the Android Qeo library.Application Context reference is required to
             * start the Qeo service and is then used to create a publisher.
             */
            @Override
            public void onClick(View v)
            {
                mStartState = false;
                mQeoserviceIntent = new Intent(mCtx, QeoLocalService.class);
                mCtx.startService(mQeoserviceIntent);
                mStartPubBtn.setEnabled(false);
                mStopPubBtn.setEnabled(true);

            }
        });
        /*
         * The stopButton listener stops the publisher and stops the Android Qeo service.
         */
        mStopPubBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                if (mQeoserviceIntent != null) {
                    mStartState = true;
                    mCtx.stopService(mQeoserviceIntent);
                    mStartPubBtn.setEnabled(true);
                    mStopPubBtn.setEnabled(false);
                }
            }
        });
        return v;

    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.EXTRA_PUBLISH_STATE, mStartState);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mQeoserviceIntent != null) {
            mCtx.stopService(mQeoserviceIntent);
        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            boolean gen = intent.getBooleanExtra(getString(R.string.broadcast_general_failure), false);

            boolean flag = intent.getBooleanExtra(getString(R.string.broadcast_qeo_force_stop), false);
            if (flag) {
                mCtx.stopService(mQeoserviceIntent);
                mStartPubBtn.setEnabled(true);
                mStopPubBtn.setEnabled(false);
            }
            else if (!gen) {
                Log.d(GaugeWriterFragment.class.getSimpleName(), "error");
                String errMsg = intent.getStringExtra(getString(R.string.broadcast_qeo_fail_msg));
                if (errMsg != null && !errMsg.isEmpty()) {
                    Toast.makeText(getActivity(), "Qeo Service failed due to " + errMsg, Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "QeoService initialization failed, exiting!", Toast.LENGTH_LONG)
                        .show();
                }

                mCtx.stopService(mQeoserviceIntent);
                mStartPubBtn.setEnabled(true);
                mStopPubBtn.setEnabled(false);
                if (mReceiver != null) {
                    mCtx.unregisterReceiver(mReceiver);
                    mReceiver = null;
                }
                GaugeWriterFragment.this.getActivity().finish();
            }
        }
    };

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onDestroyView()
     */
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (mReceiver != null) {
            mCtx.unregisterReceiver(mReceiver);
        }
    }
}
