package com.example.mistletoe_taskmanager;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckBoxRefreshAdapter extends BaseAdapter{
	List<ProgramUtil> list = new ArrayList<ProgramUtil>();
	// 类LayoutInflater用于将一个XML布局文件实例化为一个View对象
	LayoutInflater layoutInflater;
	Context context;	
	// 构造函数，参数为列表对象 和 Context
	public CheckBoxRefreshAdapter(List<ProgramUtil> list, Context context) {
		this.list = list;
		this.context = context;
	}
	public int getCount() {
		return list.size();
	}
	public Object getItem(int position) {
		return list.get(position);
	}
	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ProgramUtil pUtils = list.get(position);
		
		CheckBox cb = (CheckBox)convertView.findViewById(R.id.myCheckBox);
		cb.setTag(position); 
		if (pUtils.getProgramName().charAt(0) == '/') //这是一个文件管理项目的代码，不允许check目录，故隐藏CheckBox  
		{  
			cb.setVisibility(cb.INVISIBLE);  
		}else{  
				cb.setVisibility(cb.VISIBLE);  
				cb.setChecked(list.get(position).getSelected()); //从数据list中恢复checked状态  
			    cb.setOnClickListener(new View.OnClickListener() {  
			    public void onClick(View view) {  
			    	CheckBox cb = (CheckBox)view;  
			    	// 下面直接用View的tag中获取position信息，并修改对应的最初的数据list  
			    	list.get((Integer)view.getTag()).setSelected(cb.isChecked());  
			    	}  
			    });  
		} 		
		return convertView;		
	}

}
