package com.eincs.pantheon.test.simple;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.DefaultCookie;
import org.jboss.netty.util.CharsetUtil;

import com.eincs.pantheon.handler.service.simple.Bind;
import com.eincs.pantheon.handler.service.simple.SimpleService;
import com.eincs.pantheon.message.AthensContentType;
import com.eincs.pantheon.message.AthensRequest;
import com.eincs.pantheon.message.AthensResponse;

@Bind(path="/setCookie", method={ "GET" })
public class SetCookieService implements SimpleService {

	@Override
	public void doServe(AthensRequest request, AthensResponse response) {
		String name = request.getParams().get("name");
		String value = request.getParams().get("value");
		
		Cookie newCookie = new DefaultCookie(name, value);
		
		response.setContentType(AthensContentType.TEXT_HTML);
		response.setContents(getResponseString(newCookie, request));
		response.getCookies().add(newCookie);
	}

	private ChannelBuffer getResponseString(Cookie newCookie, AthensRequest request) {

		final StringBuilder sb = new StringBuilder();

		// create Pseudo Menu
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<title>Cookie List</title>\r\n");
		sb.append("</head>\r\n");
		sb.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");

		sb.append("<h1>new cookie</h1>");
		sb.append(newCookie.toString());
		sb.append("<br />");
		
		sb.append("<h1>prev cookies</h1>");
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
