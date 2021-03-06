// Juan Anaya
// MDF3 - 201608
// ListActivity

package fullsail.paix.com.AnayaJuan_CE01.List;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fullsail.paix.com.AnayaJuan_CE01.DataModel.Person;
import fullsail.paix.com.AnayaJuan_CE01.Form.FormActivity;
import fullsail.paix.com.AnayaJuan_CE01.R;

public class ListActivity extends AppCompatActivity implements SelectedPerson {

    //TAG
    private static final String TAG = "ListActivity";
    //Form Request Code
    private static final int FORM_REQUEST_CODE = 1;
    //Actions
    private static final String ACTION_VIEW_DATA = "com.fullsail.android.ACTION_VIEW_DATA";
    //Extras
    public static final String EXTRA_FIRST_NAME = "com.fullsail.android.EXTRA_FIRST_NAME";
    public static final String EXTRA_LAST_NAME = "com.fullsail.android.EXTRA_LAST_NAME";
    public static final String EXTRA_AGE = "com.fullsail.android.EXTRA_AGE";

    /*Life Cycle*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Set Content List Fragment
        ContentListFragment contentListFragment = new ContentListFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.FrameLayout_ContentList_FragHolder, contentListFragment).commit();

    }

    /*Menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Load Custom Menu
        getMenuInflater().inflate(R.menu.menu_content_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_newForm:
                //Transition to Form Activity
                Intent intent = new Intent(ListActivity.this, FormActivity.class);
                startActivityForResult(intent, FORM_REQUEST_CODE);
                //Dev
                Log.i(TAG, "onOptionsItemSelected: " + "New Form");
                return true;
        }
        return false;
    }

    /*Activity Result*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Set Fragment every time the app comes back from another activity to refresh data.
        ContentListFragment contentListFragment = new ContentListFragment();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.FrameLayout_ContentList_FragHolder, contentListFragment).commit();
        //Dev
        Log.i(TAG, "onActivityResult: " + "Back from Form Activity");
    }

    /*Interface from Content List Fragment triggered when a Cell on the list is selected*/
    @Override
    public void selectedPerson(Person person) {
        //Create the intent
        Intent intent = new Intent();
        intent.setAction(ACTION_VIEW_DATA);
        intent.putExtra(EXTRA_FIRST_NAME, person.getFirstName());
        intent.putExtra(EXTRA_LAST_NAME, person.getLastName());
        intent.putExtra(EXTRA_AGE, person.getAge());
        //Verify the intent will resolve to an Activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

}
