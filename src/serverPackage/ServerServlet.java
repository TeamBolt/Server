package serverPackage;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

/**
 * Class is used for sorting competitors by elapsed ascending.
 * Also used to accept data and populate data store.
 * 
 * @author Kevari Francis & Chris Harmon
 */
@SuppressWarnings("serial")
public class ServerServlet extends HttpServlet {
	private String fastData;
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		
		String message = "";
		// We bypass the datastore if we can on this page so that results can be updated more quickly.
		if (fastData != null) {
			message = fastData;
		} else {
			DatastoreService data = DatastoreServiceFactory.getDatastoreService();
			Query q = new Query("Message");
			List<Entity> results = data.prepare(q).asList(FetchOptions.Builder.withDefaults());
			if ( !results.isEmpty() ) {
				Entity e = results.get(results.size()-1);
				message = (String) e.getProperty("message");
			}
		}
		message = Participant.sortByElapsed(message);

		// Print the data.
		String table = "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head>" +
				"<table id='tab'>" +
				"<caption>EVENT</caption>" +
				"<tr>" +
				"<th> RUN </th>" +
				"<th><a id='bibs' href=\"bibdescending\"> BIB </a></th>" +
				"<th><a href=\"startdescending\"> START </a></th>" +
				"<th><a href=\"stopdescending\"> FINISH </a></th>" +
				"<th><a href=\"elapseddescending\"> ELAPSED </a></th>" +
				"</tr>" +
				"</table>" +
				"<script>" +
				"var text = '" + message + "';" +
				"var obj = JSON.parse(text);" +
				"var dnf = 'ignore';" +			
				"var color = \"even\";" +
				"var t = obj[0].event;" +		
				"for (var k in obj) {" +
				"if ( k%2 == 0 ) {" +
				"color = \"odd\";" +
				"} else {" +
				"color = \"even\";" +
				"}" +
				"if (obj[k].elapsed == 'DNF') {" +
				"dnf = 'dnf';" +
				"} else {" +
				"dnf = 'ignore';}" +
				"document.getElementById('tab').innerHTML += '<tr class=\"' + color + '\">' +" +
				"'<td>' +  obj[k].run + '</td>' +" +
				"'<td>' +  obj[k].bib + '</td>' +" +
				"'<td>' + obj[k].start + '</td>' +" +
				"'<td class= \"' + dnf + '\">' + obj[k].finish + '</td>' +" +
				"'<td class= \"' + dnf + '\">' + obj[k].elapsed + '</td>' +" +
				"'</tr>';" +
				"}" +
				"document.getElementById('tab').innerHTML += " +
				"'<caption>' + t + '</caption>'" +
				"</script>";

		resp.getWriter().println(table);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		String data = req.getParameter("data");
		
		// If data is identical with the cached data there's nothing to do here.
		if ( data == null || ( fastData != null && data.equals(fastData) ) ) return;
		fastData = data;
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		

		Query q = new Query("Message");
		List<Entity> list = ds.prepare(q).asList(FetchOptions.Builder.withDefaults());
		String last = null;
		if ( !list.isEmpty() ) {
			// Look at the last thing in the datastore
			Entity laste = list.get(list.size()-1);
			last = (String) laste.getProperty("message");
		}
		
		// If it's identical to the new data, do nothing, otherwise
		// clear out old data and create new data.
		if ( last == null || !last.equals(data) ) {
			for (Entity p : list) {
				ds.delete(p.getKey());
			}
			
			Entity e = new Entity("Message");
			e.setProperty("message", data);
			ds.put(e);
		}
	}
}


