package studentcapture.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by c13gan on 2016-04-26.
 */
@Repository
public class CourseDAO {
    // This template should be used to send queries to the database
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    /**
     * Attempts to add a course to the database.
     * 
     * @author tfy12hsm
     */
    public CourseModel addCourse(CourseModel course) {
        String addCourseStatement =
                "INSERT INTO Course VALUES (?,?,?,?,?,?,?)";

        try {
            int rowsAffected = jdbcTemplate.update(addCourseStatement,
                    course.getCourseId(),
                    course.getYear(),
                    course.getTerm(),
                    course.getCourseName(),
                    course.getCourseDescription(), 
                    course.getActive());
            if (rowsAffected == 1) {
            	return getCourseWithoutID(course);
            } else {
            	return new CourseModel();
            }
        } catch (IncorrectResultSizeDataAccessException e){
            return new CourseModel();
        } catch (DataAccessException e1){
            return new CourseModel();
        }
    }

    /**
     * 
     * 
     * @param course
     * @return
     * 
     * @author tfy12hsm
     */
    public CourseModel getCourseWithoutID(CourseModel course){
    	try {
    		String sql = "SELECT CourseId from course WHERE Year=? "
        			+ "AND Term=? AND CourseName=?";
    		List<Map<String,Object>> sqlResponse = jdbcTemplate.queryForList(
    				sql, new Object[]{
        			course.getYear(),
        			course.getTerm(),
        			course.getCourseName()});
        	return new CourseModel(sqlResponse.get(sqlResponse.size() - 1));
    	} catch (IncorrectResultSizeDataAccessException e) {
			return new CourseModel();
		} catch (DataAccessException e1) {
			return new CourseModel();
		}
    }
    
    /**
     * Attempts to retrieve all data regarding a course from the database.
     *
     * @author tfy12hsm
     */
	public CourseModel getCourse(CourseModel course) {
		try {
            String getCourseStatement =
                    "SELECT * FROM Course WHERE CourseId=?";
            Map<String, Object> map = jdbcTemplate.queryForMap(
        			getCourseStatement, course.getCourseId());
            return new CourseModel(map);		
		} catch (IncorrectResultSizeDataAccessException e) {
			return new CourseModel();
		} catch (DataAccessException e1) {
			return new CourseModel();
		}
	}
	
	/**
     * Attempts to retrieve all data regarding a course from the database.
     *
     * @param courseID	target courses database identification
     * @return			sought after course
     * 
     * @author tfy12hsm
     */
	public CourseModel getCourse(String courseId) {
		try {
            String getCourseStatement =
                    "SELECT * FROM Course WHERE CourseId=?";
            Map<String, Object> map = jdbcTemplate.queryForMap(
        			getCourseStatement, courseId);
            return new CourseModel(map);		
		} catch (IncorrectResultSizeDataAccessException e) {
			return new CourseModel();
		} catch (DataAccessException e1) {
			return new CourseModel();
		}
	}

	/**
	 * 
	 * 
	 * @param course
	 * @return
	 * 
	 * @author tfy12hsm
	 */
	public CourseModel updateCourse(CourseModel course) {
		String changeDescriptionOnCourseStatement = "UPDATE Course SET "
				+ "Year=?,Term=?,CourseName=?,CourseDescription=?,Active=? "
				+ "WHERE CourseId=?";
		try {
            int rowsAffected = jdbcTemplate.update(
            		changeDescriptionOnCourseStatement, 
            		course.getYear(),
            		course.getTerm(),
            		course.getCourseName(),
            		course.getCourseDescription(),
            		course.getActive(),
            		course.getCourseId());
            if (rowsAffected == 1) {
            	return course;
            } else {
            	return new CourseModel();
            }
        } catch (IncorrectResultSizeDataAccessException e){
            return new CourseModel();
        } catch (DataAccessException e1){
            return new CourseModel();
        }
	}
	
    /**
     * Attempts to remove a course from the database.
     *
     * @param courseID	courses database identification
     * @return			true if successful, else false
     * 
     * @author tfy12hsm
     */
    public CourseModel removeCourse(CourseModel course) {
        String removeCourseStatement = "DELETE FROM Course WHERE CourseID=?";

        try {
            int rowsAffected = jdbcTemplate.update(removeCourseStatement,
                    course.getCourseId());
            
            if(rowsAffected == 1) {
            	return course;
            } 
            return new CourseModel();
        } catch (IncorrectResultSizeDataAccessException e){
           	return new CourseModel();
        } catch (DataAccessException e1){
            return new CourseModel();
        }
    }
    
    public CourseModel removeCourse(String courseId) {
        String removeCourseStatement = "DELETE FROM Course WHERE CourseID=?";

        try {
        	CourseModel course = getCourse(courseId);
            int rowsAffected = jdbcTemplate.update(removeCourseStatement,
                    courseId);         
            if(rowsAffected == 1) {
            	return course;
            } 
            return new CourseModel();
        } catch (IncorrectResultSizeDataAccessException e){
           	return new CourseModel();
        } catch (DataAccessException e1){
            return new CourseModel();
        }
    }

    /**
     * Fetches the code for a course.
     * Useful when constructing a file path.
     * @param courseId Unique identifier for the course
     * @return course code
     */
    public String getCourseCodeFromId(CourseModel course){
        String query = "SELECT coursecode FROM Course WHERE courseid = ?;";
        String courseCode;

        try {
            courseCode = jdbcTemplate.queryForObject(query, new Object[]{course.getCourseId()}, String.class);
            	
            if (courseCode == null) {
                courseCode = "Missing value";
            } else {
                courseCode = courseCode.trim();
            }
        } catch (IncorrectResultSizeDataAccessException up) {
            throw up;
        } catch (DataAccessException down) {
            throw down;
        }

        return courseCode;
    }
}
