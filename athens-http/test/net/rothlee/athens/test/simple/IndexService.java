package net.rothlee.athens.test.simple;

import net.rothlee.athens.handler.service.simple.Bind;
import net.rothlee.athens.handler.service.simple.SimpleService;
import net.rothlee.athens.message.AthensContentType;
import net.rothlee.athens.message.AthensRequest;
import net.rothlee.athens.message.AthensResponse;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;

@Bind(path="/", method={ "GET" })
public class IndexService implements SimpleService {

	@Override
	public void doServe(AthensRequest request, AthensResponse response) {
		response.setContentType(AthensContentType.TEXT_HTML);
		response.setContents(getMenuString());
	}

	private ChannelBuffer getMenuString() {

		final StringBuilder sb = new StringBuilder();

		sb.append("Hello World!");

		ChannelBuffer buf = ChannelBuffers.copiedBuffer(sb
                .toString(), CharsetUtil.UTF_8);
		
		return buf;
	}
}
