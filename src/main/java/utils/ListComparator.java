package utils;

import java.util.LinkedList;
import java.util.List;

public class ListComparator {
	
	public static String addedItem (List<String> listOld, List<String> listNew) {
		System.out.println("Added item from " + listOld + " to " + listNew);
		// TODO avoid copy objects
		List<String> copyListNew = new LinkedList(listNew);
		copyListNew.removeAll(listOld);
		return copyListNew.get(0);
	}
	
	public static String removedItem (List<String> listOld, List<String> listNew) {
		System.out.println("Removed item from " + listOld + " to " + listNew);
		listOld.removeAll(listNew);
		return listOld.size()==1 ? listOld.get(0) : null;
	}
}
