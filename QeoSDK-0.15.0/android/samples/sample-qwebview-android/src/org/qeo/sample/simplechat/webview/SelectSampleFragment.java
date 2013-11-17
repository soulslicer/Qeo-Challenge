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
package org.qeo.sample.simplechat.webview;

import java.util.ArrayList;
import java.util.List;

import org.qeo.sample.json.qsimplechat.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Dialog fragment to select a sample.
 */
public class SelectSampleFragment
    extends DialogFragment
    implements OnItemClickListener, OnClickListener
{
    private ArrayAdapter<String> mAdapter;
    private ListView mListView;
    private MainActivity mActivity;
    private List<String> mSamples;
    private Button mLoadUrlButton;
    private EditText mEditTextUrl;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mSamples = new ArrayList<String>();
        mSamples.add("QSimplechat");
        mSamples.add("QSimplechat-policy");
        mSamples.add("QGauge");
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, mSamples);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getDialog().setTitle("Select a sample");
        View v = inflater.inflate(R.layout.fragment_select_sample, container, false);
        mListView = (ListView) v.findViewById(R.id.listViewSamples);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mLoadUrlButton = (Button) v.findViewById(R.id.buttonGo);
        mLoadUrlButton.setOnClickListener(this);
        mEditTextUrl = (EditText) v.findViewById(R.id.editTextUrl);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
    {
        // clicked on list item
        mActivity.loadAsset(mSamples.get((int) id));
        this.dismiss();
    }

    @Override
    public void onClick(View v)
    {
        // clicked on load url button
        String url = mEditTextUrl.getText().toString();
        if (!url.startsWith("http://")) {
            url = "http://" + url;
        }
        Toast.makeText(mActivity, "Loading url " + url, Toast.LENGTH_LONG).show();
        mActivity.loadUrl(url);
        this.dismiss();

    }
}
