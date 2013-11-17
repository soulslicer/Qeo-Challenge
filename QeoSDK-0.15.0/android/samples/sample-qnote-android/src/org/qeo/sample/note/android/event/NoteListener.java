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

import org.qeo.sample.note.Note;

/**
 * Interface which should be implemented if you want to be notified when notes are added or removed.
 *
 */
public interface NoteListener
{
    /**
     * Will be called when a new note is added.
     *
     * @param note contains the note that was added
     */
    void onNoteAdded(Note note);

    /**
     * Will be called when a note is removed.
     *
     * @param note (Only the noteId will be filled in !)
     */
    void onNoteRemoved(Note note);
}
