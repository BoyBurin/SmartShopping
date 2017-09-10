package com.example.seniorproject.smartshopping.controller.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.dialogfragment.FragmentDialogAddShoppingList;
import com.example.seniorproject.smartshopping.controller.fragment.loginfragment.LoginFragment;
import com.example.seniorproject.smartshopping.controller.fragment.mainfragment.ShoppingListFragment;
import com.example.seniorproject.smartshopping.model.dao.Group;
import com.example.seniorproject.smartshopping.model.dao.User;
import com.example.seniorproject.smartshopping.model.manager.GroupManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements ShoppingListFragment.ShoopingListFloatingButton
, FragmentDialogAddShoppingList.DeleteAddShoppingListDialog, FragmentDialogAddShoppingList.PickImageShoppingListDialog {
    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    private ImageButton btnPromotion;
    private ImageButton btnGroup;
    private ImageButton btnInventory;
    private ImageButton btnShoppingList;
    private ImageButton btnShoppingHistory;
    private ImageButton btnSetting;


    private FirebaseDatabase mRootRef;
    private DatabaseReference mGroupRef;

    final String SHOPPING_LIST_FRAGMENT = "ShoppingListFragment";
    final String  DIALOG_ADD_SHOPPING_LIST_FRAGMENT = "dialogAddShoppingListFragment";


    /***********************************************************************************************
     ************************************* Methods ********************************************
     ***********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init(){

        setTitle(GroupManager.getInstance().getCurrentGroup().getGroup().getName());

        btnPromotion = (ImageButton) findViewById(R.id.btnPromotion);
        btnPromotion.setOnClickListener(topBarOnClickListener);

        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(topBarOnClickListener);

        btnShoppingList = (ImageButton) findViewById(R.id.btnShoppingList);
        btnShoppingList.setOnClickListener(topBarOnClickListener);

        mRootRef = FirebaseDatabase.getInstance();
        mGroupRef = mRootRef.getReference().child("groups");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null) {
            ShoppingListFragment shoppingListFragment = ShoppingListFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerMain, shoppingListFragment,
                            SHOPPING_LIST_FRAGMENT)
                    .detach(shoppingListFragment)
                    .commit();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == FragmentDialogAddShoppingList.RC_PHOTO_PICKER){
            if(resultCode == RESULT_OK){
                Fragment addShoppingListDialog = getSupportFragmentManager()
                        .findFragmentByTag(DIALOG_ADD_SHOPPING_LIST_FRAGMENT);

                addShoppingListDialog.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onStart() {

        super.onStart();
        //updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    /***********************************************************************************************
     ************************************* Listener variable ********************************************
     ***********************************************************************************************/

    final View.OnClickListener topBarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == btnPromotion){
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
            }

            if(view == btnShoppingList){
                ShoppingListFragment shoppingListFragment = (ShoppingListFragment)
                        getSupportFragmentManager().findFragmentByTag(SHOPPING_LIST_FRAGMENT);

                getSupportFragmentManager().beginTransaction()
                        .attach(shoppingListFragment)
                        .commit();
            }

        }
    };

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    /***********************************************************************************************
     ************************************* Implement Methods ********************************************
     ***********************************************************************************************/

    @Override
    public void setShoopingListFloatingButtonFloationgButton() {
        DialogFragment addShoppingListDialog =
                FragmentDialogAddShoppingList.newInstance();
        addShoppingListDialog.show(getSupportFragmentManager(), DIALOG_ADD_SHOPPING_LIST_FRAGMENT);

    }


    @Override
    public void delete() {
        Fragment addShoppingListDialog = getSupportFragmentManager().findFragmentByTag(DIALOG_ADD_SHOPPING_LIST_FRAGMENT);
        getSupportFragmentManager().beginTransaction()
                .remove(addShoppingListDialog)
                .commit();
        hideKeyboard();
    }

    @Override
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"),
                FragmentDialogAddShoppingList.RC_PHOTO_PICKER);
    }

}
