package studentcapture.submission;

import studentcapture.model.Grade;

import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
import java.sql.Timestamp;
import java.util.Map;

/**
 * Created by c13arm on 2016-05-11.
 */

public class Submission {
    @NotNull
    private Integer assignmentID;
    @NotNull
    private Integer studentID;
    private Boolean studentPublishConsent = false;
    private Timestamp submissionDate;
    private Grade grade;
    private Boolean publishStudentSubmission = false;
    private String courseID;
    private String courseCode;
    private String feedback;
    private Status subStatus;
    private String firstName;
    private String lastName;
    private String status;
    private String teacherName;
    private MultipartFile studentVideo;
    private MultipartFile feedbackVideo;

    public Submission(int studentID, int assignmentID) {
        this.studentID = studentID;
        this.assignmentID = assignmentID;
    }

    public Submission() {
    }

    /**
     * Constructor that parses map of database elements.
     *
     * @param map map retrieved from database
     * @author tfy12hsm
     */
    public Submission(Map<String, Object> map) {
        // These three variables (assignmentID, studentID, submissionDate) cannot be null.
        assignmentID = (Integer) map.get("AssignmentId");
        studentID = (Integer) map.get("StudentId");
        submissionDate = (Timestamp) map.get("SubmissionDate");

        try {
            firstName = (String) map.get("FirstName"); // Upper/lower-case doesn't matter
        } catch (NullPointerException e) {
            firstName = null;
        }

        try {
            lastName = (String) map.get("LastName");
        } catch (NullPointerException e) {
            lastName = null;
        }

        try {
            studentPublishConsent = (Boolean) map.get("StudentPublishConsent");
        } catch (NullPointerException e) {
            studentPublishConsent = null;
        }

        try {
            status = (String) map.get("Status");
        } catch (NullPointerException e) {
            status = null;
        }
        try {
            publishStudentSubmission = (Boolean) map.get("PublishStudentSubmission");
        } catch (NullPointerException e) {
            publishStudentSubmission = null;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MultipartFile getStudentVideo() {
        return studentVideo;
    }

    public void setStudentVideo(MultipartFile studentVideo) {
        this.studentVideo = studentVideo;
    }

    public MultipartFile getFeedbackVideo() {
        return feedbackVideo;
    }

    public void setFeedbackVideo(MultipartFile feedbackVideo) {
        this.feedbackVideo = feedbackVideo;
    }

    //A submission must have one of these statuses
    public enum Status {
        ANSWER("Answer"),
        NOANSWER("NoAnswer"),
        BLANK("Blank");

        Status(String status) {

        }
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Grade getGrade() {
        return grade;
    }

    public void studentAllowsPublication(boolean studentPublishConsent) {
        this.studentPublishConsent = studentPublishConsent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Timestamp getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Timestamp submissionDate) {
        this.submissionDate = submissionDate;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public int getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(int assignmentID) {
        this.assignmentID = assignmentID;
    }

    public Boolean getStudentPublishConsent() {
        return studentPublishConsent;
    }

    public void setStudentPublishConsent(boolean studentPublishConsent) {
        this.studentPublishConsent = studentPublishConsent;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Status getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(Status subStatus) {
        this.subStatus = subStatus;
    }

    public void setPublishStudentSubmission(Boolean publishStudentSubmission) {
        this.publishStudentSubmission = publishStudentSubmission;
    }


    public Boolean getPublishStudentSubmission() {
        return publishStudentSubmission;
    }

    public Boolean getFeedbackIsVisible() {
        return grade.getFeedbackIsVisible();
    }

    public void setFeedbackIsVisible(Boolean publishFeedback) {
        grade.setFeedbackIsVisible(publishFeedback);
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }


    @Override
    public String toString() {
        return "Submission{" +
                "\n\tassignmentID=" + assignmentID +
                ", \n\tstudentID=" + studentID +
                ", \n\tstudentPublishConsent=" + studentPublishConsent +
                ", \n\tsubmissionDate=" + submissionDate +
                ", \n\tgrade=" + grade +
                ", \n\tteacherID=" + grade.getTeacherID() +
                ", \n\tpublishStudentSubmission=" + publishStudentSubmission +
                ", \n\tcourseID='" + courseID + '\'' +
                ", \n\tcourseCode='" + courseCode + '\'' +
                ", \n\tfeedback='" + feedback + '\'' +
                ", \n\tsubStatus=" + subStatus +
                ", \n\tfirstName='" + firstName + '\'' +
                ", \n\tlastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Submission that = (Submission) o;

        if (assignmentID != null ? !assignmentID.equals(that.assignmentID) : that.assignmentID != null) return false;
        if (studentID != null ? !studentID.equals(that.studentID) : that.studentID != null) return false;
        if (studentPublishConsent != null ? !studentPublishConsent.equals(that.studentPublishConsent) : that.studentPublishConsent != null)
            return false;
        if (submissionDate != null ? !submissionDate.equals(that.submissionDate) : that.submissionDate != null)
            return false;
        if (grade.getGrade() != null ? !grade.getGrade().equals(that.grade.getGrade()) : that.grade.getGrade() != null) return false;
        if (grade.getTeacherID() != null ? !grade.getTeacherID().equals(that.grade.getTeacherID()) : that.grade.getTeacherID() != null) return false;
        if (publishStudentSubmission != null ? !publishStudentSubmission.equals(that.publishStudentSubmission) : that.publishStudentSubmission != null)
            return false;
        if (courseID != null ? !courseID.equals(that.courseID) : that.courseID != null) return false;
        if (courseCode != null ? !courseCode.equals(that.courseCode) : that.courseCode != null) return false;
        if (feedback != null ? !feedback.equals(that.feedback) : that.feedback != null) return false;

        if (subStatus != that.subStatus) return false;
        if (firstName != null ? !firstName.equals(that.firstName) : that.firstName != null) return false;
        if (lastName != null ? !lastName.equals(that.lastName) : that.lastName != null) return false;
        if (teacherName != null ? !teacherName.equals(that.teacherName) : that.teacherName != null) return false;
        return status != null ? status.equals(that.status) : that.status == null;

    }

}
