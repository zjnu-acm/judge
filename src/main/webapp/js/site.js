jQuery(function ($) {
    $("marquee").hover(function () {
        this.stop && this.stop();
    }, function () {
        this.start && this.start();
    });
});
