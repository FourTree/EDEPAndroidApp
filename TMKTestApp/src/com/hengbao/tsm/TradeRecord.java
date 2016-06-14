package com.hengbao.tsm;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



public class TradeRecord {

/*	private static boolean issendflag = false;
	private static int currentnumber = 0;
	private static int offest_time;
	private static int offest_date;
	private static int offest_terminalnumber;
	private static int offest_money;
	private static byte[]recordType;
	private static List<Map> recordlist = new ArrayList<Map>();*/
	private final int SUMRECORD = 10;
	private static List<String> RECORD = new ArrayList<String>();

	public TradeRecord(){
		if(!RECORD.isEmpty()){
			RECORD.clear();
		}
		byte[] msg ;
		msg = new byte[]{(byte)0x00,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x3F,(byte)0x00};	
		CommandList.addList(0, msg);
		msg = new byte[]{(byte)0x00,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0xDF,(byte)0x04};	
		CommandList.addList(1, msg);
		msg = new byte[]{(byte)0x00,(byte)0x20,(byte)0x00,(byte)0x00,(byte)0x03,(byte)0x88,(byte)0x88,(byte)0x88};	
		CommandList.addList(2, msg);
	}
	
	/** 
     * 
     */  
	public void getTradeRecord(byte[] appletaid){       
   		//GetSimInfo getSimInfo = new GetSimInfo();
		Result result =	GetSimInfo.DealAPDUs(appletaid);
		if(result.getResulttag())	
		{
			byte[] apdu = new byte[]{(byte)0x00,(byte)0xB2,(byte)0x00,(byte)0xC4,(byte)0x17};	
			for(int i=0;i<SUMRECORD;i++)
			{
				apdu[2] = (byte)(i+1);
				result = GetSimInfo.DealAPDU(apdu);
				if(!result.getResulttag())
				{
					ShowDialog.creatDialog(0, "提示", "读取交易记录失败！",null);
					GetSimInfo.close();
					return;
				}
				if(result.getResValue().equals("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF") || result.getSW().equals("6a83")){
					if(i == 0){
						ShowDialog.creatDialog(0, "提示", "卡内暂无交易记录！",null);
						GetSimInfo.close();
						return;
					}
					break;
				}	
				saveRecord(result.getResValue(),i);
			}
			GetSimInfo.close();
			String[] record = new String[RECORD.size()];
			for(int i=0;i<RECORD.size();i++)
			{
				record[i] = RECORD.get(i);
			}
			ShowDialog.creatDialog(3, "账户交易明细", null,record);
		}else{
			GetSimInfo.close();
			Util.bytesToString(result.getTagvalue()).replace(" ", "");
			ShowDialog.creatDialog(0, "提示", "账户交易记录查询失败！",null);
		}
    } 
	
	private void saveRecord(String record,int i){
		//将apdu转换为String格式
		if(record.isEmpty())
		{
			return;
		}
		String yy = record.substring(32,36);//年
		String mm = record.substring(36,38);//月
		String dd = record.substring(38,40);//日
		String tp =  record.substring(40, record.length());
		String time = tp.substring(0,2)+":"+ tp.substring(2,4)+":"+tp.substring(4,6);//时间
		String money =  record.substring(10,18);//金额，十六进制字符串
		//var temp = parseInt(money,16);   //"AF" returns   175
		int tpmoney = Integer.valueOf(money,16);//金额，十进制数字
		String type = record.substring(18, 20);
		String temp = yy+"年"+mm+"月"+dd+"日"+" "+time+(type.equals("02")? "   充值金额":"   消费金额：")+(tpmoney/100.0)+"元";
		RECORD.add(temp);
		return;
	}
	
/*	private  String[] getRecord(){
		
		String[] record = new String[RECORD.size()];
		
		return record;
	}*/
	
}



