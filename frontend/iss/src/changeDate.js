import {Globals} from "./Globals"

$(document).ready(function () {
    $.get(Globals.host + Globals.portURL + "/conference", function(data) {
        $("#conferenceDetails").html(data)
    });


    $("#changeDateConference").click(function () {
        $.post(Globals.host + Globals.portURL + "/changeDate", {
            submissionDeadline: $("#changeConferenceSubmissionDeadline").val(),
            proposalDeadline: $("#changeConferenceProposalsDeadline").val(),
            biddingDeadline: $("#changeCnferenceBiddingDealine").val(),
        })
            .done(function () {
                //Nu stiu exact aici cum vine faza, dar for now...
                window.location = "ADD HOME LINK HERE";
            })
            .fail(function (error) {
                $("input[type=text]").val = error.responseText;
            });
    });
});