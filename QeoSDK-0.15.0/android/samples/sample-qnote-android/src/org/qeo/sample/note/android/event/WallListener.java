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

package org.qeo.sample.note.android.event;

import org.qeo.sample.note.Wall;

/**
 * Interface which should be implemented if the user wants to be notified if new walls are added or walls are removed.
 * 
 */
public interface WallListener
{
    /**
     * 
     * Triggered when a new Wall is added.
     * 
     * @param wall the wall that was added
     */
    void onWallAdded(Wall wall);

    /**
     * 
     * Triggered when a Wall is removed.
     * 
     * @param wall (Only the wallId will be filled in !)
     */
    void onWallRemoved(Wall wall);
}
