package nz.ac.vuw.swen301.assignment3.server;

public class LogEvent {
    private String id;
    private String message;
    private String timestamp;
    private String thread;
    private String logger;
    private String level;
    private String errorDetails;

    public LogEvent(String id, String message, String timestamp, String thread, String logger, String level, String errorDetails) {
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
        this.thread = thread;
        this.logger = logger;
        this.level = level;
        this.errorDetails = errorDetails;
    }

    public LogEvent(){

    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getThread() {
        return thread;
    }

    public String getLogger() {
        return logger;
    }

    public String getLevel() {
        return level;
    }

    public String getErrorDetails() {
        return errorDetails;
    }
}
