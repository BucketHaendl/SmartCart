package com.buckethaendl.smartcart.util;

import com.buckethaendl.smartcart.objects.instore.Shelf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Importancer {

    /**
     * Finds the most important shelf (with the most occurrances) in the list of shelves
     * @param shelves The list of shelves, containing duplicate entries
     * @return The shelf that occurred most often
     */
    public static Shelf findMostImportant(List<Shelf> shelves){

        Map<Shelf, Integer> occurrences = new HashMap<Shelf, Integer>();

        int highestCount=0;
        Shelf bestResult=null;

        for (Shelf shelf : shelves) {

            if(occurrences.containsKey(shelf)) {

                int current = occurrences.get(shelf);
                occurrences.put(shelf, (current + 1));

            }

            else occurrences.put(shelf, 1);

        }

        for(Map.Entry<Shelf, Integer> entry : occurrences.entrySet()) {

            if(entry.getValue() > highestCount) bestResult = entry.getKey();

        }

        return bestResult;

    }

}
