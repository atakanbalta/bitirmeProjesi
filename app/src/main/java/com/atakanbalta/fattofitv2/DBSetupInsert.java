package com.atakanbalta.fattofitv2;

import android.content.Context;

public class DBSetupInsert {


    private final Context context;


    /*PUBLIC CLASSS----------------- */


    public DBSetupInsert(Context ctx){
        this.context =ctx;
    }



    /* SETUP FOOD------------------ */
    public void setupInsertToFood(String values){

        DBAdapter db = new DBAdapter(context);
        db.open();
        db.insert("food",
                ("food_id,food_name,food_manufactor_name,food_serving_size,food_serving_mesurment,food_serving_name_number,food_serving_name_word,food_energy,food_proteins,food_carbohydrates,food_fat,food_energy_calculated,food_proteins_calculated,food_carbohydrates_calculated,food_fat_calculated,food_user_id,food_barcode,food_category_id,food_thumb,food_image_a,food_image_b,food_image_c,food_notes"),
                values);

    }
    /* SETUP CATEGORYS-------------- */
    public void setupInsertToCategories(String values) {
        DBAdapter db = new DBAdapter(context);
        db.open();
        db.insert("categories",
                "category_id, category_name, category_parent_id, category_icon, category_note",
                values);
        db.close();
    }
    public void insertAllCategories(){
        setupInsertToCategories("NULL, 'Ekmek', '0', '', NULL");
        setupInsertToCategories("NULL, 'Ekmek', '1', '', NULL");
        setupInsertToCategories("NULL, 'Gevrekler', '1', '', NULL");
        setupInsertToCategories("NULL, 'Dondurulmuş ekmek', '1', '', NULL");
        setupInsertToCategories("NULL, 'GevrekEkmek', '1', '', NULL");
        // Parent id: 6
        setupInsertToCategories("NULL, 'Dessert and baking', '0', '', NULL");
        setupInsertToCategories("NULL, 'Baking', '6', '', NULL");
        setupInsertToCategories("NULL, 'Bisküviler', '6', '', NULL");


        setupInsertToCategories("NULL, 'İçecekler', '0', '', NULL");
        setupInsertToCategories("NULL, 'Soda', '9', '', NULL");


        setupInsertToCategories("NULL, 'Meyve ve sebzeleer', '0', '', NULL");
        setupInsertToCategories("NULL, 'Dondurulmuş meyve ve sebzeleer', '11', '', NULL");
        setupInsertToCategories("NULL, 'Meyveler', '11', '', NULL");
        setupInsertToCategories("NULL, 'sebzeleer', '11', '', NULL");
        setupInsertToCategories("NULL, 'Kavanozlanmış meyve ve sebzeler', '11', '', NULL");


        setupInsertToCategories("NULL, 'Sağlık', '0', '', NULL");
        setupInsertToCategories("NULL, 'Atıştırmalık', '16', '', NULL");
        setupInsertToCategories("NULL, 'Protein bars', '16', '', NULL");
        setupInsertToCategories("NULL, 'Protein tozu', '16', '', NULL");


        setupInsertToCategories("NULL, 'Et,balık,tavuk', '0', '', NULL");
        setupInsertToCategories("NULL, 'Et', '20', '', NULL");
        setupInsertToCategories("NULL, 'Tavuk', '20', '', NULL");
        setupInsertToCategories("NULL, 'Balık', '20', '', NULL");


        setupInsertToCategories("NULL, 'Yumurta ve günlük', '0', '', NULL");
        setupInsertToCategories("NULL, 'Yumurta', '24', '', NULL");
        setupInsertToCategories("NULL, 'Cream and sour cream', '24', '', NULL");
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
        setupInsertToFood("NULL, 'Yulaf', 'Lifalif', '60', 'gram', '60', 'g', '389', '11.4', '63.1', '7.8', '233', '7', '38', '5', NULL, NULL, '3', 'axa_havregryn_lettkokt_thumb.jpg', 'axa_havregryn_lettkokt_a.jpg', 'axa_havregryn_lettkokt_b.jpg', 'axa_havregryn_lettkokt_c.jpg', NULL");
        setupInsertToFood("NULL, 'Enerji İçeceği 50cl', 'RedBull', '500', 'gram', '1', 'boks', '50', '0.4', '11.5', '0', '250', '2', '58', '0', NULL, NULL, '10', 'ringnes_battery_energy_drink_50cl_thumb.jpg', 'ringnes_battery_energy_drink_50cl_a.jpg', 'ringnes_battery_energy_drink_50cl_b.jpg', 'ringnes_battery_energy_drink_50cl_c.jpg', NULL");
        setupInsertToFood("NULL, 'Brokoli', 'Bama', '300', 'gram', '0.5', 'stk', '33', '2.8', '7', '0.4', '99', '8', '21', '1', NULL, NULL, '14', 'bama_brokkoli_thumb.jpg', 'bama_brokkoli_a.jpg', 'bama_brokkoli_b.jpg', 'bama_brokkoli_c.jpg', NULL");
        setupInsertToFood("NULL, 'Pişmiş fasulye', 'Coop', '420', 'gram', '1', 'boks', '116', '5', '19', '0.5', '487', '21', '80', '2', NULL, NULL, '15', 'coop_baked_beans_thumb.jpg', 'coop_baked_beans_a.jpg', 'coop_baked_beans_b.jpg', 'coop_baked_beans_c.jpg', NULL");
        setupInsertToFood("NULL, 'Protein Ögünü ', 'Strongr', '70', 'gram', '1', 'skje', '382', '43.3', '43.2', '4.8', '267', '30', '30', '3', NULL, NULL, '17', 'strongr_pure_protein_meal_sjokoladesmak_thumb.jpg', 'strongr_pure_protein_meal_sjokoladesmak_a.jpg', 'strongr_pure_protein_meal_sjokoladesmak_b.jpg', 'strongr_pure_protein_meal_sjokoladesmak_c.jpg', NULL");
        setupInsertToFood("NULL, '100% Whey Gold Standard Cookies and Cream', 'Gymgrossisten', '30', 'gram', '1', 'skje', '363', '78.9', '7.8', '2.5', '109', '24', '2', '1', NULL, NULL, '19', 'gymgrossisten_100_whey_gold_standard_cookies_and_cream_thumb.jpg', 'gymgrossisten_100_whey_gold_standard_cookies_and_cream_a.jpg', 'gymgrossisten_100_whey_gold_standard_cookies_and_cream_b.jpg', 'gymgrossisten_100_whey_gold_standard_cookies_cnd_cream_c.jpg', NULL");
        setupInsertToFood("NULL, '100% Whey Gold Standard Delicious Strawberry', 'Gymgrossisten', '30', 'gram', '1', 'skje', '375', '82.5', '4.5', '2.9', '113', '25', '1', '1', NULL, NULL, '19', 'gymgrossisten_100_whey_gold_standard_delicious_strawberry_thumb.jpg', 'gymgrossisten_100_whey_gold_standard_delicious_strawberry_a.jpg', 'gymgrossisten_100_whey_gold_standard_delicious_strawberry_b.jpg', 'gymgrossisten_100_whey_gold_standard_delicious_strawberry_c.jpg', NULL");
        setupInsertToFood("NULL, 'Whey 80 Ice Coffee', 'Gymgrossisten', '30', 'gram', '1', 'skje', '396', '76', '4', '8', '119', '23', '1', '2', NULL, NULL, '19', 'gymgrossisten_whey_80_ice_coffee_thumb.jpg', 'gymgrossisten_whey_80_ice_coffee_a.jpg', 'gymgrossisten_whey_80_ice_coffee_b.jpg', 'gymgrossisten_whey_80_ice_coffee_c.jpg', NULL");
        setupInsertToFood("NULL, 'Egg', 'Flemming', '63', 'gram', '1', 'stk', '142', '12.4', '0.3', '10.1', '89', '8', '0', '6', NULL, NULL, '25', 'flemming_egg_thumb.jpg', 'flemming_egg_a.jpg', 'flemming_egg_b.jpg', 'flemming_egg_c.jpg', NULL");
        setupInsertToFood("NULL, 'Taco Sos ', 'Coop', '15', 'gram', '1', 'ts', '34', '1.2', '6.5', '0.2', '5', '0', '1', '0', NULL, NULL, '34', 'co-op_tex_mex_taco_sauce_thumb.jpg', 'co-op_tex_mex_taco_sauce_a.jpg', 'co-op_tex_mex_taco_sauce_b.jpg', 'co-op_tex_mex_taco_sauce_c.jpg', NULL");
        setupInsertToFood("NULL, 'Çilek Reçeli', 'Seyidoglu', '30', 'gram', '1', 'spiseskje', '193', '0.4', '46', '0.2', '58', '0', '14', '0', NULL, NULL, '40', 'first_price_bringaebaersyltetooy_thumb.jpg', 'first_price_bringaebaersyltetooy_a.jpg', 'first_price_bringaebaersyltetooy_b.jpg', 'first_price_bringaebaersyltetooy_c.jpg', NULL");
        setupInsertToFood("NULL, 'Utvalde kirsebær', 'Lerums', '30', 'gram', '1', 'spiseskje', '159', '0.3', '39', '0', '48', '0', '12', '0', NULL, NULL, '40', 'lerums_utvalde_kirsebear_thumb.jpg', 'lerums_utvalde_kirsebear_a.jpg', 'lerums_utvalde_kirsebear_b.jpg', 'lerums_utvalde_kirsebear_c.jpg', NULL");
        setupInsertToFood("NULL, 'Kuruyemiş', 'Tadım', '600', 'gram', '1', 'pakke', '512', '16.1', '37.1', '32.3', '3 072', '97', '223', '194', NULL, NULL, '42', 'first_price_noottemiks_thumb.jpg', 'first_price_noottemiks_a.jpg', 'first_price_noottemiks_b.jpg', 'first_price_noottemiks_c.jpg', NULL");






        setupInsertToFood("NULL, 'Chocolate peanut', 'Atkins', '60', 'gram', '1', 'stk', '386', '29.4', '24.8', '19', '232', '18', '15', '11', NULL, NULL, '18', 'atkins_chocolate_peanut_thumb.jpg', 'atkins_chocolate_peanut_a.jpg', 'atkins_chocolate_peanut_b.jpg', 'atkins_chocolate_peanut_c.jpg', NULL");
    }





} //dbsetupınsert
