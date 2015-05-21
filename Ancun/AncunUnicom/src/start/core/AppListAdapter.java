package start.core;

import java.util.List;
import java.util.Map;

import android.widget.BaseAdapter;

/**
 * @author Start   
 * @Description: 适配器基类
 * @ClassName: BaseListAdapter.java   
 * @date 2014年7月24日 下午1:57:56      
 * @说明  代码版权归 杭州反盗版中心有限公司 所有
 */
public abstract class AppListAdapter extends BaseAdapter {

	protected AppActivity mActivity;
	protected List<Map<String,String>> mItemDatas;
	
	public AppListAdapter(AppActivity activity){
		this.mActivity=activity;
	}
	
	public void setItemDatas(List<Map<String,String>> mItemDatas) {
		this.mItemDatas = mItemDatas;
	}
	
	public List<Map<String, String>> getItemDatas() {
		return mItemDatas;
	}

	@Override
	public int getCount() {
		return mItemDatas.size();
	}

	@Override
	public Map<String,String> getItem(int position) {
		return mItemDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
