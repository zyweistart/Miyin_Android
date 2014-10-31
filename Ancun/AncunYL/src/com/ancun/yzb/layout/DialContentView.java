package com.ancun.yzb.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import start.core.AppException;
import start.utils.TimeUtils;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Message;
import android.os.Vibrator;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ancun.bean.ContactModel;
import com.ancun.bean.RecentModel;
import com.ancun.core.BaseActivity;
import com.ancun.core.BaseScrollContent;
import com.ancun.service.AppService;
import com.ancun.yzb.R;

public class DialContentView extends BaseScrollContent implements Filterable,OnClickListener, OnLongClickListener, OnItemClickListener {

	/**
	 * 拨号声音
	 */
	private static final int PLAY_TONE = 0x88888;
	/**
	 * 声音的播放时间
	 */
	private static final int DTMF_DURATION_MS = 120;
	/**
	 * 存储DTMF Tones
	 */
	private static final HashMap<String, Integer> mToneMap = new HashMap<String, Integer>();

	private ImageButton btn_0, btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7,
			btn_8, btn_9, btnStar, btnPound, dial_show_hiden, btnDelete;
	/**
	 * 拨号按钮：0~9，*，退格，录音拨号，普通拨号
	 */
	private Button dial_recording, dial_normal, dial_add_contact,
			dial_add_exist_contact;
	/**
	 * 拨打的号码字符串
	 */
	private StringBuilder mPhone;
	/**
	 * 振动反馈
	 */
	private Vibrator dialVibrator;
	/**
	 * 监视器对象锁
	 */
	private Object mToneGeneratorLock = new Object();
	/**
	 * 声音产生器
	 */
	private ToneGenerator mToneGenerator;

	private AudioManager audioManager;

	private FilterContact mFilter;
	private ListView listview;
	private DataAdapter adapter;
	private List<RecentModel> mListDataItems;
	private List<RecentModel> mListDataItemsFilter;
	private LinearLayout not_found_frame;
	private LinearLayout dial_frame;
	private TextView dial_content;
	private int lastPosition = -1;

	static {
		mToneMap.put("1", ToneGenerator.TONE_DTMF_1);
		mToneMap.put("2", ToneGenerator.TONE_DTMF_2);
		mToneMap.put("3", ToneGenerator.TONE_DTMF_3);
		mToneMap.put("4", ToneGenerator.TONE_DTMF_4);
		mToneMap.put("5", ToneGenerator.TONE_DTMF_5);
		mToneMap.put("6", ToneGenerator.TONE_DTMF_6);
		mToneMap.put("7", ToneGenerator.TONE_DTMF_7);
		mToneMap.put("8", ToneGenerator.TONE_DTMF_8);
		mToneMap.put("9", ToneGenerator.TONE_DTMF_9);
		mToneMap.put("0", ToneGenerator.TONE_DTMF_0);
		mToneMap.put("#", ToneGenerator.TONE_DTMF_P);
		mToneMap.put("*", ToneGenerator.TONE_DTMF_S);
	}

	public DialContentView(BaseActivity activity) {
		super(activity, R.layout.module_scroll_dial);
		dialVibrator = (Vibrator) activity.getApplication().getSystemService(Service.VIBRATOR_SERVICE);
		// 按键声音播放设置及初始化,设置声音的大小 STREAM_DTMF
		mToneGenerator = new ToneGenerator(AudioManager.STREAM_MUSIC, 70);
		audioManager = (AudioManager) getCurrentActivity().getSystemService(Context.AUDIO_SERVICE);

		listview = (ListView) findViewById(R.id.contacts_listview);
		listview.setOnItemClickListener(this);
		not_found_frame = (LinearLayout) findViewById(R.id.not_found_frame);
		dial_frame = (LinearLayout) findViewById(R.id.dial_frame);
		dial_content = (TextView) findViewById(R.id.dial_content);

		dial_show_hiden = (ImageButton) findViewById(R.id.dial_show_hiden);
		dial_show_hiden.setOnClickListener(this);
		btn_0 = (ImageButton) findViewById(R.id.dial_0);
		btn_0.setOnClickListener(this);
		btn_1 = (ImageButton) findViewById(R.id.dial_1);
		btn_1.setOnClickListener(this);
		btn_2 = (ImageButton) findViewById(R.id.dial_2);
		btn_2.setOnClickListener(this);
		btn_3 = (ImageButton) findViewById(R.id.dial_3);
		btn_3.setOnClickListener(this);
		btn_4 = (ImageButton) findViewById(R.id.dial_4);
		btn_4.setOnClickListener(this);
		btn_5 = (ImageButton) findViewById(R.id.dial_5);
		btn_5.setOnClickListener(this);
		btn_6 = (ImageButton) findViewById(R.id.dial_6);
		btn_6.setOnClickListener(this);
		btn_7 = (ImageButton) findViewById(R.id.dial_7);
		btn_7.setOnClickListener(this);
		btn_8 = (ImageButton) findViewById(R.id.dial_8);
		btn_8.setOnClickListener(this);
		btn_9 = (ImageButton) findViewById(R.id.dial_9);
		btn_9.setOnClickListener(this);
		btnStar = (ImageButton) findViewById(R.id.dial_star);
		btnStar.setOnClickListener(this);
		btnPound = (ImageButton) findViewById(R.id.dial_pound);
		btnPound.setOnClickListener(this);
		btnDelete = (ImageButton) findViewById(R.id.dial_clear);
		btnDelete.setOnClickListener(this);
		btnDelete.setOnLongClickListener(this);
		dial_recording = (Button) findViewById(R.id.dial_recording);
		dial_recording.setOnClickListener(this);
		dial_normal = (Button) findViewById(R.id.dial_normal);
		dial_normal.setOnClickListener(this);
		dial_add_contact = (Button) findViewById(R.id.dial_add_contact);
		dial_add_contact.setOnClickListener(this);
		dial_add_exist_contact = (Button) findViewById(R.id.dial_add_exist_contact);
		dial_add_exist_contact.setOnClickListener(this);

		mPhone = new StringBuilder();

		loadData(true);
	}

	public void loadData(final Boolean flag) {
		new Thread() {

			public void run() {

				mListDataItems = getCurrentActivity().getRecentDaoImpl().findCallRecords();
				if (mListDataItemsFilter == null) {
					mListDataItemsFilter = new ArrayList<RecentModel>();
				} else {
					mListDataItemsFilter.clear();
				}
				mListDataItemsFilter.addAll(mListDataItems);

				getCurrentActivity().runOnUiThread(new Runnable() {
					public void run() {
						if (flag) {
							adapter = new DataAdapter();
							listview.setAdapter(adapter);
						} else {
							getFilter().filter(mPhone);
						}
					}
				});

			};

		}.start();
	}

	public class DataAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mListDataItemsFilter.size();
		}

		@Override
		public Object getItem(int position) {
			return mListDataItemsFilter.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder;
			if (convertView == null) {
				holder = new HolderView();
				convertView = getLayoutInflater().inflate(R.layout.lvitem_recent, null);
				holder.call_flag = (ImageView) convertView.findViewById(R.id.iv_call_flag);
				holder.name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.phone = (TextView) convertView.findViewById(R.id.tv_phone);
				holder.phonem = (TextView) convertView.findViewById(R.id.tv_phone_main);
				holder.time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.dial_frame = (LinearLayout) convertView.findViewById(R.id.dial_frame);
				holder.dial_recording = (Button) convertView.findViewById(R.id.dial_recording);
				holder.dial_normal = (Button) convertView.findViewById(R.id.dial_normal);
				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}
			RecentModel recent = mListDataItemsFilter.get(position);
			if (recent.getStatus() == CallLog.Calls.INCOMING_TYPE) {
				// 呼入
				holder.call_flag.setImageResource(R.drawable.ic_call_in);
			} else if (recent.getStatus() == CallLog.Calls.OUTGOING_TYPE) {
				// 呼出
				holder.call_flag.setImageResource(R.drawable.ic_call_out);
			} else {
				// 呼入未接通
				holder.call_flag.setImageResource(R.drawable.ic_call_noin);
			}
			ContactModel contactModel = getCurrentActivity()
					.getContactDaoImpl().getContactModelByPhone(recent.getPhone());
			if (contactModel != null
					&& !TextUtils.isEmpty(contactModel.getName())) {
				holder.name.setText(contactModel.getName());
				holder.phone.setText(recent.getPhone());
				holder.name.setVisibility(View.VISIBLE);
				holder.phone.setVisibility(View.VISIBLE);
				holder.phonem.setVisibility(View.GONE);
			} else {
				holder.phonem.setText(recent.getPhone());
				holder.name.setVisibility(View.GONE);
				holder.phone.setVisibility(View.GONE);
				holder.phonem.setVisibility(View.VISIBLE);
			}
			holder.time.setText(TimeUtils.customerTimeConvert(recent.getCalltime()));
			holder.dial_recording.setTag(recent);
			holder.dial_recording.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RecentModel recent = (RecentModel) v.getTag();
					// SharedPreferencesUtils.setCallType(getContext(), 2);
					AppService.inAppDial(getCurrentActivity(),
							recent.getPhone());
				}
				
			});
			holder.dial_normal.setTag(recent);
			holder.dial_normal.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					RecentModel recent = (RecentModel) v.getTag();
					Intent intent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + recent.getPhone()));
					getCurrentActivity().startActivity(intent);
				}
			});
			holder.dial_frame.setVisibility(lastPosition == position ? View.VISIBLE : View.GONE);
			return convertView;
		}

	}

	private class HolderView {
		private ImageView call_flag;
		private TextView name;
		private TextView phone;
		private TextView phonem;
		private TextView time;
		private LinearLayout dial_frame;
		private Button dial_recording;
		private Button dial_normal;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.dial_show_hiden) {
			if (lastPosition != -1) {
				lastPosition = -1;
				adapter.notifyDataSetChanged();
			}
			dial_show_hiden.setImageResource(dial_frame.isShown() ? R.drawable.dial_show : R.drawable.dial_hidden);
			dial_frame.setVisibility(dial_frame.isShown() ? View.GONE: View.VISIBLE);
		} else if (v.getId() == R.id.dial_0 || v.getId() == R.id.dial_1
				|| v.getId() == R.id.dial_2 || v.getId() == R.id.dial_3
				|| v.getId() == R.id.dial_4 || v.getId() == R.id.dial_5
				|| v.getId() == R.id.dial_6 || v.getId() == R.id.dial_7
				|| v.getId() == R.id.dial_8 || v.getId() == R.id.dial_9
				|| v.getId() == R.id.dial_star || v.getId() == R.id.dial_pound) {
			String tag = String.valueOf(v.getTag());
			mPhone.append(tag);
			showDigits();
			getHandlerContext().sendMessage(PLAY_TONE, mToneMap.get(tag));
		} else if (v.getId() == R.id.dial_normal || v.getId() == R.id.dial_recording) {
			// 拨号
			if (mPhone.length() > 0) {
				if (v.getId() == R.id.dial_normal) {
					Intent intent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:" + mPhone));
					getCurrentActivity().startActivity(intent);
				} else {
					// SharedPreferencesUtils.setCallType(getAppContext(), 2);
					AppService.inAppDial(getCurrentActivity(),
							mPhone.toString());
				}
				showDigits();
			} else {
				getHandlerContext().makeTextShort("请输入号码后再点击拨号");
			}
		} else if (v.getId() == R.id.dial_clear) {
			// 删除
			if (mPhone.length() > 0) {
				mPhone.deleteCharAt(mPhone.length() - 1);
				showDigits();
			}
		} else if (v.getId() == R.id.dial_add_contact
				|| v.getId() == R.id.dial_add_exist_contact) {
			Intent intent;
			if (mPhone.length() > 0) {
				if (v.getId() == R.id.dial_add_contact) {
					// 新建联系人
					intent = new Intent(Intent.ACTION_INSERT);
					intent.setType("vnd.android.cursor.dir/person");
					intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
					intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
				} else {
					// 保存到已有联系人
					intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
					intent.setType("vnd.android.cursor.item/person");
					intent.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);
					intent.setType(ContactsContract.RawContacts.CONTENT_ITEM_TYPE);
				}
				intent.putExtra(
						android.provider.ContactsContract.Intents.Insert.PHONE,
						mPhone.toString());
				intent.putExtra(
						android.provider.ContactsContract.Intents.Insert.PHONE_TYPE,
						android.provider.Contacts.PhonesColumns.TYPE_MOBILE);
				startActivity(intent);
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (v.getId() == R.id.dial_clear) {
			if (mPhone.length() > 0) {
				mPhone.delete(0, mPhone.length());
				showDigits();
			}
		}
		return false;
	}

	@Override
	public void onProcessMessage(Message msg) throws AppException {
		switch (msg.what) {
		case PLAY_TONE:
			dialVibrator.vibrate(new long[] { 0, 10, 20, 30 }, -1);
			Integer tone_id = (Integer) msg.obj;
			if (tone_id != null && tone_id != -1) {
				// -1是只振动不发声
				Integer tone = tone_id.intValue();
				int ringerMode = audioManager.getRingerMode();
				if (ringerMode == AudioManager.RINGER_MODE_SILENT
						|| ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
					// 静音或者震动时不发出声音
					return;
				}
				synchronized (mToneGeneratorLock) {
					if (mToneGenerator == null) {
						return;
					}
					mToneGenerator.startTone(tone, DTMF_DURATION_MS);
				}
			}
			break;
		}
	}

	public void showDigits() {
		getFilter().filter(mPhone);
		String tmpPhone = mPhone.toString();
		if (mPhone.length() > 15) {
			tmpPhone = mPhone
					.subSequence(mPhone.length() - 15, mPhone.length())
					.toString();
		}
		dial_content.setText(tmpPhone);
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new FilterContact();
		}
		return mFilter;
	}

	private class FilterContact extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			mListDataItemsFilter.clear();
			if (prefix == null || prefix.length() == 0) {
				// 输入为空
				mListDataItemsFilter.addAll(mListDataItems);
			} else {
				for (RecentModel userInfo : mListDataItems) {
					String phone = userInfo.getPhone();
					if (TextUtils.isEmpty(phone)) {
						continue;
					}
					if (phone.contains(prefix.toString())) {
						mListDataItemsFilter.add(userInfo);
					}
				}
			}
			results.values = mListDataItemsFilter;
			results.count = mListDataItemsFilter.size();
			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			if (mListDataItemsFilter.size() > 0) {
				adapter.notifyDataSetChanged();
				listview.setVisibility(View.VISIBLE);
				not_found_frame.setVisibility(View.GONE);
			} else {
				listview.setVisibility(View.GONE);
				not_found_frame.setVisibility(View.VISIBLE);
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		HolderView v = (HolderView) view.getTag();
		dial_frame.setVisibility(View.GONE);
		dial_show_hiden.setImageResource(R.drawable.dial_show);
		v.dial_frame.setVisibility(View.VISIBLE);
		if (lastPosition != position) {
			lastPosition = position;
		} else {
			lastPosition = -1;
		}
		adapter.notifyDataSetChanged();
	}

}