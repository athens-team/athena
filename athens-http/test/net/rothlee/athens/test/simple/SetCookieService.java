package net.rothlee.athens.test.simple;

import net.rothlee.athens.handler.codec.http.HttpContentType;
import net.rothlee.athens.handler.service.simple.Bind;
import net.rothlee.athens.handler.service.simple.SimpleService;
import net.rothlee.athens.message.AthensRequest;
import net.rothlee.athens.message.AthensResponse;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.DefaultCookie;
import org.jboss.netty.util.CharsetUtil;

@Bind(path="/setCookie", method={ "GET" })
public class SetCookieService implements SimpleService {

	@Override
	public void doServe(AthensRequest request, AthensResponse response) {
		String name = request.getParams().get("name");
		String value = request.getParams().get("value");
		
		Cookie newCookie = new DefaultCookie(name, value);
		
		response.setContentType(HttpContentType.TEXT_HTML);
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