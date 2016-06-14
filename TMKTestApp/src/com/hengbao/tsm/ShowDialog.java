package com.hengbao.tsm;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.MediaStore.Images;



public class ShowDialog{
	
	private static Context mContext;
	private static String strTitle;
	private static String strMsg;
	private static String[] stritem;
	private static int returnvalue;
	
	private static final int CLICK_OK = 0;
	private static final int CLICK_NO = 1;
	private static final int CLICK_CANCEL = 2;
	private static final int DIALOG_SHOWMESSAGE = 0;
	private static final int DIALOG_CHOOSE = 2;
	private static final int DIALOG_SHOWRECORD = 3;
	private static final int DIALOG_PROGRESSDIALOG = 4;
	private static AlertDialog alertDialog;
	private static ProgressDialog progressDialog;

	public static int creatDialog(int type,String title,String message,String[]item)
	{
		mContext = ExitApplication.getInstance().getActivity();
		strTitle = title ;
		strMsg = message;
		stritem = item;
		cancelDialog();
		onCreateDialog(type);
		return returnvalue;
	}
	
	protected static void onCreateDialog(int id){
        // TODO Auto-generated method stub
		switch(id){
			case DIALOG_SHOWMESSAGE:
				try {
					alertDialog = new AlertDialog.Builder(mContext)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle(strTitle)
					.setMessage(strMsg)
					.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							// TODO Auto-generated method stub
							returnvalue = CLICK_OK;
							dialog.cancel();  
						}
					}).create();
				} catch (Exception e) {
					e.printStackTrace();
				}
				alertDialog.show();
				break;
			case DIALOG_SHOWRECORD:
				try {
					alertDialog = new AlertDialog.Builder(mContext)
					.setIcon(android.R.drawable.ic_dialog_info)
					.setTitle(strTitle)
					.setItems(stritem, new DialogInterface.OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
						}})
					.setPositiveButton("确定", 
					new DialogInterface.OnClickListener() {						
						@Override
						public void onClick(DialogInterface dialog, int whichButton) {
							// TODO Auto-generated method stub
							returnvalue = CLICK_OK;
							dialog.cancel();  
						}
					}).create();
				} catch (Exception e) {
					e.printStackTrace();
				}
				alertDialog.show();
				break;
			case DIALOG_CHOOSE:
				alertDialog = new AlertDialog.Builder(mContext)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(strTitle)
				.setMessage(strMsg)
				.setPositiveButton("确定", 
				new DialogInterface.OnClickListener() {						
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// TODO Auto-generated method stub
						returnvalue = CLICK_OK;
						dialog.cancel();  
					}
				}).setNegativeButton("取消", 
				new DialogInterface.OnClickListener() {						
					@Override
					public void onClick(DialogInterface dialog, int whichButton) {
						// TODO Auto-generated method stub
						returnvalue = CLICK_CANCEL;
						dialog.cancel();  
					}
				}).create();
				alertDialog.show();
				break;	
			case DIALOG_PROGRESSDIALOG:
					progressDialog = ProgressDialog.show( mContext,strTitle, strMsg,true);  
			   	    progressDialog.setCancelable(false);//当点击按钮返回的时候Dialog消失 
			default:
				return;
		}
	}
	
	public static void cancelalertDialog() {
		// TODO Auto-generated method stub
		if((alertDialog != null)&&alertDialog.isShowing() )
		{
			alertDialog.cancel(); 
		} 
	}
	
	public static void cancelprogressDialog() {
		// TODO Auto-generated method stub
		if((progressDialog != null) && progressDialog.isShowing())
		{
			progressDialog.cancel(); 
		}
 
	}
	
	public static void cancelDialog() {
		// TODO Auto-generated method stub
		if((alertDialog != null)&&alertDialog.isShowing() )
		{
			alertDialog.cancel(); 
		} 
		if((progressDialog != null) && progressDialog.isShowing())
		{
			progressDialog.cancel(); 
		}
 
	}
	
	private int setReturnValue(int returnvalue) {
		// TODO Auto-generated method stub
		return returnvalue;
	}



	
	/**
     * Function to display ProgressDialog
     * @param context - application context
     * @param title - Progress dialog title
     * @param message - Progress message
     * @param message - Progress image
     * */
/*	public static ProgressDialog CreatCircularDialog(Context context,String strTitle,String strMsg,int imageid)
	{
	    ProgressDialog m_pDialog;

        // TODO Auto-generated method stub
        
        //创建ProgressDialog对象
        m_pDialog = new ProgressDialog(context);

        // 设置进度条风格，风格为圆形，旋转的
        m_pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        // 设置ProgressDialog 标题
        m_pDialog.setTitle(strTitle);
      
        // 设置ProgressDialog 提示信息
        m_pDialog.setMessage(strMsg);

        // 设置ProgressDialog 标题图标
        //m_pDialog.setIcon(R.drawable.ic_1);
        m_pDialog.setIcon(imageid);

        // 设置ProgressDialog 的进度条是否不明确
        m_pDialog.setIndeterminate(false);
      
        // 设置ProgressDialog 是否可以按退回按键取消
        //m_pDialog.setCancelable(true);
        m_pDialog.setCancelable(false);
      
        // 设置ProgressDialog 的一个Button
        m_pDialog.setButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i)
            {
                //点击“确定按钮”取消对话框
                dialog.cancel();
            }
        });

        // 让ProgressDialog显示
        m_pDialog.show();

		return m_pDialog;

	}*/



}

