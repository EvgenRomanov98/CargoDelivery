function validateForm() {
    let passwordCheck = $('#passwordCheck');
    let pass = $('#registerPassword');
    if (pass.val() !== passwordCheck.val()) {
        pass.addClass('is-invalid')
        passwordCheck.addClass('is-invalid')
        alert('Passwords not same')
        return false;
    }
    if (Array.from(document.querySelectorAll('form[action=registration] .is-invalid')).length !== 0){
        return false;
    }
    pass.addClass('is-valid')
    passwordCheck.addClass('is-valid')
    return true;
}

function checkUniqueEmail(url) {
    let email = $('#email');
    $.get(url,
        {
            check: 'email',
            param: email.val()
        },
        function (data) {
            let resp = JSON.parse(data);
            if (resp.exist) {
                email.addClass("is-invalid");
                alert("Email already exist");
                return;
            }
            email.removeClass('is-invalid');
            email.addClass('is-valid');
        })
}

function checkUniquePhone(url) {
    let phone = $('#phone');
    $.get(url,
        {
            check: 'phone',
            param: phone.val()
        },
        function (data) {
            let resp = JSON.parse(data);
            if (resp.exist) {
                phone.addClass("is-invalid");
                alert("Phone already exist");
                return;
            }
            phone.removeClass('is-invalid')
            phone.addClass("is-valid")
        })
}

