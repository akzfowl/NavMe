package com.vah.mobi;


import java.io.InputStream;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class PathPage extends Activity {
    public void onCreate(Bundle savedInstanceState) 
    {
    	setContentView(R.layout.mpath);
    	Bundle b=getIntent().getExtras();
    	String murl=b.getString("URL");
    	ImageView iv=(ImageView)findViewById(R.id.pimg);
    	try{
        super.onCreate(savedInstanceState);
        URL u=new URL(murl);
        InputStream c=(InputStream)u.getContent();
        Drawable d=Drawable.createFromStream(c,"src");
        iv.setImageDrawable(d);
    		}
    	catch(Exception e)
    	{
    		Toast te=Toast.makeText(this,e.toString(),Toast.LENGTH_LONG);
            te.show();
    	}
    }
}