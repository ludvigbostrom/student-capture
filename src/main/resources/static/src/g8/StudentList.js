/**
 * Created by c13lbm on 4/26/16.
 *
 * This React-component uses Submission.java and SubmissionDAO.java
 */

var StudentList = React.createClass({

    submissions: null,
    participants: null,

    // This method is called after the method "render".
    componentDidMount: function () {
        var newTableObject = document.getElementById("students-table");
        sorttable.makeSortable(newTableObject);
        var table11_Props = {
            filters_row_index: 1,
            remember_grid_values: true
        };
        setFilterGrid("students-table", table11_Props);
    },

    // Method that is called when a user clicks on a submission (a table row).
    // It creates a new interface for submission grading..
    clickhandle: function (user, event) {
        console.log(event);
        console.log(user);
        //console.log(event.currentTarget.childNodes[0].innerText)
        /*var user = {
         name: event.currentTarget.childNodes[0].innerText,
         date:event.currentTarget.childNodes[1].innerText,
         grade: event.currentTarget.childNodes[2].innerText,
         }*/
        console.log("Data: " + user.studentName + "_" + user.submissionDate + "_" + user.grade + "_" + user.assignmentID);
        window.studentName = user.studentName;
        window.assignmentID = user.assignmentID;
        window.courseID = user.courseID;
        window.teacherID = user.teacherID;
        document.getElementById("answerContainer").innerHTML = ""; //TODO: find better solution.
        ReactDOM.render(<RenderHandle />, document.getElementById("answerContainer"));
        //TODO: render other user story.
    },

    componentWillMount: function () {
        // GET request to database to get all the submissions from the students.
        $.ajax({
            url: window.globalURL + "/DB/getAllSubmissions", // URL to send to
            type: "GET", // Type of http
            async: false,
            data: {assignmentID: 1200},
            success: function(data,status) { // Function to perform when ok
                this.submissions = data;
            }.bind(this),
            error: function(xhr, status, err) {
                // Handle the error
                console.log("Error Submissions");
            }.bind(this)
        });

        // GET request to database to get all the participants in a course.
        $.ajax({
            url: window.globalURL + "/DB/getAllParticipantsFromCourse", // URL to send to
            type: "GET", // Type of http
            async: false,
            data: {courseID: 1200},
            success: function(data,status) { // Function to perform when ok
                this.participants = data;
            }.bind(this),
            error: function(xhr, status, err) {
                // Handle the error
                console.log("Error Participants");
            }.bind(this)
        });
    },

    render: function () {
        var tmp = this;
        var userList = this.submissions.map(function (user) {
            var date = new Date(user.submissionDate);
            return (
                <tr onClick={tmp.clickhandle.bind(tmp,user)}>
                    <video width="96" height="54" class="clip-thumbnail"> <source src="http://techslides.com/demos/sample-videos/small.mp4" type="video/mp4"/> </video>
                    <td>{user.firstName + " " + user.lastName}</td>
                    <td>{date.getFullYear() + "-" + (date.getMonth()+1/*Months start from 0*/) + "-" + date.getDate()}</td>
                    <td>{user.gradeSign}</td>
                </tr>
            );
            // console.log(tmp);
        });
        return (
            <div className="row">
                <div className="four columns offset-by-one">
                    <table className="u-full-width sortable" id="students-table">
                        <thead>
                        <tr >

                            <th>Student</th>
                            <th>Date</th>
                            <th>Grade</th>

                        </tr>
                        </thead>
                        <tbody>
                        {userList}
                        </tbody>
                    </table>
                </div>
                <div className="six columns" id="answerContainer">

                </div>

            </div>
        );
    }
});

window.StudentList = StudentList;