package com.hengbao.tsm;

import java.io.IOException;
import org.simalliance.openmobileapi.Channel;
import org.simalliance.openmobileapi.Reader;
import org.simalliance.openmobileapi.SEService;
import org.simalliance.openmobileapi.Session;

import android.util.Log;




public class GetSimInfo implements SEService.CallBack{
	
	final String LOG_TAG = "GetSimCardInfo.java";
	public  static SEService seService;
	private static Session session = null;
	private static Channel channel = null;
	/**
     * Callback interface if informs that this SEService is connected to the SmartCardService
     */
	@Override
	public void serviceConnected(SEService service) {
        	seService = service;
            if (!seService.isConnected())
            {
            	ShowDialog.creatDialog(0, "提示", "SE is SIM or UICC so select container",null);
            }
            creatsession();
    }

   	public GetSimInfo() {

    }
  	
   	private void creatsession() {
		// TODO Auto-generated method stub
    	Reader[] readers = seService.getReaders();
		int i = 0;  
        for (Reader reader : readers)
        {
        	if(reader.getName().substring(0,3).equals("SIM"))
	   		{
	   			 break;
	   		}
        	i += 1;
        }
        if ((readers.length == 0)||(i >= readers.length)) {
                return;
        }
        try {
        	session = readers[i].openSession();
 		} 
        catch (IOException e) {  	 
 		
 		}
        if(session == null)
        {
        	ShowDialog.creatDialog(0, "提示", "SE is SIM or UICC so select container",null);
        }  
	}

   	public static  byte[] testChanneltransmit(byte[] cmd) {
		byte[] rsp = null;
		try {
			rsp = channel.transmit(cmd);
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return rsp;
    }
	
	public static void close() {
		if (channel != null && (!channel.isClosed())) {
			 channel.close();
		}
    }
	
/*	
	private static byte[]  ReadFileIccid() {
		
		byte[] iccid = null;
		String icc = null;
		try {
			byte[] cmd = new byte[] {(byte)0xA0,(byte)0xA4,0x00,0x00,0x02,0x3f,(byte)0x00};
			iccid = testChanneltransmit(cmd);
			icc = Util.bytesToString(iccid);
			
			iccid = testChanneltransmit(new byte[]{(byte)0xA0,(byte)0xA4,0x00,0x00,0x02,0x2F,(byte)0xE2});
			icc = Util.bytesToString(iccid);
			
			iccid = testChanneltransmit(new byte[]{(byte)0xA0,(byte)0xb0,0x00,0x00,0x0a});
			icc = Util.bytesToString(iccid);
        } catch (Exception e) {
        	e.printStackTrace();
        }
		return iccid;
    }*/
	
	public static boolean testLogicalChannel(byte[] aid) {
        try {
        	channel = session.openLogicalChannel(aid);
        } catch (Exception e) {
        	e.printStackTrace();
        	return false;
        }
        return true;
    }
 	
 	public static Result DealAPDUs(byte[] aid){
		byte[] rsp = null;
		boolean result = true;
		String response = null;
		String resValue  = null;
		if(!testLogicalChannel(aid))
		{
			CommandList.clearList();
			return new Result(false,aid,null,null);
		}
		for (int i = 1; i <= CommandList.getListSize(); i++) {
			
Log.i("log----test","\n\n\n\n"+"cmdmap ---->: "+ i+"-----"+ Util.bytesToString(CommandList.getList(i-1)) +"\n\n\n\n"); 	
//			String cmddd =  Util.bytesToString(CommandList.getList(i-1));
/*			byte currentcmdins = cmdmap.get(i)[1];
			rsp = GetSimInfo.testChanneltransmit(cmdmap.get(i));*/
			
			byte currentcmdins = CommandList.getList(i-1)[1];
			rsp = testChanneltransmit(CommandList.getList(i-1));
Log.i("log----res","\n\n\n\n"+"cmdmap ---->: "+ i+"-----"+ rsp +"\n\n\n\n");			
			response = Util.bytesToString(rsp).replace(" ", "");
Log.i("log----res","\n\n\n\n"+"cmdmap ---->: "+ i+"-----"+ response +"\n\n\n\n");			
			if("61".equals(response.substring(response.length()-4,response.length()-2)))
			{
				byte[] rsplen = response.substring(response.length()-2,response.length()).getBytes();
				byte len = Util.uniteBytes(rsplen[0], rsplen[1]);
				rsp = testChanneltransmit(new byte[] {(byte)0x00,(byte)0xc0,(byte)0x00,(byte)0x00,len });
//				System.out.println("(byte)0x00,(byte)0xc0,(byte)0x00,(byte)0x00"+len+";res="+rsp);
				response = Util.bytesToString(rsp).replace(" ", "");
//				System.out.println("(byte)0x00,(byte)0xc0,(byte)0x00,(byte)0x00"+len+";res="+response);
			}
			resValue = response.substring(0,response.length()-4);
			response = response.substring(response.length()-4,response.length());
			if(!"9000".equals(response))
			{
				if(currentcmdins == (byte)0xE4)
				{
					response.replace("A", "a");
					if("6a88".equals(response))
					{
						continue;
					}
				}
				result = false;
				break;
			}
		}
//Log.i("LOG_TAG","\n\n\n\n"+"---->msgcmd ---->: "+ Util.bytesToString(rsp) +"\n\n\n\n"); 
		//System.arraycopy(rsp, rsp.length-2, cmdresponse, 0, 2);
//Log.i("LOG_TAG","\n\n\n\n"+"---->msgcmd ---->: "+ Util.bytesToString(msgcmd) +"\n\n\n\n"); 	
		//close();
		CommandList.clearList();
		return new Result(result,rsp,response,resValue);
	}
 	
 	
 	public static Result DealAPDU(byte[] apdu){
		byte[] rsp = null;
		boolean result = true;
					
		byte currentcmdins = apdu[1];
		rsp = testChanneltransmit(apdu);
		String response = Util.bytesToString(rsp).replace(" ", "");
		if("61".equals(response.substring(response.length()-4,response.length()-2)))
		{
			byte[] rsplen = response.substring(response.length()-2,response.length()).getBytes();
			byte len = Util.uniteBytes(rsplen[0], rsplen[1]);
			rsp = testChanneltransmit(new byte[] {(byte)0x00,(byte)0xc0,0x00,0x00,len });
			response = Util.bytesToString(rsp).replace(" ", "");
		}
		String resValue = response.substring(0,response.length()-4);
		response = response.substring(response.length()-4,response.length());
		if(!"9000".equals(response))
		{
			result = false;
			if(currentcmdins == (byte)0xE4)
			{
				response.replace("A", "a");
				if("6a88".equals(response))
				{
					result = true;
				}
			}
			if(currentcmdins == (byte)0xB2)
			{
				response.replace("A", "a");
				if("6a83".equals(response))
				{
					result = true;
				}
			}
		}
//Log.i(LOG_TAG,"\n\n\n\n"+"---->msgcmd ---->: "+ Util.bytesToString(rsp) +"\n\n\n\n"); 
		//System.arraycopy(rsp, rsp.length-2, cmdresponse, 0, 2);
Log.i("LOG_TAG","\n\n\n\n"+"---->msgcmd ---->: "+ Util.bytesToString(rsp) +"\n\n\n\n"); 	
		return new Result(result,rsp,response,resValue);
	}
}


class Result{
	private boolean mtag ;
	private byte[] mvalue;
	private String mresSW;
	private String mresValue;
	public Result()
	{
	}
	public Result(boolean tag,byte[] value,String sw ,String resValue)
	{
		this.mtag = tag;
		this.mvalue = value;
		this.mresSW = sw;
		this.mresValue = resValue;
	}
	public byte[] getTagvalue()
	{
		return mvalue;
	}
	public boolean getResulttag()
	{
		return mtag;
	}
	public String getSW()
	{
		return mresSW;
	}
	public String getResValue()
	{
		return mresValue;
	}
}