package com.eincs.pantheon.test.simple;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;

import com.eincs.pantheon.handler.service.simple.Bind;
import com.eincs.pantheon.handler.service.simple.SimpleService;
import com.eincs.pantheon.message.AthensContentType;
import com.eincs.pantheon.message.AthensRequest;
import com.eincs.pantheon.message.AthensResponse;

@Bind(path="/home", method={ "GET" })
public class MenuService implements SimpleService {

	@Override
	public void doServe(AthensRequest request, AthensResponse response) {
		response.setContentType(AthensContentType.TEXT_HTML);
		response.setContents(getMenuString());
	}

	private ChannelBuffer getMenuString() {

		final StringBuilder sb = new StringBuilder();

		// create Pseudo Menu
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>Athens Test Form</title>\r\n");
		sb.append("</head>\r\n");
		sb.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");

		sb.append("<table border=\"0\">");
		sb.append("<tr>");
		sb.append("<td>");
		sb.append("<h1>Athens Test Form</h1>");
		sb.append("Choose one FORM");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</table>\r\n");

		// GET
		sb.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		sb.append("<FORM ACTION=\"/formget\" METHOD=\"GET\">");
		sb.append("<input type=hidden name=getform value=\"GET\">");
		sb.append("<table border=\"0\">");
		sb.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		sb.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		sb.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		sb.append("</td></tr>");
		sb.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		sb.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		sb.append("</table></FORM>\r\n");
		sb.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		// POST
		sb.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		sb.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
		sb.append("<input type=hidden name=getform value=\"POST\">");
		sb.append("<table border=\"0\">");
		sb.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		sb.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		sb.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		sb.append("<tr><td>Fill with file (only file name will be transmitted): <br> <input type=file name=\"myfile\">");
		sb.append("</td></tr>");
		sb.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		sb.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		sb.append("</table></FORM>\r\n");
		sb.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		// POST with enctype="multipart/form-data"
		sb.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		sb.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
		sb.append("<input type=hidden name=getform value=\"POST\">");
		sb.append("<table border=\"0\">");
		sb.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		sb.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		sb.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		sb.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
		sb.append("</td></tr>");
		sb.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		sb.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		sb.append("</table></FORM>\r\n");
		sb.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		sb.append("</body>");
		sb.append("</html>");

		ChannelBuffer buf = ChannelBuffers.copiedBuffer(sb
                .toString(), CharsetUtil.UTF_8);
		
		return buf;
	}
}