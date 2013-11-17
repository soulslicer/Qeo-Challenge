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
package org.qeo.sample.gauge.android.reader;

import java.util.ArrayList;

import org.qeo.sample.gauge.android.reader.interfaces.Device;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * GaugeReaderAdapter displays the list of all publisher devices publishing traffic stats over Qeo.
 *
 *
 */
public class GaugeReaderAdapter
        extends ArrayAdapter<Device>
{
    private final Context mContext;
    private final int mTextViewResourceId;

    /**
     *
     * @param context Activitycontext
     * @param textViewResourceId resourceid of textview
     * @param devices List of devices
     */
    public GaugeReaderAdapter(Context context, int textViewResourceId, ArrayList<Device> devices)
    {
        super(context, textViewResourceId, devices);
        mContext = context;
        mTextViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View root;
        final Device device = getItem(position);

        if (null == convertView) {
            root = LayoutInflater.from(mContext).inflate(mTextViewResourceId, (ViewGroup) convertView);
        }
        else {
            root = convertView;
        }

        TextView deviceName = (TextView) root.findViewById(R.id.device_name);
        TextView deviceDescription = (TextView) root.findViewById(R.id.device_description);
        // ImageButton deviceType = (ImageButton) root.findViewById(R.id.device_type_icon);

        deviceName.setText(device.getName());
        deviceDescription.setText(device.getDescription());

        return root;
    }
}
