package sch.cse.qralarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.IOException;

/**
 * Created by Youngsun on 7/23/2015.
 */
public class QRFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        Log.i("MSA", "onCreateView");
        final View rootView = inflater.inflate(R.layout.fragment_qr, container, false);

        final Button btnMail = (Button) rootView.findViewById(R.id.btnSend);
        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mSF = getActivity().getSharedPreferences(AppConstant.QR_CODE, getActivity().MODE_PRIVATE);
                String qrStr = mSF.getString(AppConstant.QR_CODE_STRING, "");
                if (!qrStr.equals("")) {
                    try {
                        File bmpfile = SendMail.SaveBitmapToFileCache(getActivity().getApplicationContext(),makeQR(getActivity(), qrStr, rootView));
                        SendMail.send(getActivity().getApplicationContext(),getActivity(),bmpfile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button btnNew = (Button) rootView.findViewById(R.id.btnNew);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dialog
                AlertDialog.Builder alt_bld = new AlertDialog.Builder(getActivity());
                alt_bld.setMessage("Select")
                        .setCancelable(true).setPositiveButton("Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IntentIntegrator.initiateScan(getActivity());
                    }
                }).setNeutralButton("Text", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                        alert.setTitle("QR Generator");
                        alert.setMessage("Type Keyword");
                        final EditText input = new EditText(getActivity());
                        alert.setView(input);
                        alert.setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (!input.getText().toString().equals("")) {
                                    makeQR(getActivity(), input.getText().toString(), getActivity().getWindow().getDecorView().findViewById(android.R.id.content));
                                }
                            }
                        }).show();
                    }
                }).show();


            }
        });

        SharedPreferences mSF = getActivity().getSharedPreferences(AppConstant.QR_CODE, getActivity().MODE_PRIVATE);
        String qrStr = mSF.getString(AppConstant.QR_CODE_STRING, "");
        if (!qrStr.equals("")) {
            makeQR(getActivity(), qrStr, rootView);
        }

        return rootView;
    }

    public static Bitmap makeQR(Activity mActivity, String strResult, View rootView) {
        SharedPreferences mSF = mActivity.getSharedPreferences(AppConstant.QR_CODE, mActivity.MODE_PRIVATE);
        SharedPreferences.Editor msfEditor = mSF.edit();
        ImageView ivQR = (ImageView) rootView.findViewById(R.id.ivQR);
        int width = (int) mActivity.getResources().getDimension(R.dimen.qr_preview_width);
        int height = (int) mActivity.getResources().getDimension(R.dimen.qr_preview_height);
        QRCodeWriter mQRW = new QRCodeWriter();
        try {
            BitMatrix bmQR = mQRW.encode(strResult, BarcodeFormat.QR_CODE, width, height);
            Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    mBitmap.setPixel(i, j, bmQR.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }

            ivQR.setImageBitmap(mBitmap);

            msfEditor.putString(AppConstant.QR_CODE_STRING, strResult);
            msfEditor.commit();
            return mBitmap;

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

}
