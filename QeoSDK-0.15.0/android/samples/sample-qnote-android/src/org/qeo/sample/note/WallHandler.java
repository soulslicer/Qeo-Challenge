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

package org.qeo.sample.note;

import java.io.IOException;

import org.qeo.DefaultEventReaderListener;
import org.qeo.EventReader;
import org.qeo.EventReaderListener;
import org.qeo.QeoFactory;
import org.qeo.StateReader;
import org.qeo.StateWriter;
import org.qeo.exception.QeoException;

/**
 * Basic implementation of a wall handler.<br/>
 * <br/>
 * The main responsibility of the WallHandler is to publish a Wall and to handle NoteRequests.<br/>
 * <br/>
 * A NoteRequest can be used to do several actions:<br/>
 * <ul>
 * <li>
 * Create a new note.</li>
 * <li>
 * Modify an existing note.</li>
 * <li>
 * Move a note from one wall to another.</li>
 * <li>
 * Delete a note from a wall.</li>
 * </ul>
 * <b>Overview of flow of NoteRequests:</b><br/>
 * The wall receives a NoteRequest with id N and wallId W:<br>
 * <ul>
 * <li>if W is the receiving wall:</li>
 * <ul>
 * <li>The wall doesn't have a note with id N yet: <b>Create</b></li>
 * <li>The wall has a note with id N: <b>Modify</b></li>
 * </ul>
 * <li>if W is not the receiving wall:</li>
 * <ul>
 * <li>The wall doesn't have a note with id N yet: <b>Ignore</b></li>
 * <li>The wall has a note with id N: <b>Delete</b></li>
 * </ul>
 * </ul>
 */
public class WallHandler
{

    private final Wall mWall;
    private final EventReader<NoteRequest> mNoteRequestReader;
    private final StateReader<Note> mNoteReader;
    private final StateWriter<Note> mNoteWriter;
    private final StateWriter<Wall> mWallWriter;

    private final EventReaderListener<NoteRequest> mNoteRequestListener =
            new DefaultEventReaderListener<NoteRequest>() {
                /**
                 * Function will be triggered when somebody has issued a new NoteRequest. This method contains the logic
                 * to determine which of the actions to perform.
                 */
                @Override
                public void onData(NoteRequest t)
                {
                    if (t.wallId != mWall.id) {
                        // This note request is not addressed to our wall.
                        final Note note = findNote(t.id);
                        if (note != null) {
                            // We are currently owner of this note.
                            // Type = Deletion
                            note.wallId = mWall.id;
                            mNoteWriter.remove(note);
                        }
                        // Type = Ignore
                        return;
                    }
                    else {
                        // This note request is addressed to our wall.
                        final Note note = findNote(t.id);
                        if (note == null) {
                            // The note was not found in our own wall
                            // Type = Creation
                            final Note newNote = new Note();
                            newNote.id = t.id;
                            newNote.wallId = t.wallId;
                            newNote.message = t.message;
                            mNoteWriter.write(newNote);
                        }
                        else {
                            // The note already exists in our own wall.
                            // Type = Modification
                            note.message = t.message;
                            note.wallId = t.wallId;
                            mNoteWriter.write(note);
                        }
                    }

                }
            };

    /**
     * Finds a note with a given note id which matches the wall of this WallHandler.
     * 
     * @param id The note id.
     * @return null If not found.
     */
    public Note findNote(int id)
    {
        for (final Note note : mNoteReader) {
            if (note.wallId == mWall.id && note.id == id) {
                return note;
            }
        }
        return null;
    }

    /**
     * Constructs a new WallHandler, handling the supplied wall.
     * 
     * @param qeo Factory which will be used to construct Qeo reader and writers.
     * @param wall The wall this WallHandler will manage.
     * @throws QeoException Exception that will be thrown when no reader or write can be created; or when writing fails.
     */
    public WallHandler(QeoFactory qeo, Wall wall)
        throws QeoException
    {
        this.mWall = wall;

        mWallWriter = qeo.createStateWriter(Wall.class);
        mNoteReader = qeo.createStateReader(Note.class, null);
        mNoteRequestReader = qeo.createEventReader(NoteRequest.class, mNoteRequestListener);
        mNoteWriter = qeo.createStateWriter(Note.class);

        mWallWriter.write(wall);

    }

    /**
     * Closes all Qeo communication channels.
     * 
     * @throws IOException Exception which is thrown when reader or writer cannot be closed.
     */
    public void close()
        throws IOException
    {

        mNoteRequestReader.close();
        mNoteWriter.close();
        mWallWriter.close();
        mNoteReader.close();

    }

}
