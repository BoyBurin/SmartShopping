package com.example.seniorproject.smartshopping.controller.fragment.purchaseitem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.seniorproject.smartshopping.R;
import com.example.seniorproject.smartshopping.controller.activity.OCRActivity;
import com.example.seniorproject.smartshopping.controller.fragment.inventoryfragment.InventoryFragment;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventory;
import com.example.seniorproject.smartshopping.model.dao.iteminventory.ItemInventoryMap;
import com.example.seniorproject.smartshopping.model.dao.itemocr.ItemOCR;
import com.example.seniorproject.smartshopping.model.dao.itemocr.PurchaseItemWithAction;
import com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem.AddPurchaseItemCreator;
import com.example.seniorproject.smartshopping.model.daorecyclerview.addpurchaseitem.BaseAddPurchaseItem;
import com.example.seniorproject.smartshopping.model.manager.group.GroupManager;
import com.example.seniorproject.smartshopping.model.manager.iteminventory.ItemInventoryManager;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupAddPurchaseItemDetail;
import com.example.seniorproject.smartshopping.view.customviewgroup.CustomViewGroupPurchaseItem;
import com.example.seniorproject.smartshopping.view.recyclerviewadapter.AddPurchaseItemRecyclerViewAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class PurchaseItemAddFragment extends Fragment implements AddPurchaseItemRecyclerViewAdapter.OnItemClickListener {


    /***********************************************************************************************
     ************************************* Interface ********************************************
     ***********************************************************************************************/

    interface AddPurchaseItemInterface{
        public void addPurchaseItem(ItemOCR purchaseItem);
    }

    /***********************************************************************************************
     ************************************* Variable class ********************************************
     ***********************************************************************************************/


    private CustomViewGroupAddPurchaseItemDetail customViewGroupItemDetail;
    private RecyclerView recyclerView;

    private AddPurchaseItemRecyclerViewAdapter addPurchaseItemRecyclerViewAdapter;
    private ArrayList<ItemInventoryMap> itemInventoryMaps;
    private ItemInventoryManager itemInventoryManager;
    private AddPurchaseItemCreator addPurchaseItemCreator;
    private ArrayList<BaseAddPurchaseItem> baseAddPurchaseItems;
    private ItemInventoryMap currentItem;

    private FirebaseFirestore db;
    private CollectionReference cItems;
    private ListenerRegistration cItemsListener;


    /***********************************************************************************************
     ************************************* Method class ********************************************
     ***********************************************************************************************/

    public PurchaseItemAddFragment() {
        super();
    }

    @SuppressWarnings("unused")
    public static PurchaseItemAddFragment newInstance() {
        PurchaseItemAddFragment fragment = new PurchaseItemAddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

        if (savedInstanceState != null)
            onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_purchase_item_add, container, false);
        initInstances(rootView, savedInstanceState);
        return rootView;
    }

    private void init(Bundle savedInstanceState) {
        itemInventoryManager = new ItemInventoryManager();
        baseAddPurchaseItems = new ArrayList<>();
        addPurchaseItemCreator = new AddPurchaseItemCreator();
        itemInventoryMaps = new ArrayList<>();
        addPurchaseItemRecyclerViewAdapter = new AddPurchaseItemRecyclerViewAdapter(getContext());

        db = FirebaseFirestore.getInstance();
        cItems = db.collection("groups")
                .document(GroupManager.getInstance().getCurrentGroup().getId())
                .collection("items");

        cItemsListener = cItems.addSnapshotListener(itemListener);


    }

    @SuppressWarnings("UnusedParameters")
    private void initInstances(View rootView, Bundle savedInstanceState) {

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        customViewGroupItemDetail = (CustomViewGroupAddPurchaseItemDetail) rootView.findViewById(R.id.customViewGroupItemDetail);

        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(baseAddPurchaseItems.get(position).getType() == BaseAddPurchaseItem.ADD_PURCHASE_ITEM){
                    return 1;
                }
                else return 2;
            }
        });

        addPurchaseItemRecyclerViewAdapter.setItemClickListener(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(addPurchaseItemRecyclerViewAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(cItemsListener != null){
            cItemsListener.remove();
            cItemsListener = null;
        }
    }

    /*
         * Save Instance State Here
         */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @SuppressWarnings("UnusedParameters")
    private void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore Instance State here
    }

    public void setInterface(){
        baseAddPurchaseItems = new ArrayList<>();

        baseAddPurchaseItems.addAll(addPurchaseItemCreator.createAddPurchaseItem(itemInventoryManager.getItemInventoryMaps()));
        baseAddPurchaseItems.add(addPurchaseItemCreator.createAddPurchaseItemButton(cancelListener, addListener));

        addPurchaseItemRecyclerViewAdapter.setAddPurchaseItems(baseAddPurchaseItems);
        addPurchaseItemRecyclerViewAdapter.notifyDataSetChanged();
    }

    /***********************************************************************************************
     ************************************* Listener variables ********************************************
     ***********************************************************************************************/

    final EventListener<QuerySnapshot> itemListener = new EventListener<QuerySnapshot>() {
        @Override
        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
            if (e != null) {
                Log.w("TAG", "listen:error", e);
                return;
            }


            for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED:
                        DocumentSnapshot documentSnapshot = dc.getDocument();
                        ItemInventory item = documentSnapshot.toObject(ItemInventory.class);
                        String id = documentSnapshot.getId();

                        //Add
                        ItemInventoryMap itemMap = new ItemInventoryMap(id, item);
                        itemInventoryManager.addItemInventory(itemMap);
                        setInterface();

                        //Toast.makeText(getContext(), "Added " + item.getName(), Toast.LENGTH_SHORT).show();

                        break;

                    case MODIFIED:
                        documentSnapshot = dc.getDocument();
                        ItemInventory update = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        int index = itemInventoryManager.getIndexByKey(id);
                        itemMap = itemInventoryManager.getItemInventory(index);

                        //Update
                        itemMap.setItemInventory(update);
                        itemInventoryManager.sortItem();
                        setInterface();

                        Toast.makeText(getContext(), "Update " + update.getName(), Toast.LENGTH_SHORT).show();
                        break;

                    case REMOVED:
                        documentSnapshot = dc.getDocument();
                        item = documentSnapshot.toObject(ItemInventory.class);
                        id = documentSnapshot.getId();

                        index = itemInventoryManager.getIndexByKey(id);
                        itemInventoryManager.removeItemInventory(index);

                        setInterface();

                        Toast.makeText(getContext(), "Remove " + item.getName(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }
    };

    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            PurchaseItemOCRFragment addPurchaseItemInterface = (PurchaseItemOCRFragment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(OCRActivity.PURCHASE_ITEM_OCR_FRAGMENT);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(PurchaseItemAddFragment.this)
                    .show(addPurchaseItemInterface)
                    .commit();
        }
    };

    View.OnClickListener addListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(currentItem == null || customViewGroupItemDetail.isPriceEmpty() || customViewGroupItemDetail.isAmountEmpty()){
                return;
            }
            PurchaseItemOCRFragment addPurchaseItemInterface = (PurchaseItemOCRFragment) getActivity().getSupportFragmentManager()
                    .findFragmentByTag(OCRActivity.PURCHASE_ITEM_OCR_FRAGMENT);


            ItemOCR purchaseItem = new ItemOCR();
            purchaseItem.setPrice(customViewGroupItemDetail.getPrice() * customViewGroupItemDetail.getAmount());
            purchaseItem.setAmount(customViewGroupItemDetail.getAmount());
            purchaseItem.setItemInventoryMap(currentItem);


            ((AddPurchaseItemInterface)addPurchaseItemInterface).addPurchaseItem(purchaseItem);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .remove(PurchaseItemAddFragment.this)
                    .show(addPurchaseItemInterface)
                    .commit();

        }
    };

    /***********************************************************************************************
     ************************************* Inner class ********************************************
     ***********************************************************************************************/

    /***********************************************************************************************
     ************************************* Implementation ********************************************
     ***********************************************************************************************/

    @Override
    public void onItemClick(ItemInventoryMap itemInventory) {
        currentItem = itemInventory;
        customViewGroupItemDetail.setItemName(itemInventory.getItemInventory().getName());
    }

}