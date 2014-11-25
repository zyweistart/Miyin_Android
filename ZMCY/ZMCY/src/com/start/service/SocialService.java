package com.start.service;

import java.util.Map;

import start.core.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;

import com.start.core.BaseActivity;
import com.start.core.Config;
import com.start.core.Constant.Handler;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.UMAuthListener;
import com.umeng.socialize.controller.listener.SocializeListeners.UMDataListener;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class SocialService {

	public static UMSocialService socialShare(Activity activity,
			String shareContent, String shareMedia) {
		UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.share");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, Config.WXAPPID, Config.WXAPPSECRET);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity, Config.WXAPPID,
				Config.WXAPPSECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 添加QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, Config.QQAPPID,
				Config.QQAPPKEY);
		qqSsoHandler.addToSocialSDK();
		// 添加QQ空间
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				Config.QQAPPID, Config.QQAPPKEY);
		qZoneSsoHandler.addToSocialSDK();
		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 设置腾讯微博SSO handler
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// 设置分享内容
		mController.setShareContent(shareContent);
		if (!TextUtils.isEmpty(shareMedia)) {
			// 设置分享图片, 参数2为图片的url地址
			mController.setShareMedia(new UMImage(activity, shareMedia));
		}
		mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
		// 设置需要移除的分享平台
//		mController.getConfig().removePlatform(SHARE_MEDIA.WEIXIN,
//				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QZONE, SHARE_MEDIA.QQ,
//				SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT);
		return mController;
	}

	public static void socialWexinLogin(final BaseActivity activity) {
		
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, Config.WXAPPID, Config.WXAPPSECRET);
		wxHandler.addToSocialSDK();

		mController.doOauthVerify(activity, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {
			
					private ProgressDialog mPDialog;
					
					@Override
					public void onStart(SHARE_MEDIA platform) {
						// 授权开始
						mPDialog = new ProgressDialog(activity);
						mPDialog.setMessage(activity.getString(R.string.wait));
						mPDialog.setIndeterminate(true);
						mPDialog.setCancelable(false);
						mPDialog.show();
					}

					@Override
					public void onError(SocializeException e,SHARE_MEDIA platform) {
						// 授权错误
						mPDialog.dismiss();
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						// 授权完成,获取相关授权信息
						mController.getPlatformInfo(activity,SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										// 获取平台数据开始
									}

									@Override
									public void onComplete(int status,Map<String, Object> info) {
										if (status == 200 && info != null) {
											Message message=new Message();
											message.what=Handler.HANDLERTHIRDPARTYLANDINGWX;
											message.obj=info;
											activity.getHandlerContext().sendMessage(message);
											mPDialog.dismiss();
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						//授权取消
						mPDialog.dismiss();
					}

				});
	}

	public static void socialQQLogin(final BaseActivity activity) {
		final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.login");
		// 添加QQ平台
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, Config.QQAPPID,Config.QQAPPKEY);
		qqSsoHandler.addToSocialSDK();

		mController.doOauthVerify(activity, SHARE_MEDIA.QQ,
			new UMAuthListener() {
			
				private ProgressDialog mPDialog;
			
				@Override
				public void onStart(SHARE_MEDIA platform) {
					// 授权开始
					mPDialog = new ProgressDialog(activity);
					mPDialog.setMessage(activity.getString(R.string.wait));
					mPDialog.setIndeterminate(true);
					mPDialog.setCancelable(false);
					mPDialog.show();
				}
	
				@Override
				public void onError(SocializeException e, SHARE_MEDIA platform) {
					// 授权错误
					mPDialog.dismiss();
				}
	
				@Override
				public void onComplete(Bundle value, SHARE_MEDIA platform) {
					// 授权完成,获取相关授权信息
					mController.getPlatformInfo(activity, SHARE_MEDIA.QQ,
							new UMDataListener() {
								@Override
								public void onStart() {
									// 获取平台数据开始
								}
	
								@Override
								public void onComplete(int status,Map<String, Object> info) {
									if (status == 200 && info != null) {
										Message message=new Message();
										message.what=Handler.HANDLERTHIRDPARTYLANDINGQQ;
										message.obj=info;
										activity.getHandlerContext().sendMessage(message);
										mPDialog.dismiss();
									}
								}
							});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				// 授权取消
				mPDialog.dismiss();
			}

		});
	}

}
