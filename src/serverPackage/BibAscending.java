package serverPackage;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class BibAscending extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		
		// Get the run from the datastore and sort it.
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Message");
		List<Entity> results = data.prepare(q).asList(FetchOptions.Builder.withDefaults());
		String message = "";
		if ( !results.isEmpty() ) {
			Entity e = results.get(results.size()-1);
			message = (String) e.getProperty("message");
			
			message = Participant.sortByBib(message);
		}

		// Print out the run.
		String table = "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head>" +
				"<table id='tab'>" +
				"<caption>EVENT</caption>" +
				"<tr>" +
				"<th> RUN </th>" +
				"<th><a id='bibs' href=\"bibdescending\"> BIB </a></th>" +
				"<th><a href=\"startdescending\"> START </a></th>" +
				"<th><a href=\"stopdescending\"> FINISH </a></th>" +
				"<th><a href=\"server\"> ELAPSED </a></th>" +
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
}

