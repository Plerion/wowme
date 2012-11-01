package de.cromon.wowme;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class SettingsView extends FragmentActivity implements ActionBar.TabListener {

    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_view);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        actionBar.addTab(actionBar.newTab().setText(R.string.title_section1).setTabListener(this));
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_settings_view, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, show the tab contents in the container
        Fragment fragment = null;
        switch(tab.getPosition())
        {
        	case 0:
        	{
        		fragment = mDataLinkSettings;
        	}
        	break;
        	
        	default:
        		fragment = null;
        		break;
        }
        
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }
    
    public static class DataLinkSettingsFragment extends Fragment {
    	public DataLinkSettingsFragment() {
    	}
    	
    	@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
        	
        	View view = inflater.inflate(R.layout.tab_net_settings_layout, null);
        	EditText editHost = (EditText)view.findViewById(R.id.netSettingsEditHost);
        	EditText editPort = (EditText)view.findViewById(R.id.netSettingsEditPort);
        	
        	if(Settings.Instance.DataHost.compareTo("0.0.0.0") != 0)
        		editHost.setText(Settings.Instance.DataHost);
        	
        	editPort.setText(Integer.toString(Settings.Instance.DataPort));
        	
        	mContentView = view;
        	
        	return view;
        }
    	
    	@Override
    	public void onDestroyView() {
    		View view = mContentView;
    		
    		EditText editHost = (EditText)view.findViewById(R.id.netSettingsEditHost);
        	EditText editPort = (EditText)view.findViewById(R.id.netSettingsEditPort);
        	
        	Settings.Instance.DataHost = editHost.getText().toString();
        	Settings.Instance.DataPort = Integer.parseInt(editPort.getText().toString());
        	
        	Settings.Instance.Save();
        	
        	super.onDestroyView();
    	}
    	
    	private View mContentView;
    }
    
    private DataLinkSettingsFragment mDataLinkSettings = new DataLinkSettingsFragment();
}
