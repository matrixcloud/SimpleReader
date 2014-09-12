package com.dreamteam.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dreateam.app.ui.R;

public class FeedbackUI extends Activity
{

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fadeback_view);
		
		((Button) findViewById(R.id.feedback_btn))
				.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View arg0)
					{
						String msg = ((EditText) findViewById(R.id.feedback_edit))
								.getText().toString().trim();
						if ("".equals(msg))
						{
							Toast.makeText(FeedbackUI.this, "请输入内容，谢谢！", Toast.LENGTH_SHORT).show();
							return;
						}
						Intent intent = new Intent();
						intent.setType("message/rfc822");
						intent.setAction(Intent.ACTION_SEND);
						intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"zcloud01@outlook.com"});
						intent.putExtra(Intent.EXTRA_SUBJECT, "用户反馈");
						intent.putExtra(Intent.EXTRA_TEXT, msg);
						startActivity(Intent.createChooser(intent, "sending mail"));
						((EditText) findViewById(R.id.feedback_edit))
								.setText("");
						finish();
					}
				});

		((ImageButton) findViewById(R.id.button_return))
				.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View arg0)
					{

						FeedbackUI.this.finish();
					}
				});
	}

}
