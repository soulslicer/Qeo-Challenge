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
import java.util.ArrayList;
import java.util.List;

import org.qeo.DefaultStateChangeReaderListener;
import org.qeo.QeoFactory;
import org.qeo.StateChangeReaderListener;
import org.qeo.android.QeoAndroid;
import org.qeo.android.QeoConnectionListener;
import org.qeo.exception.QeoException;
import org.qeo.sample.note.Note;
import org.qeo.sample.note.NoteClient;
import org.qeo.sample.note.NoteRequest;
import org.qeo.sample.note.Wall;
import org.qeo.sample.note.android.QNoteApplication;
import org.qeo.sample.note.android.event.NoteListener;
import org.qeo.sample.note.android.event.WallListener;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * Service which can serve multiple NoteListeners and WallListeners. Intermediate layer between UI and Qeo-logic. Uses
 * NoteClient from java project.
 * 
 * This is a bound service. The client Service should only be active when the activity is shown to the user.<br/>
 * <br/>
 * <b>Note:</b> If one wants to provide the capability to listen in the background for new notes, this service needs to
 * become a started service.
 * 
 */
public class ClientService
        extends Service
{

    /**
     * SERVICE_STATUS intent identification.
     */
    public static final String SERVICE_STATUS = "org.qeo.sample.note.android.service.STATUS";

    /**
     * Extra information for the SERVICE_STATUS intent.
     */
    public static final String OK = "OK";
    /**
     * Extra message information for the SERVICE_STATUS intent.
     */
    public static final String MESSAGE = "";

    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private NoteClient mClient;
    private QeoFactory mQeo;

    private final List<NoteListener> mNoteListeners = new ArrayList<NoteListener>();
    private final List<WallListener> mWallListeners = new ArrayList<WallListener>();

    /**
     * This listener is triggered whenever a new note is added or a note has been removed.
     */
    private final StateChangeReaderListener<Note> mNoteListener = new DefaultStateChangeReaderListener<Note>() {
        /**
         * Will be called when a Note is added.
         * 
         * @param t added Note
         */
        @Override
        public void onData(Note t)
        {
            for (final NoteListener listener : mNoteListeners) {
                listener.onNoteAdded(t);
            }
        };

        /**
         * Will be called when a Note is removed.
         * 
         * @param t removed Note (only the noteId will be filled in !)
         */
        @Override
        public void onRemove(Note t)
        {
            for (final NoteListener listener : mNoteListeners) {
                listener.onNoteRemoved(t);
            }
        };
    };

    /**
     * This listener is triggered whenever a new wall is added or a wall has been removed (for example when the service
     * hosting the wall gets closed).
     */
    private final StateChangeReaderListener<Wall> mWallListener = new DefaultStateChangeReaderListener<Wall>() {
        @Override
        public void onData(Wall w)
        {
            for (final WallListener listener : mWallListeners) {
                listener.onWallAdded(w);
            }
        };

        @Override
        public void onRemove(Wall w)
        {
            for (final WallListener listener : mWallListeners) {
                listener.onWallRemoved(w);
            }
        };
    };

    /**
     * Adds a new wall listener.
     * 
     * @param theWallListener The new wall listener.
     */
    public void addWallListener(WallListener theWallListener)
    {
        mWallListeners.add(theWallListener);
    }

    /**
     * Adds a new note listener.
     * 
     * @param theNoteListener The new note listener.
     */
    public void addNoteListener(NoteListener theNoteListener)
    {
        mNoteListeners.add(theNoteListener);
    }

    /**
     * Uses the note client to send a note request event to the wall service.
     * 
     * @param theNoteRequest The note request
     * @throws QeoException this exception is thrown if the qeo service fails to send the note request.
     */
    public void noteRequest(NoteRequest theNoteRequest)
        throws QeoException
    {
        if (mClient != null) {
            mClient.noteRequest(theNoteRequest);
        }
    }

    /**
     * Class used for the client Binder. Because we know this service always runs in the same process as its clients, we
     * don't need to deal with IPC.
     */
    public class LocalBinder
            extends Binder
    {
        /**
         * Is used to return a binding to the QNoteClientService.
         * 
         * @return The QNoteClientService
         */
        public ClientService getService()
        {
            return ClientService.this;
        }
    }

    /**
     * The init method must be called when the Qeo Service has been initialized.
     */
    public void init()
    {
        try {
            mClient = new NoteClient(mQeo, mNoteListener, mWallListener);
        }
        catch (final QeoException e) {
            Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
        }
    }

    /**
     * The init method must be called when the Qeo Service has been initialized.
     * @param message - Message to be sent to registered receiver.
     */
    public void broadcastStatus(String message)
    {
        Intent intent = new Intent(SERVICE_STATUS);
        if (mQeo == null) {
            intent.putExtra(OK, false);
            intent.putExtra(MESSAGE, message);
        }
        else {
            intent.putExtra(OK, true);
        }
        sendBroadcast(intent);
    }

    /**
     * The onCreate method will be called when the Client Service is started (after the client binds to it).
     * 
     * Qeo initialization will happen here.
     */
    @Override
    public void onCreate()
    {
        super.onCreate();
        QeoAndroid.initQeo(getApplicationContext(), new QeoConnectionListener() {
            @Override
            public void onQeoReady(QeoFactory qeo)
            {
                Log.i(QNoteApplication.QNOTE_TAG, "Client service : onQeoReady");
                mQeo = qeo;
                init();
                broadcastStatus(null);
            }

            /*
             * (non-Javadoc)
             * 
             * @see org.qeo.android.QeoConnectionListener#onQeoClosed(org.qeo.QeoFactory)
             */
            @Override
            public void onQeoClosed(QeoFactory qeo)
            {
                Log.i(QNoteApplication.QNOTE_TAG, "QeoAndroid.initQeo.onQeoClosed");
                super.onQeoClosed(qeo);
                mQeo = null;
                broadcastStatus(null);
            }

            @Override
            public void onQeoError(QeoException e)
            {
                Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
                broadcastStatus(e.getMessage());
            }
        });
    }

    /**
     * The onDestroy method will only be called when there are no more applications bound to this service. When this
     * happens it is safe to close the connection with the Qeo service and do cleanup.
     * 
     * Since we currently use a one-to-one binding between the activity and the ClientService, the cleanup will be done
     * when the activity goes into the background.
     */
    @Override
    public void onDestroy()
    {
        if (mClient != null) {
            try {
                mClient.close();
            }
            catch (IOException e) {
                Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
            }
        }
        if (mQeo != null) {
            mQeo.close();
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return mBinder;
    }

    /**
     * Gets the notes that are currently present.
     * 
     * @return The list of notes.
     */
    public List<Note> getNotes()
    {
        if (null != mClient) {
            return mClient.getNotes();
        }
        return new ArrayList<Note>();
    }

    /**
     * Gets the notes that are currently present and which belong to a specific wall.
     * 
     * @param wall The wall.
     * @return The list of notes belonging to the wall.
     */
    public List<Note> getNotes(Wall wall)
    {
        List<Note> notes = new ArrayList<Note>();
        for (Note note : getNotes()) {
            if (note.wallId == wall.id) {
                notes.add(note);
            }
        }
        return notes;
    }

    /**
     * Gets the walls from the mClient that are currently present.
     * 
     * @return The list of walls.
     */
    public List<Wall> getWalls()
    {
        if (null != mClient) {
            return mClient.getWalls();
        }
        return new ArrayList<Wall>();
    }

}
