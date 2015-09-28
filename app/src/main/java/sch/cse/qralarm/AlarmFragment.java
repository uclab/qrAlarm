/*
9/28/2015
DataBase or SharedPreference
 */
package sch.cse.qralarm;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Comparator;

/**
 * Created by Youngsun on 7/23/2015.
 */
public class AlarmFragment extends Fragment {

    private ListView m_ListView;
    private ArrayAdapter<String> m_Adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        Log.i("MSA", "onCreateView");
        final View rootView = inflater.inflate(R.layout.fragment_alarm, container, false);

        m_Adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.my_list_item);
        m_ListView = (ListView) rootView.findViewById(R.id.listview);
        m_ListView.setAdapter(m_Adapter);
        m_ListView.setOnItemClickListener(onClickListItem);

        m_Adapter.add("Add");

        return rootView;
    }


    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
            //Toast.makeText(getActivity().getApplicationContext(), m_Adapter.getItem(arg2), Toast.LENGTH_SHORT).show();
            //Add Alarm
            if(m_Adapter.getItem(arg2).equals("Add") ) {
                final TimePickerDialog.OnTimeSetListener mTimeSetListner = new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        //Toast.makeText(getActivity().getApplicationContext(), hourOfDay+":"+minute, Toast.LENGTH_SHORT).show();
                        if (view.isShown()) {
                            m_Adapter.insert((String) (String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute)), 0);
                            m_Adapter.sort(new Comparator<String>() {
                                @Override
                                public int compare(String lhs, String rhs) {
                                    return lhs.compareTo(rhs);   //or whatever your sorting algorithm
                                }
                            });
                        }
                    }
                };
                final TimePickerDialog.OnCancelListener mCancelListner = new TimePickerDialog.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        //m_Adapter.remove(m_Adapter.getItem(0));
                        //Toast.makeText(getActivity().getApplicationContext(), "...", Toast.LENGTH_SHORT).show();
                    }
                };
                TimePickerDialog alert = new TimePickerDialog(getActivity(), mTimeSetListner, 0, 0, false);
                alert.setOnCancelListener(mCancelListner);
                alert.setCancelable(true);
                alert.show();
            }
            else {
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
                alt_bld.setMessage("Do you want to delete ?").setCancelable(false).setPositiveButton("Yes",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m_Adapter.remove(m_Adapter.getItem(arg2));
                    }
                }).setNegativeButton("No",new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert = alt_bld.create();
                alert.setTitle("Delete?");
                alert.show();
            }

        }
    };
}
