// import { HOST, PORT } from "../Globuls.js"
const HOST = "http://localhost:"
const PORT = "8080"

{
    let list = []

    function fillList() {
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: HOST + PORT + "/users",
            dataType: "json",
            complete: function (data, statusText) {
                console.log(data.responseJSON);
                if (data.statusText == "OK") {
                    list = data.responseJSON;
                    htmlCode = "";
                    for (i = 0; i < list.length; ++i) {
                        console.log(data.responseJSON[i].type);

                        htmlCode += "<li> <input id = 'checkbox" + i + "'type = 'checkbox'>" + list[i].name + "</li>";

                    }
                    $("#listOfPapers").html(htmlCode);
                }
                else {
                    alert(" can not get list of papers");
                }
            }

        });
    }

    function checkBoxes() {

        for (i = 0; i < list.length; ++i) {
            if (document.getElementById("checkbox" + i).checked) {
                console.log(list[i]);
                $.ajax({
                    type: "PUT",
                    contentType: "application/json",
                    url: HOST + PORT + "/users",
                    dataType: "json",
                    data: JSON.stringify({
                        userId: list[i].id,
                        type: document.getElementById("roleSelector").value,
                        validated: document.getElementById("validCheckbox").checked
                    }),
                    complete: function (dataPut, statusText) {
                        if (dataPut == "OK") {
                            $("#message").text("it worked.");
                        }
                    }
                });
            }
        }
    }


    $(document).ready(function () {
        fillList();
        $("#submitButton").click(function (e) {
            e.preventDefault();

            checkBoxes();
        });
    });
}