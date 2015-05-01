package serverPackage;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


// Some of us have a different configurations, so use whichever of these two
// makes eclipse less mad.
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.reflect.TypeToken;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

/**
 * Participant class is used for sorting competitors.
 * 
 * @author Kevari Francis
 */
public class Participant {
	private String event, run,bib,start,finish,elapsed;

	public Participant() {}
	public Participant(String event, String run, String bib,String start,String fin,String elapse) {
		this.event = event;
		this.run = run;
		this.bib = bib;
		this.start = start;
		this.finish = fin;
		this.elapsed = elapse;
	}

	@Override
	public String toString() {
		return  "event: " + event + ", run: " + run + ", bib: " + bib +
				", start: " + start + ", finish: " + finish + ",  elapsed: " + elapsed;
	}

	// Converts string to long, or throws an exception for the calling method to catch.
	public static long convertStringToLong(String time) {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.S");

		// Add the decimal point if it's not there so the parser is happy.
		if ( false == time.contains(".") ) {
			time = time + ".0";
		}

		Date date;
		long timestamp = 0;
		try {
			date = dateFormat.parse(time);
			cal.setTime(date);
			timestamp = cal.getTimeInMillis();
		} catch (ParseException e) {
			throw new NumberFormatException();
		}

		return timestamp;
	}

	// Sort by elapsed.
	public static String sortByElapsed(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareElapsed());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	// Sort by elapsed, then reverse.
	public static String ReverseSortByElapsed(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareElapsed());
		if (convertedPs != null) Collections.reverse(convertedPs);

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	// Sort by bib.
	public static String sortByBib(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareBib());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	// Sort by bib, then reverse.
	public static String ReverseSortByBib(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareBib());
		if (convertedPs != null) Collections.reverse(convertedPs);

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}	

	// Sort by start.
	public static String sortByStart(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareStart());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	// Sort by start, then reverse.
	public static String ReverseSortByStart(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareStart());
		if (convertedPs != null) Collections.reverse(convertedPs);

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}	

	// Reverse sort by finish, then reverse.
	public static String sortByFinish(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new ReverseCompareFinish());
		if (convertedPs != null) Collections.reverse(convertedPs);

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	// Reverse sort by finish.
	public static String ReverseSortByFinish(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new ReverseCompareFinish());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	/**
	 * Comparator for elapsed time.
	 * @author Kevari Francis
	 */
	public static class CompareElapsed implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			double t1, t2;

			try {
				t1 = Double.parseDouble(p1.elapsed);
			} catch(NumberFormatException e){
				return 1;
			}
			try {
				t2 = Double.parseDouble(p2.elapsed);
			} catch(NumberFormatException e){
				return -1;
			}
			if(t1 < t2) return -1;
			if(t1 > t2) return 1;

			return 0;
		}

	}

	/**
	 * Comparator for bib number.
	 * @author Kevari Francis
	 */
	public static class CompareBib implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			double t1, t2;

			try {
				t1 = Double.parseDouble(p1.bib);
				t2 = Double.parseDouble(p2.bib);
			} catch(NumberFormatException e){
				return 0;
			}

			if(t1 < t2) return -1;
			if(t1 > t2) return 1;
			return 0;
		}
	}

	/**
	 * Comparator for start time.
	 * @author Kevari Francis
	 */
	public static class CompareStart implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			long t1, t2;

			try {
				t1 = convertStringToLong(p1.start);
			} catch(NumberFormatException e) {
				if(p2.start.equals("WAITING")) return 0; // both waiting
				return -1; // p1 is waiting and p2 is not
			} 
			
			try{
				t2 = convertStringToLong(p2.start);
			} catch(NumberFormatException e){
				return -1; // we know that p1 is not waiting
			}

			if(t1 < t2) return -1;
			if(t1 > t2) return 1;
			return 0;
		}
	}

	/**
	 * Reverse Comparator for finish time.
	 * @author Kevari Francis
	 */
	private static class ReverseCompareFinish implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			long t1, t2;

			// waiting,dnf,time

			try {
				t1 = convertStringToLong(p1.finish);
			} catch(NumberFormatException e) {

				if(p2.finish.equals("WAITING")){
					if(p1.equals("WAITING")){
						return 0;
					} else {
						return 1;
					}
				} else if(p2.finish.equals("DNF")){
					if(p1.equals("DNF")){
						return 0;
					} else {
						return -1;
					}
				} else if(p2.finish.equals("RUNNING")){
					if(p1.equals("RUNNING")){
						return 0;
					} else if(p1.equals("WAITING")){
						return -1;
					} else{
						return 1;
					}
				} else { //p2 is a time
					return -1;
				}

			} 

			try{
				t2 = convertStringToLong(p2.finish);
			} catch(NumberFormatException e){
				return 1;
			}

			if(t1 < t2) return 1;
			if(t1 > t2) return -1;
			return 0;
		}
	}
}