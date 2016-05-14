package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import studentcapture.model.Assignment;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Created by E&S on 4/26/16.
 */
@Repository
public class AssignmentDAO {

    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;


    /**
     * Inserts an assignment into the database.
     *
     * @param courseID        the courseID existing in the course-table
     * @param assignmentTitle the title to the assignment
     * @param startDate       the startdate of the assignment, should be on
     *                        format "YYYY-MM-DD HH:MI:SS"
     * @param endDate         the enddate of the assignment, should be on
     *                        format "YYYY-MM-DD HH:MI:SS"
     * @param minTime         minimum time for the assignment, in seconds
     * @param maxTime         maximum time for the assignment, in seconds
     * @param published       true if the assignment should be published
     * @return the generated AssignmentID
     * @throws IllegalArgumentException fails if startDate or endDate is not
     *                        in the right format
     */
    public int createAssignment(String courseID, String assignmentTitle,
                                String startDate, String endDate,
                                int minTime, int maxTime, String published, String gradeScale)
    throws IllegalArgumentException {
        LocalDateTime startDateTime, endDateTime, publishedDate;

        //Check dates
        startDateTime = convertStringToLDTFormat(startDate, "startDate is not in format" +
                " YYYY-MM-DD HH:MI:SS");

        endDateTime = convertStringToLDTFormat(endDate, "endDate is not in " +
                "format YYYY-MM-DD HH:MI:SS");

        checkIfTime1IsBeforeTime2(startDateTime, endDateTime, "Start date" +
                " must be before the end date");

        if (published != null) {
            LocalDateTime currentDate = LocalDateTime.now();

            publishedDate = convertStringToLDTFormat(published, "published is" +
                    " not in format YYYY-MM-DD HH:MI:SS");

            checkIfTime1IsBeforeTime2(currentDate, publishedDate, "Published" +
                    " date must be after the current date");
        }

        // Check time
        validateMinMaxTime(minTime, maxTime);

        // Construct query, depends on if assignment has publishdate or not.
        String insertQueryString = getQueryString(published);

        // Execute query and fetch generated AssignmentID
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps =
                            connection.prepareStatement(insertQueryString,
                                    Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, courseID);
                    ps.setString(2, assignmentTitle);
                    ps.setString(3, startDate);
                    ps.setString(4, endDate);
                    ps.setInt(5, minTime);
                    ps.setInt(6, maxTime);
                    ps.setString(7, published);
                    ps.setString(8, gradeScale);
                    return ps;
                },
                keyHolder);

        // Return generated AssignmentID
        return keyHolder.getKey().intValue();
    }

    private LocalDateTime convertStringToLDTFormat(String timeDateString,
                                              String errorMessage)
            throws IllegalArgumentException {
        LocalDateTime dateTime;

        try {
            dateTime = checkIfCorrectDateTimeFormat(timeDateString);
        } catch (DateTimeParseException e){
            throw new IllegalArgumentException(errorMessage);
        }

        return dateTime;
    }

    private LocalDateTime checkIfCorrectDateTimeFormat(String dateTime)
            throws DateTimeParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, formatter);
    }

    private void checkIfTime1IsBeforeTime2(LocalDateTime t1, LocalDateTime t2,
                                     String errorMessage)
            throws IllegalArgumentException {
        if (t2.isBefore(t1)){
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private String getQueryString(String published){
        String insertQueryString;

        if(published == null) {
            insertQueryString = "INSERT INTO Assignment (AssignmentID, " +
                    "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                    "Published, GradeScale) VALUES (DEFAULT ,?,?, " +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'), " +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'),?,?,?,?);";
        } else {
            insertQueryString = "INSERT INTO Assignment (AssignmentID, " +
                    "CourseID, Title, StartDate, EndDate, MinTime, MaxTime, " +
                    "Published, GradeScale) VALUES (DEFAULT ,?,?, " +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'), " +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'),?,?," +
                    "to_timestamp(?, 'YYYY-MM-DD HH:MI:SS'),?);";
        }
        return insertQueryString;
    }

    private void validateMinMaxTime(int minTime, int maxTime)
            throws IllegalArgumentException{
        if (minTime >= maxTime) {
            throw new IllegalArgumentException("minTime must be less than " +
                    "maxTime");
        }
        if (minTime < 0) {
            throw new IllegalArgumentException("minTime must be greater or " +
                    "equal to 0");
        }
        if (maxTime <= 0) {
            throw new IllegalArgumentException("maxTime must be greater " +
                    "than 0");
        }
    }

    /**
     * Used to verify if a given date is in the right format.
     *
     * @param format The format to check against.
     * @param value The date to check.
     * @return True if the date follows the format, false if not.
     */
    /*private static boolean isValidDateFormat(String format, String value)
            throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = sdf.parse(value);

        return value.equals(sdf.format(date));
    }
*/
    /**
     * Fetches info about an assignment from the database.
     * @param assignmentID Unique identifier for an assignment.
     * @return A list containing information about the assignment.
     *      The list is on the form [course ID, assignment title, opening datetime, closing datetime, minimum video time, maximum video time]
     */
    public Assignment getAssignmentInfo(int assignmentID){
        ArrayList<String> returnValues = new ArrayList<>();

        // Construct query
        String columns[] = {"courseid", "title", "startdate", "enddate", "mintime", "maxtime"};
        String tempVal;
        String query;

        // Execute query
        try {
            for (String c : columns) {
                query = "SELECT " + c + " FROM assignment WHERE (assignmentid = ?);";
                tempVal = jdbcTemplate.queryForObject(query, new Object[]{assignmentID}, String.class);

                if (tempVal == null) {
                    tempVal = "Missing value";
                } else {
                    tempVal = tempVal.trim();
                }
                returnValues.add(tempVal);
            }
        } catch (IncorrectResultSizeDataAccessException up) {
            throw up;
        } catch (DataAccessException down) {
            throw down;
        }
        // Format results
        Assignment assignment = new Assignment();
        assignment.setCourseID(returnValues.get(0));
        assignment.setTitle(returnValues.get(1));
        assignment.setStartDate(new Timestamp(System.currentTimeMillis()));
        assignment.setEndDate(new Timestamp(System.currentTimeMillis()));
        assignment.setMinTime(Integer.parseInt(returnValues.get(4)));
        assignment.setMaxTime(Integer.parseInt(returnValues.get(5)));

        return assignment;
    }

	public String getAssignmentID(String courseID,String assignmentTitle){
    	String sql = "SELECT assignmentID from Assignment WHERE courseID = ? AND Title = ?";
    	return jdbcTemplate.queryForObject(sql, new Object[]{courseID,assignmentTitle},String.class);
	}

    public String getCourseIDForAssignment(String assignmentID) {
        String sql = "SELECT courseID from Assignment WHERE assignmentID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{assignmentID},String.class);
    }

    /**
     * Returns a sought assignment from the database.
     * 
     * @param assignmentId		assignments identifier
     * @return					sought assignment
     * 
     * @author tfy12hsm
     */
	public Optional<Assignment> getAssignment(int assignmentId) {
		try {
            String getAssignmentStatement = "SELECT * FROM "
                    + "Assignment WHERE AssignmentId=?";

			Map<String, Object> map = jdbcTemplate.queryForMap(
	    			getAssignmentStatement, assignmentId);
			Assignment result = new Assignment(map);
	    	
	    	return Optional.of(result);
		} catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}
	}
    
    public boolean updateAssignment(String assignmentID, String assignmentTitle,
                                    String startDate, String endDate, int minTime, int maxTime,
                                    boolean published){
        throw new UnsupportedOperationException();
    }

    public boolean removeAssignment(String assignmentID){
        throw new UnsupportedOperationException();
    }
}