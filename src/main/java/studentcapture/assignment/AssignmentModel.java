package studentcapture.assignment;

import java.sql.Timestamp;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Created by David Björkstrand on 4/25/16.
 * This class is the model for a assignment. I.e. it contains all data needed for an assignment.
 * Add more fields if needed.
 */
public class AssignmentModel {
	private Integer assignmentID;
    private String courseID;
    private String title;
    private String info;
    private AssignmentVideoIntervall videoIntervall;
    private AssignmentDateIntervalls assignmentIntervall;
    private GradeScale scale;
    private String recap;
    private Timestamp published;
    private String description;

    public AssignmentModel(String title,
                           String info,
                           AssignmentVideoIntervall videoIntervall,
                           AssignmentDateIntervalls assignmentIntervall,
                           String scale,
                           String recap) throws InputMismatchException {
        this.courseID = "1200"; //should be changed.
        this.title = title;
        this.info = info;
        this.videoIntervall = videoIntervall;
        this.assignmentIntervall = assignmentIntervall;
        this.scale = GradeScale.valueOf(scale);
        this.recap = recap;
    }

    public AssignmentModel() {


    }

    public AssignmentModel(Map<String, Object> map) {
    	assignmentID = (Integer) map.get("AssignmentId");
		courseID = (String) map.get("CourseId");
		title = (String) map.get("Title");
		assignmentIntervall = new AssignmentDateIntervalls();
		assignmentIntervall.setStartDate((String) map.get("StartDate"));
		assignmentIntervall.setEndDate((String) map.get("EndDate"));
		videoIntervall = new AssignmentVideoIntervall();
		videoIntervall.setMinTimeSeconds((Integer) map.get("MinTime"));
		videoIntervall.setMaxTimeSeconds((Integer) map.get("MaxTime"));
		try {
			published = (Timestamp) map.get("Published");
		} catch (NullPointerException e) {
			published = null;
		}
		description = (String) map.get("Description");
		try {
			scale = (GradeScale) map.get("Scale");
			if(scale == null) {
				throw new NullPointerException();
			}
		} catch (Exception e) {
			scale = GradeScale.NUMBER_SCALE;
		}
	}

	public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setVideoIntervall(AssignmentVideoIntervall videoIntervall) {
        this.videoIntervall = videoIntervall;
    }

    public AssignmentVideoIntervall getVideoIntervall() {
        return videoIntervall;
    }

    public void setAssignmentIntervall(AssignmentDateIntervalls assignmentIntervall) {
        this.assignmentIntervall = assignmentIntervall;
    }

    public AssignmentDateIntervalls getAssignmentIntervall() {
        return assignmentIntervall;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getScale() {
        return scale.name();
    }

    public void setScale(String scale) {
        this.scale = GradeScale.valueOf(scale);
    }

    public String getRecap() {
        return recap;
    }

    public void setRecap(String recap) {
        this.recap = recap;
    }
}
