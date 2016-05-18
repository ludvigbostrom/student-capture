package studentcapture.datalayer.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import studentcapture.model.Participant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by c13gan on 2016-04-26.
 */
@Repository
public class ParticipantDAO {

    // This template should be used to send queries to the database
	@Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Add a new participant to a course by connecting the tables "User" and "Course" in the database.
     *
     * @param userID        unique identifier for a person
     * @param courseId      unique identifier, registration code
     * @param function      student, teacher, ....
     * @return              true if insertion worked, else false
     * 
     * @author tfy12hsm
     */
    public boolean addParticipant(String userID, String courseID, String function) {
    	String addParticipantStatement = "INSERT INTO Participant VALUES (?,?,?)";
        boolean result;
        int userId = Integer.parseInt(userID);
        int courseId = Integer.parseInt(courseID);
        try {
            int rowsAffected = jdbcTemplate.update(addParticipantStatement,
                    userId, courseId, function);
            result = rowsAffected == 1;
        } catch (IncorrectResultSizeDataAccessException e){
            result = false;
        } catch (DataAccessException e1){
            result = false;
        }

        return result;
    }


    /**
     * Get the role for a participant in a selected course
     *
     * @param userID        unique identifier for a person
     * @param courseId      unique identifier, registration code
     * @return              role of a person
     * 
     * @author tfy12hsm
     */
    public Optional<String> getFunctionForParticipant(String userID, String courseID) {
    	String result = null;
        int userId = Integer.parseInt(userID);
        int courseId = Integer.parseInt(courseID);
        String getFunctionForParticipantStatement =
                "SELECT Function FROM Participant WHERE (UserId=? AND CourseId=?)";

        try {
    		result = jdbcTemplate.queryForObject(
    				getFunctionForParticipantStatement, new Object[]
    						{userId, courseId},
                    String.class);
    	} catch (IncorrectResultSizeDataAccessException e){
            //TODO
        } catch (DataAccessException e1){
        	//TODO
        }

        return Optional.of(result);
    }


    /**
     * Returns a list of all participants of a course including their function
     *
     * @param courseId      unique identifier, registration code
     * @return              List of tuples: CAS_ID - function
     * 
     * @author tfy12hsm
     */
    public Optional<List<Participant>> getAllParticipantsFromCourse(String courseID){
    	List<Participant> participants = new ArrayList<>();
    	int courseId = Integer.parseInt(courseID);
        String getAllParticipantFromCourseStatement =
                "SELECT * FROM Participant WHERE (CourseId=?)";

		try {
			List<Map<String, Object>> rows = jdbcTemplate.queryForList(
					getAllParticipantFromCourseStatement, courseId);
			for (Map<String, Object> row : rows) {
				Participant participant = new Participant(row);
				participants.add(participant);
			}

		} catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}

        return Optional.of(participants);
    }

    /**
     * Returns a list of all courses a person is registered for, including their function
     *
     * @param userID        unique identifier for a person
     * @return              List of tuples: CourseID - function
     * 
     * @author tfy12hsm
     */
    public Optional<List<Participant>> getAllCoursesIDsForParticipant(String userID) {
    	List<Participant> participants = new ArrayList<>();
    	int userId = Integer.parseInt(userID);

        String getAllCoursesForParticipantStatement =
                "SELECT * FROM Participant WHERE (UserId=?)";

    	try {
	    	List<Map<String, Object>> rows = jdbcTemplate.queryForList(
	    			getAllCoursesForParticipantStatement, userId);
	    	for (Map<String, Object> row : rows) {
	    		Participant participant = new Participant(row);
	    		participants.add(participant);
	    	}

	    } catch (IncorrectResultSizeDataAccessException e){
			//TODO
		    return Optional.empty();
		} catch (DataAccessException e1){
			//TODO
			return Optional.empty();
		}

        return Optional.of(participants);
    }


    /**
     * Removes the connection between a person and a course from the database
     *
     * @param userID        unique identifier for a person
     * @param courseId      unique identifier, registration code
     * @return              true if removal worked, else false
     * 
     * @author tfy12hsm
     */
    public boolean removeParticipant(String userID, String courseID){
    	boolean result;
    	int userId = Integer.parseInt(userID);
    	int courseId = Integer.parseInt(courseID);
        String removeParticipantStatement =
                "DELETE FROM Participant WHERE (UserId=? AND CourseId=?)";

        try {
            int rowsAffected = jdbcTemplate.update(removeParticipantStatement,
                    userId, courseId);
            result = rowsAffected == 1;
        } catch (IncorrectResultSizeDataAccessException e){
            result = false;
        } catch (DataAccessException e1){
            result = false;
        }
        return result;
    }
}
