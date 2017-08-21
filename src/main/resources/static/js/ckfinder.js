(function (window, undefined) {
    var $ = window.jQuery, CKFinder = window.CKFinder;
    function destroyCKEditor($this) {
        $this.ckeditorGet().destroy();
    }
    function ckfinder($this, c) {
        CKFinder.setupCKEditor($this.ckeditorGet(), c);
    }
    $(function ($) {
        var editor;
        var ckfinderpath = $("[name='ckfinderPath']").val();
        $("textarea.editable").on("focus", function () {
            editor && destroyCKEditor(editor);
            editor = $(this).ckeditor({
                toolbar: [
                    ["Source"],
                    ["TextColor", "-", "Bold", "Italic", "Underline", "-", "Subscript", "Superscript", "RemoveFormat"],
                    ["Cut", "Copy", "Paste", "PasteText", "PasteFromWord"],
                    ["Link", "Unlink", "Image", "Table"],
                    "/",
                    ["Find", "Replace", "-", "SelectAll"],
                    ["JustifyLeft", "JustifyCenter", "JustifyRight", "JustifyBlock"],
                    ["Font", "FontSize", "Maximize"]
                ]
            }, function () {
                editor.ckeditorGet().focus();
            });
            ckfinder(editor, editor.data("ckfinder") || ckfinderpath);
        });
    });
})(this);
