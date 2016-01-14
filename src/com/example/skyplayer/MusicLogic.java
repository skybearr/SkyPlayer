package com.example.skyplayer;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;

public class MusicLogic extends Activity {
	private static MusicLogic _instance;
	public static MusicLogic getInstacne()
	{
		if(_instance == null)
		{
			_instance = new MusicLogic();
		}
		return _instance;
	}
	
	private ArrayList<Music> list;
	private ArrayList<String> names;
	
	public ArrayList<Music> getMusicList()
	{
		return list;
	}
	
	public ArrayList<String> getMusicNames()
	{
		return names;
	}
	
	public void initMusic(Cursor cursor)
	{
		list = new ArrayList<Music>();
		names = new ArrayList<String>();
		
	    int i = 0;
	    int cursorCount = cursor.getCount();
	    if (cursorCount > 0 )
	    {
		    cursor.moveToFirst();
		    while (i < cursorCount)
		    {
	            //如果要制定路径的歌曲
//	            if(url.toLowerCase().indexOf("指定的歌曲路径") > 0)
//	            {
//	            	
//	            }
	            String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
	            if(url.indexOf(".mp3") != -1)
	            {
	            	Music m = new Music();
		            m.setId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
		            String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
		            System.out.print(name);
		            m.setTitle(name);
		            m.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
		            m.setUrl(url);
		            m.setLasttime(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
		            m.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
		            list.add(m);
		            names.add(name);
	            }
	            
	            i++;
	            cursor.moveToNext();                
	        }
		    cursor.close();
	    }
	    cursor = null;
	}
	
}
