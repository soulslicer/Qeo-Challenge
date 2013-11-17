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

import org.qeo.sample.gauge.android.writer.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * 
 * GaugeWriterActivity displays the UI to start/stop the Writer. <br/>
 * The writer will keep on running even if the application is sent to background.
 */
public class GaugeWriterActivity
        extends FragmentActivity
{
    private static final String FRAGMENT_NAME = "WriterUIFragment";
    private GaugeWriterFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaugewriter_layout);

        if (savedInstanceState == null) {
            mFragment = new GaugeWriterFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.writer_container, mFragment, FRAGMENT_NAME)
                    .commit();
        }
        else {
            // Do Nothing
        }
    }
}
