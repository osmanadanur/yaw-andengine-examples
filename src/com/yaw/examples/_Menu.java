package com.yaw.examples;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author YaW
 * Simple list launcher for the examples
 */
public class _Menu extends Activity{
	
	static final String[] listExamplesDesc = new String[]{"Particle DigEffect"};
	static final String[] listExamplesAct = new String[]{"Particle_DigEffect"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.menu);
		
		ListView listMenu = (ListView) findViewById(R.id.listMenu);
		listMenu.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listExamplesDesc));
		listMenu.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position, long id) {
				Intent inNewActivity = new Intent();
				inNewActivity.setClassName(getBaseContext().getPackageName(), getBaseContext().getPackageName()+"."+listExamplesAct[position]);
				startActivity(inNewActivity);
			}
		});
	}

}
