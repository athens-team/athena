/*
 * Copyright 2012 Athens Team
 * 
 * This file to you under the Apache License, version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.eincs.athens.core;

import java.util.Set;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.Channels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.internal.Sets;

/**
 * @author Jung-Haeng Lee
 */
public class TransferSessions {

	private static final Logger logger = LoggerFactory
			.getLogger(TransferSessions.class);

	public static Set<Channel> channels = Sets.newHashSet();

	public static void addChannel(Channel channel) {
		synchronized (TransferSessions.channels) {
			channels.add(channel);
		}
	}

	public static void removeChannel(Channel channel) {
		synchronized (TransferSessions.channels) {
			channels.remove(channel);
		}
	}

	public static void broadcast(Object message) {
		synchronized (TransferSessions.channels) {
			for (Channel channel : channels) {
				try {
					Channels.write(channel, message);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			}
		}
	}
}
