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



public class TradeRecordDeal {

	private static boolean issendflag = false;
	private static int currentnumber = 0;
	private static int offest_time;
	private static int offest_date;
	private static int offest_terminalnumber;
	private static int offest_money;
//	private static byte[]recordType;
	private static List<Map> recordlist = new ArrayList<Map>();
	private static Tag[] recordTL; 
	
	public static boolean RecordType(byte[] apdu){
		//判断都有哪些
		//暂时只存储不处理
		if(!recordlist.isEmpty())
		{
			recordlist.clear();
		}
		if((apdu.length <3)||((byte)0x9F != apdu[0] )&&( 0x4F != apdu[1])&&(apdu[2] <= 0))
		{
			return false;
		}
		recordTL = cutTL(apdu);
		return true;
	}
	
	//private void cutTL(String strTL,byte len){
	private static Tag[] cutTL(byte[]apdu){
		Tag[] TL = new Tag[10];
		String strTL = null;
		char[] charTL;
		byte[] asciiTL = Util.ByteToAscii(apdu, apdu.length);
		try {
			String ascii = asciiTL.toString();
			Log.e(" test ", "\n"+"asciiTL.toString()-----"+ ascii);
			strTL = new String(Util.ByteToAscii(apdu, apdu.length),0,apdu.length *2,"utf-8");
			Log.e(" test ", "\n"+"utf-8------------------"+strTL);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i= 0;
		int apduoffset = 3;
		int charoffest = 6;
		charTL = strTL.toCharArray();
		do{
			if(((byte)0x9F == apdu[apduoffset])||(0x5F == apdu[apduoffset]))
			{
				TL[i] = new Tag(String.copyValueOf(charTL, charoffest, 4),apdu[apduoffset+2]); 
				charoffest += 6;
				apduoffset += 3;
			}else{
				TL[i] = new Tag(String.copyValueOf(charTL, charoffest, 2),apdu[apduoffset+1]); ;
				charoffest += 4;
				apduoffset += 2;
			}
			i++;
		}while((i <=9)&&(charoffest < charTL.length));
		return TL;
	}
	
	
	
	public static void saveRecord(byte[] apdu){
		//将apdu转换为String格式
/*		byte[] mapdu = Util.ByteToAscii(apdu, apdu.length);
		String result = null;
		try {
			result = new String(mapdu,0x00,mapdu.length,"utf-8");
			Log.e(" test ", "\n"+"APDU>>>>>>>"+result);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		int offest = 0;
		int i = 0;
		Map<String,byte[]> recordmap = new HashMap<String,byte[]>();
		while((offest < apdu.length)&&(i < recordTL.length))
		{
			int len = recordTL[i].getTaglen();
			byte[] value = new byte[len];
			System.arraycopy(apdu, offest, value,0,len);
			recordmap.put(recordTL[i].getTagtag(), value);
			offest += len;
			i++;
		}
		recordlist.add(recordmap);
	}
	
	public static String[] getRecord(){
		//将apdu转换为String格式
		if(recordlist.isEmpty())
		{
			return null;
		}
		String[] record = new String[recordlist.size()];
		Balance balance = new Balance();
		for(int i = 0; i<recordlist.size();i++)
		{
			Map mtemp = recordlist.get(i);
			byte[] data = (byte[])mtemp.get("9A");
			String strdata = Util.bcd2Str(data);
			byte[] time = (byte[])mtemp.get("9F21");
			String strtime = Util.bcd2Str(time);
			byte[] money = (byte[])mtemp.get("9F02");
			String strmoney = balance.BytetoMoney(money);
			byte[] name = (byte[])mtemp.get("9F4E");
			String strname = null;
			try{
				strname = new String(name,0,name.length,"utf-8");
			}catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*record[i] = strdata.concat(strtime).concat("  ")
					.concat(strmoney).concat("  ").concat(strname);*/
			record[i] = strdata.concat(strtime).concat("    ")
					.concat(strmoney);
		}
		return record;
	}
}

	class Tag{
		private String mtag ;
		private byte mlen;
		public Tag()
		{
			
		}
		public Tag(String tag,byte len)
		{
			this.mtag = tag;
			this.mlen = len;
		}
		public byte getTaglen()
		{
			return mlen;
		}
		public String getTagtag()
		{
			return mtag;
		}
	}



