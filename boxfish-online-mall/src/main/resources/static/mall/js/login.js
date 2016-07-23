$("#btnLogin").on('click', function () {
    var $username = $("#username").val();
    var $password = $("#password").val();

    if ($username == "" || $password == "") {
        $("#errorMsg").css("color", "red");
        $("#errorMsg").html("用户名/密码不能为空!");
        return false;
    }
    var $userInfo = '{ "username":"' + $username + '","password":"' + $password + '"}';
    $.ajax({
        url: '/invitation/home',
        type: 'post',
        contentType: 'application/json',
        dataType: 'json',
        cache: false,
        data: $userInfo,
        success: function (result) {
            //noinspection JSUnresolvedVariable
            $accessToken = result.data.access_token;
            $role = result.data.role;
            window.location.href = "/mall/combos.html?role=" + $role + "&access_token=" + $accessToken;
        },
        error: function (errorMsg) {
            console.log(errorMsg);
            $("#errorMsg").css("color", "red");
            //noinspection JSUnresolvedVariable
            $("#errorMsg").html($.parseJSON(errorMsg.responseText).returnMsg);
        }
    });

});