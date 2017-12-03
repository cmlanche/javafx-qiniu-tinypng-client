/*******************************************************************************
 * Copyright 2014 Alexander Casall, Manuel Mauky
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.fx.mvvm;


import com.fx.mvvm.utils.notifications.NotificationObserver;

/**
 * <p>
 * Interface for a View Model.
 * </p>
 * <p>
 * You can use a notification mechanism by using the {@link #publish(String, Object...)} method. In the View you can
 * subscribe to this notifications by using viewModel.
 * {@link #subscribe(String messageName, NotificationObserver observer)}.
 * </p>
 * <p>
 * Some additional hints to this layer:
 * </p>
 * 
 * <p>
 * Never create a dependency to the view in this layer - if you have to create a new view after a business step, notify
 * the view with the {@link NotificationCenter} or a callback that the view can create the new view. When you write a
 * new {@link View} you should create the associated {@link ViewModel} with tests before.
 * </p>
 * 
 * @author alexander.casall
 * 
 */
public interface ViewModel {

	int ConstantId = 111;
	
	/**
	 * Publishes a notification to the subscribers of the messageName.
	 * <p>
	 *     
	 * This notification mechanism uses the {@link NotificationCenter} internally with the difference that messages send
	 * by this method aren't globally available. Instead they can only be received by this viewModels {@link #subscribe(String, NotificationObserver)}
	 * method or when using this viewModel instance as argument to the {@link NotificationCenter#subscribe(Object, String, NotificationObserver)} method.
	 * <p>
	 *     
	 * See {@link NotificationTestHelper} for a utility that's purpose is to simplify unit tests with notifications.
	 * 
	 * @param messageName
	 *            of the notification
	 * @param payload
	 *            to be send
	 */
	default void publish(boolean ispublic, String messageName, Object... payload) {
		MvvmFX.getNotificationCenter().publish(System.identityHashCode(ispublic?ConstantId:this), messageName, payload);
	}
	
	/**
	 * Subscribe to a notification with a given {@link NotificationObserver}. 
	 * The observer will be invoked on the UI thread.
	 * 
	 * @param messageName
	 *            of the Notification
	 * @param observer
	 *            which should execute when the notification occurs
	 */
	default void subscribe(boolean isPublic, String messageName, NotificationObserver observer) {
		MvvmFX.getNotificationCenter().subscribe(System.identityHashCode(isPublic?ConstantId:this), messageName, observer);
	}
	
	/**
	 * Remove the observer for a specific notification by a given messageName.
	 * 
	 * @param messageName
	 *            of the notification for that the observer should be removed
	 * @param observer
	 *            to remove
	 */
	default void unsubscribe(boolean isPublic, String messageName, NotificationObserver observer) {
		MvvmFX.getNotificationCenter().unsubscribe(System.identityHashCode(isPublic?ConstantId:this), messageName, observer);
	}
	
	/**
	 * Removes the observer for all messages.
	 * 移除所有的订阅
	 * 
	 * @param observer
	 *            to be removed
	 */
	default void unsubscribe(boolean isPublic, NotificationObserver observer) {
		MvvmFX.getNotificationCenter().unsubscribe(System.identityHashCode(isPublic?ConstantId:this), observer);
	}
}
