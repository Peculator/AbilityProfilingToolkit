package org.ambientdynamix.contextplugins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import de.itm.stage.abilityprofiling.DB.Permission;

/**
 * Created by sven on 17.02.15.
 */
public class PermissionsActivity extends Activity {
    private ListView listView;
    private String[][] sourceValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        listView = (ListView) findViewById(R.id.permissionListView);
        if (BindDynamixActivity.permissions != null) {

            sourceValues = new String[BindDynamixActivity.permissions.length][2];

            for (int i = 0; i < BindDynamixActivity.permissions.length; i++) {
                sourceValues[i][0] = BindDynamixActivity.permissions[i].getAppName();
                sourceValues[i][1] = BindDynamixActivity.permissions[i].getPermissionState();

            }

            listView = (ListView) findViewById(R.id.permissionListView);

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < sourceValues.length; ++i) {
                list.add(sourceValues[i][0] +" - "+ sourceValues[i][1]);
            }

            final MyArrayAdapter adapter = new MyArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Log.i("my", "TODO - Open Dialog");

                    AlertDialog.Builder builder = new AlertDialog.Builder(PermissionsActivity.this);
                    builder.setTitle("Permissions for "+sourceValues[position][1]+" ("+sourceValues[position][0]+")")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setItems(new String[]{Permission.PermissionStates.ADMIN.toString(),
                                    Permission.PermissionStates.YES.toString(),Permission.PermissionStates.NO.toString()}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("my",which+"");
                                    BindDynamixActivity.permissions[position].setPermissionState(Permission.PermissionStates.values()[(which+2)%3].toString());
                                    BindDynamixActivity.messageListChange.add(BindDynamixActivity.permissions[position]);
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                    finish();
                                    startActivity(getIntent());

                                }
                            });
                    builder.create();
                    builder.show();
                    return false;
                }
            });
        }

    }
}