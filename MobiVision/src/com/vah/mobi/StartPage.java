package com.vah.mobi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartPage extends Activity {
	LocationManager g,n;
    String cs,gString,dbase,sbase,ffurl,furl,murl="http://maps.google.com/staticmap?path=rgba%3A0xff000099%2Cweight%3A6",murl2="&zoom=16&size=300x300&key=YOUR_KEY_HERE";
    /* 
     * cs - Toast
     * gString - URI Parsing to Google Maps
     * dbase - Distance String 
     * sbase - Speed string
     * ffurl - Static maps url
     * furl - Static maps url
     * murl - Static maps url --to construct maps url--
     * 
     */
    Context c=this; // Store context
    Location lkg,lkn,lk;
    /*
     * lkg - Last know location
     * lkn - --do-- To calculate distance
     * lk -  --do-- To calculate distance
     * 
     */
    Double d=0.0; // Value of distance
    Float s=0.0f; // Value of Speed
    int chk=0;   // To reduce the static map url String 
    Button mcall,getLoc,start,seep;
    /*
     * mcall-
     * getLoc-
     * start-
     * sleep-
     * 
     */
    Intent mCall;  
    TextView di,sp;	
    boolean st=false,sep=false;
    /*
     * mcall
     * di
     * st
     * sep
     * 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try
        {
        getLoc=(Button)this.findViewById(R.id.getloc);
        di=(TextView)this.findViewById(R.id.dist);
        sp=(TextView)this.findViewById(R.id.speed);
        di.setText(dbase);
        sp.setText(sbase);
        start=(Button)this.findViewById(R.id.start);
        seep=(Button)this.findViewById(R.id.seep); //See Path Button
        seep.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(c,PathPage.class);
				Bundle b=new Bundle();
				b.putString("URL",ffurl);
				intent.putExtras(b);
				if(furl.equals(murl))
				{
					Toast t=Toast.makeText(c,"No GPS",Toast.LENGTH_SHORT);
                	t.show();
				}
				else startActivity(intent);
			}
		});
        start.setTextColor(Color.BLUE);
        g=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        n=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        g.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,(float) 0.0,new LocationListener()
        {

			public void onLocationChanged(Location arg0) {
				String check="0";
				try
				{
					if(lkg==null)
					{
						lkg=new Location(arg0);
						check="1";
					}
				if(arg0!=null && lkg!=null && st)
				{
					if(chk==0)
					{
						furl=furl+"|"+lkg.getLatitude()+","+lkg.getLongitude();
						chk=5;
					}
					else chk--;
					d+=lkg.distanceTo(arg0);
					check="2";
					lkg=new Location(arg0);
					check="3";
					s=arg0.getSpeed();
					check="4";
					sp.setText(sbase+(s*3.6)+" KM/Hour");
					check="5";
					di.setText(dbase+(d/1000)+" KM");}
				}
				catch(Exception e)
				{
					Toast tet=Toast.makeText(c,e+" "+check,Toast.LENGTH_SHORT);
					tet.show();
				}
				
			}

			public void onProviderDisabled(String arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				builder.setMessage("GPS not enabled! Enable now?")
				       .setCancelable(false)
				       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
						        Intent in = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
						        startActivity(in);
				           }
				       })
				       .setNegativeButton("No", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				                dialog.cancel();
				           }
				       }).show();
			}

			public void onProviderEnabled(String arg0) {	
			}

			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			}
        	
        });
        lkn=n.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        n.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,(float)2.0,new LocationListener()
        {
        	public void onLocationChanged(Location arg0)
        	{
        		if(arg0!=null)
        			lkn=arg0;
        	}

        	public void onProviderDisabled(String arg0) {}

        	public void onProviderEnabled(String arg0) {}

        	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
        });
        getLoc.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v)
        	{
        		try{
        	        cs=lkg.getLatitude()+" "+lkg.getLongitude()+"\n"+((System.currentTimeMillis()-lkg.getTime())/60000)+" Minutes Old";
        	        lk=lkg;
        		}
        		catch(Exception e)
        		{
        		cs="No/Old Satellite Info\n(Required For Speed And Distance)\n";
        			try{
     
        			cs+="Cell Triangulation: "+lkn.getLatitude()+" "+lkn.getLongitude()+"\n"+((System.currentTimeMillis()-lkn.getTime())/60000)+" Minutes Old";
        			lk=lkn;
        				}	
        			catch(Exception ef)
        			{
        				cs+="No Network Info";
        			}
        		}
                Toast t=Toast.makeText(c,cs,Toast.LENGTH_SHORT);
                t.show();
             
                
        	}
        });
    
        mcall=(Button)this.findViewById(R.id.mcall);
        start.setOnClickListener(new View.OnClickListener(){

			public void onClick(View v) {
				if(!st)
				{
					sep=false;
					seep.setEnabled(sep);
					furl=murl;
					st=true;
					start.setText("Stop");
					start.setTextColor(Color.RED);
					dbase="DISTANCE\n";
					sbase="SPEED\n";
					sp.setText(sbase);
					di.setText(dbase);
				}
				else
				{
					ffurl=furl+murl2;
					sep=true;
					seep.setEnabled(sep);
					d=0.0;
					s=0.0f;
					dbase="";
					sbase="";
					sp.setText(sbase);
					di.setText(dbase);
					st=false;
					start.setText("Start");
					start.setTextColor(Color.BLUE);
					Toast t=Toast.makeText(c,ffurl,Toast.LENGTH_LONG);
	                t.show();
				}
				
			}
        	
        });
        mcall.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
		        if(lk!=null)
		        {
		        gString="geo:"+lk.getLatitude()+","+lk.getLongitude();
		        Uri gUri=Uri.parse(gString);
		        mCall=new Intent(Intent.ACTION_VIEW,gUri);
		        
			startActivity(mCall);	
		
		        }
		        else
		        	{
		        	Toast te=Toast.makeText(c,"Get Your Location First",Toast.LENGTH_LONG);
	                te.show();
		        	}
			}
		});
        }
        catch(Exception e)
        {
        	Toast te=Toast.makeText(c,e.toString(),Toast.LENGTH_LONG);
            te.show();
         }
        }
        
    }
