(function ($) {
$(document).ready(function () {
    const buttonAdd = $("#addButton");
    buttonAdd.on('click',
        async function () {
            let user = {
                firstName: document.getElementById("formGroupExampleInput").value,
                lastName: document.getElementById("formGroupExampleInput2").value,
                age: document.getElementById("formGroupExampleInput3").value,
                username: document.getElementById("formGroupExampleInput4").value,
                password: document.getElementById("formGroupExampleInput5").value,
                roles: [ document.getElementById("exampleFormControlSelect2").value ]
            }

            $.ajax("/users", {
                method: "post",
                data: JSON.stringify(user),
                dataType: "json",
                contentType: "application/json",
                success: function () {
                    alert("CREATED");
                    const myNode = document.getElementById("usersTable");
                    while (myNode.firstChild) {
                        myNode.removeChild(myNode.lastChild);
                    }
                    updatePage();
                    document.getElementById("allUsers-tab").click();
                }
            })
        });

    function updatePage() {
        $.ajax("/users", {
            dataType: "json",
            success: function (msg) {
                let users = msg;
                const div = $("#usersTable");
                for (let i = 0; i < users.length; i++) {
                    let table = $("<tr></tr>");
                    table.attr("id", users[i].id);
                    $("<td></td>").text(users[i].id).appendTo(table);
                    $("<td></td>").text(users[i].firstName).appendTo(table);
                    $("<td></td>").text(users[i].lastName).appendTo(table);
                    $("<td></td>").text(users[i].age).appendTo(table);
                    $("<td></td>").text(users[i].username).appendTo(table);
                    $("<td></td>").text(users[i].allRoles).appendTo(table);
                    let btn1 = $("<td></td>").appendTo(table);
                    $("<button></button>").addClass("btn btn-info text-white").val(users[i].id)
                        .text("Edit").on("click",
                            async function (event) {
                                event.preventDefault();
                                let id = $(this).attr("value");
                                let user = await getUser(id);

                                $('.myForm #updateGroupInputID').val(user.id);
                                $('.myForm #updateGroupInput').val(user.firstName);
                                $('.myForm #updateGroupInput2').val(user.lastName);
                                $('.myForm #updateGroupInput3').val(user.age);
                                $('.myForm #updateGroupInput4').val(user.username);
                                $('.myForm #updateGroupInput5').val(user.password);

                                $('.myForm #exampleModal').modal();
                            }
                    ).appendTo(btn1);
                    let btn2 = $("<td></td>").appendTo(table);
                    $("<button></button>").addClass("btn btn-danger text-white").val(users[i].id)
                        .text("Delete").on("click",
                        async function (event) {
                            event.preventDefault();
                            let id = $(this).attr("value");
                            let user = await getUser(id);

                            $('.myForm2 #deleteGroupInputID').val(user.id);
                            $('.myForm2 #deleteGroupInput').val(user.firstName);
                            $('.myForm2 #deleteGroupInput2').val(user.lastName);
                            $('.myForm2 #deleteGroupInput3').val(user.age);
                            $('.myForm2 #deleteGroupInput4').val(user.username);
                            $('.myForm2 #deleteBtn').val(user.id);
                            $('.myForm2 #exampleModal2').modal();
                        }
                    )
                        .appendTo(btn2);
                    table.appendTo(div);
                }
            }
        })
    }

    async function getUser(id){
        let value = "lox";
        await $.ajax("/users/" + id, {
            dataType: "json",
            success: function (msg) {
                value = msg;
            }
        })
        return value;
    }

    const buttonEdit = $("#editBtn");
    buttonEdit.click(
        function () {
            let role = document.getElementById("updateGroupInputSelect2").value;
            if(role == null || role == "") role = "none";

            let user = {
                id: document.getElementById("updateGroupInputID").value,
                firstName: document.getElementById("updateGroupInput").value,
                lastName: document.getElementById("updateGroupInput2").value,
                age: document.getElementById("updateGroupInput3").value,
                username: document.getElementById("updateGroupInput4").value,
                password: document.getElementById("updateGroupInput5").value,
                roles: [ role ],
            }

            $.ajax("/users", {
                method: "put",
                data: JSON.stringify(user),
                dataType: "json",
                contentType: "application/json",
                success: function () {
                    const myNode = document.getElementById("usersTable");
                    while (myNode.firstChild) {
                        myNode.removeChild(myNode.lastChild);
                    }
                    updatePage();
                    document.getElementById("allUsers-tab").click();
                }
            })
        }
    )

    const buttonDelete = $("#deleteBtn");
    buttonDelete.click(
        function () {
            $.ajax("/users/" + $(this).attr("value"), {
                method: "DELETE",
                dataType: "text",
                success: function (msg) {
                    $("#usersTable")
                        .find("#" + msg)
                        .remove();

                }
            })
        }
    )

    updatePage();

    });

})(jQuery);