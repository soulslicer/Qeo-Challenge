/************** COPYRIGHT AND CONFIDENTIALITY INFORMATION *********************
 **                                                                          **
 ** Copyright (c) 2012 Technicolor                                           **
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

package org.qeo.sample.note.android.service;

import java.io.IOException;
import java.util.Random;

import org.qeo.QeoFactory;
import org.qeo.android.QeoAndroid;
import org.qeo.android.QeoConnectionListener;
import org.qeo.exception.QeoException;
import org.qeo.sample.note.Wall;
import org.qeo.sample.note.WallHandler;
import org.qeo.sample.note.android.QNoteApplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * Creates a wall and creates a WallHandler instance to manage the wall. Can be triggered by an Intent from
 * QNoteWallServiceActivity.
 * 
 * This is a started service. The wall should stay active even though the client activity is not active. We don't want
 * the wall to disappear and lose all its notes just because the user wants to do something else.
 * 
 */
public class WallService
        extends Service
{
    /**
     * The DESCRIPTION_KEY is used for passing the description to the service. It is optional, if no description is
     * given it will just pass 'Default' as its description.
     */
    public static final String DESCRIPTION_KEY = "org.qeo.note.description";

    private WallHandler mWallService;
    private String mDescription = "Default";
    private QeoFactory mQeo;

    /**
     * Initializes the wall. First it will pick a random ID for the wall and a given description. This description is
     * provided through the intent that starts this service.
     */
    private void initWall()
    {
        // Initialize the Wall
        final Wall wall = new Wall();
        wall.description = mDescription;
        wall.id = new Random().nextInt(Integer.MAX_VALUE);
        try {
            mWallService = new WallHandler(mQeo, wall);
        }
        catch (final QeoException e) {
            Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
        }
    }

    /**
     * When the Wall Service is stopped this method will be called. The WallHandler will be closed and the connection
     * with the Qeo service will be broken.
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        /*
         * As the WallHandler is started asynchronously, there is a small time window after onStartCommand() that
         * fWallService is null.
         */
        try {
            if (mWallService != null) {
                mWallService.close();
            }
        }
        catch (IOException e) {
            Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
        }
        mQeo.close();
    }

    /**
     * This service is a started service and will be started by an intent. We will use the intent to pass the
     * Description of the wall to the service before it starts.
     * 
     * {@inheritDoc}
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        if (intent.getExtras() != null) {
            final Object extra = intent.getExtras().get(DESCRIPTION_KEY);
            if (extra != null) {
                final String tmp = (String) extra;
                if (!tmp.equals("")) {
                    mDescription = (String) extra;
                }
            }
        }

        QeoAndroid.initQeo(getApplicationContext(), new QeoConnectionListener() {
            /**
             * When Qeo becomes ready (the qeo-android-service has been bound to this service) this callback will be
             * called.
             */
            @Override
            public void onQeoReady(QeoFactory qeo)
            {
                mQeo = qeo;
                initWall();
            }

            @Override
            public void onQeoClosed(QeoFactory qeo)
            {
                super.onQeoClosed(qeo);
                mQeo = null;
            }

            @Override
            public void onQeoError(QeoException e)
            {
                Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
            }
        });

        // We do not want this service to restart itself after getting killed.
        // We will lose the notes anyway if the service is restarted.
        return START_NOT_STICKY;
    }

    /**
     * We use an unbounded started service to control the Wall. We do not need to bind so it just returns null.
     * 
     * @param intent the intent.
     * 
     * @return always null. We do not bind to this service so it's not needed.
     */
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
