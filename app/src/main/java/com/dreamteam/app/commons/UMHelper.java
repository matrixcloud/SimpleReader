package com.dreamteam.app.commons;

import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * @description TODO
 * @author zcloud
 * @date 2014年5月9日
 *
 */
public class UMHelper
{
	private final static UMSocialService mController = UMServiceFactory.
			getUMSocialService("com.dreamteam.app.reader.umeng.usr",	RequestType.SOCIAL);
	
	
	public static UMSocialService getUMSocialService()
	{
		return mController;
	}
}
