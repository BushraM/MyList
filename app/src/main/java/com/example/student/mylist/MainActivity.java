package com.example.student.mylist;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class MainActivity extends ActionBarActivity {

    String[] colourNames;
    static String currentColour;
    PrintWriter pr;
    static boolean colourExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout main_layout = (RelativeLayout) findViewById(R.id.mainLayout);

        //Checks if there is a colour already saved in SD card
        if ( colourExists(main_layout) == false ) {//default

            main_layout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        colourNames = getResources().getStringArray(R.array.listArray);
        ListView lv = (ListView) findViewById(R.id.listView);

        ArrayAdapter aa = new ArrayAdapter(this, R.layout.activity_listview, colourNames);
        lv.setAdapter(aa);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                RelativeLayout main_layout = (RelativeLayout) findViewById(R.id.mainLayout);
                String[] terms = getResources().getStringArray(R.array.listValues);
                currentColour = terms[position];

                main_layout.setBackgroundColor(Color.parseColor("#" + currentColour.substring(2)));

            }
        });

        registerForContextMenu(lv);

    }


    public static boolean colourExists(RelativeLayout main_layout){

        File myFile = new File("/sdcard/mycolours.txt");
        FileInputStream fIn = null;

        try {
            fIn = new FileInputStream(myFile);
            colourExists = true;

            BufferedReader myReader = new BufferedReader(
                    new InputStreamReader(fIn));

            if ((currentColour = myReader.readLine()) != null){
                main_layout.setBackgroundColor(Color.parseColor("#" + currentColour.substring(2)));
                myReader.close();
                fIn.close();
                return true;
            }
            else{
                myReader.close();
                fIn.close();
                return false;
            }

        } catch (FileNotFoundException e) {
            colourExists = false;
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "write colour to SDCard");
        menu.add(0, v.getId(), 0,"read colour from SDCard");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "write colour to SDCard") {
            Toast.makeText(getApplicationContext(), "writing to sd card", Toast.LENGTH_LONG).show();


            try {
                 pr = new PrintWriter("/sdcard/mycolours.txt");
                 pr.write(currentColour);

                 Toast.makeText(getApplicationContext(), "writing to sd card completed!", Toast.LENGTH_LONG).show();

            }catch (FileNotFoundException e){
                Toast.makeText(getApplicationContext(), "writing to sd card failed!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }finally {
                if(pr != null)
                   pr.close();
            }

        } else if (item.getTitle() == "read colour from SDCard") {
            Toast.makeText(getApplicationContext(), "reading from sd card", Toast.LENGTH_LONG).show();

            RelativeLayout main_layout = (RelativeLayout) findViewById(R.id.mainLayout);

            //Checks if there is a colour already saved in SD card
            if ( colourExists(main_layout) == false ) {
                Toast.makeText(getApplicationContext(), "There is nothing in the sd card", Toast.LENGTH_LONG).show();
            }

        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
}
