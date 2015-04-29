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


// I think some of us have a different configuration, so use whichever of these two
// makes eclipse less mad.
import com.google.appengine.repackaged.com.google.gson.Gson;
import com.google.appengine.repackaged.com.google.gson.reflect.TypeToken;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;

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
			System.out.println("Invalid time entered.");
		}

		return timestamp;
	}

	public static String sortByElapsed(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareElapsed());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	public static String ReverseSortByElapsed(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new ReverseCompareElapsed());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}


	public static String sortByBib(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareBib());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	public static String ReverseSortByBib(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new ReverseCompareBib());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}	

	public static String sortByStart(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareStart());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	public static String ReverseSortByStart(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new ReverseCompareStart());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}	

	public static String sortByFinish(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new CompareFinish());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	public static String ReverseSortByFinish(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		if (convertedPs != null) Collections.sort(convertedPs,new ReverseCompareFinish());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

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

	private static class ReverseCompareElapsed implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			double t1, t2;

			try {
				t1 = Double.parseDouble(p1.elapsed);
			} catch(NumberFormatException e){
				return -1;
			}
			try {
				t2 = Double.parseDouble(p2.elapsed);
			} catch(NumberFormatException e){
				return 1;
			}

			if(t1 < t2) return 1;
			if(t1 > t2) return -1;
			return 0;
		}
	}

	public static class CompareBib implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			double t1, t2;

			try {
				t1 = Double.parseDouble(p1.bib);
				t2 = Double.parseDouble(p2.bib);
			} catch(NumberFormatException e){
				e.printStackTrace();
				return 0;
			}

			if(t1 < t2) return -1;
			if(t1 > t2) return 1;
			return 0;
		}
	}

	private static class ReverseCompareBib implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			double t1, t2;

			try {
				t1 = Double.parseDouble(p1.bib);
				t2 = Double.parseDouble(p2.bib);
			} catch(NumberFormatException e){
				return 0;
			}

			if(t1 < t2) return 1;
			if(t1 > t2) return -1;
			return 0;
		}
	}

	public static class CompareStart implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			long t1, t2;

			try {
				t1 = convertStringToLong(p1.start);
				t2 = convertStringToLong(p2.start);
			} catch(NumberFormatException e){
				e.printStackTrace();
				return 0;
			}

			if(t1 < t2) return -1;
			if(t1 > t2) return 1;
			return 0;
		}
	}

	private static class ReverseCompareStart implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			long t1, t2;

			try {
				t1 = convertStringToLong(p1.start);
				t2 = convertStringToLong(p2.start);
			} catch(NumberFormatException e){
				return 0;
			}

			if(t1 < t2) return 1;
			if(t1 > t2) return -1;
			return 0;
		}
	}

	public static class CompareFinish implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			long t1, t2;

			try {
				t1 = convertStringToLong(p1.finish);
				t2 = convertStringToLong(p2.finish);


			} catch(NumberFormatException e){
				e.printStackTrace();
				return 0;
			}

			if(t1 < t2) return -1;
			if(t1 > t2) return 1;
			return 0;
		}
	}

	private static class ReverseCompareFinish implements Comparator<Participant>{

		@Override
		public int compare(Participant p1, Participant p2) {
			long t1, t2;

			try {
				t1 = convertStringToLong(p1.finish);
				t2 = convertStringToLong(p2.finish);
			} catch(NumberFormatException e){
				e.printStackTrace();
				return 0;
			}

			if(t1 < t2) return 1;
			if(t1 > t2) return -1;
			return 0;
		}
	}
}