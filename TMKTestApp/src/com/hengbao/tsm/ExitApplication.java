package com.hengbao.tsm;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class ExitApplication extends Application {

		private List<Activity> activityList=new LinkedList<Activity>();
		private static ExitApplication instance;
		private static Context currentcontext;
		private static int Location ;
		private ExitApplication()
		{
		}
		//单例模式中获取唯一的ExitApplication 实例
		public static ExitApplication getInstance()
		{
			if(null == instance)
			 {
				 instance = new ExitApplication();
			 }
			 return instance;
		}
		//添加Activity 到容器中
		public void addActivity(Activity activity)
		{
			currentcontext = activity;
			activityList.add(Location, activity);
			Location += 1;
		}
		
		public Context getActivity()
		{
			return currentcontext;
		}
		//回退Activity 并finish当前Activity
		public void backAct()
		{
			//currentcontext = activityList.;
			activityList.remove(Location-1);
			Location -= 1;
			currentcontext = activityList.remove(Location-1);;
		}
		
		//遍历所有Activity 并finish
		public void exit()
		{
			 for(Activity activity:activityList)
			 {
			 	activity.finish();
			 }
			 System.exit(0);
		}
 }