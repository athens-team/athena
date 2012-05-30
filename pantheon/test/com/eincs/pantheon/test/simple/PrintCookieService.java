package com.eincs.pantheon.test.simple;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.util.CharsetUtil;

import com.eincs.pantheon.handler.service.simple.Bind;
import com.eincs.pantheon.handler.service.simple.SimpleService;
import com.eincs.pantheon.message.PanteonContentType;
import com.eincs.pantheon.message.PanteonRequest;
import com.eincs.pantheon.message.PanteonResponse;

@Bind(path="/printCookie", method={ "GET" })
public class PrintCookieService implements SimpleService {

	@Override
	public void doServe(PanteonRequest request, PanteonResponse response) {
		response.setContentType(PanteonContentType.TEXT_HTML);
		response.setContents(getResponseString(request));
	}

	private ChannelBuffer getResponseString(PanteonRequest request) {

		final StringBuilder sb = new StringBuilder();

		// create Pseudo Menu
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>Cookie List</title>\r\n");
		sb.append("</head>\r\n");
		sb.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");

		for(Cookie cookie : request.getCookies()) {
			sb.append(cookie.toString());
			sb.append("<br />");
		}

		sb.append("</body>");
		sb.append("</html>");

		ChannelBuffer buf = ChannelBuffers.copiedBuffer(sb
                .toString(), CharsetUtil.UTF_8);
		
		return buf;
	}
}
