(function (global, factory) {
    if (typeof define === 'function' && define.amd) {
        define(['angular', 'ckeditor'], function (angular, ckeditor) {
            return factory(angular, ckeditor);
        });
    } else {
        return factory(global.angular, global.CKEDITOR);
    }
}(typeof window !== 'undefined' ? window : this, function (angular, CKEDITOR) {
    'use strict';
    function run($q, $interval) {
        $defer = $q.defer();
        if (angular.isUndefined(CKEDITOR)) {
            throw new Error('CKEDITOR not found');
        }
        CKEDITOR.disableAutoInline = true;
        function checkLoaded() {
            if (CKEDITOR.status === 'loaded') {
                loaded = true;
                $interval.cancel(cancel);
                $defer.resolve();
            }
        }
        CKEDITOR.on('loaded', checkLoaded);
        var cancel = $interval(checkLoaded, 100);
    }
    function bind($timeout, $q, ckeditor) {
        var CKFinder = window.CKFinder;
        var g_instances = [];
        return {
            restrict: 'C',
            priority: 1,
            require: ['ngModel', '^?form'],
            scope: {ckeditor: '='},
            link: function (scope, element, attrs, ctrls) {
                var ngModel = ctrls[0];
                var form = ctrls[1] || null;
                var options = angular.extend({}, ckeditor.options, scope.ckeditor);
                var EMPTY_HTML = '',
                        isTextarea = element[0].tagName.toLowerCase() === 'textarea',
                        data = [],
                        isReady = false;
                var instance;

                if (!isTextarea) {
                    element.attr('contenteditable', true);
                }

                function onLoad() {
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
                        var item = data.pop() || EMPTY_HTML;
                        isReady = false;
                        instance.setData(item, function () {
                            setModelData(setPristine);
                            isReady = true;
                        });
                    };

                    instance.on('pasteState', setModelData);
                    instance.on('change', setModelData);
                    instance.on('blur', setModelData);
                    instance.on('key', setModelData); // for source view

                    instance.on('instanceReady', function () {
                        scope.$broadcast('ckeditor.ready');
                        scope.$apply(function () {
                            data.push(ngModel.$viewValue);
                            onUpdateModelData(true);
                        });
                        instance.document.on('keyup', setModelData);
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
                    var ckfinder = typeof attrs.ckfinder === 'undefined' ? ckeditor.ckfinder : attrs.ckfinder;
                    ckfinder && CKFinder.setupCKEditor(instance, ckfinder);
                }

                if (options.deferred) {
                    element.on('focus', function () {
                        function doOnload() {
                            for (var i = 0; i < g_instances.length; ++i) {
                                (g_instances[i] !== instance) && g_instances[i].destroy();
                            }
                            g_instances.length = 0;
                            onLoad();
                            g_instances.push(instance);
                        }
                        loaded ? doOnload() : $defer.promise.then(doOnload);
                    });
                    scope.$on('$destroy', function () {
                        var index = g_instances.indexOf(instance);
                        if (index >= 0) {
                            g_instances.splice(index, 1);
                        }
                    });
                } else {
                    if (loaded) {
                        onLoad();
                    } else {
                        $defer.promise.then(onLoad);
                    }
                }
            }
        };
    }
    var $defer, loaded = false;
    return angular.module('ngCkeditor', []).run(['$q', '$interval', run]).directive('editable', ['$timeout', '$q', 'ckeditor', bind]).constant('ckeditor', {});
}));
