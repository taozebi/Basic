package com.taoze.basic.common.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 万能通用Adapter
 * @date 2015-10-28
 * @version 1.0.0
 * 
 */
public abstract class BaseCommonAdapter<T> extends BaseAdapter {
	
	protected Context mContext;
	protected List<T> mDatas;
	protected int mResourceId;
	
	public BaseCommonAdapter(Context context, List<T> datas, int resourceId) {
		super();
		this.mContext = context;
		this.mDatas = datas;
		this.mResourceId = resourceId;
	}

	@Override
	public int getCount() {
		return mDatas==null?0:mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(0);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent);
	}
	
	@SuppressWarnings("unchecked")
	private View createViewFromResource(int position, View convertView,
										ViewGroup parent) {
    	ViewHolder viewHolder;
        if (convertView == null) {
        	viewHolder = new ViewHolder(mContext,parent,mResourceId);
        } else {
        	viewHolder = (ViewHolder) convertView.getTag();
        }
        bindView(position,viewHolder);
        return viewHolder.convertView;
    }
	
	/**
	 * 绑定View
	 * @param position
	 * @param viewHolder
	 */
	abstract protected void bindView(int position, ViewHolder viewHolder);

	
	/**
	 * 通用ViewHolder
	 * ViewHolder
	 * @author Wkkyo
	 * @date 2015-10-28
	 * @version 1.0.0
	 */
	protected final class ViewHolder {
		
		private View convertView;
		
		private SparseArray<View> views;
		
		public ViewHolder(Context context, ViewGroup parent, int layoutId) {
			this.views = new SparseArray<View>();
			this.convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
			this.convertView.setTag(this);
		}

		public <K extends View> K getView(int viewId){
			View view = views.get(viewId);
			if(view == null){
				view = convertView.findViewById(viewId);
				views.put(viewId, view);
			}
			return (K) view;
		}
		
	}
}

