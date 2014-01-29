package com.dreamteam.app.ui;

import android.app.Activity;
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
							Toast.makeText(FeedbackUI.this, "����������", Toast.LENGTH_SHORT).show();
							return;
						}
						msg = "#"
								+ "v1.0"
								+ "#" + msg;
						new Thread()
						{
							//send a mail to me
							@Override
							public void run()
							{
								try
								{
									Thread.sleep(2000);
								}
								catch(InterruptedException e)
								{
									e.printStackTrace();
								}
							}
						}.start();
						
						((EditText) findViewById(R.id.feedback_edit))
								.setText("");
						Toast.makeText(FeedbackUI.this, "感谢你的建议！", Toast.LENGTH_SHORT).show();
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
