jQuery(function ($) {
    $('marquee').hover(function () {
        this.stop && this.stop();
    }, function () {
        this.start && this.start();
    });
    $('[data-lang]').each(function () {
        var s = '?lang=' + $(this).data('lang'), t = location.search.substr(1).replace(/(^|&)lang(=[^&]*)?($|(?=&))/g, '').replace(/^&+/, '');
        $(this).attr('href', t ? s + '&' + t : s);
    });
});

jQuery(function ($) {
    var offset = 220;
    var duration = 500;
    var selector = '.back-to-top';
    var element = $(selector);

    // element.length || (element = $('<span class="back-to-top"></span>').appendTo('body'));

    $(window).scroll(function () {
        element[$(this).scrollTop() > offset ? 'fadeIn' : 'fadeOut'](duration);
    });

    element.css('opacity', .2).click(function () {
        $('html, body').animate({scrollTop: 0}, duration);
        return false;
    });
});
