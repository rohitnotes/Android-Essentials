// Juan Pablo Anaya
// MDF3 - 201608
// FormFragment

package com.paix.jpam.anayajuan_ce07.form;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.paix.jpam.anayajuan_ce07.R;
import com.paix.jpam.anayajuan_ce07.dataModel.Person;

public class FormFragment extends Fragment {

    //TAG
    private static final String TAG = "FormFragment";
    //Interface Listener for saving new Person
    NewPersonListener listener;
    //Person to be saved
    Person person;
    //Edit Texts
    private EditText firstNameHolder;
    private EditText lastNameHolder;
    private EditText ageHolder;

    //On Attach
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //Outer Class Interface
        if (context instanceof NewPersonListener) {
            listener = (NewPersonListener) context;
        } else {
            throw new IllegalArgumentException("Please Add Interface");
        }
    }

    //On Create
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        //Dev
        Log.i(TAG, "onCreate: " + "FORM_FRAGMENT");
    }

    /*Menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_form, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.MenuItem_Save:
                //Set Listener for activity to save new data
                if (!isEmpty(firstNameHolder) && !isEmpty(lastNameHolder) && !isEmpty(ageHolder)) {
                    int age = Integer.parseInt(ageHolder.getText().toString().trim());
                    person = new Person(firstNameHolder.getText().toString().trim(),
                            lastNameHolder.getText().toString().trim(), age);
                    listener.saveNewPersonFromForm(person);
                    return true;
                }
                Toast.makeText(getContext(), "No Empty Fields", Toast.LENGTH_SHORT).show();
                return false;
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //Set Edit Text Views
        View v = inflater.inflate(R.layout.fragment_form, container, false);
        //Text Views
        firstNameHolder = (EditText) v.findViewById(R.id.EditText_Form_FirstName);
        lastNameHolder = (EditText) v.findViewById(R.id.EditText_Form_LastName);
        ageHolder = (EditText) v.findViewById(R.id.EditText_Form_Age);
        //Return Custom View
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Clear UI

    }

    /*Check For Empty TextFields*/
    public static Boolean isEmpty(EditText editText) {
        if (editText.getText().toString().equals("")) {
            return true;
        } else {
            return false;
        }
    }

}
