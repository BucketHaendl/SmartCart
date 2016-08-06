package com.buckethaendl.smartcart.util;

import android.util.Log;

import com.buckethaendl.smartcart.data.service.WaSaArticle;
import com.buckethaendl.smartcart.objects.instore.Shelf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Importancer {

    public static final int OFFLINE_ARTICLES_COUNT_FAKE = 9999; //a faked amount of articles in an offline shelf to ensure its always chosen!

    /**
     * Finds the most important shelf (with the most occurrances) in the list of shelves
     * @param shelves The list of shelves, containing duplicate entries
     * @return The shelf that occurred most often
     */
    public static Shelf findMostImportant(List<Shelf> shelves) {

        Map<Shelf, Integer> articleOccurrances = new HashMap<Shelf, Integer>();

        int highestCount=0;
        Shelf bestResult=null;

        for (Shelf shelf : shelves) {

            List<WaSaArticle> articles = shelf.getArticles();

            //todo BUG: is never true! even though values are same, they are never contained!!!
            //todo BUG am wochenende beheben! -> wird nie auf true / aufgerufen gesetzt!
            //wird über hashcode verglichen! Überschreiben!!!
            int current = 0;

            if(articleOccurrances.containsKey(shelf)) {

                current = articleOccurrances.get(shelf);

            }

            //if article number is known for this shelf
            if(articles != null) {
                articleOccurrances.put(shelf, (current + articles.size()));
            }

            //if no articles known (e.g. for offline database)
            else articleOccurrances.put(shelf, (current + OFFLINE_ARTICLES_COUNT_FAKE));

        }

        for(Map.Entry<Shelf, Integer> entry : articleOccurrances.entrySet()) {

            if(entry.getValue() > highestCount) {

                bestResult = entry.getKey();

                Log.v("Importancer", "Shelf " + bestResult.getShelfId() + " value " + entry.getValue() + " > " + highestCount);

                highestCount = entry.getValue();

            }

        }

        if(bestResult != null) {

            Log.v("Importancer", "MOST IMPORTANT SHELF " + bestResult.getShelfId() + " with amount of items " + highestCount);

            if(bestResult.getArticles() != null) {

                StringBuilder builder = new StringBuilder();
                for(WaSaArticle article : bestResult.getArticles()) {

                    builder.append(article.toString());
                    builder.append("\n");

                }


                Log.v("Importancer", "MOST IMPORTANT SHELF contains example articles:\n" + builder.toString());

            }

        }

        return bestResult;

    }

}
