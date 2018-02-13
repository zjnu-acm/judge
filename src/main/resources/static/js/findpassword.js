jQuery(function ($) {
    function d(event) {
        event.keyCode === 13 && button.click();
    }
    function click() {
        image.attr('src', src + (src.indexOf('?') < 0 ? '?' : '&') + '_=' + +new Date());
        imgv.val('');
        (user.val() ? imgv : user).focus();
    }
    function enable() {
        button.removeAttr('disabled');
    }
    function disable() {
        button.attr('disabled', true);
    }
    var div = $('#findpassword'), user = div.find('[name="username"]'), button = div.find('[type="button"]'), image = div.find('img'), imgv = div.find('[name="imgVerify"]');
    var src = image.attr('src');
    image.click(click);
    imgv.bind('keypress', d);
    user.bind('keypress', d);
    button.click(function () {
        var uid = user.val(), vcode = imgv.val();
        if (!uid) {
            alert('请输入用户名');
            user.focus();
        } else if (!vcode) {
            alert('请输入验证码');
            imgv.focus();
        } else {
            try {
                disable();
                $.post('resetPassword.js', {username: uid, verify: vcode}).always(click, enable).fail(function (result) {
                    alert(result.responseText || 'Error Occur');
                });
            } catch (e) {
                enable();
                alert('无法连接到服务器，请稍后再试！');
            }
        }
    });
});
