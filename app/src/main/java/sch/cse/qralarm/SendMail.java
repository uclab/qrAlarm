package sch.cse.qralarm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class SendMail {

	public static void send(Context context,Activity activity, File datafile) throws IOException {

		if(datafile.exists()) {
			Log.i("file exist","ok");
		}
		else Log.i("file exist","no");

		String emailContent;
		emailContent = "Dear ";
		emailContent = emailContent + "\n" + "Here is a QR Code";
		emailContent = emailContent + "\n" + "from qrApp";
		emailContent = emailContent + "\n" + "This mail is generated from app automatically" + "\n";

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		i.putExtra(Intent.EXTRA_EMAIL  , "");
		i.putExtra(Intent.EXTRA_SUBJECT, "[QR Code]");
		i.putExtra(Intent.EXTRA_TEXT   , emailContent);

		Uri filePath = Uri.fromFile(datafile);
		i.putExtra(Intent.EXTRA_STREAM, filePath);
		try {
			activity.startActivity(Intent.createChooser(i, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(activity, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
		}
	}

	public static File SaveBitmapToFileCache(Context context, Bitmap bitmap) throws IOException {
		//File outputDir = context.getExternalFilesDir(); // context being the Activity pointer
		File outputDir = context.getExternalFilesDir(null);
		outputDir.mkdirs();
		File fileCacheItem = File.createTempFile("qrimg", ".jpg", outputDir);
		OutputStream out = null;

		try
		{
			fileCacheItem.createNewFile();
			out = new FileOutputStream(fileCacheItem);

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
		}
		catch (Exception e)
		{
			Log.e("file","file created fail");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				out.close();
				return fileCacheItem;
			}
			catch (IOException e)
			{
				Log.e("file","file close fail");
				e.printStackTrace();
			}
		}
		return null;
	}

}
