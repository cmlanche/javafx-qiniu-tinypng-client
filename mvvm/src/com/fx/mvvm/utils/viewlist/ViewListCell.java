/*******************************************************************************
 * Copyright 2013 Alexander Casall
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
package com.fx.mvvm.utils.viewlist;

import com.fx.mvvm.ViewModel;
import com.fx.mvvm.ViewTuple;
import com.fx.mvvm.internal.viewloader.View;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;


/**
 * Cell which displays the {@link View} from a {@link ViewTuple}. You have to provide the mapping.
 * 
 * @author sialcasa
 * 
 * @param <T>
 *            which is used to create get the {@link ViewTuple}
 */
@SuppressWarnings("rawtypes")
public abstract class ViewListCell<T> extends ListCell<T> implements
        ViewTupleMapper<T> {
	
	@Override
	public abstract ViewTuple<? extends View, ? extends ViewModel> map(T element);
	
	@Override
	protected void updateItem(T item, boolean empty) {
		super.updateItem(item, empty);
		if (empty) {
			setText(null);
			setGraphic(null);
		} else {
			setText(null);
			Node currentNode = getGraphic();
			Parent view = map(item).getRoot();
			if (currentNode == null || !currentNode.equals(view)) {
				setGraphic(view);
			}
		}
	}
	
}
