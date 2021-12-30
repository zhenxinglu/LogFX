package org.logfx;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class LogFileFilter {
    private static Pattern pattern = Pattern.compile(":.*:.*\\s+(TRACE|DEBUG|INFO|WARN|ERROR|FATAL)\\s+");
    private String filePath;
    private Filters filters;

    public LogFileFilter(String filePath, Filters filters) {
        this.filePath = filePath;
        this.filters = filters;
    }

    public List<String> normalizeLines() throws IOException {
        List<String> normalizedLines = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            Iterator<String> lineIterator = lines.iterator();

            String continuousLine = "";
            while ((lineIterator.hasNext())) {
                String line = lineIterator.next();

                if (isLog4jLine(line)) {
                    if (lineIterator.hasNext()) {
                        String nextLine = lineIterator.next();
                        if (isLog4jLine(nextLine)) {
                            if (!continuousLine.equals("")) {
                                normalizedLines.add(continuousLine);
                            }
                            normalizedLines.add(line);
                            continuousLine = nextLine;
                        } else {
                            continuousLine += line + "\n" + nextLine;
                        }
                    } else {
                        if (!continuousLine.equals("")) {
                            normalizedLines.add(continuousLine);
                        }
                        normalizedLines.add(line);
                        continuousLine = "";
                    }
                } else {
                    continuousLine += "\n" + line;
                }
            }

            if (!continuousLine.equals("")) {
                normalizedLines.add(continuousLine);
            }

        }
        return normalizedLines;
    }

    public List<LogRecord> getLogRecords() throws IOException {
        List<LogRecord> records = new ArrayList<>();

        List<String> lines = normalizeLines();
        lines.forEach(line -> {
            Optional<LogRecord> record = process(line);
            record.ifPresent(records::add);
        });

        return records;
    }

    private Optional<LogRecord> process(String line) {
        if (line.trim().isEmpty()) {
            return Optional.empty();
        }

        boolean caseSensitveInInclude = filters.isCaseSensitiveInInclude();
        boolean caseSensitiveInExclude = filters.isCaseSensitiveInExclude();
        List<String> includeKeyWords = filters.getIncludeKeywords();
        List<String> excludeKeyWords = filters.getExcludeKeywords();

        String tempLine = caseSensitiveInExclude ? line : line.toLowerCase();
        List<String> tempExcludeKeyWords = caseSensitiveInExclude ?
                                           excludeKeyWords : excludeKeyWords.stream().map(kw -> kw.toLowerCase()).toList();
        if(tempExcludeKeyWords.stream().anyMatch(tempLine::contains)) {
            return Optional.empty();
        }

        LogRecord logRecord = parseLine(line);
        if (!isRecordBetweenTime(logRecord, filters)) {
            return Optional.empty();
        }

        if (includeKeyWords.isEmpty()) {
            return Optional.of(logRecord);
        }

        tempLine = caseSensitveInInclude? line: line.toLowerCase();
        List<String> tempIncludeKeyWords = caseSensitveInInclude ?
                                           includeKeyWords : includeKeyWords.stream().map(kw -> kw.toLowerCase()).toList();
        tempLine = tempIncludeKeyWords.stream().anyMatch(tempLine::contains)? line: null;

        return tempLine != null ? Optional.of(logRecord) : Optional.empty();
    }

    private boolean isRecordBetweenTime(LogRecord logRecord, Filters filters) {
        String fromTime = filters.getFromTime();
        String toTime = filters.getToTime();

        if (!fromTime.isEmpty() && logRecord.getTimestamp().compareTo(fromTime) < 0) {
            return false;
        }

        if (!toTime.isEmpty() && logRecord.getTimestamp().compareTo(toTime) > 0) {
            return false;
        }

        return true;
    }

    private LogRecord parseLine(String line) {
        //09:55:50.164 INFO  [pool-3-thread-1] c.c.tcs.hfci.gant.GantryCommunicator:407 - Invoked command 100.
        String[] parts = line.split("\s+",3);
        return new LogRecord(parts[0], Level.parse(parts[1]), line);
    }

    public static List<LogRecord> combine(Filters filters, List<String> logPaths) throws IOException {
        List<LogFileFilter> logFileFilters = logPaths.stream().map(logPath -> new LogFileFilter(logPath, filters)).toList();

        List<List<LogRecord>> recordLists = new ArrayList<>();
        for (LogFileFilter logFileFilter : logFileFilters) {
            List<LogRecord> logRecords = logFileFilter.getLogRecords();
            recordLists.add(logRecords);
        }
        return  combine(recordLists);
    }


    public static List<LogRecord> combine(List<List<LogRecord>> recordLists) {
        int capacity = recordLists.stream().mapToInt(recordList -> recordList.size()).sum();
        ArrayList<LogRecord> totalRecords = new ArrayList<>(capacity);

        recordLists.forEach(recordList -> totalRecords.addAll(recordList));

        totalRecords.sort(Comparator.comparing(LogRecord::getTimestamp));

        return totalRecords;
    }

    public static void main(String[] args) {
        List<String> strList = new ArrayList<>();
        strList.add("09:49:55.816 FATAL [UpcAdapterRequestWorker] c.cosy.tcs.hfci.upc.work.RequestWorker - Error occurred while handling connection");
        strList.add("java.net.SocketException: Socket closed");
        strList.add("at java.net.SocketInputStream.socketRead(Unknown Source) ~[?:1.8.0_144]");
        strList.add("at java.lang.Thread.run(Unknown Source) WARN [?:1.8.0_144]");
        strList.add("09:50:17.545 INFO  [main] c.c.tcs.comm.Configuration - CFG | Reading Common.r type java.lang.String");
        strList.add("09:50:17.547 INFO  [main] c.c.tcs.hfci.upc.UpcAdapterService-Upc - Starting Upc in room room1");

        strList.forEach(line -> System.out.println(isLog4jLine(line)));
    }

    private static boolean isLog4jLine(String line) {
        return pattern.matcher(line).find();
    }
}
