package studentcapture.assignment;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles validation of date intervals.
 */
public class AssignmentDateIntervalls {

    private LocalDateTime startDate;
    private boolean startDateIsSet;
    private LocalDateTime endDate;
    private boolean endDateIsSet;
    private LocalDateTime publishedDate;
    private boolean publishedDateIsSet;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss");

    public AssignmentDateIntervalls() {
        startDateIsSet = false;
        endDateIsSet = false;
        publishedDateIsSet = false;
    }

    public void setStartDate(String startDate) throws IllegalArgumentException {
        try {
            startDateIsSet = true;
            this.startDate = LocalDateTime.parse(startDate, FORMATTER);
            validate();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("You must set a start date");
        }
    }
    
    public void setStartDateHard(String startDate) throws DateTimeParseException, IllegalArgumentException {
        startDateIsSet = true;
        this.startDate = LocalDateTime.parse(startDate, FORMATTER);
        //validate();
    }

    public String getStartDate() {
        return FORMATTER.format(startDate);
    }

    public void setEndDate(String endDate) throws IllegalArgumentException {
        try {
            endDateIsSet = true;
            this.endDate = LocalDateTime.parse(endDate, FORMATTER);
            validate();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("You must set a end date");
        }
    }

    public void setEndDateHard(String endDate) throws DateTimeParseException, IllegalArgumentException {
        endDateIsSet = true;
        this.endDate = LocalDateTime.parse(endDate, FORMATTER);
        //validate();
    }
    
    public String getEndDate() {
        return FORMATTER.format(endDate);
    }

    public void setPublishedDate(String publishedDate) throws DateTimeParseException, IllegalArgumentException {
        if (!publishedDate.equals("yyyy-mm-dd 00:00")) {
            publishedDateIsSet = true;
            this.publishedDate = LocalDateTime.parse(publishedDate, FORMATTER);
            validate();
        } else {
            this.publishedDateIsSet = false;
            this.publishedDate = null;
        }
    }

    public String getPublishedDate() {
        if (publishedDateIsSet) {
            return FORMATTER.format(publishedDate);
        } else {
            return null;
        }
    }

    private void validate() throws IllegalArgumentException {
        if (publishedDateIsSet && startDateIsSet) {
            if (publishedDate.isAfter(startDate)) {
                throw new IllegalArgumentException("Publish date can't be after start date");
            }
        }

        if (startDateIsSet && endDateIsSet) {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date can't be after end date");
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AssignmentDateIntervalls that = (AssignmentDateIntervalls) o;

        if (startDateIsSet != that.startDateIsSet) return false;
        if (endDateIsSet != that.endDateIsSet) return false;
        if (publishedDateIsSet != that.publishedDateIsSet) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null)
            return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null)
            return false;
        return publishedDate != null ? publishedDate.equals(that.publishedDate) : that.publishedDate == null;

    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (startDateIsSet ? 1 : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (endDateIsSet ? 1 : 0);
        result = 31 * result + (publishedDate != null ? publishedDate.hashCode() : 0);
        result = 31 * result + (publishedDateIsSet ? 1 : 0);
        return result;
    }
}
