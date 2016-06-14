//package com.gsta.hellosmartcard;
package com.hengbao.tsm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.security.AccessControlException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.simalliance.openmobileapi.service.CardException;

public class AccessController
{
	
	final String LOG_TAG = "TsmTest.MainActivity";
	
  protected PackageManager mPackageManager = null;

  public AccessController(PackageManager paramPackageManager)
  {
    this.mPackageManager = paramPackageManager;
  }
  
  //获取数字证书，将证书转换为符合X509标准的类型
  public Certificate decodeCertificate(byte[] paramArrayOfByte)throws CertificateException
  {
    CertificateFactory localCertificateFactory = CertificateFactory.getInstance("X.509");
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    return (X509Certificate)localCertificateFactory.generateCertificate(localByteArrayInputStream);
  }
  
  public Certificate[] getAPPCerts(String paramString)
		  throws CertificateException, NoSuchAlgorithmException, AccessControlException, CardException
		  {
	            List<PackageInfo> list = mPackageManager.getInstalledPackages(4416);
	            if ((paramString == null) || (paramString.length() == 0))
	            	throw new AccessControlException("Package Name not defined");
	            ArrayList<Certificate> arrayList = new ArrayList<Certificate>();
	            Object object = null;
	            Iterator<PackageInfo> iterator = list.iterator();
	    	     while (iterator.hasNext())
	    	     {
	    	    	PackageInfo localPackageInfo = iterator.next();
	                 String str = localPackageInfo.packageName;
	                 if(paramString.equals(str))
	                 {
	                 	object = localPackageInfo;
	                 	break;
	                 }
	             }
	             if (object == null)
			     {
		            throw new AccessControlException("Package does not exist");
			     }
	             Signature[] arrayOfSignature = ((PackageInfo)object).signatures;
	             int j = arrayOfSignature.length;
	             for(int i = 0; i <  j; i++)
	             {
	                   byte[] arrayOfByte = arrayOfSignature[i].toByteArray();
	                   Certificate localCertificate = decodeCertificate(arrayOfByte);
	                  // boolean bool = arrayList.add(localCertificate);
	                   arrayList.add(localCertificate);
	             }
	             Certificate[] arrayOfCertificate = new Certificate[arrayList.size()];
	             return (Certificate[])arrayList.toArray(arrayOfCertificate);
		  }
		    
		    
/*		 public byte[] getAppCertHash(Certificate paramCertificate)
		    throws CertificateEncodingException
		  {
		   	 Object object = null;
			  //messageDigest = null;
		        try
		        {
		        	MessageDigest messageDigest = MessageDigest.getInstance("SHA");
		          	object = messageDigest;
		          	 if (object == null)
			         {
		          		throw new AccessControlException("Hash can not be computed");
			         }
		        }
		        catch (Exception localException)
		        {
		         	throw new AccessControlException("Hash can not be computed");
		        }
		        
		        byte[] arrayOfByte = paramCertificate.getEncoded();
		    	return ((MessageDigest) object).digest(arrayOfByte);
		         
		  }
*/ 
	 public byte[] getAppCertHash(Certificate paramCertificate)
			 throws CertificateEncodingException
			 { 
				Object object = null;
				for(int i = 0; i<10 ; i++)
				{
				        try
				        {
				        	object = MessageDigest.getInstance("SHA");
				        	if(object != null)
				        		break;
				        	
				        }
				        catch (Exception localException)
				        {
				        }
				} 
				if (object == null)  
					 throw new AccessControlException("Hash can not be computed"); 
				else
				{
					 byte[] arrayOfByte = paramCertificate.getEncoded();
				 	 return ((MessageDigest) object).digest(arrayOfByte);
				}
			 }
	 //计算appHASH值
	 public String countAppHash(PackageManager packageManager,String packageName,Context context)
	 { 
		String st = "";	
		 try{
	        	  //PackageManager packageManager = getPackageManager();
	    	      //AccessController accessController = new AccessController(packageManager);
	    	      Certificate[] arrayOfCertificate = getAPPCerts("com.hengbao.tsm");
	    	      
	    	      Log.i(LOG_TAG,"arrayOfCertificate.length ---->: "+String.valueOf(arrayOfCertificate.length)+"\n"); 
	    	      Log.i(LOG_TAG,"arrayOfCertificate[0] ---->: "+  arrayOfCertificate[0].toString()+"\n"); 
	    	      
	    	      int i = arrayOfCertificate.length;
	    	      int j = 0;
	    	      Signature[] arrayOfSignature;
	    	      if ( j >= arrayOfCertificate.length)
	    	      {
	    	        //PackageManager localPackageManager2 = getPackageManager();
	    	        //String str1 = getPackageName();
	    	        arrayOfSignature = mPackageManager.getPackageInfo(packageName, 64).signatures;
	    	       // i = arrayOfSignature.length;
	    	        //j = 0;
	    	      //Signature localSignature = arrayOfSignature[j];
			        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
			        //byte[] arrayOfByte2 = localSignature.toByteArray();
			        //byte[] arrayOfByte3 = localMessageDigest.digest(arrayOfSignature[j].toByteArray());
			        //String str6 = String.valueOf(this.st);
			        StringBuilder localStringBuilder2 = new StringBuilder(String.valueOf(st));
			        //String str7 = bytesToHexString(localMessageDigest.digest(arrayOfSignature[j].toByteArray()));
			        //String str8 = str7 + "\n";
			        st = Util.bytesToHexString(messageDigest.digest(arrayOfSignature[j].toByteArray()))+ "\n";
			        Log.i(LOG_TAG,"ST11 ---->: "+  st +"\n"); 
	    	      }
	    	      else
	    	      {
	    	    	  for(j = 0; j<i; j++)
	    	    	  {
	    	    		  st = Util.bytesToHexString(getAppCertHash(arrayOfCertificate[j])) + "\n";
	    	    		  Log.i(LOG_TAG,"ST22 ---->: "+  st +"\n"); 
	    	    	  }
	    	      }
	    			//建立与卡交互的service
	    			//GetSimInfo callback = new GetSimInfo();
	    			//SEService seService = new SEService(context, callback);
	    	      return st;   
	        }
	        catch (Exception localException)
	        {
	        	//建立与卡交互的service
    			//GetSimInfo callback = new GetSimInfo();
    			//SEService seService = new SEService(context, callback);
	        	return "";
	        }
	 }
}




