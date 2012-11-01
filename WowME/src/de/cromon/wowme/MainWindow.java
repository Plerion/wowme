package de.cromon.wowme;

import java.util.LinkedList;
import java.util.List;

import de.cromon.input.InputListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainWindow extends Activity {

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Settings.Instance = new Settings(this.getSharedPreferences("WoWMEPreferences_Shared", 0));
        setContentView(R.layout.activity_main_window);
        
        new Game(this).setupGame();
    }
    
    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
    	synchronized(mInputListeners) {
    		for(InputListener listener : mInputListeners)
    			listener.OnTouchEvent(event);
    	}
    	
    	return super.onTouchEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_window, menu);
        return true;
    }
    
    public void addInputListener(InputListener listener) {
    	synchronized(mInputListeners) {
    		mInputListeners.add(listener);
    	}
    }

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch(item.getItemId())
		{
			case R.id.menu_settings:
			{				
				Intent settings = new Intent(this, SettingsView.class);
				this.startActivity(settings);
			}
			break;
		}
		
		return true;
	}
    
	private List<InputListener> mInputListeners = new LinkedList<InputListener>();
    
}
