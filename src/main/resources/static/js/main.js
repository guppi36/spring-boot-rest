(function ($) {
$(document).ready(function () {
    $('.table .eBtn').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function(user, status){
            $('.myForm #updateGroupInputID').val(user.id);
            $('.myForm #updateGroupInput').val(user.firstName);
            $('.myForm #updateGroupInput2').val(user.lastName);
            $('.myForm #updateGroupInput3').val(user.age);
            $('.myForm #updateGroupInput4').val(user.email);
        });

        $('.myForm #exampleModal').modal();
    });
});

$(document).ready(function () {
    $('.table .eBtn2').on('click', function (event) {
        event.preventDefault();
        var href = $(this).attr('href');
        $.get(href, function(user, status){
            $('.myForm2 #deleteGroupInputID').val(user.id);
            $('.myForm2 #deleteGroupInput').val(user.firstName);
            $('.myForm2 #deleteGroupInput2').val(user.lastName);
            $('.myForm2 #deleteGroupInput3').val(user.age);
            $('.myForm2 #deleteGroupInput4').val(user.email);
        });
        $('.myForm2 #exampleModal2').modal();
    });
});
})(jQuery);