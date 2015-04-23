package serverPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


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

	public String sortByElapsed(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		Collections.sort(convertedPs,new CompareElapsed());

		//list of objects to json
		return new Gson().toJson(convertedPs);
	}

	public String ReverseSortByElapsed(String str){
		//list of json to object
		ArrayList<Participant> convertedPs = new Gson().fromJson(str, new TypeToken<Collection<Participant>>(){}.getType());
		Collections.sort(convertedPs,new ReverseCompareElapsed());

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
}