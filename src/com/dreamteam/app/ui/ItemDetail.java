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
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.commons.HtmlFilter;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.db.FavoItemDbHelper;
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

@SuppressWarnings("deprecation")
public class ItemDetail extends FragmentActivity
{
	@SuppressWarnings("unused")
	private String tag = "ItemDetail";
	
	private ImageButton speechBtn;
	private ImageButton collectBtn;
	private ImageButton shareBtn;
	private ImageButton commentBtn;
	private TextView countTv;//评论列表
	private TextView topTitleTv;
	private static WebView mWebView;
	
	private SpeechSynthesizer tts;
	private SynthesizerListener mTtsListener;
	public String speechText;
	
	boolean canSpeech = true;
	//html全局属性
	public final static String WEB_STYLE = "<style>* {font-size:16px;line-height:20px;} p {color:#333;backgroud-color:#0} a {color:#3E62A6;} img {max-width:310px;} "
			+ "img.alignleft {float:left;max-width:120px;margin:0 10px 5px 0;border:1px solid #ccc;background:#fff;padding:2px;} "
			+ "pre {font-size:9pt;line-height:12pt;font-family:Courier New,Arial;border:1px solid #ddd;border-left:5px solid #6CE26C;background:#f6f6f6;padding:5px;} "
			+ "a {text-decoration: none}" 
			+ "h1 {text-align:center;font-size:20px}"
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
//		initTts();
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
				if(status == 200 && comments != null && !comments.isEmpty())
				{
					countTv.setText(comments.size() + "");
				}
			}
		}, -1);
	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	private void initView()
	{
		setContentView(R.layout.feed_item_detail);

		topTitleTv = (TextView) findViewById(R.id.fid_top_title);
//		speechBtn = (ImageButton) findViewById(R.id.fid_btn_speech);
//		speechBtn.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v)
//			{
//				if(!canSpeech)
//				{
//					tts.stopSpeaking(mTtsListener);
//					canSpeech = true;
//					return;
//				}
//				if (speechText.length() <= 0 || speechText == null)
//				{
//					return;
//				}
//				tts.startSpeaking(speechText, mTtsListener);
//				canSpeech = false;
//			}
//		});
		
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
				DbManager helper = new DbManager(ItemDetail.this, DbManager.DB_NAME, null, 1);
				SQLiteDatabase db = helper.getWritableDatabase();
				FavoItemDbHelper.insert(db, title, pubdate, itemDetail);
				Toast.makeText(ItemDetail.this, "添加成功", Toast.LENGTH_SHORT).show();
			}
		});

		countTv = (TextView) findViewById(R.id.fid_tv_comment_count);
		
		mWebView = (WebView) findViewById(R.id.my_web_view);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDisplayZoomControls(false);
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.getSettings().setJavaScriptEnabled(true);
	}
	
	private void loadData()
	{
		Intent intent = getIntent();
		String sectionTitle = intent.getStringExtra("section_title");
		topTitleTv.setText(sectionTitle);
		
		
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
		SharedPreferences pref = AppContext.getPrefrences(this);
		if(!pref.getBoolean("pref_imageLoad", false))
		{
			itemDetail = itemDetail.replaceAll("(<|;)\\s*(IMG|img)\\s+([^;^>]*)\\s*(;|>)", "");
		}
		sb.append("<h1>" + title + "</h1>"
				  + "<p>" + pubdate + "</p>");
		

		sb.append("<body>" + itemDetail + "<body>");
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
//		tts.stopSpeaking(null);
//		tts.destory();
	}
}

