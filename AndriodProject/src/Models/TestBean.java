package Models;

import java.util.ArrayList;

public class TestBean {
	
	
	
	public static String getAValue(){
		return "OMG!";
	}
	
	
	public static ArrayList<String> getList(){
	    ArrayList<String> list = new ArrayList<String>();
	    list.add("John's Wedding");
	    list.add("My Kegger");
	    list.add("Dinner Stuff");
	    list.add("Random Stuff");
	    list.add("Just add water");
	    return list;
	}

}
