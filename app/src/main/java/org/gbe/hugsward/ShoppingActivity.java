package org.gbe.hugsward;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import model.Book;

public class ShoppingActivity extends AppCompatActivity {

    private static final String jsonDummy = "{\n" +
            "    \"isbn\": \"c8fabf68-8374-48fe-a7ea-a00ccd07afff\",\n" +
            "    \"title\": \"Henri Potier à l'école des sorciers\",\n" +
            "    \"price\": 35,\n" +
            "    \"cover\": \"http://henri-potier.xebia.fr/hp0.jpg\"\n" +
            "  }";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        if(savedInstanceState == null) {
            Gson gson = new Gson();
            Book b = gson.fromJson(jsonDummy, Book.class);
            BookCardFragment f = BookCardFragment.newInstance(b, 0);
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.shopping_main_frame, f, "tag")
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shopping, menu);
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
