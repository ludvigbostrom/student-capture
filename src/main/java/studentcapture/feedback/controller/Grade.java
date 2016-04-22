package studentcapture.feedback.controller;

/**
 * Created by c12osn on 2016-04-21.
 *
 * Contains a grade, meaning a score and a feedback.
 */
public class Grade {
    private final String score;
    private final String feedback;

    public Grade(String grade, String feedback){
        this.score = grade;
        this.feedback = feedback;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Grade grade = (Grade) o;

        if (grade != null ? !grade.equals(grade.score) : grade.score != null) return false;
        return feedback != null ? feedback.equals(grade.feedback) : grade.feedback == null;

    }

    @Override
    public int hashCode() {
        int result = score != null ? score.hashCode() : 0;
        result = 31 * result + (feedback != null ? feedback.hashCode() : 0);
        return result;
    }



}
