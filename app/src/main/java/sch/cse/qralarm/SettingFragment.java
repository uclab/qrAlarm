package sch.cse.qralarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

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

        final CheckBox cbSilentMode = (CheckBox)rootView.findViewById(R.id.cbSilent);
        final CheckBox cb24hours = (CheckBox)rootView.findViewById(R.id.cb24hours);

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

        cb24hours.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences mSF = getActivity().getSharedPreferences(AppConstant.SETTING, getActivity().MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mSF.edit();
                if(b) {
                    mEditor.putBoolean(AppConstant.SETTING_24HOURS,true);
                }
                else {
                    mEditor.putBoolean(AppConstant.SETTING_24HOURS,false);
                }
                mEditor.commit();
            }
        });

        SharedPreferences mSF = getActivity().getSharedPreferences(AppConstant.SETTING, getActivity().MODE_PRIVATE);
        boolean isSilentMode = mSF.getBoolean(AppConstant.SETTING_SILENT, false);
        boolean is24hours = mSF.getBoolean(AppConstant.SETTING_24HOURS, false);
        if (isSilentMode) {
            cbSilentMode.setChecked(true);
        }
        else cbSilentMode.setChecked(false);

        if (is24hours) {
            cb24hours.setChecked(true);
        }
        else cb24hours.setChecked(false);

        return rootView;
    }


}
