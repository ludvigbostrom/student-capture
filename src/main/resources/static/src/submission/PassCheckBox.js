/**
 * Created by sejiaw on 2016-05-10.
 */



/**
 * Checkbox that is used to determine if a student passed an assignment or not.
 * @type {number} current state of passed
 */
var PassCheckBox = React.createClass({
    //Sets initial state for the checkbox to false.
    getInitialState: function() {
        return {

            checkBox: this.props.checkBox || false

        };
    },
    // Render function for CheckBox
    render: function() {
        return (

                    <input id="ifStudentPass" type="hidden"
                           value="1"
                           checkBox={this.state.checkBox}
                           onClick={this.handleClick}/>
        );
    },
    //Changes from checked to unchecked and vice-versa.
    handleClick: function(e) {

        this.setState({
            checkBox: e.target.checkBox
        });

    }
});

window.PassCheckBox=PassCheckBox;
