jQuery(function ($) {
    function d(event) {
        if (event.keyCode === 13)
            button.trigger('click');
    }
    var form = $('.fp-form'), newp = form.find('#newPassword'),
            renp = form.find('#reNewPassword'), button = form.find('#submit');
    newp.bind('keypress', d);
    renp.bind('keypress', d);
    button.click(function () {
        var p = newp.val(), p2 = renp.val();
        if (!p) {
            alert('请输入密码');
            newp.focus();
            return false;
        }
        if (p !== p2) {
            alert('两次密码不一致');
            newp.focus();
            return false;
        }
        if (p.length < 6 || p.length > 20) {
            alert('密码应该在6-20位之间');
            newp.focus();
            return false;
        }
        $.post(window.location.href.replace('.html', '.js'), {newPassword: p, action: 'changePassword'});
    });
});
