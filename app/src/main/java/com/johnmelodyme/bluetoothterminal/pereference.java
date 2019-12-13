package com.johnmelodyme.bluetoothterminal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
        settings = new String[]{"Font Size", "Save In Folder",
                "Email to Developer", "Source Code", "About"};
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
                if(CLICKED_LIST.equals("Font Size")){

                }
                else if (CLICKED_LIST.equals("Save In Folder")){

                }
                else if (CLICKED_LIST.equals("Email TO developer")){

                }
                else if (CLICKED_LIST.equals("Source Code")){
                    SOURCE_CODE();
                }
                else if (CLICKED_LIST.equals("About")){

                }
                else {
                    System.out.println("$LIST:: null");
                }
            }
        });
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
