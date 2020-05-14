package com.atakanbalta.fattofitv2;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class FoodFragment extends Fragment {

    /*- 01 Class Variables -------------------------------------------------------------- */
    private View mainView;
    private Cursor listCursor;

    // Action buttons on toolbar
    private MenuItem menuItemEdit;
    private MenuItem menuItemDelete;

    // Holder for buttons on toolbar
    private String currentId;
    private String currentName;




    /*- 02 Fragment Variables ----------------------------------------------------------- */

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /*- 03 Constructur ------------------------------------------------------------------ */

    public FoodFragment() {
        // GEREKLİ SİLME
    }


    /*- 04 Creating Fragment ------------------------------------------------------------- */
    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    /*- 05 on Activity Created ---------------------------------------------------------- */

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Set title */
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Food");

        // Populate the list of categories
        populateListFood();

        // Create menu
        setHasOptionsMenu(true);
    } // onActivityCreated


    /*- 06 On create view ---------------------------------------------------------------- */
    // Sets main View
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_food, container, false);
        return mainView;
    }

    /*- 07 set main view ----------------------------------------------------------------- */
    // Changing view method in fragmetn
    private void setMainView(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

    /*- 08 on Create Options Menu -------------------------------------------------------- */
    // Creating action icon on toolbar
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        // Inflate menu
        //MenuInflater menuInflater = ((MainActivity)getActivity()).getMenuInflater();
        // inflater.inflate(R.menu.menu_categories, menu);

        ((MainActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_food, menu);

        // Assign menu items to variables
        menuItemEdit = menu.findItem(R.id.menu_action_food_edit);
        menuItemDelete = menu.findItem(R.id.menu_action_food_delete);

        // Hide as default
        menuItemEdit.setVisible(false);
        menuItemDelete.setVisible(false);
    }

    /*- 09 on Options Item Selected ------------------------------------------------------ */

    // Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int id = menuItem.getItemId();
        if (id == R.id.menu_action_food_add) {
            addFood();
        }
        if (id == R.id.menu_action_food_edit) {
            editFood();
        }
        if (id == R.id.menu_action_food_delete) {
            deleteFood();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    /*- Our own methods -*/


    /*- populate List -------------------------------------------------------------- */
    public void populateListFood(){

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Get categories
        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_manufactor_name",
                "food_description",
                "food_serving_size_gram",
                "food_serving_size_gram_mesurment",
                "food_serving_size_pcs",
                "food_serving_size_pcs_mesurment",
                "food_energy_calculated"
        };
        try{
            listCursor = db.select("food", fields, "", "", "food_name", "ASC");
        }
        catch (SQLException sqle){
            Toast.makeText(getActivity(), sqle.toString(), Toast.LENGTH_LONG).show();
        }



        ListView lvItems = (ListView)getActivity().findViewById(R.id.listViewFood);




        FoodCursorAdapter continentsAdapter = new FoodCursorAdapter(getActivity(), listCursor);


        try{
            lvItems.setAdapter(continentsAdapter); // uses ContinensCursorAdapter
        }
        catch (Exception e){
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }


        // OnClick
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                listItemClicked(arg2);
            }
        });


        // Close db
        db.close();

    }

    /*- List item clicked ------------------------------------------------------------ */
    public void listItemClicked(int listItemIDClicked) {

        /* Change layout */
        int id = R.layout.fragment_food_view;
        setMainView(id);

        // Show edt button
        menuItemEdit.setVisible(true);
        menuItemDelete.setVisible(true);

        // Move cursor to ID clicked
        listCursor.moveToPosition(listItemIDClicked);


        currentId = listCursor.getString(0);
        currentName = listCursor.getString(1);

        // Change title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(currentName);


        /*  Get data from database */

        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();


        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_manufactor_name",
                "food_description",
                "food_serving_size_gram",
                "food_serving_size_gram_mesurment",
                "food_serving_size_pcs",
                "food_serving_size_pcs_mesurment",
                "food_energy",
                "food_proteins",
                "food_carbohydrates",
                "food_fat",
                "food_energy_calculated",
                "food_proteins_calculated",
                "food_carbohydrates_calculated",
                "food_fat_calculated",
                "food_user_id",
                "food_barcode",
                "food_category_id",
                "food_image_a",
                "food_image_b",
                "food_image_c"
        };
        String currentIdSQL = db.quoteSmart(currentId);
        Cursor foodCursor = db.select("food", fields, "_id", currentIdSQL);

        // Convert cursor to strings
        String stringId = foodCursor.getString(0);
        String stringName = foodCursor.getString(1);
        String stringManufactorName = foodCursor.getString(2);
        String stringDescription = foodCursor.getString(3);
        String stringServingSize = foodCursor.getString(4);
        String stringServingMesurment = foodCursor.getString(5);
        String stringServingNameNumber = foodCursor.getString(6);
        String stringServingNameWord = foodCursor.getString(7);
        String stringEnergy = foodCursor.getString(8);
        String stringProteins = foodCursor.getString(9);
        String stringCarbohydrates = foodCursor.getString(10);
        String stringFat = foodCursor.getString(11);
        String stringEnergyCalculated = foodCursor.getString(12);
        String stringProteinsCalculated = foodCursor.getString(13);
        String stringCarbohydratesCalculated = foodCursor.getString(14);
        String stringFatCalculated = foodCursor.getString(15);
        String stringUserId = foodCursor.getString(16);
        String stringBarcode = foodCursor.getString(17);
        String stringCategoryId = foodCursor.getString(18);
        String stringImageA = foodCursor.getString(19);
        String stringImageB = foodCursor.getString(20);
        String stringImageC = foodCursor.getString(21);


        // Headline
        TextView textViewViewFoodName = (TextView) getView().findViewById(R.id.textViewViewFoodName);
        textViewViewFoodName.setText(stringName);

        // Sub headline
        TextView textViewViewFoodManufactorName = (TextView) getView().findViewById(R.id.textViewViewFoodManufactorName);
        textViewViewFoodManufactorName.setText(stringManufactorName);

        // Image

        // Calculation line
        TextView textViewViewFoodAbout = (TextView) getView().findViewById(R.id.textViewViewFoodAbout);
        String foodAbout = stringServingSize + " " + stringServingMesurment + " = " +
                stringServingNameNumber  + " " + stringServingNameWord + ".";
        textViewViewFoodAbout.setText(foodAbout);

        // Description
        TextView textViewViewFoodDescription = (TextView) getView().findViewById(R.id.textViewViewFoodDescription);
        textViewViewFoodDescription.setText(stringDescription);

        // Calories table
        TextView textViewViewFoodEnergyPerHundred = (TextView) getView().findViewById(R.id.textViewViewFoodEnergyPerHundred);
        TextView textViewViewFoodProteinsPerHundred = (TextView) getView().findViewById(R.id.textViewViewFoodProteinsPerHundred);
        TextView textViewViewFoodCarbsPerHundred = (TextView) getView().findViewById(R.id.textViewViewFoodCarbsPerHundred);
        TextView textViewViewFoodFatPerHundred = (TextView) getView().findViewById(R.id.textViewViewFoodFatPerHundred);

        TextView textViewViewFoodEnergyPerN = (TextView) getView().findViewById(R.id.textViewViewFoodEnergyPerN);
        TextView textViewViewFoodProteinsPerN = (TextView) getView().findViewById(R.id.textViewViewFoodProteinsPerN);
        TextView textViewViewFoodCarbsPerN = (TextView) getView().findViewById(R.id.textViewViewFoodCarbsPerN);
        TextView textViewViewFoodFatPerN = (TextView) getView().findViewById(R.id.textViewViewFoodFatPerN);

        textViewViewFoodEnergyPerHundred.setText(stringEnergy);
        textViewViewFoodProteinsPerHundred.setText(stringProteins);
        textViewViewFoodCarbsPerHundred.setText(stringCarbohydrates);
        textViewViewFoodFatPerHundred.setText(stringFat);

        textViewViewFoodEnergyPerN.setText(stringEnergyCalculated);
        textViewViewFoodProteinsPerN.setText(stringProteinsCalculated);
        textViewViewFoodCarbsPerN.setText(stringCarbohydratesCalculated);
        textViewViewFoodFatPerN.setText(stringFatCalculated);

        db.close();
    }


    /*- Edit food ----------------------------------------------------------------- */
    String selectedMainCategoryName = "";
    public void editFood(){

        /* Change layout */
        int id = R.layout.fragment_food_edit;
        setMainView(id);



        currentId = listCursor.getString(0);
        currentName = listCursor.getString(1);

        // Change title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Edit " + currentName);


        /*  Get data from database */

        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();


        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_manufactor_name",
                "food_description",
                "food_serving_size_gram",
                "food_serving_size_gram_mesurment",
                "food_serving_size_pcs",
                "food_serving_size_pcs_mesurment",
                "food_energy",
                "food_proteins",
                "food_carbohydrates",
                "food_fat",
                "food_energy_calculated",
                "food_proteins_calculated",
                "food_carbohydrates_calculated",
                "food_fat_calculated",
                "food_user_id",
                "food_barcode",
                "food_category_id",
                "food_image_a",
                "food_image_b",
                "food_image_c"
        };
        String currentIdSQL = db.quoteSmart(currentId);
        Cursor foodCursor = db.select("food", fields, "_id", currentIdSQL);

        // Convert cursor to strings
        String stringId = foodCursor.getString(0);
        String stringName = foodCursor.getString(1);
        String stringManufactorName = foodCursor.getString(2);
        String stringDescription = foodCursor.getString(3);

        String stringServingSize = foodCursor.getString(4);
        String stringServingMesurment = foodCursor.getString(5);
        String stringServingNameNumber = foodCursor.getString(6);
        String stringServingNameWord = foodCursor.getString(7);

        String stringEnergy = foodCursor.getString(8);
        String stringProteins = foodCursor.getString(9);
        String stringCarbohydrates = foodCursor.getString(10);
        String stringFat = foodCursor.getString(11);
        String stringEnergyCalculated = foodCursor.getString(12);
        String stringProteinsCalculated = foodCursor.getString(13);
        String stringCarbohydratesCalculated = foodCursor.getString(14);
        String stringFatCalculated = foodCursor.getString(15);

        String stringUserId = foodCursor.getString(16);
        String stringBarcode = foodCursor.getString(17);
        String stringCategoryId = foodCursor.getString(18);
        String stringImageA = foodCursor.getString(19);
        String stringImageB = foodCursor.getString(20);
        String stringImageC = foodCursor.getString(21);


        /* General */

        // Name
        EditText editTextEditFoodName = (EditText) getView().findViewById(R.id.editTextEditFoodName);
        editTextEditFoodName.setText(stringName);

        // Manufactor
        TextView editTextEditFoodManufactor = (TextView) getView().findViewById(R.id.editTextEditFoodManufactor);
        editTextEditFoodManufactor.setText(stringManufactorName);

        // Description
        EditText editTextEditFoodDescription = (EditText) getView().findViewById(R.id.editTextEditFoodDescription);
        editTextEditFoodDescription.setText(stringDescription);

        // Barcode
        EditText editTextEditFoodBarcode = (EditText) getView().findViewById(R.id.editTextEditFoodBarcode);
        editTextEditFoodBarcode.setText(stringBarcode);

        /* What category food is in, and its parent */
        // Toast.makeText(getActivity(), "Food category ID: " + stringCategoryId, Toast.LENGTH_LONG).show();
        String spinnerFields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        // Find the category that the food is using (has to be a sub category)
        Cursor dbCursorCurrentFoodCategory = db.select("categories", spinnerFields, "_id", stringCategoryId, "category_name", "ASC");
        // Toast.makeText(getActivity(), "Food category name: " + dbCursorCurrentFoodCategory.getString(1), Toast.LENGTH_LONG).show();

        String currentFoodCategoryID = dbCursorCurrentFoodCategory.getString(2);


        /* Sub categories */
        Cursor dbCursorSub = db.select("categories", spinnerFields, "category_parent_id", currentFoodCategoryID, "category_name", "ASC");

        // Creating array
        int dbCursorCount = dbCursorSub.getCount();
        String[] arraySpinnerCategoriesSub = new String[dbCursorCount];

        // find out sub category selected
        int selectedSubCategoryIndex = 0;
        String selectedSubCategoryParentId = "0";

        // Convert Cursor to String
        //Toast.makeText(getActivity(), "Food category (from SQLite) stringCategoryId: " + stringCategoryId, Toast.LENGTH_SHORT).show();
        for(int x=0;x<dbCursorCount;x++){
            // Toast.makeText(getActivity(), "Loop ctegoryId: " + dbCursorSub.getString(0).toString(),  Toast.LENGTH_LONG).show();
            arraySpinnerCategoriesSub[x] = dbCursorSub.getString(1).toString();

            if(dbCursorSub.getString(0).toString().equals(stringCategoryId)){
                selectedSubCategoryIndex = x;
                selectedSubCategoryParentId = dbCursorSub.getString(2).toString();
                //Toast.makeText(getActivity(), "Found current category", Toast.LENGTH_LONG).show();
            }

            dbCursorSub.moveToNext();
        }

        // Populate spinner
        Spinner spinnerSubCat = (Spinner) getActivity().findViewById(R.id.spinnerEditFoodCategorySub);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinnerCategoriesSub);
        spinnerSubCat.setAdapter(adapter);

        // Select index of sub
        spinnerSubCat.setSelection(selectedSubCategoryIndex);

        /* Main category */
        Cursor dbCursorMain = db.select("categories", spinnerFields, "category_parent_id", "0", "category_name", "ASC");

        // Creating array
        dbCursorCount = dbCursorMain.getCount();
        String[] arraySpinnerMainCategories = new String[dbCursorCount];

        // Select correct main category
        int selectedMainCategoryIndex = 0;

        // Convert Cursor to String
        for(int x=0;x<dbCursorCount;x++){
            arraySpinnerMainCategories[x] = dbCursorMain.getString(1).toString();


            if(dbCursorMain.getString(0).toString().equals(selectedSubCategoryParentId)){
                selectedMainCategoryIndex = x;
                selectedMainCategoryName = dbCursorMain.getString(1).toString();
                //Toast.makeText(getActivity(), "Found current category", Toast.LENGTH_LONG).show();
            }
            dbCursorMain.moveToNext();
        }

        // Populate spinner
        Spinner spinnerCatMain = (Spinner) getActivity().findViewById(R.id.spinnerEditFoodCategoryMain);
        ArrayAdapter<String> adapterMain = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinnerMainCategories);
        spinnerCatMain.setAdapter(adapterMain);

        // Select index of sub
        spinnerCatMain.setSelection(selectedMainCategoryIndex);


        /* Serving Table */

        // Size
        EditText editTextEditFoodSize = (EditText) getView().findViewById(R.id.editTextEditFoodSize);
        editTextEditFoodSize.setText(stringServingSize);

        // Mesurment
        EditText editTextEditFoodMesurment = (EditText) getView().findViewById(R.id.editTextEditFoodMesurment);
        editTextEditFoodMesurment.setText(stringServingMesurment);

        // Number
        EditText editTextEditFoodNumber = (EditText) getView().findViewById(R.id.editTextEditFoodNumber);
        editTextEditFoodNumber.setText(stringServingNameNumber);

        // Word
        EditText editTextEditFoodWord = (EditText) getView().findViewById(R.id.editTextEditFoodWord);
        editTextEditFoodWord.setText(stringServingNameWord);

        /* Calories table */

        // Energy
        EditText editTextEditFoodEnergyPerHundred = (EditText) getView().findViewById(R.id.editTextEditFoodEnergyPerHundred);
        editTextEditFoodEnergyPerHundred.setText(stringEnergy);

        // Proteins
        EditText editTextEditFoodProteinsPerHundred = (EditText) getView().findViewById(R.id.editTextEditFoodProteinsPerHundred);
        editTextEditFoodProteinsPerHundred.setText(stringProteins);

        // Energy
        EditText editTextEditFoodCarbsPerHundred = (EditText) getView().findViewById(R.id.editTextEditFoodCarbsPerHundred);
        editTextEditFoodCarbsPerHundred.setText(stringCarbohydrates);

        // Energy
        EditText editTextEditFoodFatPerHundred = (EditText) getView().findViewById(R.id.editTextEditFoodFatPerHundred);
        editTextEditFoodFatPerHundred.setText(stringFat);

        /* Main Category listener */
        spinnerCatMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                editFoodMainCategoryChanged(selectedItem);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        /* SubmitButton listener */
        Button buttonEditFood = (Button)getActivity().findViewById(R.id.buttonEditFood);
        buttonEditFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEditFoodSubmitOnClick();
            }
        });


        /* Close db */
        db.close();


    } // editFood
    public void editFoodMainCategoryChanged(String selectedItemCategoryName){
        if(!(selectedItemCategoryName.equals(selectedMainCategoryName))){


            /* Database */
            DBAdapter db = new DBAdapter(getActivity());
            db.open();

            // Find ID of main category
            String selectedItemCategoryNameSQL = db.quoteSmart(selectedItemCategoryName);
            String spinnerFields[] = new String[] {
                    "_id",
                    "category_name",
                    "category_parent_id"
            };
            Cursor findMainCategoryID = db.select("categories", spinnerFields, "category_name", selectedItemCategoryNameSQL);
            String stringMainCategoryID  = findMainCategoryID.getString(0).toString();
            String stringMainCategoryIDSQL = db.quoteSmart(stringMainCategoryID);


            /* Sub categories */
            Cursor dbCursorSub = db.select("categories", spinnerFields, "category_parent_id", stringMainCategoryIDSQL, "category_name", "ASC");

            // Creating array
            int dbCursorCount = dbCursorSub.getCount();
            String[] arraySpinnerCategoriesSub = new String[dbCursorCount];


            // Convert Cursor to String
            for(int x=0;x<dbCursorCount;x++){
                arraySpinnerCategoriesSub[x] = dbCursorSub.getString(1).toString();
                dbCursorSub.moveToNext();
            }

            // Populate spinner
            Spinner spinnerSubCat = (Spinner) getActivity().findViewById(R.id.spinnerEditFoodCategorySub);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, arraySpinnerCategoriesSub);
            spinnerSubCat.setAdapter(adapter);



            db.close();
        }
    }

    /*- Edit food submit on click ------------------------------------------------------ */
    private void buttonEditFoodSubmitOnClick(){
        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Error?
        int error = 0;

        // DB fields
        long rowID = Long.parseLong(currentId);


        /* General */

        // Name
        EditText editTextEditFoodName = (EditText)getActivity().findViewById(R.id.editTextEditFoodName);
        String stringName = editTextEditFoodName.getText().toString();
        String stringNameSQL = db.quoteSmart(stringName);
        if(stringName.equals("")){
            Toast.makeText(getActivity(), "Lütfen isminizi doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Manufactor
        EditText editTextEditFoodManufactor = (EditText)getActivity().findViewById(R.id.editTextEditFoodManufactor);
        String stringManufactor = editTextEditFoodManufactor.getText().toString();
        String stringManufactorSQL = db.quoteSmart(stringManufactor);
        if(stringManufactor.equals("")){
            Toast.makeText(getActivity(), "Üretici kısmını doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Description
        EditText editTextEditFoodDescription = (EditText)getActivity().findViewById(R.id.editTextEditFoodDescription);
        String stringDescription = editTextEditFoodDescription.getText().toString();
        String stringDescriptionSQL = db.quoteSmart(stringDescription);

        // Barcode
        EditText editTextEditFoodBarcode = (EditText)getActivity().findViewById(R.id.editTextEditFoodBarcode);
        String stringBarcode = editTextEditFoodBarcode.getText().toString();
        String stringBarcodeSQL = db.quoteSmart(stringBarcode);

        /* Category */
        // Sub category
        Spinner spinnerSubCat = (Spinner)getActivity().findViewById(R.id.spinnerEditFoodCategorySub);
        int intSubCategoryIndex = spinnerSubCat.getSelectedItemPosition();
        String stringSpinnerSubCategoryName = spinnerSubCat.getSelectedItem().toString();

        // Find we want to find parent ID from the text
        String stringSpinnerSubCategoryNameSQL = db.quoteSmart(stringSpinnerSubCategoryName);
        String spinnerFields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        Cursor findstringSpinnerSubCategoryID = db.select("categories", spinnerFields, "category_name", stringSpinnerSubCategoryNameSQL);
        String stringSubCategoryID  = findstringSpinnerSubCategoryID.getString(0).toString();
        String stringSubCategoryIDSQL = db.quoteSmart(stringSubCategoryID);


        /* Serving Table */

        // Size
        EditText editTextEditFoodSize = (EditText)getActivity().findViewById(R.id.editTextEditFoodSize);
        String stringSize = editTextEditFoodSize.getText().toString();
        String stringSizeSQL = db.quoteSmart(stringSize);
        double doubleServingSize = 0;
        if(stringSize.equals("")){
            Toast.makeText(getActivity(), "Lütfen boyut doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleServingSize = Double.parseDouble(stringSize);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Servis bir numara değil.", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }

        // Mesurment
        EditText editTextEditFoodMesurment = (EditText)getActivity().findViewById(R.id.editTextEditFoodMesurment);
        String stringMesurment = editTextEditFoodMesurment.getText().toString();
        String stringMesurmentSQL = db.quoteSmart(stringMesurment);
        if(stringMesurment.equals("")){
            Toast.makeText(getActivity(), "Ölçü birimini giriniz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Number
        EditText editTextEditFoodNumber = (EditText)getActivity().findViewById(R.id.editTextEditFoodNumber);
        String stringNumber = editTextEditFoodNumber.getText().toString();
        String stringNumberSQL = db.quoteSmart(stringNumber);
        if(stringNumber.equals("")){
            Toast.makeText(getActivity(), "Doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Word
        EditText editTextEditFoodWord = (EditText)getActivity().findViewById(R.id.editTextEditFoodWord);
        String stringWord = editTextEditFoodWord.getText().toString();
        String stringWordSQL = db.quoteSmart(stringWord);
        if(stringWord.equals("")){
            Toast.makeText(getActivity(), "Doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }


        /* Calories table */
        // Energy
        EditText editTextEditFoodEnergyPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodEnergyPerHundred);
        String stringEnergyPerHundred = editTextEditFoodEnergyPerHundred.getText().toString();
        stringEnergyPerHundred = stringEnergyPerHundred.replace(",", ".");
        double doubleEnergyPerHundred = 0;
        if(stringEnergyPerHundred.equals("")){
            Toast.makeText(getActivity(), "Enerjiyi doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleEnergyPerHundred = Double.parseDouble(stringEnergyPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Enerji bir sayı değil.", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringEnergyPerHundredSQL = db.quoteSmart(stringEnergyPerHundred);

        // Proteins
        EditText editTextEditFoodProteinsPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodProteinsPerHundred);
        String stringProteinsPerHundred = editTextEditFoodProteinsPerHundred.getText().toString();
        stringProteinsPerHundred = stringProteinsPerHundred.replace(",", ".");
        double doubleProteinsPerHundred = 0;
        if(stringProteinsPerHundred.equals("")){
            Toast.makeText(getActivity(), "Protein değerini giriniz!.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleProteinsPerHundred = Double.parseDouble(stringProteinsPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Protein bir sayı değil.\n" + "Yazdığın: " + stringProteinsPerHundred, Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringProteinsPerHundredSQL = db.quoteSmart(stringProteinsPerHundred);

        // Carbs
        EditText editTextEditFoodCarbsPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodCarbsPerHundred);
        String stringCarbsPerHundred = editTextEditFoodCarbsPerHundred.getText().toString();
        stringCarbsPerHundred = stringCarbsPerHundred.replace(",", ".");
        double doubleCarbsPerHundred = 0;
        if(stringCarbsPerHundred.equals("")){
            Toast.makeText(getActivity(), "Karbonhidrat değerini giriniz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleCarbsPerHundred = Double.parseDouble(stringCarbsPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Karbonhidrat bir sayı değil.\n" + "Yazdığın: " + stringCarbsPerHundred, Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringCarbsPerHundredSQL = db.quoteSmart(stringCarbsPerHundred);

        // Fat
        EditText editTextEditFoodFatPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodFatPerHundred);
        String stringFatPerHundred = editTextEditFoodFatPerHundred.getText().toString();
        stringFatPerHundred = stringFatPerHundred.replace(",", ".");
        double doubleFatPerHundred = 0;
        if(stringFatPerHundred.equals("")){
            Toast.makeText(getActivity(), "Yağ değerini giriniz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleFatPerHundred = Double.parseDouble(stringFatPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Yağ bir sayı olmalıdır.", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringFatPerHundredSQL = db.quoteSmart(stringFatPerHundred);


        /* Update */
        if(error == 0){

            /* Calories table pr meal */
            double energyCalculated = Math.round((doubleEnergyPerHundred*doubleServingSize)/100);
            double proteinsCalculated = Math.round((doubleProteinsPerHundred*doubleServingSize)/100);
            double carbsCalculated = Math.round((doubleCarbsPerHundred*doubleServingSize)/100);
            double fatCalculated = Math.round((doubleFatPerHundred*doubleServingSize)/100);

            String stringEnergyCalculated = "" + energyCalculated;
            String stringProteinsCalculated = "" + proteinsCalculated;
            String stringCarbsCalculated = "" + carbsCalculated;
            String stringfatCalculated = "" + fatCalculated;

            String stringEnergyCalculatedSQL = db.quoteSmart(stringEnergyCalculated);
            String stringProteinsCalculatedSQL = db.quoteSmart(stringProteinsCalculated);
            String stringCarbsCalculatedSQL = db.quoteSmart(stringCarbsCalculated);
            String stringfatCalculatedSQL = db.quoteSmart(stringfatCalculated);


            String fields[] = new String[] {
                    "food_name",
                    "food_manufactor_name",
                    "food_description",
                    "food_serving_size_gram",
                    "food_serving_size_gram_mesurment",
                    "food_serving_size_pcs",
                    "food_serving_size_pcs_mesurment",
                    "food_energy",
                    "food_proteins",
                    "food_carbohydrates",
                    "food_fat",
                    "food_energy_calculated",
                    "food_proteins_calculated",
                    "food_carbohydrates_calculated",
                    "food_fat_calculated",
                    "food_barcode",
                    "food_category_id"
            };
            String values[] = new String[] {
                    stringNameSQL,
                    stringManufactorSQL,
                    stringDescriptionSQL,
                    stringSizeSQL,
                    stringMesurmentSQL,
                    stringNumberSQL,
                    stringWordSQL,
                    stringEnergyPerHundredSQL,
                    stringProteinsPerHundredSQL,
                    stringCarbsPerHundredSQL,
                    stringFatPerHundredSQL,
                    stringEnergyCalculatedSQL,
                    stringProteinsCalculatedSQL,
                    stringCarbsCalculatedSQL,
                    stringfatCalculatedSQL,
                    stringBarcodeSQL,
                    stringSubCategoryIDSQL
            };

            long longCurrentID = Long.parseLong(currentId);

            db.update("food", "_id", longCurrentID, fields, values);

            // Toast
            Toast.makeText(getActivity(), "Değişiklik başarıyla kaydedildi", Toast.LENGTH_SHORT).show();

        } // error == 0

        db.close();
    } // buttonEditFoodSubmitOnClick



    /*- Delete food -------------------------------------------------------------------- */
    public void deleteFood(){

        /* Change layout */
        int id = R.layout.fragment_food_delete;
        setMainView(id);


        /* buttonCategoriesCancel listener */
        Button buttonCancel = (Button)getActivity().findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoodCancel();
            }
        });


        /* buttonCategoriesConfirmDelete listener */
        Button buttonConfirmDelete = (Button)getActivity().findViewById(R.id.buttonConfirmDelete);
        buttonConfirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoodConfirmDelete();
            }
        });

    } // deleteFood

    /*- Delete food cancel ----------------------------------------------------------------- */
    public void deleteFoodCancel(){
        // Move user back to correct design
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new FoodFragment(), FoodFragment.class.getName()).commit();


    }

    /*- Delete food confirm delete ---------------------------------------------------------- */
    public void deleteFoodConfirmDelete(){

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Current ID to long
        long longCurrentID = Long.parseLong(currentId);

        // Ready variables
        long currentIDSQL = db.quoteSmart(longCurrentID);

        // Delete
        db.delete("food", "_id", currentIDSQL);

        // Close db
        db.close();

        // Give message
        Toast.makeText(getActivity(), "Yiyecek silindi", Toast.LENGTH_LONG).show();

        // Move user back to correct design
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new FoodFragment(), FoodFragment.class.getName()).commit();


    }



    /*- Add food ------------------------------------------------------------------------------ */
    public void addFood(){
        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        /* Change layout */
        int id = R.layout.fragment_food_edit;
        setMainView(id);

        // Change title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Yiyecek ekle");


        /* Main category */
        String spinnerFields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        Cursor dbCursorMain = db.select("categories", spinnerFields, "category_parent_id", "0", "category_name", "ASC");

        // Creating array
        int dbCursorCount = dbCursorMain.getCount();
        String[] arraySpinnerMainCategories = new String[dbCursorCount];

        // Convert Cursor to String
        for(int x=0;x<dbCursorCount;x++){
            arraySpinnerMainCategories[x] = dbCursorMain.getString(1).toString();
            dbCursorMain.moveToNext();
        }

        // Populate spinner
        Spinner spinnerCatMain = (Spinner) getActivity().findViewById(R.id.spinnerEditFoodCategoryMain);
        ArrayAdapter<String> adapterMain = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinnerMainCategories);
        spinnerCatMain.setAdapter(adapterMain);


        /* Main Category listener */
        spinnerCatMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                editFoodMainCategoryChanged(selectedItem);
            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        /* SubmitButton listener */
        Button buttonEditFood = (Button)getActivity().findViewById(R.id.buttonEditFood);
        buttonEditFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAddFoodSubmitOnClick();
            }
        });


        /* Close db */
        db.close();
    } // addFood

    /*- Button add food submit on click ----------------------------------------------------- */
    public void buttonAddFoodSubmitOnClick(){
        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Error?
        int error = 0;


        /* General */

        // Name
        EditText editTextEditFoodName = (EditText)getActivity().findViewById(R.id.editTextEditFoodName);
        String stringName = editTextEditFoodName.getText().toString();
        String stringNameSQL = db.quoteSmart(stringName);
        if(stringName.equals("")){
            Toast.makeText(getActivity(), "Lütfen ismi doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Manufactor
        EditText editTextEditFoodManufactor = (EditText)getActivity().findViewById(R.id.editTextEditFoodManufactor);
        String stringManufactor = editTextEditFoodManufactor.getText().toString();
        String stringManufactorSQL = db.quoteSmart(stringManufactor);
        if(stringManufactor.equals("")){
            Toast.makeText(getActivity(), "Üreticiyi lütfen doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Description
        EditText editTextEditFoodDescription = (EditText)getActivity().findViewById(R.id.editTextEditFoodDescription);
        String stringDescription = editTextEditFoodDescription.getText().toString();
        String stringDescriptionSQL = db.quoteSmart(stringDescription);

        // Barcode
        EditText editTextEditFoodBarcode = (EditText)getActivity().findViewById(R.id.editTextEditFoodBarcode);
        String stringBarcode = editTextEditFoodBarcode.getText().toString();
        String stringBarcodeSQL = db.quoteSmart(stringBarcode);

        /* Category */
        // Sub category
        Spinner spinnerSubCat = (Spinner)getActivity().findViewById(R.id.spinnerEditFoodCategorySub);
        int intSubCategoryIndex = spinnerSubCat.getSelectedItemPosition();
        String stringSpinnerSubCategoryName = spinnerSubCat.getSelectedItem().toString();

        // Find we want to find parent ID from the text
        String stringSpinnerSubCategoryNameSQL = db.quoteSmart(stringSpinnerSubCategoryName);
        String spinnerFields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        Cursor findstringSpinnerSubCategoryID = db.select("categories", spinnerFields, "category_name", stringSpinnerSubCategoryNameSQL);
        String stringSubCategoryID  = findstringSpinnerSubCategoryID.getString(0).toString();
        String stringSubCategoryIDSQL = db.quoteSmart(stringSubCategoryID);


        /* Serving Table */

        // Size
        EditText editTextEditFoodSize = (EditText)getActivity().findViewById(R.id.editTextEditFoodSize);
        String stringSize = editTextEditFoodSize.getText().toString();
        String stringSizeSQL = db.quoteSmart(stringSize);
        double doubleServingSize = 0;
        if(stringSize.equals("")){
            Toast.makeText(getActivity(), "Boyutu doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleServingSize = Double.parseDouble(stringSize);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Öğün boyutu bir sayı olmalı", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }

        // Mesurment
        EditText editTextEditFoodMesurment = (EditText)getActivity().findViewById(R.id.editTextEditFoodMesurment);
        String stringMesurment = editTextEditFoodMesurment.getText().toString();
        String stringMesurmentSQL = db.quoteSmart(stringMesurment);
        if(stringMesurment.equals("")){
            Toast.makeText(getActivity(), "Ölçü birimini giriniz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Number
        EditText editTextEditFoodNumber = (EditText)getActivity().findViewById(R.id.editTextEditFoodNumber);
        String stringNumber = editTextEditFoodNumber.getText().toString();
        String stringNumberSQL = db.quoteSmart(stringNumber);
        if(stringNumber.equals("")){
            Toast.makeText(getActivity(), "Bir sayı giriniz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Word
        EditText editTextEditFoodWord = (EditText)getActivity().findViewById(R.id.editTextEditFoodWord);
        String stringWord = editTextEditFoodWord.getText().toString();
        String stringWordSQL = db.quoteSmart(stringWord);
        if(stringWord.equals("")){
            Toast.makeText(getActivity(), "word.", Toast.LENGTH_SHORT).show();
            error = 1;
        }


        /* Calories table */
        // Energy
        EditText editTextEditFoodEnergyPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodEnergyPerHundred);
        String stringEnergyPerHundred = editTextEditFoodEnergyPerHundred.getText().toString();
        stringEnergyPerHundred = stringEnergyPerHundred.replace(",", ".");
        double doubleEnergyPerHundred = 0;
        if(stringEnergyPerHundred.equals("")){
            Toast.makeText(getActivity(), "Enerjiyi giriniz", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleEnergyPerHundred = Double.parseDouble(stringEnergyPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Enerjiyi bir sayı olmak zorundadır.", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringEnergyPerHundredSQL = db.quoteSmart(stringEnergyPerHundred);

        // Proteins
        EditText editTextEditFoodProteinsPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodProteinsPerHundred);
        String stringProteinsPerHundred = editTextEditFoodProteinsPerHundred.getText().toString();
        stringProteinsPerHundred = stringProteinsPerHundred.replace(",", ".");
        double doubleProteinsPerHundred = 0;
        if(stringProteinsPerHundred.equals("")){
            Toast.makeText(getActivity(), "Lütfen protein değerini giriniz!", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleProteinsPerHundred = Double.parseDouble(stringProteinsPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Protein bir sayı olmak zorundadır.\n" + "Yazdığın: " + stringProteinsPerHundred, Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringProteinsPerHundredSQL = db.quoteSmart(stringProteinsPerHundred);

        // Carbs
        EditText editTextEditFoodCarbsPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodCarbsPerHundred);
        String stringCarbsPerHundred = editTextEditFoodCarbsPerHundred.getText().toString();
        stringCarbsPerHundred = stringCarbsPerHundred.replace(",", ".");
        double doubleCarbsPerHundred = 0;
        if(stringCarbsPerHundred.equals("")){
            Toast.makeText(getActivity(), "Lütfen karbonhidrat değerini giriniz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleCarbsPerHundred = Double.parseDouble(stringCarbsPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "karbonhidrat bir sayı olmak zorundadır.\nYazdığın: " + stringCarbsPerHundred, Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringCarbsPerHundredSQL = db.quoteSmart(stringCarbsPerHundred);

        // Fat
        EditText editTextEditFoodFatPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodFatPerHundred);
        String stringFatPerHundred = editTextEditFoodFatPerHundred.getText().toString();
        stringFatPerHundred = stringFatPerHundred.replace(",", ".");
        double doubleFatPerHundred = 0;
        if(stringFatPerHundred.equals("")){
            Toast.makeText(getActivity(), "Lütfen yağ değerini giriniz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleFatPerHundred = Double.parseDouble(stringFatPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Yağ bir sayı olmak zorundadır.", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }
        String stringFatPerHundredSQL = db.quoteSmart(stringFatPerHundred);



        /* Insert */
        if(error == 0){

            /* Calories table pr meal */
            double energyCalculated = Math.round((doubleEnergyPerHundred*doubleServingSize)/100);
            double proteinsCalculated = Math.round((doubleProteinsPerHundred*doubleServingSize)/100);
            double carbsCalculated = Math.round((doubleCarbsPerHundred*doubleServingSize)/100);
            double fatCalculated = Math.round((doubleFatPerHundred*doubleServingSize)/100);

            String stringEnergyCalculated = "" + energyCalculated;
            String stringProteinsCalculated = "" + proteinsCalculated;
            String stringCarbsCalculated = "" + carbsCalculated;
            String stringfatCalculated = "" + fatCalculated;

            String stringEnergyCalculatedSQL = db.quoteSmart(stringEnergyCalculated);
            String stringProteinsCalculatedSQL = db.quoteSmart(stringProteinsCalculated);
            String stringCarbsCalculatedSQL = db.quoteSmart(stringCarbsCalculated);
            String stringfatCalculatedSQL = db.quoteSmart(stringfatCalculated);


            String fields =
                    "_id, " +
                            "food_name, " +
                            "food_manufactor_name, " +
                            "food_description, " +
                            "food_serving_size_gram, " +
                            "food_serving_size_gram_mesurment, " +
                            "food_serving_size_pcs, " +
                            "food_serving_size_pcs_mesurment, " +
                            "food_energy, " +
                            "food_proteins, " +
                            "food_carbohydrates, " +
                            "food_fat, " +
                            "food_energy_calculated, " +
                            "food_proteins_calculated, " +
                            "food_carbohydrates_calculated, " +
                            "food_fat_calculated, " +
                            "food_barcode, " +
                            "food_category_id";

            String values =
                    "NULL, " +
                            stringNameSQL + ", " +
                            stringManufactorSQL + ", " +
                            stringDescriptionSQL + ", " +
                            stringSizeSQL + ", " +
                            stringMesurmentSQL + ", " +
                            stringNumberSQL + ", " +
                            stringWordSQL + ", " +
                            stringEnergyPerHundredSQL + ", " +
                            stringProteinsPerHundredSQL + ", " +
                            stringCarbsPerHundredSQL + ", " +
                            stringFatPerHundredSQL + ", " +
                            stringEnergyCalculatedSQL + ", " +
                            stringProteinsCalculatedSQL + ", " +
                            stringCarbsCalculatedSQL + ", " +
                            stringfatCalculatedSQL + ", " +
                            stringBarcodeSQL + ", " +
                            stringSubCategoryIDSQL;


            db.insert("food", fields, values);

            // Toast
            Toast.makeText(getActivity(), "Yiyecek Oluşturuldu", Toast.LENGTH_SHORT).show();

            // Move user back to correct design
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new FoodFragment(), FoodFragment.class.getName()).commit();

        } // error == 0


        /* Close db */
        db.close();
    } // buttonAddFoodSubmitOnClick
    /*- Fragment  methods -*/


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
