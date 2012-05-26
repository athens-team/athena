package com.eincs.pantheon.test.simple;


import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.util.CharsetUtil;

import com.eincs.pantheon.handler.service.simple.Bind;
import com.eincs.pantheon.handler.service.simple.SimpleService;
import com.eincs.pantheon.message.AthensContentType;
import com.eincs.pantheon.message.AthensRequest;
import com.eincs.pantheon.message.AthensResponse;

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
