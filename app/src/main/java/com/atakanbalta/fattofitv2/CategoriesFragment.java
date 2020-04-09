package com.atakanbalta.fattofitv2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
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
import android.widget.Toast;
import java.util.ArrayList;



public class CategoriesFragment extends Fragment {


    /*- Sınıf değişkenleri ---------------------------------------------------------------*/
    private View mainView;
    private Cursor listCursor;


    private MenuItem menuItemEdit;
    private MenuItem menuItemDelete;


    private String currentId;
    private String currentName;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    /* Variables */
    Cursor categoriesCursor;






    public CategoriesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoriesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoriesFragment newInstance(String param1, String param2) {
        CategoriesFragment fragment = new CategoriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /*- On create ----------------------------------------------------------------- */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView =inflater.inflate(R.layout.fragment_categories, container, false);
        return mainView;
    }
    /*- set main view ------------------------------------------------------------------ */
    private void setMainView(int id){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mainView = inflater.inflate(id, null);
        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(mainView);
    }


    /*- on Activity Created --------------------------------------------------------- */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Kategoriyi doldur

        populateList("0","");
        //menu
        setHasOptionsMenu(true);
    } // onActivityCreated


    /*- on Create Options Menu --------------------------------------------------------- */
    // Creating action icon on toolbar
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        ((MainActivity)getActivity()).getMenuInflater().inflate(R.menu.menu_categories,menu);


        menuItemEdit = menu.findItem(R.id.action_edit);
        menuItemDelete = menu.findItem(R.id.action_delete);

        // Hide
        menuItemEdit.setVisible(false);
        menuItemDelete.setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        int id = menuItem.getItemId();

        if (id == R.id.action_add) {
            Toast.makeText(getActivity(),"menu"+menuItem,Toast.LENGTH_LONG).show();
            createNewCategory();
        }
        if (id == R.id.action_edit) {
            editCategory();
        }
        if (id == R.id.action_delete) {
            deleteCategory();
        }
        return super.onOptionsItemSelected(menuItem);
    }







    public void populateList(String parentID,String parentName){
        DBAdapter db = new DBAdapter(getActivity());
        db.open();



        //Kategorileri getir
        String fields[] = new String[]{
                "_id",
                "category_name",
                "category_parent_id"
        };
        categoriesCursor =db.select("categories",fields,"category_parent_id",parentID);

        //Array oluştur
        ArrayList<String> values = new ArrayList<String> ();

        //count
        int categoriesCount =categoriesCursor.getCount();
        for (int x =0;x<categoriesCount;x++){
            values.add(categoriesCursor.getString(categoriesCursor.getColumnIndex("category_name")));
             /*Toast.makeText(getActivity(),
                    "Id: " + categoriesCursor.getString(0) + "\n" +
                            "Name: " + categoriesCursor.getString(1), Toast.LENGTH_SHORT).show(); */
             categoriesCursor.moveToNext();
        }

        //categoriesCursor.close();

        //array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,values);

        //ListView
        ListView lv =(ListView)getActivity().findViewById(R.id.listViewCategories);
        lv.setAdapter(adapter);

        //ONCLICK
        if(parentID.equals("0")) {
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    listItemClicked(arg2);
                }
            });
        }

        //DB CLOSE
        db.close();

        // kaldır ya da göster

        if(parentID.equals("0")){
            //edit butonunu kaldır
        }else{
            //edit butonunu göster

            menuItemEdit.setVisible(true);
            menuItemDelete.setVisible(true);
        }





    } //populateList

    /* List item clicked */
    public void listItemClicked(int listItemIDClicked){


        categoriesCursor.moveToPosition(listItemIDClicked);

        // ID ve name getir cursordan
        String id = categoriesCursor.getString(0);
        String name = categoriesCursor.getString(1);
        String parentID = categoriesCursor.getString(2);

        //subclass
        populateList(id,name);
        // Baslik değistir
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(name);


        currentId = id;
        currentName = name;

    } // listItemClicked

    /*Subclass */
    public void createNewCategory(){
        int id = R.layout.fragment_categories_add_edit;
        setMainView(id);

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        /* Spinnerleri kategori ile doldur */
        String fields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        Cursor dbCursor = db.select("categories", fields, "category_parent_id", "0", "category_name", "ASC");
        // Array olustur
        int dbCursorCount = dbCursor.getCount();
        String[] arraySpinnerCategories = new String[dbCursorCount+1];

        // PARENT

        arraySpinnerCategories[0] = "-";
        // Cursor to string
        for(int x=1;x<dbCursorCount+1;x++){
            arraySpinnerCategories[x] = dbCursor.getString(1).toString();
            dbCursor.moveToNext();
        }
        // Populate spinner
        Spinner spinnerParent = (Spinner) getActivity().findViewById(R.id.spinnerCategoryParent);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinnerCategories);
        spinnerParent.setAdapter(adapter);

        /* SubmitButton listener */
        Button buttonHome = (Button)getActivity().findViewById(R.id.buttonCategoriesSubmit);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewCategorySubmitOnClick();
            }
        });

        db.close();
    }
    // edit kategori
    public void editCategory(){


        /* Change layout */
        int id = R.layout.fragment_categories_add_edit;
        setMainView(id);

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        /* Ask for parent ID */
        Cursor c;
        String fieldsC[] = new String[] { "category_parent_id" };
        String currentIdSQL = db.quoteSmart(currentId);
        c = db.select("categories", fieldsC, "_id", currentIdSQL);
        String currentParentID = c.getString(0);
        int intCurrentParentID = 0;
        try {
            intCurrentParentID = Integer.parseInt(currentParentID);
        }
        catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        // Toast.makeText(getActivity(), "Parent ID: " + parentID, Toast.LENGTH_SHORT).show();

        /* Fill name */
        EditText editTextName = (EditText) getActivity().findViewById(R.id.editTextName);
        editTextName.setText(currentName);


        /* Fill spinner with categories */
        String fields[] = new String[] {
                "_id",
                "category_name",
                "category_parent_id"
        };
        Cursor dbCursor = db.select("categories", fields, "category_parent_id", "0", "category_name", "ASC");

        // Creating array
        int dbCursorCount = dbCursor.getCount();
        String[] arraySpinnerCategories = new String[dbCursorCount+1];

        // This is parent
        arraySpinnerCategories[0] = "-";

        // Convert Cursor to String
        int correctParentID = 0;
        for(int x=1;x<dbCursorCount+1;x++){
            arraySpinnerCategories[x] = dbCursor.getString(1).toString();

            if(dbCursor.getString(0).toString().equals(currentParentID)){
                correctParentID = x;
            }

            // Move to next
            dbCursor.moveToNext();
        }

        // Populate spinner
        Spinner spinnerParent = (Spinner) getActivity().findViewById(R.id.spinnerCategoryParent);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinnerCategories);
        spinnerParent.setAdapter(adapter);


        spinnerParent.setSelection(correctParentID);

        /* Close db */
        db.close();


        /* SubmitButton listener */
        Button buttonHome = (Button)getActivity().findViewById(R.id.buttonCategoriesSubmit);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editCategorySubmitOnClick();
            }
        });
    }
    //editCategorySubmitOnClick-----------------------------------------
    public void editCategorySubmitOnClick(){
        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Error?
        int error = 0;

        // İsim
        EditText editTextName = (EditText)getActivity().findViewById(R.id.editTextName);
        String stringName = editTextName.getText().toString();
        if(stringName.equals("")){
            Toast.makeText(getActivity(), "Lütfen bir isim giriniz", Toast.LENGTH_SHORT).show();
            error = 1;
        }


        // Parent
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.spinnerCategoryParent);
        String stringSpinnerCategoryParent = spinner.getSelectedItem().toString();
        String parentID;
        if(stringSpinnerCategoryParent.equals("-")){
            parentID = "0";
        }
        else{
            // Find we want to find parent ID from the text
            String stringSpinnerCategoryParentSQL = db.quoteSmart(stringSpinnerCategoryParent);
            String fields[] = new String[] {
                    "_id",
                    "category_name",
                    "category_parent_id"
            };
            Cursor findParentID = db.select("categories", fields, "category_name", stringSpinnerCategoryParentSQL);
            parentID = findParentID.getString(0).toString();


        }

        if(error == 0){
            // Güncel ID to long
            long longCurrentID = Long.parseLong(currentId);

            // Degiskenler
            long currentIDSQL = db.quoteSmart(longCurrentID);
            String stringNameSQL = db.quoteSmart(stringName);
            String parentIDSQL = db.quoteSmart(parentID);

            // Insert into database
            String input = "NULL, " + stringNameSQL + ", " + parentIDSQL;
            db.update("categories", "_id", currentIDSQL, "category_name", stringNameSQL);
            db.update("categories", "_id", currentIDSQL, "category_parent_id", parentIDSQL);

            // feedback
            Toast.makeText(getActivity(), "Değişiklik kaydedildi", Toast.LENGTH_LONG).show();

            // Dizayna geri dön
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new CategoriesFragment(), CategoriesFragment.class.getName()).commit();

        }

        /* Close db */
        db.close();
    }




    // delete kategori----------------------------------------------
    public void deleteCategory(){

        /* Layout değiş */
        int id = R.layout.fragment_categories_delete;
        setMainView(id);

        /* SubmitButton listener */
        Button buttonCancel = (Button)getActivity().findViewById(R.id.buttonCategoriesCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategoryCancelOnClick();
            }
        });

        Button buttonConfirm = (Button)getActivity().findViewById(R.id.buttonCategoriesConfirmDelete);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCategoryConfirmOnClick();
            }
        });


    }
    public void deleteCategoryCancelOnClick(){
        // Dizayna geri dön
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new CategoriesFragment(), CategoriesFragment.class.getName()).commit();

    }
    public void deleteCategoryConfirmOnClick(){
        // SQLden sil

        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();

        // Güncel ID to long
        long longCurrentID = Long.parseLong(currentId);

        // Degiskenler
        long currentIDSQL = db.quoteSmart(longCurrentID);

        // Delete
        db.delete("categories", "_id", currentIDSQL);

        // Close db
        db.close();

        //  message
        Toast.makeText(getActivity(), "Kategori başarıyla silindi!", Toast.LENGTH_LONG).show();

        // Dizayna geri dön
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new CategoriesFragment(), CategoriesFragment.class.getName()).commit();




    }





    public void createNewCategorySubmitOnClick() {
        /* Database */
        DBAdapter db = new DBAdapter(getActivity());
        db.open();


        // Error?
        int error = 0;
        // İsim
        EditText editTextName = (EditText)getActivity().findViewById(R.id.editTextName);
        String stringName = editTextName.getText().toString();
        if(stringName.equals("")){
            Toast.makeText(getActivity(), "Please fill in a name.", Toast.LENGTH_SHORT).show();
            error = 1;
        }

        // Parent
        Spinner spinner = (Spinner)getActivity().findViewById(R.id.spinnerCategoryParent);
        String stringSpinnerCategoryParent = spinner.getSelectedItem().toString();
        String parentID;
        if(stringSpinnerCategoryParent.equals("-")){
            parentID = "0";
        }
        else{
            //
            String stringSpinnerCategoryParentSQL = db.quoteSmart(stringSpinnerCategoryParent);
            String fields[] = new String[] {
                    "_id",
                    "category_name",
                    "category_parent_id"
            };
            Cursor findParentID = db.select("categories", fields, "category_name", stringSpinnerCategoryParentSQL);
            parentID = findParentID.getString(0).toString();



        }

        if(error == 0){
            // Ready variables
            String stringNameSQL = db.quoteSmart(stringName);
            String parentIDSQL = db.quoteSmart(parentID);

            // Insert into database
            String input = "NULL, " + stringNameSQL + ", " + parentIDSQL;
            db.insert("categories", "_id, category_name, category_parent_id", input);

            //  feedback
            Toast.makeText(getActivity(), "Category created", Toast.LENGTH_LONG).show();

            // Dizayna geri dön
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.flContent, new CategoriesFragment(), CategoriesFragment.class.getName()).commit();

        }
        db.close();




    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
