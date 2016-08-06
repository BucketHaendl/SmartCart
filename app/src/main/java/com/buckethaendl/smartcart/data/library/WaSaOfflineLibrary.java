package com.buckethaendl.smartcart.data.library;

import android.content.Context;

import com.buckethaendl.smartcart.App;
import com.buckethaendl.smartcart.data.service.FBBShelf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A selection of help-out entries for articles the Library cannot find in the library file
 *
 * Created by Cedric on 04.08.16.
 */
public class WaSaOfflineLibrary {

    public static final String TAG = WaSaOfflineLibrary.class.getName();

    private static WaSaOfflineLibrary instance;
    public Map<String, String> offlineWaSa;

    static {

        instance = new WaSaOfflineLibrary();

    }

    private WaSaOfflineLibrary() {

        offlineWaSa = createOfflineWaSa();

    }

    public static WaSaOfflineLibrary getInstance() {

        return WaSaOfflineLibrary.instance;

    }

    /**
     * Returns the shelves in a sync task to directly retrieve them (hackaround)
     */
    public List<? extends FBBShelf> loadShelvesOffline(String country, int market, String query) {

        return queryShelvesOffline(country, market, query);

    }

    /**
     * Queries the given data and gives back all shelves, that contain the given product and market details
     *
     * @param country 2 letter ISO code of the country to search in ("DE")
     * @param market  4 digit market number of the market to search in (6250)
     * @param query   a search query term of the product to search the shelves for ("honey")
     * @return all shelves that contain products with the ware description query term in this specific store (here: all honey shelves)
     */
    public List<? extends FBBShelf> queryShelvesOffline(String country, int market, String query) {

        final Context context = App.getGlobalContext();

        List<FBBShelf> result = new ArrayList<FBBShelf>();

        //if it contains this specific query term
        if(this.offlineWaSa.containsKey(query)) {

            FBBShelf shelf = new FBBShelf(this.offlineWaSa.get(query));
            result.add(shelf);

        }

        else {

            //todo serach in-text

        }

        return result;

    }

    public Map<String, String> createOfflineWaSa() {

        //maps search term to fbb (as string)
        Map<String, String> map = new HashMap<String, String>();

        //fill here
        map.put("Kirschen", "U_P040_OB");
        map.put("Äpfel", "U_P040_OB");
        map.put("Birnen", "U_P040_OB");
        map.put("Erdbeeren", "U_P040_OB");
        map.put("Bananen", "U_P040_OB");
        map.put("Pfirsiche", "U_P040_OB");
        map.put("Aprikosen", "U_P040_OB");
        map.put("Himbeeren", "U_P040_OB");
        map.put("Zitronen", "U_P040_OB");
        map.put("Mandarinen", "U_P040_OB");
        map.put("Orangen", "U_P040_OB");
        map.put("Clementinen", "U_P040_OB");
        map.put("Pflaumen", "U_P040_OB");
        map.put("Johannisbeeren", "U_P040_OB");
        map.put("Wassermelone", "U_P040_OB");
        map.put("Melonen", "U_P040_OB");
        map.put("Honigmelone", "U_P040_OB");
        map.put("Limetten", "U_P040_OB");
        map.put("Blaubeeren", "U_P040_OB");
        map.put("Kartoffeln", "U_P040_KT");
        map.put("Erdäpfel", "U_P040_KT");
        map.put("Kürbis", "U_P040_GM");
        map.put("Karotten", "U_P040_GM");
        map.put("Tomaten", "U_P040_GM");
        map.put("Gurken", "U_P040_GM");
        map.put("Zwiebeln", "U_P040_GM");
        map.put("Paprika", "U_P040_GM");
        map.put("Pilze", "U_P040_GM");
        map.put("Champignons", "U_P040_GM");
        map.put("Pfifferlinge", "U_P040_GM");
        map.put("Spargel", "U_P040_GM");
        map.put("Suppengrün", "U_P040_GM");
        map.put("Lauch", "U_P040_GM");
        map.put("Weißkohl", "U_P040_Gm");
        map.put("Brokkoli", "U_P040_GM");
        map.put("Salat", "U_P040_GM");
        map.put("Salatkopf", "U_P040_GM");
        map.put("Radieschen", "U_P040_GM");
        map.put("Zucchini", "U_P040_GM");
        map.put("Kohl", "U_P040_GM");
        map.put("Buch", "U_P560");
        map.put("Roman", "U_P560");
        map.put("Schrift", "U_P560");
        map.put("Studie", "U_P560");
        map.put("Werk", "U_P560");
        map.put("Lektüre", "U_P560");
        map.put("Abhandlung", "U_P560");
        map.put("Sprudel", "P100W30026E");
        map.put("Wasser", "P100W30026E");
        map.put("Mineralwasser", "P100W30026E");
        map.put("Fisch", "U_P070");
        map.put("Filet", "U_P070");
        map.put("Fischfilet", "U_P070");
        map.put("panierter Fisch", "U_P070");
        map.put("Lachsfilet", "U_P070");
        map.put("Blühpflanzen", "U_P910");
        map.put("Petersilie", "U_P910");
        map.put("Kresse", "U_P910");
        map.put("Schnittlauch", "U_P910");
        map.put("Kräuter", "U_P910");
        map.put("Zimmerpflanze", "U_P910");
        map.put("Blumenzwiebeln", "U_P910");
        map.put("Blumensamen", "U_P910");
        map.put("Lavendel", "U_P910");
        map.put("Bier", "U_P100_BI");
        map.put("Rothaus", "U_P100_BI");
        map.put("Tannenzäpfle", "U_P100_BI");
        map.put("Bitburger", "U_P100_BI");
        map.put("Malzbier", "U_P100_BI");
        map.put("Wulle", "U_P100_BI");
        map.put("Wullebier", "U_P100_BI");
        map.put("Hofbräu", "U_P100_BI");
        map.put("Warsteiner", "U_P100_BI");
        map.put("Öttinger", "U_P100_BI");
        map.put("Pils", "U_P100_BI");
        map.put("Paulaner", "U_P100_BI");
        map.put("Desperados", "U_P100_BI");
        map.put("Schöfferhofer", "U_P100_BI");
        map.put("Becks", "U_P100_BI");
        map.put("Heineken", "U_P100_BI");
        map.put("Becks Gold", "U_P100_BI");
        map.put("Becks ice", "U_P100_BI");
        map.put("Becks Lemon", "U_P100_BI");
        map.put("Schöfferhofer Grapefruit", "U_P100_BI");
        map.put("Schwabenbräu", "U_P100_BI");
        map.put("Karamalz", "U_P100_BI");
        map.put("Bierkasten", "U_P100_BI");
        map.put("Orangensaft", "U_P100_LM");
        map.put("Apfelsaft", "U_P100_LM");
        map.put("Hohes C", "U_P100_LM");
        map.put("Vitamisaft", "U_P100_LM");
        map.put("Apfelschorle", "U_P100_LM");
        map.put("Saft", "U_P100_LM");
        map.put("Traubensaft", "U_P100_LM");
        map.put("Johannesbeersaft", "U_P100_LM");
        map.put("Zitronensprudel", "U_P100_LM");
        map.put("Rotbäcken", "U_P100_LM");
        map.put("Bionade", "U_P100_LM");
        map.put("Cola", "U_P100_CO");
        map.put("Fanta", "U_P100_CO");
        map.put("Mexxomix", "U_P100_CO");
        map.put("Pepsi", "U_P100_CO");
        map.put("Pepsi light", "U_P100_CO");
        map.put("Cola light", "U_P100_CO");
        map.put("CocaCola", "U_P100_CO");
        map.put("FritzCola", "U_P100_CO");
        map.put("Cola zero", "U_P100_CO");
        map.put("Cola Dose", "U_P100_CO");
        map.put("Lift", "U_P100_CO");
        map.put("Sprite", "U_P100_CO");
        map.put("Seven Up", "U_P100_CO");

        //example shopping list
        map.put("Spaghetti", "P140W30023E");
        map.put("Bohnen", "P120W30019E");
        map.put("Cornflakes", "P140W30033E");
        map.put("Eier", "P060W30035E");
        map.put("Milch", "P060W30035E");
        map.put("H-Milch", "P061W30011E");
        map.put("Joghurt", "P060W30025E");
        map.put("Butter", "P060W30023B");
        //map.put("Käse", "P061W30012E");
        //map.put("Pizza", "P070W30004E");
        map.put("Chips", "P130W30056E");
        map.put("Schokolade", "P130W30014E");
        map.put("Wein", "P090W30004E");
        map.put("Klopapier", "P510W30022E");
        map.put("Tempos", "P430W30015B");
        map.put("Shampoo", "P460W30036E");
        map.put("Seife", "P460W30205E");
        map.put("Waschpulver", "P160W30031E");
        map.put("Waschmittel", "P160W30011E");
        map.put("Clerasil", "P460W30046E");
        map.put("Katzenfutter", "P340W30017E");
        map.put("Hundefutter", "P340W30007E");
        map.put("Zigaretten", "P440W30009A");
        map.put("Toastbrot", "P111W30014E");
        map.put("Spätzle", "P140W30058E");
        map.put("Nutella", "P110W30027E");
        map.put("Backmischung", "P140W30013E");
        map.put("Frischkäse", "P060W30007E");
        map.put("Pringles", "P130W30053A");
        map.put("Pralinen", "P130W30021E");

        //filling ended

        return map;

    }

}
