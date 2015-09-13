package org.gbe.hugsward.UI;

import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.gbe.hugsward.R;
import org.gbe.hugsward.model.BookCart;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CheckoutActivity extends AppCompatActivity {

    private static final String BOOK_CART_KEY = "BookCart";
    private static final String CHECKOUT_FRAGMENT_TAG = "CheckoutFragment";

    @Bind(R.id.checkout_frame)
    View vMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            BookCart cart = getIntent().getParcelableExtra(BOOK_CART_KEY);
            CheckoutSummaryFragment f = CheckoutSummaryFragment.newInstance(cart);
            getFragmentManager().beginTransaction().add(R.id.checkout_frame, f, CHECKOUT_FRAGMENT_TAG).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
}
