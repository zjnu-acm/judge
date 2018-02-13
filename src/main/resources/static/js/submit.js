jQuery(function ($) {
    function fun() {
        hint[+language.val() === 1 || +language.val() === 4 ? 'show' : 'hide']();
    }
    var language = $('[name="language"]').change(fun), hint = $('.submit.hint');
    fun();
});
