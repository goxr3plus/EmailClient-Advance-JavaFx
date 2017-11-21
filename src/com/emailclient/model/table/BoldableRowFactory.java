package com.emailclient.model.table;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableRow;

public class BoldableRowFactory<T extends AbstractTableItem> extends TableRow<T> {

    /**
     * Class for making the text values in the application Bold.
     */

    private final SimpleBooleanProperty bold = new SimpleBooleanProperty();
    private T currentItem = null;

    public BoldableRowFactory() {

        // For mark as read button :

        super();
        bold.addListener(
                (ObservableValue<? extends Boolean> observable, Boolean olValue, Boolean newValue) -> {

                    if (currentItem != null && currentItem == getItem()) {

                        updateItem(getItem(), isEmpty());
                    }
                });

        itemProperty().addListener((ObservableValue<? extends T> observable, T olValue, T newValue) -> {

            bold.unbind();
            if (newValue != null) {

                bold.bind(newValue.getReadProperty());
                currentItem = newValue;
            }
        });
    }

    @Override
    final protected void updateItem(T item, boolean empty) {

        super.updateItem(item, empty);
        if (item != null && !item.isRead()) {

            setStyle("-fx-font-weight: bold");
        } else {

            setStyle("");
        }
    }
}
