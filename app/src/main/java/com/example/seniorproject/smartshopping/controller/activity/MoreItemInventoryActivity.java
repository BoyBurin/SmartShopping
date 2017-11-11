package com.example.seniorproject.smartshopping.controller.activity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment.MoreItemInventoryFragment;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MoreItemInventoryActivity extends AppCompatActivity {

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/

    final String MORE_ITEM_INVENTORY_FRAGMENT = "moreItemInventoryFragment";

    private ItemInventoryMap itemInventoryMap;
    private int position;

    private FirebaseFirestore db;
    private DocumentReference dItem;

        /***********************************************************************************************
         ************************************* Methods ********************************************
         ***********************************************************************************************/

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_more_item_inventory);

            position = getIntent().getIntExtra("position", 0);
            itemInventoryMap = getIntent().getParcelableExtra("itemInventoryMap");


            initInstances();

            if(savedInstanceState == null){
                MoreItemInventoryFragment moreItemInventoryFragment = MoreItemInventoryFragment.newInstance(itemInventoryMap);
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.containerMoreItemInventory, moreItemInventoryFragment,
                                MORE_ITEM_INVENTORY_FRAGMENT)
                        .commit();
            }
        }

        private void initInstances() {
            setTitle(itemInventoryMap.getItemInventory().getName().toString());
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#A1887F")));

            db = FirebaseFirestore.getInstance();
            dItem = db.collection("groups").document(GroupManager.getInstance().getCurrentGroup().getId())
                    .collection("items").document(itemInventoryMap.getId());
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            hideSoftKeyboard();
            finish();
            return true;
        }

        if(item.getItemId() == R.id.action_delete){
            dItem.delete().addOnSuccessListener(deleteItemSuccess)
                    .addOnFailureListener(deleteItemFailed);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Runtime.getRuntime().gc();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if(inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    /***********************************************************************************************
         ************************************* Listener variable ********************************************
         ***********************************************************************************************/

    final OnSuccessListener<Void> deleteItemSuccess = new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(Void aVoid) {
            Log.d("TAG", itemInventoryMap.getItemInventory().getName() + " successfully deleted!");
        }
    };

    final OnFailureListener deleteItemFailed = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Log.d("TAG", "Error deleting " + itemInventoryMap.getItemInventory().getName());
        }
    };


        /***********************************************************************************************
         ************************************* Implement Methods ********************************************
         ***********************************************************************************************/
}
