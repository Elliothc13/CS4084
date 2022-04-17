package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;


public class ProfileFragment extends Fragment {

    public void onLoginLoadFromDb() {
        // loadProfileImage(userId);
        // setProfileImage()


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        AppUser user = new AppUser();

        TextView userName = (TextView) v.findViewById(R.id.userName);
        EditText editUserName = (EditText) v.findViewById(R.id.editUserName);
        userName.setText(user.getName());
        editUserName.getText().clear();

        TextView email = (TextView) v.findViewById(R.id.email);
        EditText editEmail = (EditText) v.findViewById(R.id.editEmail);
        email.setText(user.getEmail());
        editEmail.getText().clear();

        ImageButton editBt = (ImageButton) v.findViewById(R.id.editButton);
        Button confirmBt = (Button) v.findViewById(R.id.confirmBt);
        Button cancelBt = (Button) v.findViewById(R.id.cancelBt);



        Button addBusinessBt = (Button) v.findViewById(R.id.addBusinessBt);
        TextView businessName = (TextView) v.findViewById(R.id.businessName);
        EditText editBusinessName = (EditText) v.findViewById(R.id.editBusinessName);
        TextView businessType = (TextView) v.findViewById(R.id.businessType);
        Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // updateBusinessType
            }
        });
        Button setLocationBt = (Button) v.findViewById(R.id.setLocationBt);
        editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBt.setVisibility(View.GONE);
                userName.setVisibility(View.INVISIBLE);

                editUserName.setVisibility(View.VISIBLE);
                confirmBt.setVisibility(View.VISIBLE);
                cancelBt.setVisibility(View.VISIBLE);

                businessName.setVisibility(View.INVISIBLE);
                businessType.setVisibility(View.INVISIBLE);
                editBusinessName.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);

                editUserName.requestFocus();

            }
        });
        confirmBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmBt.setVisibility(View.GONE);
                cancelBt.setVisibility(View.GONE);

                editUserName.setVisibility(View.GONE);
                editEmail.setVisibility(View.GONE);

                userName.setText(editUserName.getText());
                email.setText(editEmail.getText());

                userName.setVisibility(View.VISIBLE);
                editBt.setVisibility(View.VISIBLE);

                if (user.hasBusiness()) {

                } else if(true) {

                }
                v.clearFocus();
            }
        });


        cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmBt.setVisibility(View.GONE);
                cancelBt.setVisibility(View.GONE);

                editUserName.setVisibility(View.GONE);
                editUserName.setText(userName.getText());

                editEmail.setVisibility(View.GONE);
                editEmail.setText(email.getText());

                userName.setVisibility(View.VISIBLE);
                email.setVisibility((View.VISIBLE));

                if (user.hasBusiness()) {
                    editBusinessName.setText(businessName.getText());
                    editBusinessName.setVisibility(View.GONE);
                    businessName.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);
                    ArrayAdapter businessTypesArr = (ArrayAdapter) spinner.getAdapter();
                    spinner.setSelection(businessTypesArr.getPosition(businessType.getText()));
                    businessType.setVisibility(View.VISIBLE);
                    setLocationBt.setVisibility(View.GONE);
                }
                editBt.setVisibility(View.VISIBLE);
                v.clearFocus();
            }

        });
        // Inflate the layout for this fragment
        return v;
    }
}