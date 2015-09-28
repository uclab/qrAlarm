package sch.cse.qralarm;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Youngsun on 7/23/2015.
 */
public class SettingFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        Log.i("MSA", "onCreateView");
        final View rootView = inflater.inflate(R.layout.fragment_setting, container, false);



        final Button btnSearchSound = (Button)rootView.findViewById(R.id.btnSearchSound);
        final TextView tvSelectedSound = (TextView)rootView.findViewById(R.id.tvselectedsound);
        //if tv exists show
        SharedPreferences mSF = getActivity().getSharedPreferences(AppConstant.SETTING, getActivity().MODE_PRIVATE);
        String soundStr = mSF.getString(AppConstant.SETTING_SOUND_TITLE, "");
        if( !soundStr.equals("")) {
            tvSelectedSound.setText(soundStr);
        }
        //button
        btnSearchSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(intent, 10);
            }
        });


        //checkbox
        final CheckBox cbSilentMode = (CheckBox)rootView.findViewById(R.id.cbSilent);
        cbSilentMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences mSF = getActivity().getSharedPreferences(AppConstant.SETTING, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSF.edit();
                if(b) {
                    mEditor.putBoolean(AppConstant.SETTING_SILENT,true);
                }
                else {
                    mEditor.putBoolean(AppConstant.SETTING_SILENT,false);
                }
                mEditor.commit();
            }
        });


        boolean isSilentMode = mSF.getBoolean(AppConstant.SETTING_SILENT, false);

        if (isSilentMode) {
            cbSilentMode.setChecked(true);
        }
        else cbSilentMode.setChecked(false);

        return rootView;
    }

    public static void setSound(Activity mActivity, Uri mURI, View rootView) {
        SharedPreferences mSF = mActivity.getSharedPreferences(AppConstant.SETTING, mActivity.MODE_PRIVATE);
        SharedPreferences.Editor msfEditor = mSF.edit();
        TextView tvsound = (TextView) rootView.findViewById(R.id.tvselectedsound);
        String title="";
        String[] proj = { MediaStore.Images.Media.TITLE };
        Cursor cursor = mActivity.getContentResolver().query(mURI, proj, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            cursor.moveToFirst();
            title = cursor.getString(columnIndex);
        }
        if (cursor != null) {
            cursor.close();
        }


        tvsound.setText(title);

        msfEditor.putString(AppConstant.SETTING_SOUND_PATH, mURI.getPath());
        msfEditor.putString(AppConstant.SETTING_SOUND_TITLE, title);
        msfEditor.commit();
    }


}
