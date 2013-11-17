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

import org.qeo.sample.note.android.service.WallService;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 * Activity to manage WallService.
 *
 */
public class WallServiceDialog
        extends Dialog
{
    private EditText mDescriptionEditText;
    private Button mStartServiceButton;
    private boolean mServiceRunning;

    /**
     * @param context The context in which this dialog is called.
     */
    public WallServiceDialog(Context context)
    {
        super(context);
    }

    /**
     * This method is called when the start button of the service is clicked.
     *
     * @param v The view (button) that was clicked.
     */
    public void startServiceButtonClicked(View v)
    {
        String description = mDescriptionEditText.getText().toString();
        if (description.equals("")) {
            Toast.makeText(this.getContext(), R.string.wall_service_noempty, Toast.LENGTH_LONG).show();
        }
        else {
            final Intent serviceStartIntent = new Intent(getContext(), WallService.class);
            serviceStartIntent.putExtra(WallService.DESCRIPTION_KEY, description);
            getContext().startService(serviceStartIntent);
            Toast.makeText(getContext(), R.string.wall_service_starting, Toast.LENGTH_SHORT).show();
            this.dismiss();
        }
    }

    /**
     * Will be called when the Dialog is created.
     *
     * Initialization of the UI elements of the dialog and registering button callbacks.
     *
     * @param savedInstanceState the instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_service);
        this.setTitle(R.string.wall_service_dialog_title);
        mDescriptionEditText = (EditText) this.findViewById(R.id.activity_service_wall_description_edittext);
        mDescriptionEditText.addTextChangedListener(mDescriptionTextWatcher);
        mStartServiceButton = (Button) this.findViewById(R.id.activity_service_start_service_button);
        mStartServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startServiceButtonClicked(v);
            }
        });
        mStartServiceButton.setEnabled(false);
    }

    /**
     * Checks whether the description field has been changed.
     */
    private final TextWatcher mDescriptionTextWatcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (!s.toString().equals("")) {
                mStartServiceButton.setEnabled(!mServiceRunning);
            }
            else {
                mStartServiceButton.setEnabled(false);
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

}
