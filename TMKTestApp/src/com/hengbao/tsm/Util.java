package com.hengbao.tsm;

import java.io.UnsupportedEncodingException;

public class Util {

    /*
     * String->byte[]
     */
    public static byte[] String2Bytes(String s) {
        byte[] returnBytes;
        int nlen = (s.length() + 1) / 2;
        returnBytes = new byte[nlen];//
        for (int i = 0; i < nlen; i++) {
            int temp = Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
            returnBytes[i] = new Integer(temp).byteValue();
        }
        return returnBytes;
    }
    
    /*
     * String->~byte[]
     */
    public static byte[] String2NotBytes(String s) {
        byte[] returnBytes;
        int nlen = (s.length() + 1) / 2;
        returnBytes = new byte[nlen];//
        for (int i = 0; i < nlen; i++) {
            int temp = Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
            returnBytes[i] = new Integer(temp).byteValue();
            returnBytes[i] = (byte) ~returnBytes[i];
        }
        return returnBytes;
    }
    
  
    
    public static String b2hex(byte byte0) {
        String s = Integer.toHexString(byte0 & 0xff);
        String s1;
        if (s.length() == 1)
            s1 = "0".concat(s);
        else
            s1 = s;
        return s1.toUpperCase();
    }
    
    public static String buf2hex(byte abyte0[]) {
        String s = new String();
        for (int i = 0; i < abyte0.length; i++)
            s = s.concat(b2hex(abyte0[i]));

        s = s.toUpperCase();
        // s= s.toLowerCase();
        return s;
    }

    public static String buf2hex(byte abyte0[], int len) {
        String s = new String();
        for (int i = 0; i < len; i++)
            s = s.concat(b2hex(abyte0[i]));

        // s = s.toUpperCase();
        s = s.toLowerCase();
        return s;
    }
    
 // ASCII码转换为hex字节，len是输入数据的长度
    public static byte[]  AsciiToByte( byte[]indata,int len)
    {
    	int x, y, n;
    	byte[] outdata = new byte[len];
    	
    	y = 0;
    	for (x=0; x<len; x++)
    	{
    		if ((indata[x] >= 0x30) && (indata[x] <= 0x39))
    			n = (byte) (indata[x] - 0x30);
    		else if ((indata[x] >= 0x41) && (indata[x] <= 0x46))
    			n = (byte) (indata[x] - 0x37);
    		else
    			n = 0x00;
    		
    		if ((x % 2) == 0)
    			outdata[y] = (byte) ((n << 4) | 0x0F);
    		else
    		{
    			outdata[y] &= 0xF0;
    			outdata[y] |= n;
    			y++;
    		}
    	}
    	byte[] resultdata = new byte[y];
    	System.arraycopy(outdata, 0x00, resultdata, 0x00,y);
    	return resultdata;
    }

    // hex字节转换为ASCII码，len是输入数据的长度
    public static byte[] ByteToAscii( byte[]indata,int len)
    {
    	int x, y, n;
    	byte[] outdata = new byte[len*2];
    	x = 0;
    	for (y=0; y<(len*2); y++)
    	{
    		if ((y % 2) == 0)
    		{
    			n = (byte) (indata[x] >> 4);
    		}
    		else
    		{
    			n = (byte) (indata[x] & 0x0F);
    			x++;
    		}
    		if(n < 0)
    		{
    			n = (byte) (n + 16);
    		}
    		if ((n >= 0x00) && (n <=0x09))
    			outdata[y] = (byte) (n + 0x30);
    		else if ((n >= 0x0A) && (n <= 0x0F))
    			outdata[y] = (byte) (n + 0x37);
    		else
    			outdata[y] = 0x00;
    	}
		return outdata;
    }

/*    //
    private static byte uniteBytes(byte src0, byte src1) {
    	  byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
    	    .byteValue();
    	  _b0 = (byte) (_b0 << 4);
    	  byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
    	    .byteValue();
    	  byte ret = (byte) (_b0 ^ _b1);
    	  return ret;
    }*/
    
    
    
    /**　　
   	* 将一个ascii型数组转为hex型数组；
  	*@param srcdata   ascii型数组
  　    * @return result   转换后的hex型数组
     * @param len 
    */
    public static byte[] AsciiToHEX( byte[]  srcdata, int len) {
    	
    	byte[] result = new byte[len];
    	
    	for(int i=0; i<len; i++)
    	{
    		result[i] = (byte) (srcdata[i]- 0x30);
    	}
    	
		return result;
    }
    
    /**　　
   	* 将一个hex型数组转为ascii型数组；
  	*@param srcdata   hex型数组
  　    * @return result   转换后的ascii型数组
    */
    public static byte[] HEXToAscii( byte[]  srcdata, int len) {
    	
    	if(srcdata == null)
    	{
    		return null;
    	}
    	byte[] result = new byte[len];
    	
    	for(int i=0; i<len; i++)
    	{
    		result[i] = (byte) (srcdata[i] + 0x30);
    	}
    	
		return result;
    }
    

	/* ** 
     * bytesToString
     * @param byte[] bytes 
     * @return String
     */  
	public static String bytesToString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes)
                sb.append(String.format("%02x ", b & 0xFF));
        
        return sb.toString();
    }
	
	  public static byte[] bytesXORBytes(byte[] src,byte[] src2){       
	    	   if(src.length != src2.length)
	    		   return null;
	    	   byte[] res = new byte[src.length];
	    	   for(int i=0;i<src.length;i++)
	    	   {
	    		   res[i] =(byte) ((((byte)~src[i]) & src2[i]) |((byte)(src[i] & ((byte)(~src2[i])))));
	    	   }
	           return res;       
	    } 

    /** 
     * bytesToHexString
     * @param src 
     * @return string
     */  
    public static String bytesToHexString(byte[] src){       
    	StringBuffer stringBuiffer = new StringBuffer();       
           if (src == null || src.length <= 0) {       
               return null;       
           }       
           for (int i = 0; i < src.length; i++) {       
               int v = src[i] & 0xFF;       
               String hv = Integer.toHexString(v);       
               if (hv.length() < 2) {       
            	   stringBuiffer.append(0);       
               }       
               stringBuiffer.append(hv);       
           }       
           return stringBuiffer.toString();       
    } 
    //
    public static byte uniteBytes(byte src0, byte src1) {
    	  byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
    	    .byteValue();
    	  _b0 = (byte) (_b0 << 4);
    	  byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
    	    .byteValue();
    	  byte ret = (byte) (_b0 ^ _b1);
    	  return ret;
    }
    
    public static byte[] HexString2Bytes(String src) {
    	  int len = src.length();
    	  byte[] ret = new byte[(len+1) / 2];
    	  byte[] tmp = src.getBytes();
    	  int i=0,j=0;
    	  if((len % 2)==1){
    		  i=1;
    		  j=1;
    		  ret[0]= Byte.decode("0x" + new String(new byte[] { tmp[0] })).byteValue();
    	  }
    	  for (; i < len; i += 2,j++) {
    	   ret[j] = uniteBytes(tmp[i], tmp[i + 1]);
    	  }
    	  return ret;
    	 }
    
    
    /**　　
   	* 连接两个byte数组，之后返回一个新的连接好的byte数组
  　　* @param a1
  　　* @param a2
  　　* @return 一个新的连接好的byte数组
    */
    public static byte[] ArrayJoin(byte[] a1, byte[] a2) {
    	if((a1 == null)&&(a2 != null)){
    		//byte[] result = new byte[a1.length + a2.length];
    		return a2;
    	}
    	else if((a2 == null)&&(a1 != null)){
    		//byte[] result = new byte[a1.length + a2.length];
    		return a1;
    	}
    	else if((a1 == null)&&(a2 == null)){
    		//byte[] result = new byte[a1.length + a2.length];
    		return null;
    	}
    	byte[] result = new byte[a1.length + a2.length];
		System.arraycopy(a1, 0, result, 0, a1.length);
		System.arraycopy(a2, 0, result, a1.length, a2.length);
		return result;
    } 
    
  
    /**　　
   	* 将一个int型值转为4字节byte[]型；
  	* @param value    int型数值
  　    * @return result   转换后的4字节BYTE[]
    */
   public static byte[] IntToByte(int value) {
    	byte[] result = new byte[0x04];
    	int temp = value;
    	for(int i = 3 ;i>=0 ; i--)
    	{
    		result[i] = (byte)(temp%256);
    		temp = (temp/256);
     	}
		return result;
    }
	

    /**　　
   	* 将一个byte[]型值转为int型,长度不大于4；
  　　* @param srcnum[]
  	* @param srcnum[] offset
  	* @param len
  　　* @return 一个新的int值
    */
    public static int Byte2ToInt(byte[] srcnum,int offset,int len) {
		int  desnum = 0;
    	for(int i = 0 ;i<len;i++)
    	{
    		int sum = srcnum[offset+i];
    		if(srcnum[offset+i] < 0)
			{
    			sum += 256;
			}
    		for(int j=len-i;j>1;j-- )
    		{
    			sum *= 256;
    		}
    		desnum += sum;
    	}
		 return desnum;
    }
    
    /**　　
   	* 将一个byte[]型的DCD值转为String型；
  　　* @param byte[] value   存储BCD格式的数组
  　　* @return 一个新的String值
    */
    public static String BCD2String(byte[] value)
    {
    	   String strBCD = "";
    	   for(int i = 0;i<value.length;i++){
    		   String temp = Integer.toString(value[i]);
    		   strBCD = strBCD.concat(temp);
    	   }
    	   return strBCD;
    }
 
    public static String bcd2Str(byte[] bytes) {  
        StringBuffer temp = new StringBuffer(bytes.length * 2);  
        for (int i = 0; i < bytes.length; i++) {  
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));  
            temp.append((byte) (bytes[i] & 0x0f));  
        }  
        //return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString(); 
        return temp.toString(); 
    }  

}
