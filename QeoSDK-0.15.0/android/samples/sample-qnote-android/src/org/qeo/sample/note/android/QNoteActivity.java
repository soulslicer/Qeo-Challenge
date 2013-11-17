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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.qeo.exception.QeoException;
import org.qeo.sample.note.Note;
import org.qeo.sample.note.NoteRequest;
import org.qeo.sample.note.Wall;
import org.qeo.sample.note.WallDecorator;
import org.qeo.sample.note.android.event.NoteListener;
import org.qeo.sample.note.android.event.WallListener;
import org.qeo.sample.note.android.service.ClientService;
import org.qeo.sample.note.android.service.ClientService.LocalBinder;
import org.qeo.sample.note.android.service.WallService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * 
 * Activity which shows published notes and has a UI to request new notes.
 * 
 */
public class QNoteActivity
    extends Activity
    implements NoteListener, WallListener
{

    private ClientService mService;
    private boolean mClientServiceReady = false;
    private boolean mBound = false;
    private boolean mBinding = false;
    private ImageButton mCreateNoteButton;
    private ImageButton mRefreshWallButton;
    private ImageButton mAddWallButton;
    private ImageButton mDeleteWallButton;
    private GridView mNotesGridView;
    private GridNoteAdapter mNotesGridViewAdapter;
    private EditText mNoteEditText;
    private Spinner mWallSpinner;
    private ArrayAdapter<WallDecorator> mWallSpinnerAdapter;
    private Wall mSelectedWall;

    /**
     * Defines callbacks for service binding, passed to bindService().
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service)
        {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            final LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if (mService != null) {
                if (mClientServiceReady) {
                    onServiceReady();
                }
            }
            else {
                mBound = false;
                Toast.makeText(QNoteActivity.this, R.string.service_not_found, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0)
        {
            mBound = false;
        }
    };

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            mClientServiceReady = intent.getBooleanExtra(ClientService.OK, false);
            if (mClientServiceReady) {
                if (mService != null) {
                    onServiceReady();
                }
            }
            else {
                String errMsg = intent.getStringExtra(ClientService.MESSAGE);
                if (errMsg != null && !errMsg.isEmpty()) {
                    Toast.makeText(QNoteActivity.this, "Qeo Service failed due to " + errMsg, Toast.LENGTH_LONG).show();
                }
                else

                {
                    Toast.makeText(QNoteActivity.this, "QeoService initialization failed, exiting!", Toast.LENGTH_LONG)
                        .show();
                }

                finish();
            }
        }
    };

    /**
     * A listener for when a user clicks on the remove button of a Note.
     */
    private final OnClickListener mOnRemoveButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view)
        {
            final Note note = (Note) view.getTag();
            try {
                // We pick -1 as a wall id to pick a nonexistent wall but this could be any integer that is not used by
                // a wall.
                NoteRequest req = new NoteRequest();
                req.id = note.id;
                req.wallId = -1;
                req.message = "";
                mService.noteRequest(req);
            }
            catch (final QeoException e) {
                Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
            }
        }
    };

    /**
     * A listener for when a user clicks on the edit button of a Note.
     */
    private final OnClickListener mOnEditButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view)
        {
            final Note note = (Note) view.getTag();
            final EditText input = new EditText(QNoteActivity.this);
            input.setText(note.message);

            // Shows an Dialog with text input.
            new AlertDialog.Builder(QNoteActivity.this).setTitle("Update Status").setMessage("Modify note")
                .setView(input).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        String value = input.getText().toString();
                        try {
                            // Send the note request for a modification.
                            NoteRequest req = new NoteRequest();
                            req.id = note.id;
                            req.wallId = note.wallId;
                            req.message = value;
                            mService.noteRequest(req);
                        }
                        catch (final QeoException e) {
                            Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        // Do nothing.
                    }
                }).show();
        }
    };

    /**
     * A listener for when a user clicks on the move button of a Note.
     */
    private final OnClickListener mOnMoveButtonClickListener = new OnClickListener() {
        @Override
        public void onClick(View view)
        {
            final Note note = (Note) view.getTag();
            final MoveDialog dialog = new MoveDialog(QNoteActivity.this, mWallSpinnerAdapter);

            dialog.setOkClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Wall selectedWall = dialog.getResult();
                    try {
                        // Send a note request for a move.
                        NoteRequest req = new NoteRequest();
                        req.id = note.id;
                        req.wallId = selectedWall.id;
                        req.message = note.message;
                        mService.noteRequest(req);
                        Toast.makeText(getApplicationContext(),
                            getString(R.string.move_note_toast) + " '" + selectedWall.description + "'",
                            Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                    catch (final QeoException e) {
                        Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
                    }
                }
            });
            dialog.show();
        }
    };

    /**
     * This method is called when the refresh wall button is clicked.
     * 
     * @param v The view that was clicked.
     */
    public void refreshWallButtonClicked(View v)
    {
        final Wall selectedWall = (Wall) mWallSpinner.getSelectedItem();
        if (selectedWall != null) {
            List<Note> notes = mService.getNotes(selectedWall);
            List<Wall> walls = mService.getWalls();
            // First clear the adapter and then add the wall one by one.
            // Why not use addAll? It's not supported in API level 9.
            mWallSpinnerAdapter.clear();
            for (Wall wall : walls) {
                mWallSpinnerAdapter.add(new WallDecorator(wall));
            }
            mWallSpinnerAdapter.notifyDataSetChanged();

            // Update the notes. The dataset invalidation is done int he adapter itself.
            mNotesGridViewAdapter.setNotes(notes);
            Toast.makeText(getApplicationContext(), R.string.refresh_walls_toast, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This method is called when the add wall button is clicked.
     * 
     * @param v The view that was clicked.
     */
    public void addWallButtonClicked(View v)
    {
        final WallServiceDialog dialog = new WallServiceDialog(this);
        dialog.show();
    }

    /**
     * This method is called when the delete wall button is clicked.
     * 
     * @param v The view that was clicked.
     */
    public void deleteWallButtonClicked(View v)
    {
        final Intent serviceStopIntent = new Intent(getApplicationContext(), WallService.class);
        getApplicationContext().stopService(serviceStopIntent);
        Toast.makeText(getApplicationContext(), R.string.wall_service_stopping, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is called when the create note button is clicked.
     * 
     * @param v The view that was clicked.
     */
    public void createNoteButtonClicked(View v)
    {
        final String noteMessage = mNoteEditText.getText().toString();
        if (noteMessage.equals("")) {
            Toast.makeText(this, R.string.cannot_publish_empty_notes, Toast.LENGTH_LONG).show();
        }
        else {
            final Wall wall = (Wall) mWallSpinner.getSelectedItem();
            NoteRequest noteRequest = new NoteRequest();
            noteRequest.id = new Random().nextInt(Integer.MAX_VALUE);
            noteRequest.wallId = wall.id;
            noteRequest.message = noteMessage;
            try {
                mService.noteRequest(noteRequest);
            }
            catch (final QeoException e) {
                Log.e(QNoteApplication.QNOTE_TAG, e.getMessage(), e);
            }
            mNoteEditText.setText("");
        }
    }

    private final OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int arg2, long arg3)
        {
            final Spinner spin = (Spinner) adapterView;
            final Wall wall = (Wall) spin.getSelectedItem();
            mSelectedWall = wall;
            mNotesGridViewAdapter.setNotes(mService.getNotes(wall));
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0)
        {
        }

    };

    /**
     * Adds new wall to the spinner. Enables button to create notes.
     * 
     * @param wall The wall that was added.
     */
    @Override
    public void onWallAdded(Wall wall)
    {
        WallDecorator wallDecorator = new WallDecorator(wall);
        if (mWallSpinnerAdapter.getPosition(wallDecorator) < 0) {
            mWallSpinnerAdapter.add(wallDecorator);
        }

        // Enable the create button if the message is not empty.
        mCreateNoteButton.setEnabled(!mNoteEditText.getText().toString().equals(""));
        if (isMyServiceRunning()) {
            mDeleteWallButton.setEnabled(true);
            mAddWallButton.setEnabled(false);
        }
    }

    /**
     * Removes wall from the spinner. Disables button to create notes if no walls are left.
     * 
     * @param wall the wall that was removed.
     */
    @Override
    public void onWallRemoved(Wall wall)
    {
        if (!isMyServiceRunning()) {
            mDeleteWallButton.setEnabled(false);
            mAddWallButton.setEnabled(true);
        }
        mWallSpinnerAdapter.remove(new WallDecorator(wall));
        if (mWallSpinnerAdapter.getCount() == 0) {
            // Enable the create button if the message is not empty.
            mCreateNoteButton.setEnabled(false);
        }
        else {
            // We need to check if the wall that was removed was the selected wall.
            // If it is, we select the first wall by default.
            if (mSelectedWall.id == wall.id) {
                mWallSpinner.setSelection(0);
                mSelectedWall = mWallSpinnerAdapter.getItem(0);
                mNotesGridViewAdapter.setNotes(mService.getNotes(mSelectedWall));
            }
        }
    }

    /**
     * Show the note when it's added.
     * 
     * @param note The note that was added.
     */
    @Override
    public void onNoteAdded(Note note)
    {
        final WallDecorator selectedWall = (WallDecorator) mWallSpinner.getSelectedItem();
        if (selectedWall != null) {
            if (note.wallId == selectedWall.id) {
                mNotesGridViewAdapter.add(note);
            }
        }
    }

    /**
     * No longer show the note when it is removed.
     * 
     * @param note The note that was removed.
     */
    @Override
    public void onNoteRemoved(Note note)
    {
        mNotesGridViewAdapter.remove(note);
    }

    /**
     * When the service is ready, add listeners, configure UI.
     */
    protected void onServiceReady()
    {
        if (isMyServiceRunning()) {
            mDeleteWallButton.setEnabled(true);
            mAddWallButton.setEnabled(false);
        }
        else {
            mDeleteWallButton.setEnabled(false);
            mAddWallButton.setEnabled(true);
        }

        mService.addNoteListener(QNoteActivity.this);
        mService.addWallListener(QNoteActivity.this);

        ArrayList<WallDecorator> walls = new ArrayList<WallDecorator>();
        for (Wall wall : mService.getWalls()) {
            walls.add(new WallDecorator(wall));
        }

        final Wall selectedWall = (Wall) mWallSpinner.getSelectedItem();
        ArrayList<Note> notes = new ArrayList<Note>();
        if (selectedWall != null) {
            notes.addAll(mService.getNotes(selectedWall));
        }

        mWallSpinnerAdapter =
            new ArrayAdapter<WallDecorator>(QNoteActivity.this, R.layout.wall_spinner_item,
                R.id.wall_spinner_item_description, walls);
        mNotesGridViewAdapter =
            new GridNoteAdapter(QNoteActivity.this, R.layout.grid_cell, notes, mOnRemoveButtonClickListener,
                mOnMoveButtonClickListener, mOnEditButtonClickListener);

        mWallSpinner.setAdapter(mWallSpinnerAdapter);
        mNotesGridView.setAdapter(mNotesGridViewAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCreateNoteButton = (ImageButton) this.findViewById(R.id.activity_main_create_note);
        mCreateNoteButton.setEnabled(false);

        mAddWallButton = (ImageButton) this.findViewById(R.id.add_wall_button);
        mAddWallButton.setEnabled(!isMyServiceRunning());
        mDeleteWallButton = (ImageButton) this.findViewById(R.id.delete_wall_button);
        mDeleteWallButton.setEnabled(isMyServiceRunning());
        mRefreshWallButton = (ImageButton) this.findViewById(R.id.refresh_wall_button);
        mRefreshWallButton.setEnabled(true);

        mNotesGridView = (GridView) this.findViewById(R.id.activity_main_gridview);
        mNoteEditText = (EditText) this.findViewById(R.id.activity_main_note_edittext);
        mNoteEditText.addTextChangedListener(mMessageTextWatcher);
        mWallSpinner = (Spinner) this.findViewById(R.id.activity_main_wall_spinner);
        mWallSpinner.setOnItemSelectedListener(mOnItemSelectedListener);
    }

    /**
     * This is a listener that is attached to the message edittext field. It will check if the message field is empty.
     * If it is empty it will disable the create note button.
     */
    private final TextWatcher mMessageTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (!s.toString().equals("")) {
                mCreateNoteButton.setEnabled(mWallSpinner.getCount() > 0);
            }
            else {
                mCreateNoteButton.setEnabled(false);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
        }

        @Override
        public void afterTextChanged(Editable s)
        {
        }
    };

    /**
     * This method uses the ActivityManager to determine if the WallService is running or not.
     * 
     * @return Whether the service is running or not.
     */
    private boolean isMyServiceRunning()
    {
        final ActivityManager manager =
            (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (final RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (WallService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Called when the activity is becoming visible to the user.
     * 
     * We bind to the Client Service in the onStart method instead of the onCreate. We do this because onCreate is only
     * called once in the Activity lifecycle and since we want to unbind from the service when the activity is not
     * visible and bind again when it is visible, we use the onStart instead of onCreate.
     * 
     * @see Activity.onStart()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        // Bind to LocalService
        registerReceiver(mReceiver, new IntentFilter(ClientService.SERVICE_STATUS));
        final Intent intent = new Intent(getApplicationContext(), ClientService.class);
        mBinding = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Called when the activity is becoming invisible to the user.
     * 
     * @see QNoteActivity.onStart()
     */
    @Override
    protected void onStop()
    {
        super.onStop();

        // Unbind from the service
        if (mBinding || mBound) {
            unbindService(mConnection);
            mBound = false;
        }
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mConnection = null;
    }
}
