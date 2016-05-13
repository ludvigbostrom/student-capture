/**
 * Back button for teacher feedback
 * @author: dv13trm, c14gls, group 6
 */


/**
 * Creates the back button used by the GUI.
 * Makes it possible for the teacher to go back to the previous page.
 */
//Creates the BackButton
var BackButton = React.createClass({
    // Upon clicking on the button, it should go back to the previous GUI window.
    onClick: function() {

        /**
         * TODO: Write code that takes the user back to the previous page.
         */
    },
    // Render function for BackButton
    render: function() {

        return(
            <button id="backbutton" onClick={this.onClick}>Back</button>

        );
    }
});

window.BackButton = BackButton;