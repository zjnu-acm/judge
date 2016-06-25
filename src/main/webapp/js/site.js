jQuery(function ($) {
    $("marquee").hover(function () {
        this.stop && this.stop();
    }, function () {
        this.start && this.start();
    });
    $("[data-lang]").each(function () {
        var s = "?lang=" + $(this).data("lang"), t = location.search.substr(1);
        $(this).attr("href", t ? s + "&" + t.replace(/lang(=[^&]*)?($|&)/g,"") : s);
    });
});
