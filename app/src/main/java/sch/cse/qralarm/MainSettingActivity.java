package sch.cse.qralarm;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


/**
 * Created by Youngsun on 7/23/2015.
 */
public class MainSettingActivity extends ActionBarActivity {

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_setting);

        backPressCloseHandler = new BackPressCloseHandler(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        ActionBar.Tab tab = actionBar.newTab()
                .setText("QR")
                .setTabListener(new TabListener<QRFragment>(
                        this, "QR", QRFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText("Alarm")
                .setTabListener(new TabListener<AlarmFragment>(
                        this, "Alarm", AlarmFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText("Setting")
                .setTabListener(new TabListener<SettingFragment>(
                        this, "Setting", SettingFragment.class));
        actionBar.addTab(tab);
    }

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /**
         * Constructor used each time a new tab is created.
         *
         * @param activity The host Activity, used to instantiate the fragment
         * @param tag      The identifier tag for the fragment
         * @param clz      The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }

    //for QR Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("QRResult", "onActivityResult");
        Log.i("QRResult", requestCode+","+resultCode+","+data.getData().getPath());
        if(resultCode == RESULT_OK && requestCode == 10){
            Uri uriSound=data.getData();

            SettingFragment.setSound(this, uriSound, getWindow().getDecorView().findViewById(android.R.id.content));
            Toast.makeText(this,uriSound.getPath(),Toast.LENGTH_SHORT).show();
        }
        else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Log.d("QRResult", "Cancelled scan");
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("QRResult", "Scanned");
                    String strResult = data.getStringExtra(Intents.Scan.RESULT);
                    QRFragment.makeQR(this, strResult, getWindow().getDecorView().findViewById(android.R.id.content));
                    Toast.makeText(this, strResult, Toast.LENGTH_LONG).show();
                    //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            } else {
                Log.d("QRResult", "Weird");
                // This is important, otherwise the result will not be passed to the fragment
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        backPressCloseHandler.onBackPressed();
    }
}
