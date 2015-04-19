package serverPackage;

import java.io.IOException;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ServerServlet extends HttpServlet {
	private String message = "";

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("text/html");
		String table = "<head><link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\"></head>" +
				"<table id='tab'>" +
				"<caption>EVENT</caption>" +
				"<tr>" +
				"<th> RUN </th>" +
				"<th> BIB </th>" +
				"<th> START </th>" +
				"<th> FINISH </th>" +
				"<th> ELAPSED </th>" +
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
		message = data;
	}
	
}

