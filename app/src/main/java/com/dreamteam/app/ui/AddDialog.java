package com.dreamteam.app.ui;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.dreamteam.app.commons.ItemListEntityParser;
import com.dreamteam.app.commons.SectionHelper;
import com.dreamteam.app.commons.SeriaHelper;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.entity.ItemListEntity;
import com.dreamteam.app.utils.CategoryNameExchange;
import com.dreateam.app.ui.R;

public class AddDialog extends DialogFragment implements OnItemSelectedListener
{
	private static final String tag = "AddDialog";
	private Spinner mSpinner;
	private EditText urlEt;
	private RelativeLayout checkLayout;
	private String tableName_en;
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.add_dialog, null);
		
		urlEt = (EditText) view.findViewById(R.id.add_url_et);
		checkLayout = (RelativeLayout) view.findViewById(R.id.add_check_layout);
		
		mSpinner = (Spinner) view.findViewById(R.id.add_rssSpinner);
		ArrayAdapter<CharSequence> cateAdapter = ArrayAdapter.createFromResource(getActivity()
				, R.array.feed_category, android.R.layout.simple_spinner_item);
		cateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(cateAdapter);
		mSpinner.setOnItemSelectedListener(this);
		
		builder.setView(view)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				})
				.setPositiveButton("确定", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						checkRss();
					}
				});
		return builder.create();
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		String tableName = parent.getItemAtPosition(pos).toString();
		Log.d(tag, tableName);
		tableName_en = new CategoryNameExchange(getActivity()).zh2en(tableName);
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	private void checkRss()
	{
		final String url = urlEt.getText().toString();
		
		new AsyncTask<String, Integer, String>() {

			@Override
			protected void onPostExecute(String result) 
			{
				checkLayout.setVisibility(View.GONE);
				
				if(result == null)
				{
//					Toast.makeText(AddDialog.this.getActivity(), "抱歉，暂不支持该rss", Toast.LENGTH_SHORT).show();
					Log.e(tag, "抱歉，暂不支持该rss");
					return;
				}
				Log.e(tag, "添加成功");
//				Toast.makeText(AddDialog.this.getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
				//加入section表
				DbManager mgr = new DbManager(AddDialog.this.getActivity(), DbManager.DB_NAME, null, 1);
				SQLiteDatabase db = mgr.getWritableDatabase();
				SectionHelper.insert(db, tableName_en, result, url);
				db.close();
				
				Intent intent = new Intent();
				intent.setAction(Main.ACTION_ADD_SECTION);
				getActivity().sendBroadcast(intent);
			}

			@Override
			protected void onPreExecute() 
			{
				checkLayout.setVisibility(View.VISIBLE);
			}

			@Override
			protected String doInBackground(String... params) {
				String title = null;
				
				ItemListEntityParser parser = new ItemListEntityParser();
				ItemListEntity entity = parser.parse(params[0]);
				if(entity != null)
				{
					SeriaHelper helper = SeriaHelper.newInstance();
					File cache = SectionHelper.newSdCache(params[0]);
					helper.saveObject(entity, cache);
					title = parser.getFeedTitle();
				}
				return title;
			}
		
		}.execute(url);
	}
	
}
