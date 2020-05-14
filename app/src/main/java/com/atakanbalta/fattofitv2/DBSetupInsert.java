package com.atakanbalta.fattofitv2;

import android.content.Context;
import android.database.sqlite.SQLiteException;

public class DBSetupInsert {



    /* Variables */
    private final Context context;

    /* Public Class ------------------------------------------------------ */
    public DBSetupInsert(Context ctx){
        this.context = ctx;
    }



    /* Setup Insert To Categories ----------------------------------------- */
    // To insert to category table
    public void setupInsertToCategories(String values){
        try{
            DBAdapter db = new DBAdapter(context);
            db.open();
            db.insert("categories",
                    "_id, category_name, category_parent_id, category_icon, category_note",
                    values);
            db.close();
        }
        catch (SQLiteException e){
            // Toast.makeText(context, "Error; Could not insert categories.", Toast.LENGTH_SHORT).show();
        }
    }
    /* Setup Insert To Food ----------------------------------------------- */
    // To insert food to food table
    public void setupInsertToFood(String values){

        try {
            DBAdapter db = new DBAdapter(context);
            db.open();
            db.insert("food",
                    "_id, food_name, food_manufactor_name, food_serving_size_gram, food_serving_size_gram_mesurment, food_serving_size_pcs, food_serving_size_pcs_mesurment, food_energy, food_proteins, food_carbohydrates, food_fat, food_energy_calculated, food_proteins_calculated, food_carbohydrates_calculated, food_fat_calculated, food_user_id, food_barcode, food_category_id, food_thumb, food_image_a, food_image_b, food_image_c, food_notes",
                    values);
            db.close();
        }
        catch (SQLiteException e){
        }

    }
    public void insertAllCategories(){
        setupInsertToCategories("NULL, 'Ekmek', '0', '', NULL");
        setupInsertToCategories("NULL, 'Ekmek', '1', '', NULL");
        setupInsertToCategories("NULL, 'Gevrekler', '1', '', NULL");
        setupInsertToCategories("NULL, 'Dondurulmuş ekmek', '1', '', NULL");
        setupInsertToCategories("NULL, 'GevrekEkmek', '1', '', NULL");
        // Parent id: 6
        setupInsertToCategories("NULL, 'Tatlılar ve Unlu Yiyecekler', '0', '', NULL");
        setupInsertToCategories("NULL, 'Unlu Yiyecekler', '6', '', NULL");
        setupInsertToCategories("NULL, 'Bisküviler', '6', '', NULL");


        setupInsertToCategories("NULL, 'İçecekler', '0', '', NULL");
        setupInsertToCategories("NULL, 'Soda', '9', '', NULL");


        setupInsertToCategories("NULL, 'Meyve ve sebzeleer', '0', '', NULL");
        setupInsertToCategories("NULL, 'Dondurulmuş meyve ve sebzeleer', '11', '', NULL");
        setupInsertToCategories("NULL, 'Meyveler', '11', '', NULL");
        setupInsertToCategories("NULL, 'Sebzeler', '11', '', NULL");
        setupInsertToCategories("NULL, 'Kavanozlanmış meyve ve sebzeler', '11', '', NULL");


        setupInsertToCategories("NULL, 'Sağlık', '0', '', NULL");
        setupInsertToCategories("NULL, 'Atıştırmalık', '16', '', NULL");
        setupInsertToCategories("NULL, 'Protein barları', '16', '', NULL");
        setupInsertToCategories("NULL, 'Protein tozu', '16', '', NULL");


        setupInsertToCategories("NULL, 'Et,balık,tavuk', '0', '', NULL");
        setupInsertToCategories("NULL, 'Et', '20', '', NULL");
        setupInsertToCategories("NULL, 'Tavuk', '20', '', NULL");
        setupInsertToCategories("NULL, 'Balık', '20', '', NULL");


        setupInsertToCategories("NULL, 'Yumurta ve günlük', '0', '', NULL");
        setupInsertToCategories("NULL, 'Yumurta', '24', '', NULL");
        setupInsertToCategories("NULL, 'Krema', '24', '', NULL");
        setupInsertToCategories("NULL, 'Yogurt', '24', '', NULL");


        setupInsertToCategories("NULL, 'Akşam Yemeği', '0', '', NULL");
        setupInsertToCategories("NULL, 'Hazır yemekler', '28', '', NULL");
        setupInsertToCategories("NULL, 'Pizza', '28', '', NULL");
        setupInsertToCategories("NULL, 'Noodle', '28', '', NULL");
        setupInsertToCategories("NULL, 'Makarna', '28', '', NULL");
        setupInsertToCategories("NULL, 'Pirinç', '28', '', NULL");
        setupInsertToCategories("NULL, 'Taco', '28', '', NULL");


        setupInsertToCategories("NULL, 'Peynir', '0', '', NULL");
        setupInsertToCategories("NULL, 'Krem Peynir', '35', '', NULL");


        setupInsertToCategories("NULL, 'Ekmek üstüne', '0', '', NULL");
        setupInsertToCategories("NULL, 'Soğuk etler', '37', '', NULL");
        setupInsertToCategories("NULL, 'Tatlı sürülebilirler', '37', '', NULL");
        setupInsertToCategories("NULL, 'Reçel', '37', '', NULL");


        setupInsertToCategories("NULL, 'Atıştırmalık', '0', '', NULL");
        setupInsertToCategories("NULL, 'Fıstık', '41', '', NULL");
        setupInsertToCategories("NULL, 'Patetes Cipsi', '41', '', NULL");





    }










    /*INSERT FOOD TO DB-------------------- */
    public void insertAllFood(){
        setupInsertToFood("NULL, 'Yulaf', 'Lifalif', '60', 'gram', '60', 'g', '389', '11.4', '63.1', '7.8', '233', '7', '38', '5', NULL, NULL, '3', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Enerji İçeceği 50cl', 'RedBull', '500', 'gram', '1', 'g', '50', '0.4', '11.5', '0', '250', '2', '58', '0', NULL, NULL, '10', 'NULL', 'NULL', 'NULLg', 'NULL', NULL");
        setupInsertToFood("NULL, 'Brokoli', 'Brokoli', '300', 'gram', '0.5', 'kg', '33', '2.8', '7', '0.4', '99', '8', '21', '1', NULL, NULL, '14', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Pişmiş fasulye', 'Duru bulgur', '1000', 'gram', '1', 'kg', '116', '5', '19', '0.5', '487', '21', '80', '2', NULL, NULL, '15', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Protein Ögünü ', 'Strongr', '70', 'gram', '1', 'g', '382', '43.3', '43.2', '4.8', '267', '30', '30', '3', NULL, NULL, '17', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, '100% Whey Gold Protein tozu', 'Hard-line', '30', 'g', '1', 'g', '363', '78.9', '7.8', '2.5', '109', '24', '2', '1', NULL, NULL, '19', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, '100% Whey Gold Standard Protein tozu', 'Hard-line', '30', 'gram', '1', 'g', '375', '82.5', '4.5', '2.9', '113', '25', '1', '1', NULL, NULL, '19', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Whey 80 Soğuk kahve', 'Hard-line', '30', 'gram', '1', 'ml', '396', '76', '4', '8', '119', '23', '1', '2', NULL, NULL, '19', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Çilek Reçeli', 'Seyidoglu', '30', 'gram', '1', 'g', '193', '0.4', '46', '0.2', '58', '0', '14', '0', NULL, NULL, '40', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Kuruyemiş', 'Tadım', '600', 'gram', '1', 'g', '512', '16.1', '37.1', '32.3', '3 072', '97', '223', '194', NULL, NULL, '42', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Fıstık ezmesi', 'FitNut', '60', 'gram', '1', 'g', '386', '29.4', '24.8', '19', '232', '18', '15', '11', NULL, NULL, '18', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Nutella', 'Nutella', '100', 'gram', '1', 'g', '521', '49.4', '34.8', '19', '232', '18', '15', '11', NULL, NULL, '18', 'atkins_chocolate_peanut_thumb.jpg', 'atkins_chocolate_peanut_a.jpg', 'atkins_chocolate_peanut_b.jpg', 'atkins_chocolate_peanut_c.jpg', NULL");
        setupInsertToFood("NULL, 'Elma', 'Migros', '90', 'gram', '90', 'g', '47', '0.23', '12.43', '0.15', '35', '0.10', '10', '0', NULL, NULL, '13', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, Armut'Migros', '100', 'gram', '100', 'g', '69', '0.72', '15.36', '0.10', '35', '0.10', '10', '0', NULL, NULL, '13', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, Kivi'Migros', '100', 'gram', '90', 'g', '47', '0.23', '12.43', '0.15', '35', '0.10', '10', '0', NULL, NULL, '13', 'NULL', 'NULL', 'NULL', 'NULL', NULL");
        setupInsertToFood("NULL, 'Portakal', 'Migros', '100', 'gram', '90', 'g', '47', '0.23', '12.43', '0.15', '35', '0.10', '10', '0', NULL, NULL, '13', 'NULL', 'NULL', 'NULL', 'NULL', NULL");




    }





} //dbsetupınsert
