package com.dreamteam.app.ui;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.commons.Appcontext;
import com.dreamteam.app.commons.HtmlFilter;
import com.dreamteam.app.db.DBHelper;
import com.dreamteam.app.utils.MD5;
import com.dreateam.app.ui.R;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SynthesizerListener;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.UMComment;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.FetchCommetsListener;

public class ItemDetail extends FragmentActivity
{
	@SuppressWarnings("unused")
	private String tag = "ItemDetail";
	
	private ImageButton speechBtn;
	private ImageButton collectBtn;
	private ImageButton shareBtn;
	private ImageButton commentBtn;
	private TextView countTv;//评论列表
	private WebView mWebView;
	
	private SpeechSynthesizer tts;
	private SynthesizerListener mTtsListener;
	public String speechText;
	
	boolean canSpeech = true;
	//html全局属性
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;} a {color:#3E62A6;} img {max-width:310px;} "
			+ "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} "
			+ "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} "
			+ "a.tag {font-size:15px;text-decoration:none;background-color:#bbd6f3;border-bottom:2px solid #3E6D8E;border-right:2px solid #7F9FB6;color:#284a7b;margin:2px 2px 2px 0;padding:2px 4px;white-space:nowrap;}</style>";
	private String title;
	private String pubdate;
	private String itemDetail;
	
	//umeng
	private UMSocialService mController;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		initView();
		loadData();
		initTts();
		initComments();
	}

	private void initComments()
	{
		String key = MD5.Md5(title);

		mController = UMServiceFactory.getUMSocialService("com.umeng.share" + key,
					RequestType.SOCIAL);
		mController.getComments(this, new FetchCommetsListener()
		{
			@Override
			public void onStart()
			{
			}
			
			@Override
			public void onComplete(int status, List<UMComment> comments, SocializeEntity entity)
			{
				if(status == 200 && comments != null)
				{
					countTv.setText(comments.size() + "");
				}
			}
		}, -1);
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView()
	{
		setContentView(R.layout.feed_item_detail);

		speechBtn = (ImageButton) findViewById(R.id.fid_btn_speech);
		speechBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				if(!canSpeech)
				{
					tts.stopSpeaking(mTtsListener);
					canSpeech = true;
					return;
				}
				if (speechText.length() <= 0 || speechText == null)
				{
					return;
				}
				tts.startSpeaking(speechText, mTtsListener);
				canSpeech = false;
			}
		});
		
		shareBtn = (ImageButton) findViewById(R.id.fid_btn_share);
		shareBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				mController.openShare(ItemDetail.this, false);
			}
			
		});
		
		commentBtn = (ImageButton) findViewById(R.id.fid_btn_comment);
		commentBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v)
			{
				mController.openComment(ItemDetail.this, false);
			}
			
		});
		collectBtn = (ImageButton) findViewById(R.id.fid_btn_collecte);
		collectBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v)
			{
				//�ղ�����ݿ�
				DBHelper helper = new DBHelper(ItemDetail.this, "reader.db", null, 1);
				SQLiteDatabase db = helper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("title", title);
				values.put("pubdate", pubdate);
				values.put("item_detail", itemDetail);
				db.insert("favorite_item", null, values);
				db.close();
				Toast.makeText(ItemDetail.this, "�ղسɹ�!", Toast.LENGTH_SHORT).show();
			}
		});

		countTv = (TextView) findViewById(R.id.fid_tv_comment_count);
		
		mWebView = (WebView) findViewById(R.id.my_web_view);
//		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDefaultFontSize(15);
	}
	
	private void loadData()
	{
		Intent intent = getIntent();
		StringBuffer sb = new StringBuffer();
		title = intent.getStringExtra("title");
		pubdate = intent.getStringExtra("pubdate");
		itemDetail = intent.getStringExtra("item_detail");
		//过滤img宽和高
		itemDetail = itemDetail.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		itemDetail = itemDetail.replaceAll(
				"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
		//图片双击
		 itemDetail = itemDetail.replaceAll("(<img[^>]+src=\")(\\S+)\"",
					"$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
		//是否加载图片
		SharedPreferences pref = Appcontext.getPrefrences(this);
		if(!pref.getBoolean("pref_imageLoad", false))
		{
			itemDetail = itemDetail.replaceAll("(<|;)\\s*(IMG|img)\\s+([^;^>]*)\\s*(;|>)", "");
		}
		sb.append("<h3>" + title + "</h3>"
				  + "<p>" + pubdate + "</p>");
		

		sb.append("<hr>" + itemDetail + "</hr>");
		mWebView.loadDataWithBaseURL(null, WEB_STYLE + sb.toString(), "text/html", "UTF-8", null);
	}

	private void initTts()
	{
		speechText = HtmlFilter.filterHtml(itemDetail);

		tts = new SpeechSynthesizer(this, null);
		tts.setParameter(SpeechConstant.ENGINE_TYPE, "local");
		tts.setParameter(SpeechSynthesizer.SPEED, "50");
		tts.setParameter(SpeechSynthesizer.PITCH, "50");
		
		mTtsListener = new SynthesizerListener.Stub()
		{
			
			@Override
			public void onSpeakResumed() throws RemoteException
			{
				
			}
			
			@Override
			public void onSpeakProgress(int arg0) throws RemoteException
			{
				
			}
			
			@Override
			public void onSpeakPaused() throws RemoteException
			{
				
			}
			
			@Override
			public void onSpeakBegin() throws RemoteException
			{
				
			}
			
			@Override
			public void onCompleted(int arg0) throws RemoteException
			{
				
			}
			
			@Override
			public void onBufferProgress(int arg0) throws RemoteException
			{
				
			}
		};
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		tts.stopSpeaking(null);
		tts.destory();
	}
}

