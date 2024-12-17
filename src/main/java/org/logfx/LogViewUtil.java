package org.logfx;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.*;

import java.util.stream.Collectors;

public class LogViewUtil {
    private final static PseudoClass trace = PseudoClass.getPseudoClass("trace");
    private final static PseudoClass debug = PseudoClass.getPseudoClass("debug");
    private final static PseudoClass info = PseudoClass.getPseudoClass("info");
    private final static PseudoClass warn = PseudoClass.getPseudoClass("warn");
    private final static PseudoClass error = PseudoClass.getPseudoClass("error");
    private final static PseudoClass fatal = PseudoClass.getPseudoClass("fatal");

    private final static double MAX_FONT_SIZE = 28;
    private final static double MIN_FONT_SIZE = 10;

    private static double fontSize = 15;
    private static Label statuslabel;

    public static void init(ListView<LogRecord> listView, Label labelStatus) {
        statuslabel = labelStatus;
        listView.getStyleClass().add("log-view");

        listView.addEventFilter(ScrollEvent.SCROLL, LogViewUtil.createScrollEvent(listView));
        listView.setCellFactory(LogViewUtil::createListCell);
        setupCopySelection(listView);
    }

    private static void setupCopySelection(ListView<LogRecord> listView) {
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        listView.getSelectionModel()
                .getSelectedItems()
                .addListener((ListChangeListener<LogRecord>) c -> {
                    if(c.getList().size() > 0) {
                        statuslabel.setText("Press Ctrl + C to copy the selected lines to clipboard");
                    }
                });

        final KeyCodeCombination keyCodeCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_ANY);
        listView.setOnKeyPressed(event -> {
            if (keyCodeCopy.match(event)) {
                ObservableList<LogRecord> selectedItems = listView.getSelectionModel().getSelectedItems();

                String selectedLines = selectedItems.stream().map(r -> r.getMessage()).collect(Collectors.joining("\n"));
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(selectedLines);
                Clipboard.getSystemClipboard().setContent(clipboardContent);
            }
        });
    }

    private static EventHandler<ScrollEvent> createScrollEvent(ListView<LogRecord> listView) {
        return event -> {
            if (!event.isControlDown()) {
                return;
            }

            changeFontSize(listView, event);
            listView.refresh();
            event.consume();
        };
    }

    private static ListCell<LogRecord> createListCell(ListView<LogRecord> listView) {
        return new ListCell<>() {
            @Override
            protected void updateItem(LogRecord item, boolean empty) {
                super.updateItem(item, empty);

                pseudoClassStateChanged(trace, false);
                pseudoClassStateChanged(debug, false);
                pseudoClassStateChanged(info, false);
                pseudoClassStateChanged(warn, false);
                pseudoClassStateChanged(error, false);
                pseudoClassStateChanged(fatal, false);

                if (item == null || empty) {
                    setText(null);
                    return;
                }

                String cssString = String.format("-fx-font-size: %f;",  fontSize);
                setStyle(cssString);

                setText(item.getMessage());

                if (item.getMessage().length() > 250) {
                    Tooltip tooltip = new Tooltip(item.getMessage());
                    tooltip.setWrapText(true);
                    tooltip.setPrefWidth(1000);
                    setTooltip(tooltip);
                }

                pseudoClassStateChangedHelper(this, item.getLevel());
            }
        };
    }

private static void pseudoClassStateChangedHelper(ListCell<LogRecord> listCell, Level level) {
    PseudoClass pseudoClass = switch (level) {
        case TRACE -> trace;
        case INFO -> info;
        case WARN -> warn;
        case ERROR -> error;
        case FATAL -> fatal;
        default -> debug;
    };

    listCell.pseudoClassStateChanged(pseudoClass, true);
}


    private static void changeFontSize(ListView<LogRecord> listView, ScrollEvent event) {
        if (listView.getItems().isEmpty()) {
            return;
        }

        fontSize += (event.getDeltaY() > 0? 0.1 : -0.1);

        if (fontSize > MAX_FONT_SIZE) {
            fontSize = MAX_FONT_SIZE;
        } else if (fontSize < MIN_FONT_SIZE) {
            fontSize = MIN_FONT_SIZE;
        }
    }
}