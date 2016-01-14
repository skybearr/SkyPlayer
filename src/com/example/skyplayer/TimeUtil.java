package com.example.skyplayer;

public class TimeUtil {

	/**
	 * 格式化时间 to  55:55:55;
	 * @param time
	 * @param type
	 * @return
	 */
	public static String formatTimeToString(int time,int type)
	{
		String str = "";
		int d = time / (24 * 3600);
		int h = time / 3600;
		int m = time / 60;
		int s = time % 60;
		if(type > 3)
		{
			str = str.concat(d + "天");
		}
		if(type > 2)
		{
			if(h < 10)
			{
				str = str.concat("0" + h + ":");
			}
			else
			{
				str = str.concat(h + ":");
			}
			
		}
		if(type > 1)
		{
			if(h < 10)
			{
				str = str.concat("0" + m + ":");
			}
			else
			{
				str = str.concat(m + ":");
			}
		}
		if(s < 10)
		{
			str = str.concat("0" + s);
		}
		else
		{
			str = str.concat(s+"");
		}
		
		return str;
	}
}
