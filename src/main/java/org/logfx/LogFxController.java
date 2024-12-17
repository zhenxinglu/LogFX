package org.logfx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.logfx.model.AbstractLogFile;
import org.logfx.model.LogFileType;
import org.logfx.model.ScpLogFile;
import org.logfx.util.LogFileUtil;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogFxController {
    public static final Path APP_STATE_FILE = Paths.get(System.getProperty("user.home"), "logfx.properties");

    private static final Pattern timeRangePattern = Pattern.compile("(\\d{2}):(\\d{2}):(\\d{2})\\.\\d{3}");

    private final PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    private final static String LOG_PATH_TEXT_FIELD_PATH_TIPS ="The log URI support two formats:\n" +
                                                               "Local file:\n" +
                                                               "    d:\\tmp\\mylog.txt\n" +
                                                               "remote files: \n" +
                                                               "    ssh://{user}:{password}@{host}[:{port}]{path} , default port is 22.\n" +
                                                               "    e.g.  a) ssh://root:root12@10.80.0.69:22/usr/share/cosylab/tcs/C-TCS_log_tcs.hfcim.adapter.upc.log\n" +
                                                               "          b) ssh://root:root12@10.80.0.69/usr/share/cosylab/tcs/C-TCS_log_tcs.hfcim.adapter.upc.log\n";

    @FXML
    private VBox rootVbox;
    @FXML
    private ToggleButton includeCaseSensitiveToggleButton;

    @FXML
    private ToggleButton excludeCaseSensitiveToggleButton;

    @FXML
    private Button showLogsButton;

    @FXML
    private CheckBox log1CheckBox;
    @FXML
    private TextField log1PathTextField;
    @FXML
    private CheckBox log2CheckBox;
    @FXML
    private TextField log2PathTextField;
    @FXML
    private CheckBox log3CheckBox;
    @FXML
    private TextField log3PathTextField;
    @FXML
    private CheckBox log4CheckBox;
    @FXML
    private TextField log4PathTextField;

    @FXML
    private TextField includeWordsTextField;
    @FXML
    private TextField excludeWordsTextField;

    @FXML
    private TextField fromTimeTextField;

    @FXML
    private TextField toTimeTextField;

    @FXML
    private ListView<LogRecord> logView;

    @FXML
    private Label statusLabel;

    @FXML
    private Hyperlink contactHyperLink;

    private List<Map.Entry<CheckBox, TextField>> dataSources;

    @FXML
    public void initialize() {
        dataSources = new ArrayList<>();
        dataSources.add(Map.entry(log1CheckBox, log1PathTextField));
        dataSources.add(Map.entry(log2CheckBox, log2PathTextField));
        dataSources.add(Map.entry(log3CheckBox, log3PathTextField));
        dataSources.add(Map.entry(log4CheckBox, log4PathTextField));

        loadState();
        initLogView();
        initLogFileCheckBoxesAndTextFields();
        initKeywordFilterTextField();
        initTimeRangeTextFields();
    }

    public void initAfterConstruction(Application app) {
        contactHyperLink.setOnAction(t -> app.getHostServices().showDocument("mailto:zhenxinglu@gmail.com?subject=Query about LogFX"));
    }

    private void initLogView() {
        LogViewUtil.init(logView, statusLabel);
    }

    @FXML
    private void showLogs() {
        logView.getItems().clear();
        List<Map.Entry<CheckBox, TextField>> datas = dataSources.stream().filter(dataSource -> dataSource.getKey().isSelected()).toList();

        String includeWords = includeWordsTextField.getText().trim();
        String excludeWords = excludeWordsTextField.getText().trim();
        Filters filters = new Filters(includeWords.isEmpty()? Collections.emptyList() : KeyWordParseUtil.parse(includeWords),
                                      excludeWords.isEmpty()? Collections.emptyList() : KeyWordParseUtil.parse(excludeWords),
                                      includeCaseSensitiveToggleButton.isSelected(), excludeCaseSensitiveToggleButton.isSelected(),
                                      fromTimeTextField.getText().trim(), toTimeTextField.getText().trim());

        List<AbstractLogFile> logfiles = datas.stream().map(e -> LogFileUtil.parse(e.getValue().getText())).toList();


        rootVbox.setCursor(Cursor.WAIT);
        new Thread(() -> {
            List<LogRecord> records;
            try {
                List<String> targetFiles = logfiles.stream().map(e -> e.getLocalTargetFile()).toList();
                records = LogFileFilter.combine(filters, targetFiles);
                Platform.runLater(() -> {
                    logView.getItems().addAll(records);
                    statusLabel.setText("Logs are filtered successfully.");
                });

            } catch (Throwable t) { //some exceptions from SCP library is Throwable, so cannot use IOException here
                Platform.runLater(() -> statusLabel.setText(prettifyException(t)));
            } finally {
                Platform.runLater(() -> rootVbox.setCursor(Cursor.DEFAULT));
            }

        }).start();
    }

    private String prettifyException(Throwable t) {
        if (t instanceof NoSuchFileException) {
            return "Error: No such file: " + t.getMessage();
        }

        return t.getMessage();
    }


    private void initLogFileCheckBoxesAndTextFields() {
        dataSources.forEach(ds -> {
            ds.getKey().selectedProperty().addListener((observable, oldValue, newValue) -> {
                validate(ds.getKey(), ds.getValue());
            });

            ds.getValue().textProperty().addListener((observable, oldValue, newValue) -> {
                validate(ds.getKey(), ds.getValue());
            });

            //for the initialization when start up
            validate(ds.getKey(), ds.getValue());
        });
    }

    private void initTimeRangeTextFields() {
        TextField[] trTextFields = {fromTimeTextField, toTimeTextField};
        Arrays.stream(trTextFields).forEach(tf -> {
            tf.textProperty().addListener((observable, oldValue, newValue) -> {
                validateTimeRangeTextFields(tf);
            });
        });
    }

    private void validateTimeRangeTextFields(TextField tf) {
        String time = tf.getText().trim();

        boolean isValid = true;
        if (!time.isEmpty()) {
            Matcher matter = timeRangePattern.matcher(time);
            isValid = matter.matches();
            if (isValid) {
                int hour = Integer.parseInt(matter.group(1));
                int minute = Integer.parseInt(matter.group(2));
                int second = Integer.parseInt(matter.group(3));
                isValid = hour <= 23 && minute <= 59 && second <= 59;
            }
        }

        tf.pseudoClassStateChanged(errorClass, !isValid);
    }

    private void validate(CheckBox includeCheckBox, TextField tf) {
        boolean isValid = !includeCheckBox.isSelected() || LogFileUtil.parse(tf.getText()).isValid();
        tf.pseudoClassStateChanged(errorClass, !isValid);

        String tipMsg = isValid ? LOG_PATH_TEXT_FIELD_PATH_TIPS : "The file does not exists!\n\n" + LOG_PATH_TEXT_FIELD_PATH_TIPS;
        tf.setTooltip(new Tooltip(tipMsg));
    }

    private void initKeywordFilterTextField() {
//        EventHandler<KeyEvent> showLogsOnEnterKey = event -> showLogs();
//
//        includeWordsTextField.setOnKeyPressed(showLogsOnEnterKey);
//        excludeWordsTextField.setOnKeyPressed(showLogsOnEnterKey);
    }

    public void saveState() {
        File stateFile = APP_STATE_FILE.toFile();
        try (OutputStream output = new FileOutputStream(stateFile)) {
            Properties props = new Properties();

            dataSources.forEach(dataSource -> {
                props.setProperty(dataSource.getKey().getId(), String.valueOf(dataSource.getKey().isSelected()));
                props.setProperty(dataSource.getValue().getId(), dataSource.getValue().getText());
            });

            props.setProperty(includeCaseSensitiveToggleButton.getId(), String.valueOf(includeCaseSensitiveToggleButton.isSelected()));
            props.setProperty(excludeCaseSensitiveToggleButton.getId(), String.valueOf(excludeCaseSensitiveToggleButton.isSelected()));

            props.setProperty(includeWordsTextField.getId(), includeWordsTextField.getText());
            props.setProperty(excludeWordsTextField.getId(), excludeWordsTextField.getText());

            props.store(output, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadState() {
        File stateFile = APP_STATE_FILE.toFile();
        Properties props = new Properties();
        try(InputStream inputStream = new FileInputStream(stateFile)) {
            props.load(inputStream);

        } catch (FileNotFoundException e) {
            //this is the first time to run this program so no state file found, ignore the error
        } catch (IOException e) {
            statusLabel.setText("Error:" + e.getMessage());
        }

        restoreUi(props);
    }

    private void restoreUi(Properties props) {
        dataSources.forEach(dataSource -> {
            dataSource.getKey().setSelected(Boolean.valueOf(props.getProperty(dataSource.getKey().getId(), "true")));
            dataSource.getValue().setText(props.getProperty(dataSource.getValue().getId(), ""));
        });

        includeCaseSensitiveToggleButton.setSelected(Boolean.valueOf(props.getProperty(includeCaseSensitiveToggleButton.getId(), "false")));
        excludeCaseSensitiveToggleButton.setSelected(Boolean.valueOf(props.getProperty(excludeCaseSensitiveToggleButton.getId(), "false")));

        includeWordsTextField.setText(props.getProperty(includeWordsTextField.getId(), ""));
        excludeWordsTextField.setText(props.getProperty(excludeWordsTextField.getId(), ""));
    }

    @FXML
    public void refreshLog(ActionEvent actionEvent) {
            dataSources.stream()
                       .filter(ds -> ds.getKey().isSelected())
                       .map(ds -> LogFileUtil.parse(ds.getValue().getText()))
                       .filter(l -> l.getLogFileType() == LogFileType.SCP)
                       .forEach(l -> ((ScpLogFile)l).setRefetchFlag(true));
    }
}