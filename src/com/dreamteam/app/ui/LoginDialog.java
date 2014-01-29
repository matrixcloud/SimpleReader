package com.dreamteam.app.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.dreamteam.app.commons.Appcontext;
import com.dreateam.app.ui.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SocializeClientListener;

/**
 * @description TODO
 * @author zcloud
 * @date Dec 7, 2013
 */
@SuppressLint("NewApi")
public class LoginDialog extends DialogFragment
{

	public static final String tag = "LoginDialog";
	
	private Activity mActivity;
	
	private UMSocialService mController = UMServiceFactory.getUMSocialService("com.dreamteam.umeng.user",
			RequestType.SOCIAL);
	
	private static final int POS_SINA_WEIBO = 0;
	private static final int POS_QQZONE = 1;
	private static final int POS_RENREN = 2;
	

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState)
	{
		mActivity = getActivity();
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(R.array.login_accounts, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(!Appcontext.isNetworkAvailable(mActivity))	
				{
					Toast.makeText(mActivity, "请检查网络设置！", Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				ScializeMonitor monitor = new ScializeMonitor();
				switch(which)
				{
				case POS_SINA_WEIBO:
					mController.login(mActivity, SHARE_MEDIA.SINA, monitor);
					break;
				case POS_QQZONE:
					mController.login(mActivity, SHARE_MEDIA.QZONE, monitor);
					break;
				case POS_RENREN:
					mController.login(mActivity, SHARE_MEDIA.RENREN, monitor);
					break;
				}
			}
		});
		return builder.create();
	}
	
	private final class ScializeMonitor implements SocializeClientListener
	{

		@Override
		public void onStart(){}
		
		@Override
		public void onComplete(int status, SocializeEntity entity)
		{
			if(status == 200)
			{
				Toast.makeText(mActivity, "��½�ɹ�", Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(mActivity, "�����쳣", Toast.LENGTH_SHORT).show();
			}
		}
		
	}
}
