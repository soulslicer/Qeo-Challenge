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
import java.util.ArrayList;
import java.util.List;

import org.qeo.EventWriter;
import org.qeo.QeoFactory;
import org.qeo.StateChangeReader;
import org.qeo.StateChangeReaderListener;
import org.qeo.StateReader;
import org.qeo.exception.QeoException;

/**
 * The NoteClient class can be used to issue note requests. It has the responsibility to manage the Qeo reader and
 * writers.
 * 
 * Note Requests can be used to create, update, move and delete notes from a wall.
 */
public class NoteClient
{

    /**
     * Qeo StateReader for Note objects.
     */
    private final StateReader<Note> mNoteReader;

    /**
     * Qeo StateReader for Wall objects.
     */
    private final StateReader<Wall> mWallReader;

    /**
     * Qeo StateChangeReader for Note objects.
     */
    private final StateChangeReader<Note> mNoteChangeReader;

    /**
     * Qeo StateReader for Wall objects.
     */
    private final StateChangeReader<Wall> mWallChangeReader;

    /**
     * Qeo EventWriter to publish requests to make a wall :<br/>
     * <ul>
     * <li>create a note.</li>
     * <li>modify a note.</li>
     * <li>delete a note.</li>
     * </ul>
     */
    private final EventWriter<NoteRequest> mRequestWriter;

    /**
     * Constructs a new NoteClient.
     * 
     * @param qeo Qeo factory to be used to instantiate new Qeo readers and writers.
     * @param noteListener Listener object which is used when a new Note is published or removed.
     * @param wallListener Listener object which is used when a new Wall is published or removed.
     * @throws QeoException Exception which is thrown if readers or writers could not be created.
     */
    public NoteClient(QeoFactory qeo, StateChangeReaderListener<Note> noteListener,
        StateChangeReaderListener<Wall> wallListener)
        throws QeoException
    {
        mNoteReader = qeo.createStateReader(Note.class, null);
        mNoteChangeReader = qeo.createStateChangeReader(Note.class, noteListener);
        mWallReader = qeo.createStateReader(Wall.class, null);
        mWallChangeReader = qeo.createStateChangeReader(Wall.class, wallListener);
        mRequestWriter = qeo.createEventWriter(NoteRequest.class);
    }

    /**
     * Sends a NoteRequest event.
     * 
     * For more information about the construction of this request. See {@link NoteRequest}.
     * 
     * @param noteRequest Information regarding the note request to create.
     * @throws QeoException Exception which is thrown if request could be written.
     */
    public void noteRequest(NoteRequest noteRequest)
        throws QeoException
    {
        mRequestWriter.write(noteRequest);
    }

    /**
     * Gets all the known Notes from the note reader.
     * 
     * @return list of notes.
     */
    public List<Note> getNotes()
    {
        List<Note> notes = new ArrayList<Note>();
        if (mNoteReader != null) {
            for (Note note : mNoteReader) {
                notes.add(note);
            }
        }
        return notes;
    }

    /**
     * Gets all the known Walls from the wall reader.
     * 
     * @return list of walls.
     */
    public List<Wall> getWalls()
    {
        List<Wall> walls = new ArrayList<Wall>();
        if (mWallReader != null) {
            for (Wall wall : mWallReader) {
                walls.add(wall);
            }
        }
        return walls;
    }

    /**
     * Closes all Qeo communication channels.
     * 
     * @throws IOException Exception which is thrown if Qeo reader or writer could not be closed.
     */
    public void close()
        throws IOException
    {

        mNoteReader.close();
        mWallReader.close();
        mNoteChangeReader.close();
        mWallChangeReader.close();
        mRequestWriter.close();

    }

}
