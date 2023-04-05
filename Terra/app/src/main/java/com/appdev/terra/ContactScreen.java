package com.appdev.terra;

import static java.lang.Character.isDigit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.widget.Toast;

import com.appdev.terra.models.UserModel;
import com.appdev.terra.services.AccountService;
import com.appdev.terra.services.IServices.IFirestoreCallback;
import com.appdev.terra.services.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.Iterator;

public class ContactScreen extends AppCompatActivity {

    public static ArrayList<ContactList>  contactLists = new ArrayList<>();

    static ArrayList<PossibleContactList> possibleContactLists = new ArrayList<PossibleContactList>();

    static ArrayList<UserModel>  matchedUserModels = new ArrayList<>();
    static int profilePicture = R.drawable.baseline_person_24;

    BottomNavigationView bottomNavigationView;
    Button buttonAddFriend;

    private PossibleContactListAdapter possibleContactListAdapter  = new PossibleContactListAdapter(this, possibleContactLists);;
    private RecyclerView recyclerView2;

    final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    ArrayList<String> phoneNumbers = new ArrayList<>();

    ArrayList<String> phoneNumbersMatched = new ArrayList<>();

    UserService UserService = new UserService();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_screen);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.contact);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.contact:
                        return true;

                    case R.id.sos:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), SearchScreen.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });



        buttonAddFriend = (Button) findViewById(R.id.add_friend);

        buttonAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactScreen.this, AddFriendScreen.class);
                startActivity(intent);

            }
        });
        //Getting contact permission from User
        requestContactPermission();

        MatchUsersPhoneNumbers();

        RecyclerView recyclerView = findViewById(R.id.contact_list_view);


        setUpContactList();



        ContactListAdapter adapter = new ContactListAdapter(this, contactLists);


        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView2 = findViewById(R.id.recyclerView2);

        recyclerView2.setAdapter(possibleContactListAdapter);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));





        printPhoneNumbers(phoneNumbers);
        System.out.println(phoneNumberFormatter("(612) 345-678")+"is a formatted number");
        System.out.println(phoneNumberFormatter("+31 6 35310622 ")+"is a formatted number");
    }

    private void setUpContactList() {

        for (ContactList contact : PossibleContactListAdapter.AddedContactList) {
            contactLists.add(contact);

        }


    }

    private void setUpPossibleContactList() {
        Iterator<UserModel> iterator = matchedUserModels.iterator();
        while (iterator.hasNext()) {
            UserModel userModel = iterator.next();
            boolean cont = true;
            for (ContactList contact : PossibleContactListAdapter.AddedContactList) {
                if(contact.phoneNumber.equals(phoneNumberFormatter(Long.toString(userModel.phoneNumber)))) {
                    cont = false;
                }
            }
            for (PossibleContactList contact : PossibleContactListAdapter.DeletedContactList) {
                if(contact.phoneNumber.equals(phoneNumberFormatter(Long.toString(userModel.phoneNumber)))) {
                    cont = false;
                }
            }
            if (cont) {
                possibleContactLists.add(new PossibleContactList(userModel.name, "Unavailable", profilePicture, phoneNumberFormatter(Long.toString(userModel.phoneNumber))));
            } else {
                iterator.remove();
            }
        }
    }

    private void requestContactPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            getContactPhoneNumbers();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getContactPhoneNumbers();
            } else {
                Toast.makeText(this, "Permission must be granted to access contacts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getContactPhoneNumbers() {

        ArrayList<String> phoneNumbersTemp = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

        if (cursor != null) {
            int phoneNumberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            if (phoneNumberColumnIndex >= 0) {
                while (cursor.moveToNext()) {
                    String phoneNumber = cursor.getString(phoneNumberColumnIndex);
                    phoneNumbersTemp.add(phoneNumber);
                }
            }
            cursor.close();
        }
        phoneNumbers = phoneNumbersTemp;


    }

    private void printPhoneNumbers(ArrayList<String> phoneNumbers) {
        for (String phoneNumber : phoneNumbers) {
            System.out.println( phoneNumber);

        }
    }

    //method to get all the current users phone Numbers in database

    private void MatchUsersPhoneNumbers() {
        UserService userService = new UserService();
        userService.getAllUsers(new IFirestoreCallback<UserModel>() {
            @Override
            public void onCallback(ArrayList<UserModel> userModels) {
                for (UserModel userModel : userModels) {
                    System.out.println(Long.toString(userModel.phoneNumber)+"from db");

                    possibleContactListAdapter.notifyDataSetChanged();
                    for (String phoneNumber : phoneNumbers){
                        if (phoneNumberFormatter(phoneNumber).equals(Long.toString(userModel.phoneNumber))){
                            System.out.println(phoneNumberFormatter(phoneNumber)+" is compared to"+Long.toString(userModel.phoneNumber));
                            matchedUserModels.add(userModel);

                        }
                    }


                }
                setUpPossibleContactList();
            }

        });

    }

    private void MatchContacts() {
        AccountService accountService = new AccountService();
        for (UserModel userModel : accountService.logedInUserModel.contacts){
            contactLists.add(new ContactList(userModel.name,"Sample Status", profilePicture, phoneNumberFormatter(Long.toString(userModel.phoneNumber))));

        }

    }


    private String phoneNumberFormatter(String number) {
        String newNumber = "";
        String oldNumber = number;
        if (number.contains("+")) {
            oldNumber = oldNumber.substring((oldNumber.indexOf('+')+3));
        }
        for (int i = 0; i < oldNumber.length(); i++ ) {
            if (isDigit(oldNumber.charAt(i))) {
                newNumber += oldNumber.charAt(i);
            }
        }
        return newNumber;
    }




}