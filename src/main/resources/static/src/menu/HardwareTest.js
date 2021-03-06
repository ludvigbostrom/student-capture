/**
 * @author Ludvig Boström, c13lbm
 */


var HardwareTest = React.createClass({


    /**
     * Build formdata to send to server.
     * @param blob
     * @param fileName
     * @returns {*}
     */
    formDataBuilder: function (blob, fileName) {
        var fd = new FormData();
        fd.append("videoName", fileName);
        fd.append("video", blob);
        fd.append("text","dsvfdsfds");
        return fd;
    },
    /**
     * Callback function when video is returned from server.
     * @param fName
     */
    playVideo: function (fName) {
        

        var container = document.getElementById("tst");
        if (container.childNodes.length > 1) {
            container.removeChild(container.childNodes.item(1));
        }

        var mediaElement = document.createElement("video");
        mediaElement.setAttribute("id","you-id");

        var source = document.createElement('source');
        source.src = "data:video/webm;base64," + fName;

        source.type = 'video/webm; codecs="vp8, vorbis"';


        mediaElement.appendChild(source);
        mediaElement.controls = true;
        container.appendChild(mediaElement)
        mediaElement.play();

    },

    /**
     * Calculate upload speed.
     * @param blobsize
     * @param sendTime
     */
    calcSpeed: function (blobsize, sendTime) {

        $("#internet-speed").text(function () {
            var now = Date.now();
            var mbsec = (blobsize / ((now - sendTime) / 1000));
            //console.log("mbsec " + mbsec + "now = " + now/1000 + "stime" + sendTime/1000);
            return "Upload speed = " + mbsec.toFixed(2) + "MB/s"
        });
    },
    /**
     * Render modal content.
     * @returns {XML}
     */
    render: function () {

        return (
            <div>
                <h3>Equipment testing</h3>
                <div className="row" id="">
                    <div className="six columns" id="rec-test-container"><h5 color="black">Recording you</h5>
                        <Recorder playCallback={this.playVideo} calc={this.calcSpeed}
                                  postURL="equipmenttest" formDataBuilder={this.formDataBuilder}
                                  recButtonID="record-test" stopButtonID="stop-test" fileName="testVid.webm"
                                  contID="test-prev-container"
                        />

                    </div>
                    <div id="tst" className="six columns "><h5>The recording from server</h5>

                    </div>
                </div>
                <div className="row">
                    <p id="internet-speed"></p>
                </div>
                <div className="row">
                    <div className="four columns u-pull-left">
                        <div id="record-test" className="recControls button primary-button SCButton">Record</div>

                        <div id="stop-test" className="recControls button primary-button SCButton" disabled>Stop</div>
                        
                    </div>
                    <div className="two columns u-pull-right">
                        <div className="md-close button primary-button SCButton">Close</div>
                    </div>
                </div>
            </div>
        );
    }
});

window.HardwareTest = HardwareTest;