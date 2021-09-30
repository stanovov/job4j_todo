$(document).ready(function () {
    $.ajax({
        type: 'GET',
        url: cPath + 'login.do',
        dataType: 'json'
    }).done(function (data) {
        $('#username').val(data.username);
        $('#password').val(data.password);
    }).fail(function (err) {
        console.log(err);
    });
});

function login() {
    $('#notification').text('');
    $.ajax({
        type: 'POST',
        url: cPath + 'login.do',
        data: JSON.stringify({
            username: $('#username').val(),
            password: $('#password').val()
        }),
        dataType: 'text'
    }).done(function(data) {
        if (data === '200 OK') {
            window.location.href = cPath + 'index.html';
        } else {
            $('#notification').text('Wrong username or password');
            console.log(data);
        }
    }).fail(function(err) {
        console.log(err);
    });
}

function goToRegistration() {
    window.location.href = cPath + 'reg.html';
}