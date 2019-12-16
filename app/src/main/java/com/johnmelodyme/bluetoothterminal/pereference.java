package com.johnmelodyme.bluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * @CREATOR: JOHN MELODY MELISSA ESKHOLAZHT .C.T.K.;
 * @DATETIME: 12/12/2019;
 * @COPYRIGHT: 2019 - 2023;
 * @PROJECTNAME: BLUETOOTH LOW ENERGY TERMINAL;
 * @ACTIVITY: PEREFERENCE;
 */
public class pereference extends AppCompatActivity {
    ListView LIST_VIEW_PEREFERENCE;
    ArrayAdapter arrayAdapter;
    String [] settings;

    {
        settings = new String[]{"Save In Folder",
                "Email to Developer", "Source Code"};
    }

    public void INIT_DECLARATION(){
        LIST_VIEW_PEREFERENCE = findViewById(R.id.LIST_VIEW_PEREFERENCE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pereference);
        INIT_DECLARATION();

        arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item, settings);
        LIST_VIEW_PEREFERENCE.setAdapter(arrayAdapter);

        ON_CLICK_LIST();
    }

    private void ON_CLICK_LIST() {
        LIST_VIEW_PEREFERENCE.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String CLICKED_LIST;
                CLICKED_LIST = (String) parent.getItemAtPosition(position);
                if (CLICKED_LIST.equals("Save In Folder")){
                    SAVE_DATA_TO_EXTERNAL_STORAGE();
                }
                else if (CLICKED_LIST.equals("Email to Developer")){
                    EMAIL_TO_DEVELOPER();
                }
                else if (CLICKED_LIST.equals("Source Code")){
                    SOURCE_CODE();
                }
                else {
                    System.out.println("$LIST:: null");
                }
            }
        });
    }

    private void SAVE_DATA_TO_EXTERNAL_STORAGE() {
        
    }

    // @METHODS :: Email to Developer:
    private void EMAIL_TO_DEVELOPER() {
        Intent emailTODeveloper = new Intent(Intent.ACTION_SEND);
        emailTODeveloper.setType("text/plain");
        emailTODeveloper.putExtra(Intent.EXTRA_EMAIL, new String[]
                {"johnmelodyme@androiddeveloper.onexmail.com"});
        emailTODeveloper.putExtra(Intent.EXTRA_CC, new String[]
                {"Johnmelodyme@icloud.com"});
        emailTODeveloper.putExtra(Intent.EXTRA_SUBJECT,
                "[FROM BLUETOOTH TERMINAL APPLICATION] Hey Developer, I have some suggestion and Issue!");
        emailTODeveloper.putExtra(Intent.EXTRA_TEXT,
                "[FROM BLUETOOTH TERMINAL APPLICATION] Hey Developer, I have some suggestion and Issue!");
        try {
            startActivity(Intent.createChooser(emailTODeveloper,"Pick a Email Platform: "));
        } catch (Exception ignored) {
        }
    }

    private void SOURCE_CODE() {
        Intent source;
        String url;
        url = "https://github.com/johnmelodyme/BLUETOOTH_TERMINAL";
        source = new Intent(Intent.ACTION_VIEW,
                Uri.parse(url));
        startActivity(source);
    }
}
