jQuery(function ($) {
    function d(event) {
        if (event.keyCode === 13)
            button.trigger('click');
    }

    var form = $('.fp-form'), newp = form.find('#newPassword');
    var renp = form.find('#reNewPassword'), button = form.find('#submit');
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
        var withoutQuery = location.protocol + '//' + location.host + location.pathname;
        var query = location.search;
        $.post(withoutQuery.replace(/(\.html)?$/, '.json') + (query || ''), {
            newPassword: p,
            action: 'changePassword'
        }).fail(function (result) {
            alert(result.responseJSON && result.responseJSON.message || result.responseText || 'Error Occur');
        }).success(function (result) {
            alert(result.message);
            document.location = button.data('goto');
        });
    });
});
