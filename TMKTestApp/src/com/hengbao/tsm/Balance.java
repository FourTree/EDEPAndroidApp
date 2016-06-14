package com.hengbao.tsm;

import java.io.UnsupportedEncodingException;


public class Balance {

	  
	public Balance(){
		byte[] msg ;
		msg = new byte[]{(byte)0x00,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0x3F,(byte)0x00};	
		CommandList.addList(0, msg);
		msg = new byte[]{(byte)0x00,(byte)0xA4,(byte)0x00,(byte)0x00,(byte)0x02,(byte)0xDF,(byte)0x04};	
		CommandList.addList(1, msg);
		msg = new byte[]{(byte)0x80,(byte)0x5c,0x00,0x02,0x04};	
		CommandList.addList(2, msg);
	}
	
	
	/** 
     * getbalance  余额格式转换，显示；例：9F7906 000012345678 9000 其中：000012345678 表示：123456.78
     * @param magdate 命令数据所在集合 ,为ASSIC值
     * @param offest  命令长度字节在集合中的偏移位置 
     */  
	public int getbalance(byte[] appletaid){       
   		//GetSimInfo getSimInfo = new GetSimInfo();
		Result result =	GetSimInfo.DealAPDUs(appletaid);
		System.out.println("指令已发API=GetSimInfo.DealAPDUs()");
		
		GetSimInfo.close();
		System.out.println("关闭SimIfo，");
		System.out.println(result.getResValue());
		if(result.getResulttag())	
		{
			//int balance = Integer.valueOf(result.getResValue(),16);
			int balance = Integer.parseInt(result.getResValue(),16);
			return 	balance	;
		}
		System.out.print(result.getResulttag());
		return -1;
    } 
	
	/** 
     * showbalance  余额格式转换，显示；例：9F7906 000012345678 9000 其中：000012345678 表示：123456.78
     * @param magdate 命令数据所在集合 ,为ASSIC值
     * @param offest  命令长度字节在集合中的偏移位置 
     */  
/*	public void showbalance(byte[] appletaid){       
		Result result =	GetSimInfo.DealAPDUs(appletaid);
		if(result.getResulttag())	
		{
			byte[] temp = new byte[6];
			System.arraycopy(result.getTagvalue(),0, temp, 0, 6);
			String balance = BytetoMoney(temp);
			int balance = Integer.valueOf(result.getResValue(),16);
			ShowDialog.creatDialog(0, "账户余额", "您当前的账户余额为： "+ balance +"元",null);
		}else{
			Util.bytesToString(result.getTagvalue()).replace(" ", "");
			ShowDialog.creatDialog(0, "提示", "账户余额查询失败，请稍后重试！",null);
		}
    } */
	public void showbalance(byte[] appletaid){    
		int balance =  getbalance(appletaid);
		System.out.println("取余额个体balance()返回值");
		System.out.print(balance);
		if(balance < 0)
		{
			ShowDialog.creatDialog(0, "提示", "账户余额查询失败，请稍后重试！",null);
		}else
		{
			ShowDialog.creatDialog(0, "账户余额", "您当前的账户余额为： "+ (balance/100.0) +"元",null);
		}
    } 
	
	
    /**　　
   	* 将一个6 byte的DCD值转为String型；例：000012345678  转换为123456.78
  　　* @param byte[] value   存储ASSIC格式的数组
  　　* @return 一个新的String值
    */
    public String BytetoMoney(byte[] bytevalue)
    {
		String money = null;

		byte[] value = Util.ByteToAscii(bytevalue,bytevalue.length);
		try { 
			String result = new String(value,0,value.length,"utf-8");
			char[] temp = result.toCharArray();
			String lastdate  = String.valueOf(temp, temp.length-2,2);
			int i = 0;
			do{
				if(temp[i]!= '0')
				{
					break;
				}else{
					i += 1;
				}
			}while(i < temp.length-3);
			String frontdata = String.copyValueOf(temp, i, temp.length-i-2);
			money = frontdata.concat(".").concat(lastdate);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return money;
    }
    
}


