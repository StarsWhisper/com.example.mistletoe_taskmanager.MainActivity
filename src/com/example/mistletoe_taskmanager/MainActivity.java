package com.example.mistletoe_taskmanager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Debug.MemoryInfo;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;  
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;  
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;  
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends ListActivity {
    //=================================�����Ǳ�����ʼ��=====================================
	//��ȡ���ͻ������ʵ��
	private static PackageManager packageManager = null;
	private static ActivityManager activityManager = null;
	
	//�������еĽ����б��Խ���ProgramUtil��
	private static List<RunningAppProcessInfo> runningProcessList = null;
	private static List<ProgramUtil> infoList = null;
	
	//��ѡ�еĽ��̵�����
	private static RunningAppProcessInfo processSelected = null;
		
	//��ȡӦ�ó���Ļ�����Ϣ���Խ���PackageUtil��
	private static PackageUtil packageUtil = null;
	 
	//ˢ�ºͽ������̰�ť
	private static Button refresh = null;
	private static Button killAll = null;
	
	//��̨ˢ���б����ʾˢ����ʾ���Խ���RefreshHandler��
	private static RefreshHandler handler = null;
	
	//ProgressDialog����ˢ�½�����
	private static ProgressDialog progressDialog =null;
	
	//ListView���������Խ���procListAdapter��
	private static ProcListAdapter procListAdapter = null;
	
//****************************************�¼ӹ��ܣ��ڴ���ʾ********************************************
	private  TextView canUseMemory = null;
//***************************************�¼ӹ��ܣ���������ʾ***************************************
	private  TextView processNumber = null;
//***************************************�¼ӹ��ܣ�ѡ��ȫ����ѡ****************************************
	private static Button chooseAllProcess = null;
	private static Button invertSelectionProcess = null;
	private static checkBoxRefreshHandler checkBoxHandler = null;
	private static CheckBoxRefreshAdapter CheckBoxRefreshAdapter = null;
//*************************************************************************************************
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//****************************************�¼ӹ��ܣ��ڴ���ʾ********************************************
        canUseMemory = (TextView)findViewById(R.id.showMemory); 
//***************************************�¼ӹ��ܣ���������ʾ***************************************
        processNumber = (TextView)findViewById(R.id.showProcessNumber);
//***************************************�¼ӹ��ܣ�ѡ��ȫ����ѡ***************************************        
        chooseAllProcess = (Button)findViewById(R.id.myButton_chooseAll);
        chooseAllProcess.setOnClickListener(new chooseAllButtonListener());
        invertSelectionProcess = (Button)findViewById(R.id.myButton_invertSelection);
        invertSelectionProcess.setOnClickListener(new invertSelectedButtonListener());
//*************************************************************************************************
      
    //==============================���°�������ʵ����==================================================
        refresh = (Button)findViewById(R.id.myButton_refresh);
        //�Խ�ˢ�°�ť��������RefreshButtonListener()
        refresh.setOnClickListener(new refreshButtonListener());
        killAll = (Button)findViewById(R.id.myButton_killAll);
//********************����*******�Խ�������ѡ�С����̰�ť��������KillSelectedButtonListener()*********
        killAll.setOnClickListener(new killSelectedButtonListener());
        
        //��ȡ�����������Ա��ȡ����ͼ�������
        packageManager = this.getPackageManager();
        activityManager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);  //??????????
        //��ȡ������Ϣ
        packageUtil = new PackageUtil(this);
        
        //��ȡ�������еĽ����б�
        runningProcessList = new ArrayList<RunningAppProcessInfo>();
        infoList = new ArrayList<ProgramUtil>();
        
        //���ø����б���
        updateProcessList();
//****************************************�¼ӹ��ܣ��ڴ���ʾ********************************************
        upDateMemInfo();
    }
       

    //���ڸ��½���Ľ���
    class RefreshThread extends Thread {
		@Override
		public void run() {
			procListAdapter = buildProcListAdapter();
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
    
    //��̨ˢ���б����ʾˢ����ʾ�����ı�д 
    private class RefreshHandler extends Handler{
    	public void handleMessage(Message msg){
    		//���½���-ListView���������Խ���procListAdapter��
    		getListView().setAdapter(procListAdapter);
//***************************************�¼ӹ��ܣ���������ʾ***************************************        		
    		upDataProNum();
//***************************************�¼ӹ��ܣ���������ʾ***************************************    
    		//ȡ����ʾ���ȶԻ���
    		progressDialog.dismiss();
    	}
    }
    
    //����ListAdapter
    public ProcListAdapter buildProcListAdapter() {
		//����������еĳ���                            ��������������������������
    	if(!runningProcessList.isEmpty()){
    		runningProcessList.clear();
    	}
    	//��մ�����г��������
    	if(!infoList.isEmpty()){
    		infoList.clear();
    	}
    	//��ȡ�������еĽ���
    	runningProcessList = activityManager.getRunningAppProcesses();
    	RunningAppProcessInfo procInfo = null;
    	for (Iterator<RunningAppProcessInfo> iterator = runningProcessList.iterator(); iterator.hasNext();) {
    		procInfo = iterator.next();                                   //???????????
    		//���������Ϣ�洢�����У��Խ�buildProgramUtilSimpleInfo��ȡӦ�ó��������Ϣ������
    		ProgramUtil programUtil = buildProgramUtilSimpleInfo(procInfo.pid, procInfo.processName);
    		//��������Ϣ��ӵ�������
    		infoList.add(programUtil);
    	}
    	ProcListAdapter adapter = new ProcListAdapter(infoList, this);
    	return adapter;
	}
    
    //��ȡ�ڴ���Ϣ����getUsedMemory
    public String getUsedMemory(int pid)
	{
		//��û������ʵ��
		ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
		//����int����
		int[] pids = {pid};
		MemoryInfo[] memoryInfos =  am.getProcessMemoryInfo(pids);
		//��ý���ռ���ڴ�����������kBֵ
		int memorysize = memoryInfos[0].getTotalPrivateDirty();
		return "�ڴ�ռ��Ϊ "+ memorysize +" KB";
	}
    
    //��ȡӦ�ó��������Ϣ�ĺ���buildProgramUtilSimpleInfo
    public ProgramUtil buildProgramUtilSimpleInfo(int procId, String procNameString) {

		ProgramUtil programUtil = new ProgramUtil();
		programUtil.setProcessName(procNameString);
		
		//���ݽ���������ȡӦ�ó����ApplicationInfo����
		ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(procNameString);

		if (tempAppInfo != null) {
			//Ϊ���̼���ͼ��ͳ�������
			programUtil.setIcon(tempAppInfo.loadIcon(packageManager));
    		programUtil.setProgramName(tempAppInfo.loadLabel(packageManager).toString());
		} 
		else {
			//�����ȡʧ�ܣ���ʹ��Ĭ�ϵ�ͼ��ͳ�����
			programUtil.setIcon(getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher));
			programUtil.setProgramName(procNameString);
		}
		//���ý����ڴ�ʹ�������Խ���ȡ�ڴ���Ϣ������
		String str = getUsedMemory(procId);
		programUtil.setMemString(str);
		return programUtil;
    }


    //ˢ�°�ť���������ı�д
    private class refreshButtonListener implements android.view.View.OnClickListener {
		public void onClick(View v) {
			//�Խ������б���updateProcessList()
			updateProcessList();
//****************************************�¼ӹ��ܣ��ڴ���ʾ********************************************
	        upDateMemInfo();
		}
    }
    
//**************���ģ�*************������ѡ�С����̰�ť���������ı�д*********************************
    private class killSelectedButtonListener implements android.view.View.OnClickListener {
		public void onClick(View v) {
			int count = infoList.size();
//****************************************�¼ӹ��ܣ�ûѡ��ɾ���Ͳ�����******************************
			boolean haveCheckedAtLestOne = false;
//*************************************************************************************************
			ProgramUtil pu = null;
			//�������н��̣�����ر�
			for (int i = 0; i < count; i++) {
				pu = infoList.get(i);
//****************************************�¼ӹ��ܣ��ڴ���ʾ********************************************
				if(pu.getSelected()){
				haveCheckedAtLestOne = true;
//************************************************************************************************
				//�Խ��ر�ָ�����̺���closeOneProcess
				closeOneProcess(pu.getProcessName());
				}
			}
			if(haveCheckedAtLestOne == true){
			//�����б�
			updateProcessList();
//****************************************�¼ӹ��ܣ��ڴ���ʾ********************************************
	        upDateMemInfo();
	        Toast.makeText(MainActivity.this, "��ʾ���޷���ֹĳЩϵͳ����", Toast.LENGTH_LONG).show();
			}
//**************************ûѡ��ɾ���Ͳ����µ���ʾ*****************************************************
			else
				Toast.makeText(MainActivity.this, "����ѡ����Ҫ����Ľ���", Toast.LENGTH_LONG).show();
		}
    }
    
  //�б���������
  	@Override
  	protected void onListItemClick(ListView l, View v, int position, long id) {
  		//��õ�ǰѡ�еĽ���
      	processSelected = runningProcessList.get(position);
      	//�½��Ի���
      	AlertButtonListener listener = new AlertButtonListener();
      	Dialog alertDialog = new AlertDialog.Builder(this)
      		.setIcon(android.R.drawable.ic_dialog_info)
      		.setTitle("��ѡ��")
      		.setNegativeButton("ǿ�ƽ���", listener)
      		.setNeutralButton("�鿴����", listener).create();
      	alertDialog.show();
      	super.onListItemClick(l, v, position, id);
  	}
  	
  	private class AlertButtonListener implements 
	android.content.DialogInterface.OnClickListener {
  		//��������
  		public void onClick(DialogInterface dialog, int which) {
  			switch (which) {
  			case Dialog.BUTTON_NEUTRAL:
  				Intent intent = new Intent();
  				intent.setClass(MainActivity.this, ProcDetailActivity.class);
  				// Ϊѡ�еĽ��̻�ȡ��װ������ϸ��Ϣ
  				DetailProgramUtil programUtil = buildProgramUtilComplexInfo(processSelected.processName);
  				if (programUtil == null) {
  					break;
  				}
  				Bundle bundle = new Bundle();
  				// ʹ��Serializable��Activity֮�䴫�ݶ���
  				bundle.putSerializable("process_info", (Serializable)programUtil);
  				intent.putExtras(bundle);
  				//�򿪽�����ϸ��Ϣ����
  				startActivity(intent);				
  				break;
  			case Dialog.BUTTON_NEGATIVE:
  				//��������
  				closeOneProcess(processSelected.processName);
  				//���½���
  				updateProcessList();
//****************************************�¼ӹ��ܣ��ڴ���ʾ********************************************
  		        upDateMemInfo();
  		      if (processSelected.processName.equals("com.example.mistletoe_taskmanager")) {
  				Toast.makeText(MainActivity.this, "�޷���������������", Toast.LENGTH_LONG).show();
  			  }else
  		        Toast.makeText(MainActivity.this, "��ʾ��ĳЩϵͳ���̿����޷���ֹ", Toast.LENGTH_LONG).show();
  				break;
  			default:
  				break;
  			}
  		}
  	}
  	
  //�ر�ָ�����̺���closeOneProcess
    private void closeOneProcess(String processName) {
    	//��ֹ�û�����������
		if (processName.equals(R.string.class)) {		
			Toast.makeText(MainActivity.this, "���ܹرձ���������!", Toast.LENGTH_LONG).show();
			return;
		}
		//ͨ��һ�����������ظó����һ��ApplicationInfo����
		ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(processName);
		if (tempAppInfo == null) {
			return;
		}
		//���ݰ����رս���
		activityManager.killBackgroundProcesses(tempAppInfo.packageName);
    }
    
    /*
	 * Ϊ���̻�ȡ��װ��������
	 */
    public DetailProgramUtil buildProgramUtilComplexInfo(String procNameString) {

    	DetailProgramUtil complexProgramUtil = new DetailProgramUtil();
		// ���ݽ���������ȡӦ�ó����ApplicationInfo����
		ApplicationInfo tempAppInfo = packageUtil.getApplicationInfo(procNameString);
		if (tempAppInfo == null) {
			return null;
		}
		
		PackageInfo tempPkgInfo = null;
		try {
			tempPkgInfo = packageManager.getPackageInfo(
					tempAppInfo.packageName, 
					PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_ACTIVITIES
					| PackageManager.GET_SERVICES | PackageManager.GET_PERMISSIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if (tempPkgInfo == null) {
			return null;
		}
		
		complexProgramUtil.setProcessName(procNameString);
		complexProgramUtil.setCompanyName("����");
		complexProgramUtil.setVersionName(tempPkgInfo.versionName);
		complexProgramUtil.setVersionCode(tempPkgInfo.versionCode);
		complexProgramUtil.setDataDir(tempAppInfo.dataDir);
		complexProgramUtil.setSourceDir(tempAppInfo.sourceDir);
		complexProgramUtil.setPackageName(tempPkgInfo.packageName);
		// ��ȡ����������Ϣ����ҪΪPackageManager������Ȩ(packageManager.getPackageInfo()����)
		complexProgramUtil.setUserPermissions(tempPkgInfo.requestedPermissions);
		complexProgramUtil.setServices(tempPkgInfo.services);
		complexProgramUtil.setActivities(tempPkgInfo.activities);
		
		return complexProgramUtil;
    }
    
  //�����б����ı�д
    private void updateProcessList(){
    	progressDialog = new ProgressDialog(MainActivity.this);
    	progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
    	progressDialog.setTitle("��ʾ");
    	progressDialog.setMessage("����ˢ��...");
    	
    //�������̣߳�ִ�и��²������Խ�RefreshThread�ࣩ
    RefreshThread thread = new RefreshThread();
    handler = new RefreshHandler(); 
    thread.start();
    //��ʾ���ȶԻ���
    progressDialog.show();
    }
    
//****************************************�¼ӹ��ܣ��ڴ���ʾ********************************************
    public void upDateMemInfo(){              
    	        //���MemoryInfo����    
    	        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();  
    	        //���ϵͳ�����ڴ棬������MemoryInfo������    
    	        activityManager.getMemoryInfo(memoryInfo) ;    
    	        long memSize = memoryInfo.availMem ;              
    	        //�ַ�����ת��   
    	        String leftMemSize = Formatter.formatFileSize(getBaseContext(), memSize);  
    	        canUseMemory.setText(leftMemSize);  
    	    }  

//***************************************�¼ӹ��ܣ���������ʾ***************************************    
	public void upDataProNum(){	
	    int count1 = infoList.size();
	    processNumber.setText(Integer.toString(count1));
   }

//***************************************�¼ӹ��ܣ�ѡ��ȫ����ѡ***************************************        
	private class chooseAllButtonListener implements android.view.View.OnClickListener {
		public void onClick(View v) {
			int count = infoList.size();
			ProgramUtil pu = null;
			for (int i = 0; i < count; i++) {
				pu = infoList.get(i);
				pu.setSelected(true);
				}
			updateListViewCheckBox();
			}
		}
	private class invertSelectedButtonListener implements android.view.View.OnClickListener {
		public void onClick(View v) {
			int count = infoList.size();
			ProgramUtil pu = null;
			for (int i = 0; i < count; i++) {
				pu = infoList.get(i);
				if(pu.getSelected()){
				pu.setSelected(false);
				}
				else
					pu.setSelected(true);
			}
			updateListViewCheckBox();
		}
	}
//*********************************�¼ӹ��ܣ�checkBoxˢ�¹���**************************************	
	private class checkBoxRefreshHandler extends Handler{
	    	public void handleMessage(Message msg){
	    		//���½���-ListView���������Խ���CheckBoxRefreshAdapter��
	    		getListView().setAdapter(CheckBoxRefreshAdapter);
	    	}
	    }
	
    class CheckBoxRefreshThread extends Thread {
		@Override
		public void run() {
			CheckBoxRefreshAdapter = buildCheckBoxRefreshAdapter();
			Message msg = handler.obtainMessage();
			handler.sendMessage(msg);
		}
	}
	   
	private void updateListViewCheckBox(){
	CheckBoxRefreshThread checkBoxThread = new CheckBoxRefreshThread();
    //�������̣߳�ִ�и��²������Խ�RefreshThread�ࣩ
    checkBoxHandler = new checkBoxRefreshHandler(); 
    checkBoxThread.start();
    }
	
	public CheckBoxRefreshAdapter buildCheckBoxRefreshAdapter() {
		CheckBoxRefreshAdapter checkAdapter = new CheckBoxRefreshAdapter(infoList, this);
    	return checkAdapter;
	}
	
}	
