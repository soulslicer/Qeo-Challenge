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
package org.qeo.sample.gauge.android.reader.interfaces;

import java.util.Date;

/**
 * LogMessage interface needs to be implemented for custom log messages.
 */
public interface LogMessage
{
    /**
     * @return returns the type of log message.
     */
    String getType();

    /**
     * @return returns the content of log message.
     */
    String getContent();

    /**
     * @return returns the timestamp of log message.
     */
    Date getTimestamp();

}
