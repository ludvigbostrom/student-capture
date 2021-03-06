/**
 * Comment area for teacher feedback, 
 * this is the text teachers will give students when giving feedback.
 * @author: dv13trm, c14gls, group 6
 */


var hasEdited = false;
/**
 * Creates the comment box used by the GUI.
 * The comment box is used by the teacher to submit feedback to the student.
 */
var CommentBox = React.createClass({
    /**
     * Set initial state for text area
     * @returns {{currentValue: *}} returning text area to be empty.
     */
    getInitialState: function () {
        return {currentValue: this.props.children};
    },
    /**
     * Set value to submitted text in text area
     * @param event text that is entered will set value to this.
     */
    handleChange: function(event) {
        this.setState({value: event.target.value});
    },
    /**
     * onclick function for comment box.
     * @param event text areas event.
     */
    onClick: function (event) {
        hasEdited=true;
    },
    /**
     * Render function for comment box.
     * @returns {XML} A text area.
     */
    render: function () {
        return (
            <textarea placeholder="Write comments regarding feedback here..." id="teachercomments"
                      value={this.state.value} onChange={this.handleChange} onClick={this.onClick} />
        );
    }
});
window.CommentBox=CommentBox;