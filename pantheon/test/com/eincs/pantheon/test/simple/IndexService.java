package com.eincs.pantheon.test.simple;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;

import com.eincs.pantheon.handler.service.simple.Bind;
import com.eincs.pantheon.handler.service.simple.SimpleService;
import com.eincs.pantheon.message.PanteonContentType;
import com.eincs.pantheon.message.PanteonRequest;
import com.eincs.pantheon.message.PanteonResponse;

@Bind(path="/", method={ "GET" })
public class IndexService implements SimpleService {

	@Override
	public void doServe(PanteonRequest request, PanteonResponse response) {
		response.setContentType(PanteonContentType.TEXT_HTML);
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
