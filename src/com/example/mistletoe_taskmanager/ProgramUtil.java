package com.example.mistletoe_taskmanager;
import android.graphics.drawable.Drawable;

public class ProgramUtil{
	/*
	 * ����Ӧ�ó���ļ�Ҫ��Ϣ����
	 */
	private Drawable icon;							//����ͼ��
	private String programName;						//��������
	private String processName;						//��������
	private String memString;                       //�ڴ�ռ�ô�С	
//**************************************�¼ӹ��ܣ�ѡ��ɾ��******************************************
	private boolean selected;  
//**************************************�¼ӹ��ܣ�ѡ��ɾ��******************************************
	
	//��ʼ������
	public ProgramUtil() {
		icon = null;
		programName = "";
		processName = "";
		memString = "";
	}
	//���ͼ��
	public Drawable getIcon() {
		return icon;
	}
	//����ͼ��
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	//���Ӧ�ó�������
	public String getProgramName() {
		return programName;
	}
	//����Ӧ�ó�������
	public void setProgramName(String programName) {
		this.programName = programName;
	}
	//��ó���ռ���ڴ��С
	public String getMemString() {
		return memString;
	}
	//���ó���ռ���ڴ��С
	public void setMemString(String memString) {
		this.memString = memString;
	}
	//��ó�������
	public String getProcessName() {
		return processName;
	}
	//���ó�������
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	
//**************************************�¼ӹ��ܣ�ѡ��ɾ��******************************************
	public boolean getSelected(){
		return selected;
	}
	public void setSelected(boolean selected){
		this.selected = selected;
	}
}
