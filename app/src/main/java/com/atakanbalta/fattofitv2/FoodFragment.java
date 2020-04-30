package com.atakanbalta.fattofitv2;

import android.content.Context;
import android.database.Cursor;
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
    // TODO: Rename parameter arguments, choose names that match

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    /*- 01 Sınıf değişkenleri -------------------------------------------------------------- */

    private View mainView;
    private Cursor listCursor;

    // action button toolbarr
    private MenuItem menuItemEdit;
    private MenuItem menuItemDelete;

    // Holder  buttons  toolbar
    private String currentId;
    private String currentName;

    public FoodFragment() {
        // boş constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FoodFragment newInstance(String param1, String param2) {
        FoodFragment fragment = new FoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*- 05 on Activity Created ---------------------------------------------------------- */
    // Metodları çalıştırır
    // Set toolbar menu items
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* Başlık */
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Yiyecekler");

        // Kategori listesini doldur
        populateListFood();

        // Menuyu yarat
        setHasOptionsMenu(true);
    } // onActivityCreated


    /*- 06 On create view ---------------------------------------------------------------- */
    // Sets main View variable to the view, so we can change views in fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_food, container, false);
        return mainView;
    }

    /*- 07 set main view ----------------------------------------------------------------- */
    // Fragmentte view değiştir
    private void setMainView(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }

    /*- 08 on Create Options Menu -------------------------------------------------------- */

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {



        ((MainActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_food, menu);


        menuItemEdit = menu.findItem(R.id.menu_action_food_edit);
        menuItemDelete = menu.findItem(R.id.menu_action_food_delete);

        // Hide
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


    /*- populate List -------------------------------------------------------------- */
    public void populateListFood(){

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Kategorileri getir
        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_manufactor_name",
                "food_description",
                "food_serving_size",
                "food_serving_mesurment",
                "food_serving_name_number",
                "food_serving_name_word",
                "food_energy_calculated"
        };
        listCursor = db.select("food", fields, "", "", "food_name", "ASC");


        // Listviewi bul(doldrumak icin)
        ListView lvItems = (ListView)getActivity().findViewById(R.id.listViewFood);



        // Setup cursor adapter using cursor
        FoodCursorAdapter continentsAdapter = new FoodCursorAdapter(getActivity(), listCursor);

        // Cursoru listViewe bağla
        lvItems.setAdapter(continentsAdapter); // uses ContinensCursorAdapter


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

        // Edit buttonu göster
        menuItemEdit.setVisible(true);
        menuItemDelete.setVisible(true);

        // Move cursor to ID clicked
        listCursor.moveToPosition(listItemIDClicked);

        // ID ve name getir cursordan

        currentId = listCursor.getString(0);
        currentName = listCursor.getString(1);

        // Baslık degis
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(currentName);




        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();


        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_manufactor_name",
                "food_description",
                "food_serving_size",
                "food_serving_mesurment",
                "food_serving_name_number",
                "food_serving_name_word",
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



        // Calculation line
        TextView textViewViewFoodAbout = (TextView) getView().findViewById(R.id.textViewViewFoodAbout);
        String foodAbout = stringServingSize + " " + stringServingMesurment + " = " +
                stringServingNameNumber  + " " + stringServingNameWord + ".";
        textViewViewFoodAbout.setText(foodAbout);

        // ACIKLAMA
        TextView textViewViewFoodDescription = (TextView) getView().findViewById(R.id.textViewViewFoodDescription);
        textViewViewFoodDescription.setText(stringDescription);

        // KALORI TABLOSU
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

        /* LAYOUTU DEGİS */
        int id = R.layout.fragment_food_edit;
        setMainView(id);


        // ID ve name getir cursordan

        currentId = listCursor.getString(0);
        currentName = listCursor.getString(1);

        // Change title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Edit " + currentName);


        // Database
        DBAdapter db = new DBAdapter(getActivity());
        db.open();


        String fields[] = new String[] {
                "_id",
                "food_name",
                "food_manufactor_name",
                "food_description",
                "food_serving_size",
                "food_serving_mesurment",
                "food_serving_name_number",
                "food_serving_name_word",
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




        // İsim
        EditText editTextEditFoodName = (EditText) getView().findViewById(R.id.editTextEditFoodName);
        editTextEditFoodName.setText(stringName);

        // Üretici
        TextView editTextEditFoodManufactor = (TextView) getView().findViewById(R.id.editTextEditFoodManufactor);
        editTextEditFoodManufactor.setText(stringManufactorName);

        // Açıklama
        EditText editTextEditFoodDescription = (EditText) getView().findViewById(R.id.editTextEditFoodDescription);
        editTextEditFoodDescription.setText(stringDescription);

        // Barcode
        EditText editTextEditFoodBarcode = (EditText) getView().findViewById(R.id.editTextEditFoodBarcode);
        editTextEditFoodBarcode.setText(stringBarcode);


        String spinnerFields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        // Find the category that the food is using (has to be a sub category)
        Cursor dbCursorCurrentFoodCategory = db.select("categories", spinnerFields, "_id", stringCategoryId, "category_name", "ASC");

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
        for(int x=0;x<dbCursorCount;x++){
            arraySpinnerCategoriesSub[x] = dbCursorSub.getString(1).toString();

            if(dbCursorSub.getString(0).toString().equals(stringCategoryId)){
                selectedSubCategoryIndex = x;
                selectedSubCategoryParentId = dbCursorSub.getString(2).toString();
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

        /* Ana kategori */
        Cursor dbCursorMain = db.select("categories", spinnerFields, "category_parent_id", "0", "category_name", "ASC");

        // Creating array
        dbCursorCount = dbCursorMain.getCount();
        String[] arraySpinnerMainCategories = new String[dbCursorCount];

        // Doğru kategoriyi seçmek için
        int selectedMainCategoryIndex = 0;

        // Convert Cursor to String
        for(int x=0;x<dbCursorCount;x++){
            arraySpinnerMainCategories[x] = dbCursorMain.getString(1).toString();


            if(dbCursorMain.getString(0).toString().equals(selectedSubCategoryParentId)){
                selectedMainCategoryIndex = x;
                selectedMainCategoryName = dbCursorMain.getString(1).toString();
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
        //Toast.makeText(getActivity(), "Parent ID: " + selectedSubCategoryParentId, Toast.LENGTH_SHORT).show();


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
                // Toast.makeText(getActivity(), "Category changed to " + selectedItem, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Ana kategori değişti: " + selectedItemCategoryName, Toast.LENGTH_SHORT).show();


            /* Database */
            DBAdapter db = new DBAdapter(getActivity());
            db.open();

            // ID BUL main için
            String selectedItemCategoryNameSQL = db.quoteSmart(selectedItemCategoryName);
            String spinnerFields[] = new String[] {
                    "_id",
                    "category_name",
                    "category_parent_id"
            };
            Cursor findMainCategoryID = db.select("categories", spinnerFields, "category_name", selectedItemCategoryNameSQL);
            String stringMainCategoryID  = findMainCategoryID.getString(0).toString();
            String stringMainCategoryIDSQL = db.quoteSmart(stringMainCategoryID);


            /* ALT KATEGORİLER */
            Cursor dbCursorSub = db.select("categories", spinnerFields, "category_parent_id", stringMainCategoryIDSQL, "category_name", "ASC");

            // ARRAY OLUSTUR
            int dbCursorCount = dbCursorSub.getCount();
            String[] arraySpinnerCategoriesSub = new String[dbCursorCount];


            //cursor to string
            for(int x=0;x<dbCursorCount;x++){
                arraySpinnerCategoriesSub[x] = dbCursorSub.getString(1).toString();
                dbCursorSub.moveToNext();
            }

            // SPINNER DOLDUR
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




        // İSİM
        EditText editTextEditFoodName = (EditText)getActivity().findViewById(R.id.editTextEditFoodName);
        String stringName = editTextEditFoodName.getText().toString();
        String stringNameSQL = db.quoteSmart(stringName);
        if(stringName.equals("")){
            Toast.makeText(getActivity(), "İsim kısmını doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Üretici
        EditText editTextEditFoodManufactor = (EditText)getActivity().findViewById(R.id.editTextEditFoodManufactor);
        String stringManufactor = editTextEditFoodManufactor.getText().toString();
        String stringManufactorSQL = db.quoteSmart(stringManufactor);
        if(stringManufactor.equals("")){
            Toast.makeText(getActivity(), "Üretici kısmını doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Açıklama
        EditText editTextEditFoodDescription = (EditText)getActivity().findViewById(R.id.editTextEditFoodDescription);
        String stringDescription = editTextEditFoodDescription.getText().toString();
        String stringDescriptionSQL = db.quoteSmart(stringDescription);

        // Barcode
        EditText editTextEditFoodBarcode = (EditText)getActivity().findViewById(R.id.editTextEditFoodBarcode);
        String stringBarcode = editTextEditFoodBarcode.getText().toString();
        String stringBarcodeSQL = db.quoteSmart(stringBarcode);

        /* Kategori */
        // Alt Kategori
        Spinner spinnerSubCat = (Spinner)getActivity().findViewById(R.id.spinnerEditFoodCategorySub);
        int intSubCategoryIndex = spinnerSubCat.getSelectedItemPosition();
        String stringSpinnerSubCategoryName = spinnerSubCat.getSelectedItem().toString();

        // Textten parent bulmak
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
            Toast.makeText(getActivity(), "Porsiyon kısmını doldurunuz", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleServingSize = Double.parseDouble(stringSize);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Porsiyon bir sayı değildir.", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }

        // Ölçü
        EditText editTextEditFoodMesurment = (EditText)getActivity().findViewById(R.id.editTextEditFoodMesurment);
        String stringMesurment = editTextEditFoodMesurment.getText().toString();
        String stringMesurmentSQL = db.quoteSmart(stringMesurment);
        if(stringMesurment.equals("")){
            Toast.makeText(getActivity(), "Ölçü kısmını doldurunuz", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Numara
        EditText editTextEditFoodNumber = (EditText)getActivity().findViewById(R.id.editTextEditFoodNumber);
        String stringNumber = editTextEditFoodNumber.getText().toString();
        String stringNumberSQL = db.quoteSmart(stringNumber);
        if(stringNumber.equals("")){
            Toast.makeText(getActivity(), "Please fill in number.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Word
        EditText editTextEditFoodWord = (EditText)getActivity().findViewById(R.id.editTextEditFoodWord);
        String stringWord = editTextEditFoodWord.getText().toString();
        String stringWordSQL = db.quoteSmart(stringWord);
        if(stringWord.equals("")){
            Toast.makeText(getActivity(), "Please fill in word.", Toast.LENGTH_SHORT).show();
            error = 1;
        }


        /* Kalori tablosu */
        // Energy
        EditText editTextEditFoodEnergyPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodEnergyPerHundred);
        String stringEnergyPerHundred = editTextEditFoodEnergyPerHundred.getText().toString();
        stringEnergyPerHundred = stringEnergyPerHundred.replace(",", ".");
        double doubleEnergyPerHundred = 0;
        if(stringEnergyPerHundred.equals("")){
            Toast.makeText(getActivity(), "Enerji kısmını doldurunuz", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleEnergyPerHundred = Double.parseDouble(stringEnergyPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Energy is not a number.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Protein kısmını doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleProteinsPerHundred = Double.parseDouble(stringProteinsPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Protein is not a number.\n" + "You wrote: " + stringProteinsPerHundred, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Karbonhidrat kısmını doldurunuz..", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleCarbsPerHundred = Double.parseDouble(stringCarbsPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Carbs is not a number.\nYou wrote: " + stringCarbsPerHundred, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Yağ kısmını doldurunuz.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleFatPerHundred = Double.parseDouble(stringFatPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Yağ bir sayı değildir", Toast.LENGTH_SHORT).show();
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
                    "food_serving_size",
                    "food_serving_mesurment",
                    "food_serving_name_number",
                    "food_serving_name_word",
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
            Toast.makeText(getActivity(), "Değişiklik kaydedildi", Toast.LENGTH_SHORT).show();

        } // error == 0

        db.close();
    } // buttonEditFoodSubmitOnClick
    // DELETE FOOD-------------------------------------------------------------------------
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
    /*- Delete food cancel ----------------------------------------------------------------- */
    public void deleteFoodCancel(){
        // Move user back to correct design
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new FoodFragment(), FoodFragment.class.getName()).commit();


    }

    public void deleteFoodConfirmDelete(){
        DBAdapter db= new DBAdapter(getActivity());
        db.open();

        // Güncel idyi longa cevir
        long longCurrentID=Long.parseLong(currentId);
        //degisken
        long currentIDSQL = db.quoteSmart(longCurrentID);
        //delete
        db.delete("food","_id",currentIDSQL);

        //db kapat
        db.close();

        //Mesaj ver
        Toast.makeText(getActivity(),"Yiyecek başarıyla silindi",Toast.LENGTH_LONG).show();

        //Kullanıcıyı eski fragmente döndür
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
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Add food");


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
                // Toast.makeText(getActivity(), "Category changed to " + selectedItem, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please fill in a name.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Manufactor
        EditText editTextEditFoodManufactor = (EditText)getActivity().findViewById(R.id.editTextEditFoodManufactor);
        String stringManufactor = editTextEditFoodManufactor.getText().toString();
        String stringManufactorSQL = db.quoteSmart(stringManufactor);
        if(stringManufactor.equals("")){
            Toast.makeText(getActivity(), "Please fill in a manufactor.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please fill in a size.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleServingSize = Double.parseDouble(stringSize);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Serving size is not number.", Toast.LENGTH_SHORT).show();
                error = 1;
            }
        }

        // Mesurment
        EditText editTextEditFoodMesurment = (EditText)getActivity().findViewById(R.id.editTextEditFoodMesurment);
        String stringMesurment = editTextEditFoodMesurment.getText().toString();
        String stringMesurmentSQL = db.quoteSmart(stringMesurment);
        if(stringMesurment.equals("")){
            Toast.makeText(getActivity(), "Please fill in mesurment.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Number
        EditText editTextEditFoodNumber = (EditText)getActivity().findViewById(R.id.editTextEditFoodNumber);
        String stringNumber = editTextEditFoodNumber.getText().toString();
        String stringNumberSQL = db.quoteSmart(stringNumber);
        if(stringNumber.equals("")){
            Toast.makeText(getActivity(), "Please fill in number.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Word
        EditText editTextEditFoodWord = (EditText)getActivity().findViewById(R.id.editTextEditFoodWord);
        String stringWord = editTextEditFoodWord.getText().toString();
        String stringWordSQL = db.quoteSmart(stringWord);
        if(stringWord.equals("")){
            Toast.makeText(getActivity(), "Please fill in word.", Toast.LENGTH_SHORT).show();
            error = 1;
        }


        /* Calories table */
        // Energy
        EditText editTextEditFoodEnergyPerHundred = (EditText)getActivity().findViewById(R.id.editTextEditFoodEnergyPerHundred);
        String stringEnergyPerHundred = editTextEditFoodEnergyPerHundred.getText().toString();
        stringEnergyPerHundred = stringEnergyPerHundred.replace(",", ".");
        double doubleEnergyPerHundred = 0;
        if(stringEnergyPerHundred.equals("")){
            Toast.makeText(getActivity(), "Please fill in energy.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleEnergyPerHundred = Double.parseDouble(stringEnergyPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Energy is not a number.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please fill in proteins.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleProteinsPerHundred = Double.parseDouble(stringProteinsPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Protein is not a number.\n" + "You wrote: " + stringProteinsPerHundred, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please fill in carbs.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleCarbsPerHundred = Double.parseDouble(stringCarbsPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Carbs is not a number.\nYou wrote: " + stringCarbsPerHundred, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getActivity(), "Please fill in fat.", Toast.LENGTH_SHORT).show();
            error = 1;
        }
        else{
            try {
                doubleFatPerHundred = Double.parseDouble(stringFatPerHundred);
            }
            catch(NumberFormatException nfe) {
                Toast.makeText(getActivity(), "Carbs is not a number.", Toast.LENGTH_SHORT).show();
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
                            "food_serving_size, " +
                            "food_serving_mesurment, " +
                            "food_serving_name_number, " +
                            "food_serving_name_word, " +
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
            Toast.makeText(getActivity(), "Yiyecek başarıyla oluşturuldu", Toast.LENGTH_SHORT).show();

            // Move user back to correct design
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new FoodFragment(), FoodFragment.class.getName()).commit();

        } // error == 0


        /* Close db */
        db.close();
    } // buttonAddFoodSubmitOnClick




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

}
