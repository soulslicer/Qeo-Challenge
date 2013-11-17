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

import java.util.List;

import org.qeo.sample.note.Note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 *
 * The GridNoteAdapter is responsible for displaying the Note objects in a list view.
 *
 */
public class GridNoteAdapter
        extends ArrayAdapter<Note>
{
    private final Context mContext;
    private final OnClickListener mDeleteCallback;
    private final OnClickListener mMoveCallback;
    private final OnClickListener mEditCallback;

    /*
     * (non-Javadoc)
     *
     * @see android.widget.ArrayAdapter#add(java.lang.Object)
     */
    @Override
    public void add(Note object)
    {
        this.remove(object);
        super.add(object);
    }

    /**
     * Constructor of the GridNoteAdapter.
     *
     * @param context The context of the activity.
     * @param viewResourceId Resource id of the view.
     * @param objects A list of notes that is contained by the adapter.
     * @param deleteCallback A callback for when the delete button is clicked.
     * @param moveCallback A callback for when the move button is clicked.
     * @param editCallback A callback for when the edit button is clicked.
     */
    public GridNoteAdapter(Context context, int viewResourceId, List<Note> objects, OnClickListener deleteCallback,
        OnClickListener moveCallback, OnClickListener editCallback)
    {
        super(context, viewResourceId, objects);
        mContext = context;
        mDeleteCallback = deleteCallback;
        mMoveCallback = moveCallback;
        mEditCallback = editCallback;
    }

    /**
     * Wrapper class for the view.
     */
    public static class ViewHolder
    {
        /**
         * EditText containing the text of the note.
         */
        private TextView mGridCellPostitTextview;

        /**
         * The edit note button.
         */
        private ImageButton mGridCellPostitEditButton;

        /**
         * The remove note button.
         */
        private ImageButton mGridCellPostitRemoveButton;

        /**
         * The move note button.
         */
        private ImageButton mGridCellPostitMoveButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        final ViewHolder holder;

        if (v == null) {
            final LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.grid_cell, null);
            holder = new ViewHolder();
            holder.mGridCellPostitTextview = (TextView) v.findViewById(R.id.grid_cell_postit_textview);
            holder.mGridCellPostitEditButton = (ImageButton) v.findViewById(R.id.grid_cell_postit_edit_button);
            holder.mGridCellPostitRemoveButton = (ImageButton) v.findViewById(R.id.grid_cell_postit_remove_button);
            holder.mGridCellPostitMoveButton = (ImageButton) v.findViewById(R.id.grid_cell_postit_move_button);
            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) v.getTag();
        }

        final Note note = getItem(position);

        if (note != null) {
            holder.mGridCellPostitTextview.setText(note.message);
            holder.mGridCellPostitRemoveButton.setOnClickListener(mDeleteCallback);
            holder.mGridCellPostitRemoveButton.setTag(note);

            holder.mGridCellPostitEditButton.setOnClickListener(mEditCallback);
            holder.mGridCellPostitEditButton.setTag(note);

            holder.mGridCellPostitMoveButton.setOnClickListener(mMoveCallback);
            holder.mGridCellPostitMoveButton.setTag(note);
        }

        return v;
    }

    /**
     * Sets a new list of notes to be displayed.
     *
     * @param notes The list of notes to be displayed.
     */
    public void setNotes(List<Note> notes)
    {
        clear();
        for (Note note : notes) {
            add(note);
        }
        notifyDataSetChanged();
    }

}
