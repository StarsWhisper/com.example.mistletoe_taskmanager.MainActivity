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
	// ��LayoutInflater���ڽ�һ��XML�����ļ�ʵ����Ϊһ��View����
	LayoutInflater layoutInflater;
	Context context;	
	// ���캯��������Ϊ�б���� �� Context
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
		if (pUtils.getProgramName().charAt(0) == '/') //����һ���ļ�������Ŀ�Ĵ��룬������checkĿ¼��������CheckBox  
		{  
			cb.setVisibility(cb.INVISIBLE);  
		}else{  
				cb.setVisibility(cb.VISIBLE);  
				cb.setChecked(list.get(position).getSelected()); //������list�лָ�checked״̬  
			    cb.setOnClickListener(new View.OnClickListener() {  
			    public void onClick(View view) {  
			    	CheckBox cb = (CheckBox)view;  
			    	// ����ֱ����View��tag�л�ȡposition��Ϣ�����޸Ķ�Ӧ�����������list  
			    	list.get((Integer)view.getTag()).setSelected(cb.isChecked());  
			    	}  
			    });  
		} 		
		return convertView;		
	}

}
