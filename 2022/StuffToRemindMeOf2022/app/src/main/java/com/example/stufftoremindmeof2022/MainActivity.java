package com.example.stufftoremindmeof2022;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.stufftoremindmeof2022.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    private static final int            TEXT_VIEW_HEIGHT  = 75;
    private static final int            TEXT_VIEW_WIDTH   = 300;
    public static final  List<Reminder> reminders         = new ArrayList<Reminder>();
    private static       LinearLayout   ll                = null;
    public static final  String         MESSAGE_KEY       = "MESSAGE";
    public static final  String         MY_PREFS          = "MY_PREFS";
    private static final int            REQUEST_CODE      = 123;
    private static       ImageButton    voiceImageButton  = null;
    private static       View           currentDialogView = null;
    ///private com.example.stufftoremindmeof2022.databinding.ActivityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_PREFS, Activity.MODE_PRIVATE).edit();
        editor.putInt("whichTimeSelected", 0);
        editor.commit();



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        ///binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        ///setContentView(binding.getRoot());
        setupLayout();

        ///setSupportActionBar(binding.toolbar);  TODO: see if this needed since deprecation

        ///NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        ///appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        ///NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        /*
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
         */
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
*/

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void clearReminders()
    {
        reminders.clear();
        setupLayout();
    }


    private void setupLayout() {
        //ll = null;
        ll = new LinearLayout(this);

        // overall layout
        ll.setOrientation(LinearLayout.VERTICAL);
        // ll.setBackgroundColor(getResources().getColor(R.color.main_background));
        ll.setBackgroundResource(R.drawable.drawable_gradient_background);

        // layout for buttons
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(100, 20, 100, 2);

        // button for new reminder
        Button newReminderButton = new Button(this);
        newReminderButton.setTag("new-reminder-button");
        newReminderButton.setText(getResources().getText(R.string.new_button_label));
        newReminderButton.setTextColor(getResources().getColor(R.color.button_font));   // set button font color same as app background
        newReminderButton.setOnClickListener(this);
        newReminderButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
        ll.addView(newReminderButton, buttonLayoutParams);

        // button to clear reminders
        Button clearButton = new Button(this);
        clearButton.setTag("clear-button");
        clearButton.setText(R.string.clear_button_label);
        clearButton.setTextColor(getResources().getColor(R.color.button_font));   // set button font color same as app background
        clearButton.setOnClickListener(this);
        clearButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.0f);
        ll.addView(clearButton, buttonLayoutParams);


        // text
        /// LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(TEXT_VIEW_WIDTH, TEXT_VIEW_HEIGHT);
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        textLayoutParams.setMargins(10, 2, 10, 2);


        // line
        LinearLayout.LayoutParams separatorLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5); // LinearLayout.LayoutParams.WRAP_CONTENT);
        View separatorView = new View(this);
        separatorView.setLayoutParams(separatorLayoutParams);
        separatorView.setBackgroundColor(Color.DKGRAY);
        ll.addView(separatorView);


        // add reminder list label
        TextView tvSubtitle = new TextView(this);
        tvSubtitle.setTypeface(null, Typeface.BOLD);
        tvSubtitle.setText(" :: Reminders           ");
        tvSubtitle.setTextColor(getResources().getColor(R.color.text_view_font_color));
        tvSubtitle.setTextSize(16.0f);
        // tvSubtitle.setBackgroundColor(Color.DKGRAY);
        tvSubtitle.setGravity(Gravity.CENTER);
        ll.addView(tvSubtitle, textLayoutParams);


        // add individual reminders
        for (Reminder reminder : reminders) {
            reminder.addTextViewToLayout(ll, this, textLayoutParams);
        }

        setContentView(ll);
    }

    private void askUserForNewReminder() {
        LayoutInflater layoutInflater = this.getLayoutInflater();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyCustomAlertDialog));
        final View dialogView = layoutInflater.inflate(R.layout.dialog_new_message, null);
        currentDialogView = dialogView;
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle("Select minutes");
        // alertDialogBuilder.setContentView(R.layout.dialog_time_input_view);
        // alertDialogBuilder.setMessage("Enter time in minutes");   // cannot have array and message at same time
        // alertDialogBuilder.setItems(R.array.minutes_array, null);
        Log.d("onClick", "Calling setItems ...");

        alertDialogBuilder.setSingleChoiceItems(R.array.minutes_array, 0, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("SingleChoiceItems", "which=" + which);
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(MY_PREFS, Activity.MODE_PRIVATE).edit();
                editor.putInt("whichTimeSelected", which);
                editor.commit();
            }
        });

        alertDialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                // do nothing
            }

        });

        Log.d("onClick", "Setting cancelable");
        alertDialogBuilder.setCancelable(true);

        // final View v = getLayoutInflater().inflate(R.layout.dialog_new_message, null);

        alertDialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Log.d("onClick", "setPositiveButton#onClick - arg1 = " + arg1);
                EditText myEditText = (EditText) dialogView.findViewById(R.id.edit_text_message);
                Log.d("onClick", "myEditText: " + myEditText);
                Log.d("onClick", "myEditText.getText(): " + myEditText.getText());

                String message = myEditText.getText().toString();
                Log.d("onClick", "User entered message: " + message);
                reminders.add(new Reminder(message, 9));

                // ll.setWillNotDraw(false);
                // ll.invalidate();
                // ll.requestLayout();
                // findViewById(android.R.id.content).invalidate();
                // ll.refreshDrawableState();


                setupLayout();

                dialog.cancel();

                Intent serviceIntent = new Intent(MainActivity.this, ReminderService.class);
                serviceIntent.putExtra(MainActivity.MESSAGE_KEY, message);
                startService(serviceIntent);

                // hide app if settings allow
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                boolean isAppGoingToBackgroundOnCreate = sharedPrefs.getBoolean("minimizeOnCreate", false);
                Log.d("minimizeOnCreate", "Minimize from settings: " + Boolean.valueOf(isAppGoingToBackgroundOnCreate).toString());
                if(isAppGoingToBackgroundOnCreate) {
                    hideApp(MainActivity.this);
                }
            }

        });


        voiceImageButton = (ImageButton) dialogView.findViewById(R.id.voice_button);
        voiceImageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("voice image button", "Starting ...");

                // disable if no recognition service is present
                PackageManager pm = getPackageManager();
                List<ResolveInfo> activities = pm.queryIntentActivities(
                        new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
                if (activities.size() == 0)
                {
                    Log.i("speech button", "speech NOT enabled");
                    voiceImageButton.setEnabled(false);
                    // voiceImageButton.setText("Voice input not available");
                } else {
                    Log.i("speech button", "speech enabled");
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice recognition Demo...");
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        Log.d("onClick", "Created AlertDialog");

        alertDialog.show();
    }

    private void hideApp(Context context) {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(startMain);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        String firstMatch = null;

        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
        {
            // Populate the wordsList with the String values the recognition engine thought it heard
            ArrayList<String> matches = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            // myWordList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item, matches));
            Log.d("onActivityResult", "ArrayList matches size = " + matches.size());
            Log.d("onActivityResult", "ArrayList matches = " + matches);
            if(matches.size() > 0) {
                firstMatch = matches.get(0);
                Log.i("onActivityResult", "ArrayList matches first = " + firstMatch);
                EditText myEditText = (EditText) currentDialogView.findViewById(R.id.edit_text_message);
                myEditText.setText(firstMatch.trim());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onClick(View v) {
        Log.d("MainActivity", "onClick v.getId() = " + v.getId() + " - tag: " + v.getTag());
        String btnTag = (String) v.getTag();

        if (btnTag.equals("new-reminder-button"))
        {
            askUserForNewReminder();
        }
        else if (btnTag.equals("clear-button")) {
            clearReminders();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();



        ///inflater.inflate(R.menu.main, menu);
        inflater.inflate(R.menu.menu_main, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, Prefs.class));
                return true;
        }
        return false;
    }
}