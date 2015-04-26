package org.ambientdynamix.contextplugins;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.itm.stage.abilityprofiling.DB.Ability;
import de.itm.stage.abilityprofiling.DB.Profile;
import de.itm.stage.abilityprofiling.icf.ICFRating;

/**
 * Created by sven on 17.02.15.
 */
public class ProfilesActivity extends Activity {

    private ListView listView;
    private String[][] sourceValues;
    private String[] legalCodes;
    public static EditText et;
    public static TextView textView;

    public static TextView textViewA;
    public static TextView textViewB;
    public static TextView textViewC;
    public static TextView textViewD;

    private static Spinner spinnerA;
    private static Spinner spinnerB;
    private static Spinner spinnerC;
    private static Spinner spinnerD;
    public List<String> spinnerListA = new ArrayList<String>();
    public List<String> spinnerListB = new ArrayList<String>();
    public List<String> spinnerListC = new ArrayList<String>();
    public List<String> spinnerListD = new ArrayList<String>();
    ArrayAdapter<String> dataAdapterA;
    ArrayAdapter<String> dataAdapterB;
    ArrayAdapter<String> dataAdapterC;
    ArrayAdapter<String> dataAdapterD;
    private static Button submitBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiles);

        et = (EditText) findViewById(R.id.insertFieldCode);
        textView = (TextView) findViewById(R.id.selectedICFCode);

        textViewA = (TextView) findViewById(R.id.titleA);
        textViewB = (TextView) findViewById(R.id.titleB);
        textViewC = (TextView) findViewById(R.id.titleC);
        textViewD = (TextView) findViewById(R.id.titleD);

        if (BindDynamixActivity.abilities != null && BindDynamixActivity.abilities.length != 0) {


            legalCodes = new String[BindDynamixActivity.abilities.length];
            for (int k = 0; k < BindDynamixActivity.abilities.length; k++) {
                legalCodes[k] = BindDynamixActivity.abilities[k].getICFCode();
            }

            if (BindDynamixActivity.profiles != null) {


                sourceValues = new String[BindDynamixActivity.profiles.length][4];

                for (int i = 0; i < BindDynamixActivity.profiles.length; i++) {
                    sourceValues[i][0] = BindDynamixActivity.profiles[i].getICFCode();

                    if(sourceValues[i][0].charAt(1)!=' '){
                        sourceValues[i][0] = sourceValues[i][0].charAt(0)+ " "+ sourceValues[i][0].subSequence(1,sourceValues[i][0].length());
                    }

                    sourceValues[i][1] = BindDynamixActivity.profiles[i].getRating();
                    sourceValues[i][2] = BindDynamixActivity.profiles[i].getAppName();
                    sourceValues[i][3] = BindDynamixActivity.profiles[i].getTimeStamp().toString();
                    Log.i("my", BindDynamixActivity.profiles[i].toString());
                }

                listView = (ListView) findViewById(R.id.profileListView);

                final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i < sourceValues.length; ++i) {
                    Ability a = getAbiltyByCode(sourceValues[i][0]);
                    if(a != null)
                        list.add(a.getAbilityTitle() + " (" + sourceValues[i][0] + sourceValues[i][1] + " )");
                    else{
                        Log.i("my","error "+sourceValues[i][0]);
                    }
                }

                final MyArrayAdapter adapter = new MyArrayAdapter(this,
                        android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);

                listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfilesActivity.this);

                        Date d = new Date();
                        d.setTime(Long.parseLong(sourceValues[position][3]));

                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                        String reportDate = df.format(d);

                        String message = getAbiltyByCode(sourceValues[position][0]).getAbilityTitle() + " ["+sourceValues[position][0] + sourceValues[position][1] + "]\n\nby " +
                                sourceValues[position][2] + "\n\nat " + reportDate;

                        builder.setTitle("DETAILS").setMessage(message)
                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        BindDynamixActivity.messageListRemove.add(BindDynamixActivity.profiles[position]);
                                        Profile[] newArray = new Profile[BindDynamixActivity.profiles.length-1];
                                        int jump = 0;
                                        for (int i = 0; i < BindDynamixActivity.profiles.length; i++) {

                                            if(i!=position){
                                                newArray[i-jump] =  BindDynamixActivity.profiles[i];
                                            }
                                            else{
                                                jump = 1;
                                            }
                                        }
                                        BindDynamixActivity.profiles = newArray;

                                        adapter.remove(adapter.getItem(position));
                                        adapter.notifyDataSetChanged();
                                        dialog.dismiss();
                                        finish();
                                        startActivity(getIntent());
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


        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("my", "text changed");
                int result = 3;
                if (s.length() > 0) {

                    //First Letter
                    if (s.charAt(0) == 'b' || s.charAt(0) == 's' || s.charAt(0) == 'd' || s.charAt(0) == 'e' ||
                            s.charAt(0) == 'B' || s.charAt(0) == 'S' || s.charAt(0) == 'D' || s.charAt(0) == 'E') {
                        result = 0;

                        if (s.length() >= 2) {
                            //Space or no Space
                            boolean space = (s.charAt(1) == ' ') ? true : false;
                            String digits = "";


                            if (space) {
                                digits = s.subSequence(2, s.length()).toString();
                            } else {
                                digits = s.subSequence(1, s.length()).toString();
                            }
                            //Digits
                            String regex = "\\d+";

                            if (digits.matches(regex)) {
                                result = 0;
                                updateTextView(-1);
                            } else if (digits.length() > 0) {
                                result = 1;
                                updateTextView(-1);
                            }

                        }
                        int index = codeExists(s);
                        if (index != -1) {
                            result = 2;
                            updateTextView(index);
                            submitBtn.setEnabled(true);
                        } else {
                            submitBtn.setEnabled(false);
                        }
                    } else {
                        result = 1;
                        updateTextView(-1);
                    }

                }
                colorize(result);
                updateSpinner(s);

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        // SPINNER A
        spinnerA = (Spinner) findViewById(R.id.insertFieldRatingA);

        dataAdapterA = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerListA);
        dataAdapterA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerA.setAdapter(dataAdapterA);


        spinnerB = (Spinner) findViewById(R.id.insertFieldRatingB);
        dataAdapterB = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerListB);
        dataAdapterB.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerB.setAdapter(dataAdapterB);

        spinnerC = (Spinner) findViewById(R.id.insertFieldRatingC);
        dataAdapterC = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerListC);
        dataAdapterC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerC.setAdapter(dataAdapterC);

        spinnerD = (Spinner) findViewById(R.id.insertFieldRatingD);
        dataAdapterD = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerListD);
        dataAdapterD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerD.setAdapter(dataAdapterD);

        submitBtn = (Button) findViewById(R.id.submitButton);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Profile p = new Profile();
                p.setTimeStamp(new Date().getTime());
                p.setICFCode(et.getText().toString());
                p.setRating(getRatings());
                p.setAppName(getString(R.string.app_name));

                if (BindDynamixActivity.profiles != null && BindDynamixActivity.profiles.length>0) {
                    int oldLength = BindDynamixActivity.profiles.length;
                    Profile[] newProfiles = new Profile[oldLength + 1];

                    for (int i = 0; i < oldLength; i++) {
                        newProfiles[i] = BindDynamixActivity.profiles[i];
                    }
                    newProfiles[oldLength] = p;
                    BindDynamixActivity.profiles = newProfiles;

                    BindDynamixActivity.messageListAdd.add(p);
                } else {
                    BindDynamixActivity.profiles = new Profile[]{p};
                }
                finish();
                startActivity(getIntent());

            }
        });
        submitBtn.setEnabled(false);


        Intent intent = getIntent();
        String message = intent.getStringExtra("lastcode");
        et.setText(message);

        if (message != null && message != "") {
            colorize(2);
            updateSpinner(message);
            submitBtn.setEnabled(true);
        }


        if (intent.getStringExtra("lastcodeTitle") != null && intent.getStringExtra("lastcodeTitle") != "") {
            textView.setText(intent.getStringExtra("lastcodeTitle"));
        }


    }

    public Ability getAbiltyByCode(String icf) {
        if (BindDynamixActivity.abilities != null && BindDynamixActivity.abilities.length > 0)
            for (int i = 0; i < BindDynamixActivity.abilities.length; i++) {
                if (BindDynamixActivity.abilities[i].getICFCode().equals(icf))
                    return BindDynamixActivity.abilities[i];
            }
        return null;
    }

    private void updateTextView(int s) {
        if (s == -1) {
            textView.setText("");
            return;
        }
        if (BindDynamixActivity.abilities != null && BindDynamixActivity.abilities.length != 0) {
            textView.setText(BindDynamixActivity.abilities[s].getAbilityTitle());
        }
    }

    public void clearLists() {
        spinnerListA.clear();
        spinnerListB.clear();
        spinnerListC.clear();
        spinnerListD.clear();
    }

    public void clearTitles() {
        textViewA.setText("");
        textViewB.setText("");
        textViewC.setText("");
        textViewD.setText("");
    }

    public void updateSpinner(CharSequence s) {
        if (codeExists(s) != -1) {
            clearLists();
            clearTitles();
            if (s.charAt(0) == 'b' || s.charAt(0) == 'B') {
                // Bodyfunctions

                for (int i = 0; i < ICFRating.FUNCTIONS_RATING_SET.size(); i++) {
                    spinnerListA.add(ICFRating.FUNCTIONS_RATING_SET.get(i)[1] + " (" + ICFRating.FUNCTIONS_RATING_SET.get(i)[0] + ")");
                }

                dataAdapterA.notifyDataSetChanged();
                spinnerA.setEnabled(true);
                spinnerB.setEnabled(false);
                spinnerC.setEnabled(false);
                spinnerD.setEnabled(false);

                textViewA.setText(ICFRating.EXTENT_OF_IMPAIRMENT);

            } else if (s.charAt(0) == 's' || s.charAt(0) == 'S') {
                // Bodystructure


                for (int i = 0; i < ICFRating.STRUCTURES_RATING_SET_A.size(); i++) {
                    spinnerListA.add(ICFRating.STRUCTURES_RATING_SET_A.get(i)[1] + " (" + ICFRating.STRUCTURES_RATING_SET_A.get(i)[0] + ")");
                }

                for (int i = 0; i < ICFRating.STRUCTURES_RATING_SET_B.size(); i++) {
                    spinnerListB.add(ICFRating.STRUCTURES_RATING_SET_B.get(i)[1] + " (" + ICFRating.STRUCTURES_RATING_SET_B.get(i)[0] + ")");
                }

                for (int i = 0; i < ICFRating.STRUCTURES_RATING_SET_C.size(); i++) {
                    spinnerListC.add(ICFRating.STRUCTURES_RATING_SET_C.get(i)[1] + " (" + ICFRating.STRUCTURES_RATING_SET_C.get(i)[0] + ")");
                }

                dataAdapterA.notifyDataSetChanged();
                dataAdapterB.notifyDataSetChanged();
                dataAdapterC.notifyDataSetChanged();

                spinnerA.setEnabled(true);
                spinnerB.setEnabled(true);
                spinnerC.setEnabled(true);

                spinnerD.setEnabled(false);

                textViewA.setText(ICFRating.EXTENT_OF_IMPAIRMENT);
                textViewB.setText(ICFRating.NATURE_OF_IMPAIRMENT);
                textViewC.setText(ICFRating.LOCATION_OF_IMPAIRMENT);

            } else if (s.charAt(0) == 'd' || s.charAt(0) == 'D') {
                // Domain

                for (int i = 0; i < ICFRating.DOMAIN_RATING_SET.size(); i++) {
                    spinnerListA.add(ICFRating.DOMAIN_RATING_SET.get(i)[1] + " (" + ICFRating.DOMAIN_RATING_SET.get(i)[0] + ")");
                    spinnerListB.add(ICFRating.DOMAIN_RATING_SET.get(i)[1] + " (" + ICFRating.DOMAIN_RATING_SET.get(i)[0] + ")");
                    spinnerListC.add(ICFRating.DOMAIN_RATING_SET.get(i)[1] + " (" + ICFRating.DOMAIN_RATING_SET.get(i)[0] + ")");
                    spinnerListD.add(ICFRating.DOMAIN_RATING_SET.get(i)[1] + " (" + ICFRating.DOMAIN_RATING_SET.get(i)[0] + ")");
                }
                dataAdapterA.notifyDataSetChanged();
                dataAdapterB.notifyDataSetChanged();
                dataAdapterC.notifyDataSetChanged();
                dataAdapterD.notifyDataSetChanged();

                spinnerA.setEnabled(true);
                spinnerB.setEnabled(true);
                spinnerC.setEnabled(true);
                spinnerD.setEnabled(true);

                textViewA.setText(ICFRating.PERFORMANCE);
                textViewB.setText(ICFRating.CAPACITY_WITHOUT_ASSISTANCE);
                textViewC.setText(ICFRating.CAPACITY_WITH_ASSISTANCE);
                textViewD.setText(ICFRating.PERFORMANCE_WITHOUT_ASSISTANCE);

            } else if (s.charAt(0) == 'e' || s.charAt(0) == 'E') {
                // Environmental Factors

                for (int i = 0; i < ICFRating.ENVIRONMENT_RATING_SET.size(); i++) {
                    spinnerListA.add(ICFRating.ENVIRONMENT_RATING_SET.get(i)[1] + " (" + ICFRating.ENVIRONMENT_RATING_SET.get(i)[0] + ")");
                }

                dataAdapterA.notifyDataSetChanged();

                spinnerA.setEnabled(true);
                spinnerB.setEnabled(false);
                spinnerC.setEnabled(false);
                spinnerD.setEnabled(false);

                textViewA.setText(ICFRating.BARRIER_OR_FACILITATOR);

            }
        } else {
            //disable all
            spinnerA.setEnabled(false);
            spinnerB.setEnabled(false);
            spinnerC.setEnabled(false);
            spinnerD.setEnabled(false);
        }
    }


    private int codeExists(CharSequence s) {
        if (legalCodes != null) {
            for (int i = 0; i < legalCodes.length; i++) {
                if (s.toString().equals(legalCodes[i])) {
                    return i;
                }
            }
        }
        return -1;

    }

    public void colorize(int i) {
        // 0 regular but not existing --> yellow
        // 1 irregular --> red
        // 2 regular and exists --> green
        // 3 default

        switch (i) {
            case 0:
                et.setBackgroundColor(Color.rgb(100, 100, 20));
                break;
            case 1:
                et.setBackgroundColor(Color.rgb(100, 20, 20));
                break;
            case 2:
                et.setBackgroundColor(Color.rgb(20, 100, 20));
                break;
            case 3:
                et.setBackgroundColor(Color.TRANSPARENT);
                break;
        }
    }

    public String getRatings() {
        String ratings = "";

        String spinnerAText = "";
        String spinnerBText = "";
        String spinnerCText = "";
        String spinnerDText = "";


        if (spinnerA.isEnabled())
            spinnerAText = spinnerA.getSelectedItem().toString();

        if (spinnerB.isEnabled())
            spinnerBText = spinnerB.getSelectedItem().toString();

        if (spinnerC.isEnabled())
            spinnerCText = spinnerC.getSelectedItem().toString();

        if (spinnerD.isEnabled())
            spinnerDText = spinnerD.getSelectedItem().toString();

        if (et.getText().charAt(0) == 'b') {
            ratings += spinnerAText.substring(0, spinnerAText.indexOf(" "));

        } else if (et.getText().charAt(0) == 's') {
            ratings += spinnerAText.substring(0, spinnerAText.indexOf(" "));
            ratings += spinnerBText.substring(0, spinnerBText.indexOf(" "));
            ratings += spinnerCText.substring(0, spinnerCText.indexOf(" "));

        } else if (et.getText().charAt(0) == 'd') {
            ratings += "."+spinnerAText.substring(0, spinnerAText.indexOf(" "));
            ratings += spinnerBText.substring(0, spinnerBText.indexOf(" "));
            ratings += spinnerCText.substring(0, spinnerCText.indexOf(" "));
            ratings += spinnerDText.substring(0, spinnerDText.indexOf(" "));


        } else if (et.getText().charAt(0) == 'e') {
            ratings += spinnerAText.substring(0, spinnerAText.indexOf(" "));
        }


        return ratings;
    }
}