package com.example.mistletoe_taskmanager;
import android.graphics.drawable.Drawable;

public class ProgramUtil{
	/*
	 * 定义应用程序的简要信息部分
	 */
	private Drawable icon;							//程序图标
	private String programName;						//程序名称
	private String processName;						//进程名称
	private String memString;                       //内存占用大小	
//**************************************新加功能：选择删除******************************************
	private boolean selected;  
//**************************************新加功能：选择删除******************************************
	
	//初始化变量
	public ProgramUtil() {
		icon = null;
		programName = "";
		processName = "";
		memString = "";
	}
	//获得图标
	public Drawable getIcon() {
		return icon;
	}
	//设置图标
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	//获得应用程序名称
	public String getProgramName() {
		return programName;
	}
	//设置应用程序名称
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	//获得程序占用内存大小
	public String getMemString() {
		return memString;
	}
	//设置程序占用内存大小
	public void setMemString(String memString) {
		this.memString = memString;
	}
	//获得程序名称
	public String getProcessName() {
		return processName;
	}
	//设置程序名称
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
//**************************************新加功能：选择删除******************************************
	public boolean getSelected(){
		return selected;
	}
	public void setSelected(boolean selected){
		this.selected = selected;
	}
}
