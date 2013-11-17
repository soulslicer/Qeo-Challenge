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

package org.qeo.sample.note.android;

import android.app.Application;

/**
 * The QNote application class can be used to keep the QNoteClientService alive when we leave the activity. This
 * is currently not done.
 */
public class QNoteApplication
        extends Application
{
    /**
     * Tag used for logging.
     */
    public static final String QNOTE_TAG = "org.qeo.sample.note.QNOTE";
}
