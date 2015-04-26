package org.ambientdynamix.contextplugins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sven on 17.02.15.
 */
public class ICFActivity extends Activity {
    private ListView listView;
    private String[][] sourceValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icf);

        listView = (ListView) this.findViewById(R.id.icfListView);

        if (BindDynamixActivity.abilities != null && BindDynamixActivity.abilities.length != 0) {


            sourceValues = new String[BindDynamixActivity.abilities.length][3];

            for (int i = 0; i < BindDynamixActivity.abilities.length; i++) {
                sourceValues[i][0] = BindDynamixActivity.abilities[i].getICFCode();
                sourceValues[i][1] = BindDynamixActivity.abilities[i].getAbilityTitle();
                sourceValues[i][2] = BindDynamixActivity.abilities[i].getAbilityDescription();
            }

            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < sourceValues.length; ++i) {
                list.add(sourceValues[i][0] + ": " + sourceValues[i][1]);
            }

            TextView tv = (TextView) this.findViewById(R.id.numResults);
            tv.setText(sourceValues.length + " of " + sourceValues.length);

            final MyArrayAdapter adapter = new MyArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listView.setAdapter(adapter);


            EditText searchfield = (EditText) this.findViewById(R.id.searchField);

            searchfield.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    adapter.clear();


                    for (int i = 0; i < sourceValues.length; i++) {
                        String currentText = sourceValues[i][0] + ": " + sourceValues[i][1];
                        //
                        // Log.i("my",currentText);

                        if (s.length() > 1) {
                            String s_cap = (String.valueOf(s.charAt(0)).toUpperCase() + s.subSequence(1, s.length()).toString());
                            String s_cap_down = (String.valueOf(s.charAt(0)).toLowerCase() + s.subSequence(1, s.length()).toString());

                            String s_white_rem = (s.charAt(1) == ' ') ? s.toString().replace(" ", "") : s.toString();
                            String s_white_add = ((s.charAt(1) != ' ' && (s.charAt(0) == 'b' || s.charAt(0) == 's'
                                    || s.charAt(0) == 'd' || s.charAt(0) == 'e' || s.charAt(0) == 'B' || s.charAt(0) == 'S'
                                    || s.charAt(0) == 'D' || s.charAt(0) == 'E'))) ?
                                    (String.valueOf(s.charAt(0)) + ' ' + s.subSequence(1, s.length()).toString()) : s.toString();

                            String s_cap_white_rem = (s.charAt(1) == ' ') ? s.toString().replace(" ", "") : s.toString();
                            s_cap_white_rem = (String.valueOf(s_cap_white_rem.charAt(0)).toUpperCase() + s_cap_white_rem.subSequence(1, s_cap_white_rem.length()).toString());

                            String s_cap_white_add = (s.charAt(1) != ' ' && (s.charAt(0) == 'b' || s.charAt(0) == 's'
                                    || s.charAt(0) == 'd' || s.charAt(0) == 'e' || s.charAt(0) == 'B' || s.charAt(0) == 'S'
                                    || s.charAt(0) == 'D' || s.charAt(0) == 'E')) ?
                                    (String.valueOf(s.charAt(0)).toUpperCase() + ' ' + s.subSequence(1, s.length()).toString()) : s.toString();


                            String s_cap_down_white_rem = (s.charAt(1) == ' ') ? s.toString().replace(" ", "") : s.toString();
                            s_cap_down_white_rem = (String.valueOf(s_cap_down_white_rem.charAt(0)).toLowerCase() + s_cap_down_white_rem.subSequence(1, s_cap_down_white_rem.length()).toString());

                            String s_cap_down_white_add = (s.charAt(1) != ' ' && (s.charAt(0) == 'b' || s.charAt(0) == 's'
                                    || s.charAt(0) == 'd' || s.charAt(0) == 'e' || s.charAt(0) == 'B' || s.charAt(0) == 'S'
                                    || s.charAt(0) == 'D' || s.charAt(0) == 'E')) ?
                                    (String.valueOf(s.charAt(0)).toLowerCase() + ' ' + s.subSequence(1, s.length()).toString()) : s.toString();


                            // speciale cases
                            if (currentText.contains(s) || currentText.contains(s_white_add) ||
                                    currentText.contains(s_white_rem) || currentText.contains(s_cap) ||
                                    currentText.contains(s_cap_down) || currentText.contains(s_cap_white_rem) ||
                                    currentText.contains(s_cap_white_add) || currentText.contains(s_cap_down_white_rem) ||
                                    currentText.contains(s_cap_down_white_add))
                                adapter.add(currentText);
                        } else if ((s.length() == 1)) {
                            String s_cap = (String.valueOf(s.charAt(0)).toUpperCase());
                            String s_cap_down = (String.valueOf(s.charAt(0)).toLowerCase());

                            if (currentText.contains(s) || currentText.contains(s_cap) || currentText.contains(s_cap_down))
                                adapter.add(currentText);
                        } else {
                            adapter.add(currentText);
                        }

                    }
                    TextView tv = (TextView) findViewById(R.id.numResults);
                    tv.setText(adapter.getCount() + " of " + sourceValues.length);

                    adapter.notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Log.i("my", "Description:  " + sourceValues[position][2].trim());
                    String message = (sourceValues[position][2].trim().equals("-")) ? "NO DESCRIPTION AVAILABLE" : sourceValues[position][2];

                    AlertDialog.Builder builder = new AlertDialog.Builder(ICFActivity.this);
                    builder.setTitle(adapter.getItem(position)).setMessage(message)
                            .setPositiveButton("Create new profile", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getApplication(), ProfilesActivity.class);
                                    String message = adapter.getItem(position).substring(0, adapter.getItem(position).indexOf(":"));
                                    String message2 = adapter.getItem(position).substring(adapter.getItem(position).indexOf(":")+1);
                                    i.putExtra("lastcode", message);
                                    i.putExtra("lastcodeTitle", message2);
                                    startActivity(i);
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
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