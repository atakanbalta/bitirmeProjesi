package com.atakanbalta.fattofitv2;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;



public class CategoriesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;




    /* Variables */
    Cursor categoriesCursor;
    Cursor listCursor;





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


    /*- on Activity Created --------------------------------------------------------- */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Populate the list of categories

        populateList("0","");
    } // onActivityCreated
    public void populateList(String parentID,String parentName){
        DBAdapter db = new DBAdapter(getActivity());
        db.open();



        //get categories
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

        //array adaper
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

    } //populateList

    /* List item clicked */
    public void listItemClicked(int listItemIDClicked){

        // Move cursor to ID clicked
        categoriesCursor.moveToPosition(listItemIDClicked);

        // Get ID and name from cursor
        String id = categoriesCursor.getString(0);
        String name = categoriesCursor.getString(1);
        String parentID = categoriesCursor.getString(2);

        //subclass
        populateList(id,name);
        // Change title
        ((MainActivity)getActivity()).getSupportActionBar().setTitle(name);



    } // listItemClicked

    /*Subclass */








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_categories, container, false);


    }

    // TODO: Rename method, update argument and hook method into UI event


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
