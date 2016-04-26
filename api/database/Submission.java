import java.util.Date;

import java.util.List;

public class submission {

    /**
     * Add a new submission for an assignment
     * @param assID Unique identifier for the assignment we're submitting to
     * @param studentID Unique identifier for the student submitting
     * @param date Date of the submission
     * @return True if everything went well, otherwise false
     */

    protected boolean addSubmission(String assID, String studentID, Date date) {

    }

    /**
     * Add a grade for a submission
     * @param assID Unique identifier for the assignment with the submission being graded
     * @param teacherID Unique identifier for the teacher grading
     * @param studentID Unique identifier for the student being graded
     * @param grade The grade of the submission
     * @param date Date of the grading
     * @return True if everything went well, otherwise false
     */

    protected boolean gradeSubmission(String assID, String teacherID, String studentID, String grade, Date date) {

    }

    /**
     * Remove a submission
     * @param assID Unique identifier for the assignment with the submission being removed
     * @param studentID Unique identifier for the student whose submission is removed
     * @return True if everything went well, otherwise false
     */

    protected boolean removeSubmission(String assID, String studentID) {

    }

    /**
     * Changes the grade of a submission
     * @param assID Unique identifier for the assignment with the submission being graded
     * @param teacherID Unique identifier of the teacher updating
     * @param studentID Unique identifier for the student
     * @param grade The new grade of the submission
     * @param date The date the grade was updated
     * @return True if everything went well, otherwise false
     */

    protected boolean updateGrade(String assID, String teacherID, String studentID, String grade, Date date) {

    }

    /**
     * Get information about the grade of a submission
     * @param assID Unique identifier for the assignment submission grade bra
     * @param studentID Unique identifier for the student associated with the submission
     * @return A list containing the grade, date, and grader
     */

    protected List<Object> getGrade(String assID, String studentID) {

    }

    /**
     * Get all ungraded submissions for an assignment
     * @param assID The assignment to get submissions for
     * @return A list of ungraded submissions for the assignment
     */

    protected List<Object> getAllUngraded(String assID) {

    }

    /**
     * Get all submissions for an assignment
     * @param assID The assignment to get submissions for
     * @return A list of submissions for the assignment
     */

    protected List<Object> getAllSubmissions(String assID) {

    }

}