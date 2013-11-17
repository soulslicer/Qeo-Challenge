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

import org.qeo.sample.note.Wall;
import org.qeo.sample.note.WallDecorator;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 *
 * Activity to manage WallService.
 *
 */
public class MoveDialog
        extends Dialog
{

    private final ArrayAdapter<WallDecorator> mWallSpinnerAdapter;
    private View.OnClickListener mOkOnClickListener;
    private Spinner mWallSpinner;
    private Button mOkButton;
    private Button mCancelButton;

    /**
     * @param context The calling context.
     * @param wallAdapter The adapter which contains the walls that need to be displayed.
     */
    public MoveDialog(Context context, ArrayAdapter<WallDecorator> wallAdapter)
    {
        super(context);
        mWallSpinnerAdapter = wallAdapter;
    }

    /**
     * @param okOnClickListener A listener to be used when the OK button is clicked.
     */
    public void setOkClickListener(View.OnClickListener okOnClickListener)
    {
        mOkOnClickListener = okOnClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_move);
        this.setTitle("Destination Wall");

        mWallSpinner = (Spinner) findViewById(R.id.dialog_move_wall_spinner);
        mOkButton = (Button) findViewById(R.id.dialog_move_ok_button);
        mCancelButton = (Button) findViewById(R.id.dialog_move_cancel_button);

        if (mOkOnClickListener != null) {
            mOkButton.setOnClickListener(mOkOnClickListener);
        }

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MoveDialog.this.cancel();
            }
        });

        mWallSpinner.setAdapter(mWallSpinnerAdapter);

    }

    /**
     * @return The selected wall in the wall spinner.
     */
    public Wall getResult()
    {
        return (Wall) mWallSpinner.getSelectedItem();
    }
}
