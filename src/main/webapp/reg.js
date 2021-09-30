function signUp() {
    $('#notification').text('');
    if ($('#password').val() !== $('#repeatPassword').val()) {
        $('#notification').text('Password must be equal');
        return;
    }
    $.ajax({
        type: 'POST',
        url: cPath + 'reg.do',
        data: JSON.stringify({
            username: $('#username').val(),
            password: $('#password').val()
        }),
        dataType: 'text'
    }).done(function(data) {
        if (data === '200 OK') {
            window.location.href = cPath + 'login.html';
        } else {
            $('#notification').text('User with this login already exists');
            console.log(data);
        }
    }).fail(function(err) {
        console.log(err);
    });
}

function Cancel() {
    window.location.href = cPath + 'login.html';
}
