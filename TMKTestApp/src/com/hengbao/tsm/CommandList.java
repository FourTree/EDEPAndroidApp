package com.hengbao.tsm;

import java.util.ArrayList;
import java.util.List;


//组织要发送的指令集合
public class CommandList {

	private static List<byte[]> mListcmd = new ArrayList<byte[]>();
	
/*	public CommandList()
	{
		mListcmd = new ArrayList<byte[]>();;
	}
*/	
	//增加指令集中的指令
	public static void addList(int location,byte[] msg)
	{
		mListcmd.add(location, msg);
	}
	
	//清除指令集中的指令
	public static void clearList()
	{
		if(!mListcmd.isEmpty())
		{
			mListcmd.clear();
		}
	}
	
	//获取指令集中的指令
	public static byte[] getList(int location)
	{
/*		for(String temp:mListcmd)
		{
			return temp;
		}
*/      
/*		Iterator it1 = mListcmd.iterator();
        while(it1.hasNext())
        {
        	return it1.;
        }*/
		return mListcmd.get(location);
	}
	
	//获取指令集中的指令
	public static int getListSize()
	{
		return mListcmd.size();
	}
}