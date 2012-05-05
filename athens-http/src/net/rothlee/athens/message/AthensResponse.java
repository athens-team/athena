/*
 * Copyright 2012 Athens Team
 *
 * This file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.rothlee.athens.message;

import java.nio.charset.Charset;

import net.rothlee.athens.handler.codec.http.HttpContentType;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;


/**
 * @author roth2520@gmail.com
 */
public interface AthensResponse extends AthensMessage {

    HttpResponseStatus getStatus();

    void setStatus(HttpResponseStatus status);
    
    ChannelBuffer getContents();
    
    void setContents(ChannelBuffer contents);
    
    HttpContentType getContentType();
    
    void setContentType(HttpContentType contentType);

	Charset getCharset();
	
	void setCharset(Charset charset);
}
