package sch.cse.qralarm;


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
import android.widget.Toast;

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

        m_Adapter.add("alarm1");
        m_Adapter.add("alarm2");
        m_Adapter.add("alarm3");
        m_Adapter.add("alarm4");
        m_Adapter.add("alarm5");
        m_Adapter.add("alarm6");
        m_Adapter.add("alarm4");
        m_Adapter.add("alarm5");
        m_Adapter.add("alarm6");
        m_Adapter.add("alarm4");
        m_Adapter.add("alarm5");
        m_Adapter.add("alarm6");

        m_Adapter.add("Add..");

        return rootView;
    }


    private AdapterView.OnItemClickListener onClickListItem = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(getActivity().getApplicationContext(), m_Adapter.getItem(arg2), Toast.LENGTH_SHORT).show();
        }
    };
}
