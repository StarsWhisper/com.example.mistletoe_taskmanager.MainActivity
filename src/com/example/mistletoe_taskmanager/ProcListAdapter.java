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

public class ProcListAdapter extends BaseAdapter{
	List<ProgramUtil> list = new ArrayList<ProgramUtil>();
	// 类LayoutInflater用于将一个XML布局文件实例化为一个View对象
	LayoutInflater layoutInflater;
	Context context;
	
	// 构造函数，参数为列表对象 和 Context
	public ProcListAdapter(List<ProgramUtil> list, Context context) {
		this.list = list;
		this.context = context;
	}
	//获得列表元素总数
	public int getCount() {
		return list.size();
	}
	//获得列表元素
	public Object getItem(int position) {
		return list.get(position);
	}
	//获得列表元素的id
	public long getItemId(int position) {
		return position;
	}
	// 获取显示数据的列表的View对象
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if (convertView == null) {
			// 从给定的context中获取LayoutInflater对象
			layoutInflater = LayoutInflater.from(context);
			// 方法inflate() - 从特定的XML布局文件inflate膨胀出一个新的视图
			convertView = layoutInflater.inflate(R.layout.list_layout, null);
			
			holder = new ViewHolder();
			holder.image = (ImageView)convertView.findViewById(R.id.image_app);
			holder.nameText = (TextView)convertView.findViewById(R.id.name_app);
			holder.processName = (TextView)convertView.findViewById(R.id.package_app);
			holder.memInfo = (TextView)convertView.findViewById(R.id.cpu_app);
			
			// 为一个View添加标签，标签项在类ViewHolder中定义，可以看作是一个绑定操作
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
				
		final ProgramUtil pUtils = list.get(position);
		// 设置图标
		holder.image.setImageDrawable(pUtils.getIcon());
		// 设置程序名
		holder.nameText.setText(pUtils.getProgramName());
		// 设置包名
		holder.processName.setText(pUtils.getProcessName());
		// 设内存信息
		holder.memInfo.setText(pUtils.getMemString());
		
//********新加功能：选择删除****直接在View中添加tag来保存position信息********************************
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
//***********************************************************************************************
		
		return convertView;
	}
}
//用于存放每一行所有元素的类  用于记录，不然图片等也会错乱
class ViewHolder {
	TextView nameText;
	TextView processName;
	TextView memInfo;
	ImageView image;
}