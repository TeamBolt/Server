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
public class ServerServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		
		//Get the data out of the datastore and sort it.
		DatastoreService data = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Message");
		List<Entity> m = data.prepare(q).asList(FetchOptions.Builder.withDefaults());
		String message = "";
		for (Entity p : m) {
			message = (String) p.getProperty("message");
		}
		message = Participant.sortByElapsed(message);

		// Print the data.
		String table = "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head>" +
				"<table id='tab'>" +
				"<caption>EVENT</caption>" +
				"<tr>" +
				"<th> RUN </th>" +
				"<th><a id='bibs' href=\"server\"> BIB </a></th>" +
				"<th><a href=\"server\"> START </a></th>" +
				"<th><a href=\"server\"> FINISH </a></th>" +
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
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Entity e = new Entity("Message");
		e.setProperty("message", data);
		ds.put(e);
	}
}

