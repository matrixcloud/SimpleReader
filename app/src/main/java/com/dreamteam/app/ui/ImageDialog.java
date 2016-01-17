package com.dreamteam.app.ui;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dreamteam.app.utils.HttpUtils;
import com.dreateam.app.ui.R;

public class ImageDialog extends Activity
{
	private ProgressBar progressBar;
	private ImageView imageView;
	private Button saveBtn; 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_dialog);
		progressBar = (ProgressBar) findViewById(R.id.imagedialog_progress);
		imageView = (ImageView) findViewById(R.id.imagedialog_image);
		Intent intent = getIntent();
		String url = intent.getStringExtra("url");
		System.out.println(url);
		
		new AsyncTask<String, Integer, Bitmap>()
		{

			@Override
			protected void onPostExecute(Bitmap result)
			{
				if(result != null)
				{
					imageView.setImageBitmap(result);
				}
				else
					Toast.makeText(ImageDialog.this, R.string.network_exception, Toast.LENGTH_SHORT).show();
				progressBar.setVisibility(View.GONE);
			}

			@Override
			protected Bitmap doInBackground(String... params)
			{
				InputStream is = null;
				
				try
				{
					is = HttpUtils.getInputStream(params[0]);
					Bitmap bmp = BitmapFactory.decodeStream(is);
					return bmp;
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					if(is != null)
					{
						try
						{
							is.close();
							is = null;
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}
				return null;
			}

		}.execute(url);
	}
	
}
