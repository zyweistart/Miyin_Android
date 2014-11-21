package com.start.service;

import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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

	//微信
	private static final String WXAPPID = "wx967daebe835fbeac";
	private static final String WXAPPSECRET = "5fa9e68ca3970e87a1f83e563c8dcbce";
	//QQ
	private static final String QQAPPID = "1103513265";
	private static final String QQAPPKEY = "vE1TEWN5S4QQwzpx";

	public static UMSocialService socialShare(Activity activity,
			String shareContent, String shareMedia) {
		UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.share");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, WXAPPID, WXAPPSECRET);
		wxHandler.addToSocialSDK();
		// 添加微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity, WXAPPID,
				WXAPPSECRET);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		// 添加QQ
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQAPPID,
				QQAPPKEY);
		qqSsoHandler.addToSocialSDK();
		// 添加QQ空间
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				QQAPPID, QQAPPKEY);
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

	public static void socialWexinLogin(final Activity activity) {
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.login");
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, WXAPPID, WXAPPSECRET);
		wxHandler.addToSocialSDK();

		mController.doOauthVerify(activity, SHARE_MEDIA.WEIXIN,
				new UMAuthListener() {

					@Override
					public void onStart(SHARE_MEDIA platform) {
						// 授权开始
					}

					@Override
					public void onError(SocializeException e,
							SHARE_MEDIA platform) {
						// 授权错误
					}

					@Override
					public void onComplete(Bundle value, SHARE_MEDIA platform) {
						// 授权完成,获取相关授权信息
						mController.getPlatformInfo(activity,
								SHARE_MEDIA.WEIXIN, new UMDataListener() {
									@Override
									public void onStart() {
										Toast.makeText(activity, "获取平台数据开始...",
												Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onComplete(int status,
											Map<String, Object> info) {
										if (status == 200 && info != null) {
											StringBuilder sb = new StringBuilder();
											Set<String> keys = info.keySet();
											for (String key : keys) {
												sb.append(key
														+ "="
														+ info.get(key)
																.toString()
														+ "\r\n");
											}
											Log.d("TestData", sb.toString());
										} else {
											Log.d("TestData", "发生错误：" + status);
										}
									}
								});
					}

					@Override
					public void onCancel(SHARE_MEDIA platform) {
						//授权取消
					}

				});
	}

	public static void socialQQLogin(final Activity activity) {
		final UMSocialService mController = UMServiceFactory
				.getUMSocialService("com.umeng.login");
		// 添加QQ平台
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity, QQAPPID,
				QQAPPKEY);
		qqSsoHandler.addToSocialSDK();

		mController.doOauthVerify(activity, SHARE_MEDIA.QQ,

		new UMAuthListener() {
			@Override
			public void onStart(SHARE_MEDIA platform) {
				// 授权开始
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
				// 授权错误
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
							public void onComplete(int status,
									Map<String, Object> info) {
								if (status == 200 && info != null) {
									StringBuilder sb = new StringBuilder();
									Set<String> keys = info.keySet();
									for (String key : keys) {
										sb.append(key + "="
												+ info.get(key).toString()
												+ "\r\n");
									}
									Log.d("TestData", sb.toString());
								} else {
									Log.d("TestData", "发生错误：" + status);
								}
							}
						});
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				// 授权取消
			}

		});
	}

}
