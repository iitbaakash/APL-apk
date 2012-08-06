package com.aakash.lab;

//	import java.io.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
//	import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
//	import android.os.Environment;
import android.util.Log;
//	import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//	import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
//	import android.widget.Button;
import com.aakash.lab.R;

public class sci extends ActivityGroup {
	private String[] mFileList;
	private File mPath;
	private String mChosenFile;
	private static final String FTYPE = ".cde";
	private static final int DIALOG_LOAD_FILE = 1000;
	private String oe_path;

	/** Called when the activity is first created. */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main1);
		WebView engine = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);

		engine.setVerticalScrollBarEnabled(false);
		engine.setHorizontalScrollBarEnabled(false);

		engine.loadUrl("http://127.0.0.1/html/scilab/index.html");

		engine.setWebChromeClient(new WebChromeClient()

		{
			@Override
			public void onConsoleMessage(String message, int lineNumber,
					String sourceID) {
				Log.d("MyApplication", message + " -- From line " + lineNumber
						+ " of " + sourceID);
				super.onConsoleMessage(message, lineNumber, sourceID);
			}

		});

	}

	public void test() {
		// TODO Auto-generated method stub
		WebView engine = (WebView) findViewById(R.id.webView1);

		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		engine.getSettings().setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		engine.setWebChromeClient(new MyWebChromeClientsci());

		engine.loadUrl("javascript:savecode()");
	}

	public void test1() {
		// TODO Auto-generated method stub
		WebView engine = (WebView) findViewById(R.id.webView1);
		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		engine.getSettings().setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		engine.setWebChromeClient(new MyWebChromeClientsci());
		engine.loadUrl("javascript:saveImg()");
	}

	public void openFile() {

		// TODO Auto-generated method stub
		WebView engine = (WebView) findViewById(R.id.webView1);
		// engine.setWebViewClient(new MyWebViewClient());
		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		engine.getSettings().setJavaScriptEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setAppCacheEnabled(false);
		engine.setWebChromeClient(new MyWebChromeClientsci());
		engine.loadUrl("javascript:submit_file()");
	}

	private void loadFileList() {

		try {
			mPath.mkdirs();
		} catch (SecurityException e) {

			System.out.println("unable to write on the sd card ");
		}
		if (mPath.exists()) {

			FilenameFilter filter = new FilenameFilter() {
				public boolean accept(File dir, String filename) {
					File sel = new File(dir, filename);

					return filename.contains(FTYPE) || sel.isDirectory();
				}
			};
			mFileList = mPath.list(filter);

			onCreateDialog(DIALOG_LOAD_FILE);

		} else {
			mFileList = new String[0];
		}
	}

	protected Dialog onCreateDialog(int id) {

		Dialog dialog = null;
		AlertDialog.Builder builder = new Builder(this);

		switch (id) {
		case DIALOG_LOAD_FILE:

			builder.setTitle("Choose your file");
			if (mFileList == null) {

				System.out
						.println("Showing file picker before loading the file list ");
				dialog = builder.create();
				return dialog;
			}
			builder.setItems(mFileList, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					mChosenFile = mFileList[which];

					InputStream inStream = null;
					OutputStream outStream = null;

					try {
						File bfile = new File(
								"/data/local/linux/var/www/html/scilab/code/.open_file.cde");

						inStream = new FileInputStream(oe_path + mChosenFile);
						outStream = new FileOutputStream(bfile);

						byte[] buffer = new byte[1024];

						int length;
						// copy the file content in bytes
						while ((length = inStream.read(buffer)) > 0) {

							outStream.write(buffer, 0, length);

						}

						inStream.close();
						outStream.close();
						openFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			break;
		}
		dialog = builder.show();
		return dialog;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.open:
			oe_path = Environment.getExternalStorageDirectory()
					+ "/APL/scilab/code/";
			mPath = new File(oe_path);
			loadFileList();
			return true;
		case R.id.savecode1:
			test();
			return true;
		case R.id.savefigure:
			test1();
			return true;
		case R.id.example:
			oe_path = "/data/local/linux/var/www/html/scilab/example/";
			mPath = new File(oe_path);
			loadFileList();
			return true;
		case R.id.help:

			Intent myIntent = new Intent(sci.this, help.class);
			startActivityForResult(myIntent, 0);

			return true;
		case R.id.about:

			Intent myIntent1 = new Intent(sci.this, about.class);
			startActivityForResult(myIntent1, 0);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

final class MyWebChromeClientsci extends WebChromeClient

{

	sci jb = new sci();

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		Log.d("MyApplication", message + " -- From line " + lineNumber + " of "
				+ sourceID);
		super.onConsoleMessage(message, lineNumber, sourceID);

	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {

		Log.d("LogTag", message);

		if (message.charAt(0) == '0') {
			message = message.substring(1);

		} else {
			message = message.substring(1);

		}

		result.confirm();
		return true;
	}

}