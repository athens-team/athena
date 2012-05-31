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
package com.eincs.pantheon;

import java.util.HashMap;
import java.util.concurrent.Executor;


import com.eincs.pantheon.utils.concuurent.CurrentExecutor;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ExecutionList;

/**
 * @author roth2520@gmail.com
 */
public class PanteonLifeCycles {

	private final HashMap<Integer, AthensLifeCycleImpl> lifeCycles = Maps
			.newHashMap();

	public synchronized PanteonLifeCycle createLifeCycle() {
		AthensLifeCycleImpl lifeCycle = new AthensLifeCycleImpl();
		lifeCycles.put(lifeCycle.hashCode(), lifeCycle);
		return lifeCycle;
	}

	public synchronized void complete(PanteonLifeCycle lifeCycle) {
		AthensLifeCycleImpl lifeCycleImpl = lifeCycles.remove(lifeCycle
				.hashCode());
		if (lifeCycleImpl != null) {
			lifeCycleImpl.complete();
		}
	}

	public synchronized void completeAll() {
		for (AthensLifeCycleImpl lifeCycle : lifeCycles.values()) {
			lifeCycle.complete();
		}
	}

	private class AthensLifeCycleImpl implements PanteonLifeCycle {

		private final Executor executor = new CurrentExecutor();
		private final ExecutionList listeners = new ExecutionList();

		void complete() {
			listeners.execute();
		}

		@Override
		public void addListener(final PanteonLifeCycleLIstener listener) {
			this.listeners.add(new Runnable() {
				@Override
				public void run() {
					listener.onComplete(AthensLifeCycleImpl.this);
				}
			}, executor);
		}

	}
}