package org.sonarsource.plugins.report.support.exception;

public class ReportException extends RuntimeException {

    public ReportException(final String msg) {
        super(msg);
    }

    public ReportException(final String msg, Throwable e) {
        super(msg, e);
    }
}
