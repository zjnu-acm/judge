(function (global, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['angular', 'ckeditor'], function (angular, ckeditor) {
            return factory(angular, ckeditor);
        });
    } else {
        return factory(global.angular, global.CKEDITOR);
    }
}(typeof window !== "undefined" ? window : this, function (angular, CKEDITOR) {
    var app = angular.module('ngCkeditor', []);
    var $defer, loaded = false;
    app.run(['$q', '$timeout', function ($q, $timeout) {
            $defer = $q.defer();
            if (angular.isUndefined(CKEDITOR)) {
                throw new Error('CKEDITOR not found');
            }
            CKEDITOR.disableAutoInline = true;
            function checkLoaded() {
                if (CKEDITOR.status === 'loaded') {
                    loaded = true;
                    $defer.resolve();
                }
            }
            CKEDITOR.on('loaded', checkLoaded);
            $timeout(checkLoaded, 100);
        }]);
    app.directive('editable', ['$timeout', '$q', function ($timeout, $q) {
            var CKFinder = window.CKFinder;
            function destroyCKEditor(instance) {
                instance.destroy();
            }
            function ckfinder(instance, c) {
                CKFinder.setupCKEditor(instance, c);
            }
            var instance;
            return {
                restrict: 'C',
                require: ['ngModel', '^?form'],
                link: function (scope, element, attrs, ctrls) {
                    var ngModel = ctrls[0];
                    var form = ctrls[1] || null;
                    var EMPTY_HTML = '<p></p>',
                            isTextarea = element[0].tagName.toLowerCase() === 'textarea',
                            data = [],
                            isReady = false;

                    function onLoad() {
                        instance && destroyCKEditor(instance);
                        var options = {
                            toolbar: [
                                ['Source'],
                                ['TextColor', '-', 'Bold', 'Italic', 'Underline', '-', 'Subscript', 'Superscript', 'RemoveFormat'],
                                ['Cut', 'Copy', 'Paste', 'PasteText', 'PasteFromWord'],
                                ['Link', 'Unlink', 'Image', 'Table'],
                                '/',
                                ['Find', 'Replace', '-', 'SelectAll'],
                                ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock'],
                                ['Font', 'FontSize', 'Maximize']
                            ]
                        };
                        instance = isTextarea ? CKEDITOR.replace(element[0], options) : CKEDITOR.inline(element[0], options);
                        var configLoaderDef = $q.defer();
                        var setModelData = function (setPristine) {
                            var data = instance.getData() || '';
                            $timeout(function () { // for key up event
                                (setPristine !== true || data !== ngModel.$viewValue) && ngModel.$setViewValue(data);
                                (setPristine === true && form) && form.$setPristine();
                            }, 0);
                        }, onUpdateModelData = function (setPristine) {
                            if (!data.length) {
                                return;
                            }
                            var item = data.unshift() || EMPTY_HTML;
                            isReady = false;
                            instance.setData(item, function () {
                                setModelData(setPristine);
                                isReady = true;
                            });
                        };

                        instance.on('pasteState', setModelData);
                        instance.on('change', setModelData);
                        instance.on('blur', setModelData);
                        //instance.on('key',          setModelData); // for source view

                        instance.on('instanceReady', function () {
                            scope.$broadcast("ckeditor.ready");
                            scope.$apply(function () {
                                data.push(ngModel.$viewValue);
                                onUpdateModelData(true);
                            });
                            instance.document.on("keyup", setModelData);
                            instance.focus();
                        });
                        instance.on('customConfigLoaded', function () {
                            configLoaderDef.resolve();
                        });
                        ngModel.$render = function () {
                            data.push(ngModel.$viewValue);
                            if (isReady) {
                                onUpdateModelData();
                            }
                        };
                        attrs.ckfinder && ckfinder(instance, attrs.ckfinder);
                    }

                    element.on('focus', function () {
                        if (loaded) {
                            onLoad();
                        } else {
                            $defer.promise.then(onLoad);
                        }
                    });
                    scope.$on('$destroy', function () {
                        if (instance === element) {
                            instance = undefined;
                        }
                    });
                }
            };
        }]);
    return app;
}));
