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
package net.rothlee.athens;

import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * @author roth2520@gmail.com
 */
public interface AddressProvider {
	/**
	 * 원격지 IP + port channel에서 직접가져오는 값
	 * @return
	 */
	InetSocketAddress getRemoteAddress();
	
	InetSocketAddress getLocalAddress();
	/**
	 * X-Forwarded-For 에 담겨오는 Address
	 * @return
	 */
	InetAddress getOriginAddress();
}