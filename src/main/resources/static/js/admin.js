!function (window, undefined) {
    'use strict';
    var $ = window.jQuery, angular = window.angular;
    var isLocal = window.location.protocol === 'file:';
    var problemListCache;

    var app = angular.module('adminApp', ['ngResource', 'ui.router', 'ngSanitize', 'angular-loading-bar', 'ui.bootstrap', 'ngCkeditor', 'datetime']);
    app.factory('reload', ['$state', function ($state) {
            return function (view, params, options) {
                if (typeof view !== 'string') {
                    options = params;
                    params = view;
                    view = $state.current.name;
                }
                return $state.go(view, $.extend({}, $state.params || {}, params || {}), options || {location: 'replace', reload: true});
            };
        }
    ]);
    app.factory('title', function ($document) {
        return function (title) {
            return title ? $document.prop('title', title + ' - 在线评测系统管理后台') : $document.prop('title');
        };
    });
    app.factory('path', function ($rootScope) {
        function normalize(url) {
            var a = $('<a>', {href: url}).appendTo('body'), href = a.prop('href');
            return a.remove(), href;
        }
        var adminHref = normalize($('base').prop('href')),
                baseHref = normalize(adminHref + '../');
        var path = {
            admin: adminHref,
            api: baseHref + 'api/',
            base: baseHref
        };
        return $rootScope.path = path;
    });
    app.factory('problemApi', function ($resource, path) {
        return $resource(path.api + 'problems/:id.json', {id: '@id'}, {
            'query': {method: 'GET', isArray: false},
            'update': {method: 'PATCH'}
        });
    });
    app.factory('contestApi', function ($resource, path) {
        return $resource(path.api + 'contests/:id.json', {id: '@id'}, {
            update: {method: 'PATCH'}
        });
    });
    app.factory('localeApi', function ($resource, path) {
        return $resource(path.api + 'locales/:id.json', {id: '@id'});
    });
    app.factory('miscApi', function ($resource, path) {
        return $resource(path.api + "misc/:action.json", null, {
            getSystemInfo: {method: 'GET', params: {action: 'systemInfo'}},
            setSystemInfo: {method: 'PUT', params: {action: 'systemInfo'}},
            fix: {method: 'POST', params: {action: 'fix'}},
            getContestOnly: {method: 'GET', params: {action: 'contestOnly'}},
            setContestOnly: {method: 'PUT', params: {action: 'contestOnly'}}
        });
    });
    app.directive('requestFocus', function ($timeout) {
        return {
            restrict: 'A',
            link: function (scope, element) {
                $timeout($.proxy(element, 'focus'), 0);
            }
        };
    });
    app.directive('currentTime', function ($http, $filter, path) {
        var timeDiff = 0;
        $http.get(path.api + 'system/time.json').then(function (resp) {
            timeDiff = new Date() - resp.data * 1000;
        });
        return {
            restrict: 'A',
            scope: {
                format: '@currentTime',
                interval: '@'
            },
            link: function (scope, element) {
                function apply() {
                    var date = new Date() - timeDiff;
                    element[isInput ? 'val' : 'text']($filter('date')(date, format));
                }
                var isInput = element.is(':input'), format = scope.format;
                var stopTime = setInterval(apply, +scope.interval || 200);
                apply();
                element.on('$destroy', function () {
                    clearInterval(stopTime);
                });
            }
        };
    });

    app.config(function ($sceProvider) {
        // Completely disable SCE.  For demonstration purposes only!
        // Do not use in new projects or libraries.
        $sceProvider.enabled(false);
    });

    app.config(function ($stateProvider, $locationProvider, $urlRouterProvider) {
        $locationProvider.html5Mode(true);
        $stateProvider.state('index', {
            title: '首页',
            url: '/',
            views: {
                '': {
                    templateUrl: 'index.html',
                    controller: 'index'
                }
            }
        }).state('contest-add', {
            title: '添加比赛',
            url: '/contests/add.html',
            views: {
                '': {
                    templateUrl: 'contest-edit.html',
                    controller: 'contest-add'
                }
            }
        }).state('problem-add', {
            title: '添加题目',
            url: '/problems/add.html',
            views: {
                '': {
                    templateUrl: 'problem-edit.html',
                    controller: 'problem-add'
                }
            }
        }).state('problem-list', {
            title: '题目列表',
            url: '/problems.html?page&size&sort',
            views: {
                '': {
                    templateUrl: 'problem-list.html',
                    controller: 'problem-list'
                }
            }
        }).state('problem-view-current', {
            title: '题目查看',
            url: '/problems/:id.html',
            controller: 'problem-view-current'
        }).state('problem-view-current-alias', {
            title: '题目查看',
            url: '/problems/:id/view/current.html',
            controller: 'problem-view-current'
        }).state('problem-view-locale', {
            title: '题目查看',
            url: '/problems/:id/view/:locale.html',
            views: {
                '': {
                    templateUrl: 'problem-view.html',
                    controller: 'problem-view-locale'
                }
            }
        }).state('problem-edit-current', {
            title: '题目编辑',
            url: '/problems/:id/edit.html',
            controller: 'problem-edit-current'
        }).state('problem-edit-current-alias', {
            title: '题目编辑',
            url: '/problems/:id/edit/current.html',
            controller: 'problem-edit-current'
        }).state('problem-edit-locale', {
            title: '题目编辑',
            url: '/problems/:id/edit/:locale.html',
            views: {
                '': {
                    templateUrl: 'problem-edit.html',
                    controller: 'problem-edit-locale'
                }
            }
        }).state('contest-list', {
            title: '比赛列表',
            url: '/contests.html',
            views: {
                '': {
                    templateUrl: 'contest-list.html',
                    controller: 'contest-list'
                }
            }
        }).state('contest-view', {
            title: '比赛查看',
            url: '/contests/:id.html',
            views: {
                '': {
                    templateUrl: 'contest-view.html',
                    controller: 'contest-view'
                }
            }
        });
        isLocal || $urlRouterProvider.when(/^#?\/?$/, '/').otherwise('404.html');
    });
    app.config(function ($httpProvider) {
        //initialize get if not there
        var headers = $httpProvider.defaults.headers;

        // Answer edited to include suggestions from comments
        // because previous version of code introduced browser-related errors

        //disable IE ajax request caching
        $.extend(headers.get || (headers.get = {}), {
            'If-Modified-Since': new Date(0).toUTCString(),
            'Cache-Control': 'no-cache',
            'Pragma': 'no-cache'
        });
        $httpProvider.interceptors.push(['$q', 'path', function ($q, path) {
                return {
                    'responseError': function (response) {
                        if (response.status === 401) {
                            window.location = path.base + 'login?url=' + encodeURIComponent(document.location.href);
                            return;
                        }
                        if (response.status === 400) {
                            alert(response.data && response.data.message || response.data);
                        }
                        return $q.reject(response);
                    }
                };
            }]);
    });
    app.config(function (cfpLoadingBarProvider) {
        cfpLoadingBarProvider.latencyThreshold = 0;
        cfpLoadingBarProvider.startSize = 0.1;
    });
    app.config(function (datetimePlaceholder) {
        $.extend(datetimePlaceholder, {
            year: 'yyyy',
            yearShort: 'yy',
            month: 'MM',
            date: 'dd',
            day: 'W',
            hour: 'HH',
            hour12: 'hh',
            minute: 'mm',
            second: 'ss'
        });
    });
    app.run(function (ckeditor, path) {
        ckeditor.options = {
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
        ckeditor.ckfinder = path.base + "webjars/ckfinder/2.6.2.1/";
    });
    app.controller('index', function ($scope, contestApi, miscApi) {
        $scope.form = miscApi.getSystemInfo();
        $scope.setSystemInfo = function () {
            miscApi.setSystemInfo($scope.form, function () {
                alert('设置完成');
            });
        };
        $scope.fix = function () {
            $scope.fix.disabled = true;
            miscApi.fix(function () {
                alert('修复完成');
                $scope.fix.disabled = false;
            });
        };
        $scope.contestOnly = miscApi.getContestOnly();
        $scope.setContestOnly = function () {
            miscApi.setContestOnly($scope.contestOnly, function () {
                alert('设置成功');
            });
        };
        $scope.contests = contestApi.query({include: 'PENDING,RUNNING'});
    });

    var veryslow;
    app.run(function ($sniffer) {
        veryslow = $sniffer.msie && $sniffer.msie < 9;
    });

    app.controller('problem-list', function ($stateParams, $scope, $timeout, reload, problemApi) {
        if (veryslow && !$stateParams.size && !$stateParams.page) {
            reload({size: 20});
            return;
        }
        $scope.push = function (sort) {
            var sorts = $stateParams.sort || [];
            sorts = typeof sorts === 'string' ? [sorts] : $.extend([], sorts);
            for (var i = 0, len = sorts.length; i < len; ++i) {
                if (sorts[i].split(',')[0] === sort) {
                    i === 0 && sorts[i] === sort && (sort += ',desc');
                    sorts.splice(i, 1);
                    --len;
                }
            }
            sorts.unshift(sort);
            reload({sort: sorts, page: null});
        };
        $scope.page = problemListCache;
        $timeout(function () {
            $scope.pageChanged = function () {
                reload({page: $scope.currentPage - 1});
            };
        });
        problemApi.query($stateParams, function (page) {
            $scope.page = problemListCache = page;
            $scope.currentPage = page.number + 1;
        });
        $scope.currentPage = (+$stateParams.page || 0) + 1;
        $scope.enable = function (problem) {
            problemApi.update({id: problem.id, disabled: false}, function () {
                problem.disabled = false;
            });
        };
        $scope.disable = function (problem) {
            problemApi.update({id: problem.id, disabled: true}, function () {
                problem.disabled = true;
            });
        };
    });
    app.controller('problem-view-current', function (localeApi, reload) {
        localeApi.get({id: 'current'}, function (locale) {
            reload('problem-view-locale', {locale: locale && locale.id || 'und'});
        });
    });
    app.controller('problem-view-locale', function ($scope, $stateParams, problemApi, title, localeApi, reload) {
        problemApi.get($stateParams, function (problem) {
            $scope.problem = problem;
            problem.title && title(problem.title);
            $scope.disable = function () {
                problemApi.update({id: problem.id, disabled: true}, function () {
                    problem.disabled = true;
                });
            };
            $scope.enable = function () {
                problemApi.update({id: problem.id, disabled: false}, function () {
                    problem.disabled = false;
                });
            };
        });
        $scope.locale = $stateParams.locale;
        $scope.locales = localeApi.query();
        $scope.change = function () {
            reload({locale: $scope.locale});
        };
    });
    app.controller('problem-edit-locale', function ($scope, $stateParams, $state, problemApi, localeApi, title, reload) {
        $scope.problem = problemApi.get($stateParams, function (problem) {
            problem.title && title('编辑 ' + problem.title);
        });
        $scope.locale = $stateParams.locale;
        $scope.locales = localeApi.query();
        $scope.change = function () {
            if (!$scope.problemEditForm.$dirty || confirm('你已经修改题目，继续操作将会丢失修改，是否继续？')) {
                reload({locale: $scope.locale});
            } else {
                $scope.locale = $stateParams.locale;
            }
        };
        $scope.editor = {
            hint: 'Modify problem ' + $stateParams.id,
            hint2: 'Modify problem'
        };
        $scope.save = function () {
            $scope.problem.$update({locale: $scope.locale}, function () {
                $state.go('problem-view-locale', {id: $stateParams.id, locale: $scope.locale});
            });
        };
    });
    app.controller('problem-edit-current', function (localeApi, reload) {
        localeApi.get({id: 'current'}, function (locale) {
            reload('problem-edit-locale', {locale: locale && locale.id || 'und'});
        });
    });
    app.controller('problem-add', function ($scope, $state, problemApi, contestApi) {
        $scope.editor = {
            hint: 'Add New problem',
            hint2: 'Add a Problem'
        };
        $scope.problem = new problemApi();
        $scope.contests = contestApi.query({include: 'PENDING,RUNNING'});
        $scope.save = function () {
            $scope.problem.$save(function (problem) {
                $state.go('problem-view-current', {id: problem.id});
            });
        };
    });
    app.controller('contest-list', function ($scope, $http, path, contestApi) {
        function toParam(obj) {
            var include = [], exclude = [], t = {}, hasOwn = t.hasOwnProperty;
            for (var i in obj) {
                hasOwn.call(obj, i) && (obj[i] ? include : exclude).push(i);
            }
            return {include: include, exclude: exclude};
        }
        function load() {
            contestApi.query($.extend({}, query.includeDisabled ? {includeDisabled: true} : {}, toParam(query.include)), function (list) {
                $scope.list = list;
            });
        }
        var query;
        query = $scope.query = {
            includeDisabled: false,
            include: {PENDING: !0, RUNNING: !0, ENDED: !0, ERROR: !1}
        };
        $scope.load = load;
        load();
        $scope.enable = function (contest) {
            contestApi.update({id: contest.id, disabled: false}, function () {
                contest.disabled = false;
            });
        };
        $scope.disable = function (contest) {
            contestApi.update({id: contest.id, disabled: true}, function () {
                contest.disabled = true;
            });
        };
    });
    app.controller('contest-view', function ($stateParams, $scope, $http, problemApi, path, contestApi, title) {
        function load() {
            contestApi.get($stateParams, function (data) {
                var contest = $scope.contest = data;
                $scope.problems = data.problems;
                title(contest.title);
            });
        }
        $scope.fromCharCode = String.fromCharCode;
        var newProblem = $scope.newProblem = {};
        $scope.change = function () {
            var code = {'404': 'Not found.'};
            var val = newProblem.id;
            if (/^[1-9][0-9]*$/.test(val)) {
                problemApi.get({id: newProblem.id}, function (p) {
                    newProblem.title = p.title || 'No Title';
                    $scope.disabled = false;
                }).$promise['catch'](function (resp) {
                    newProblem.title = code[resp.status] || 'Error';
                    $scope.disabled = true;
                });
            } else {
                newProblem.title = '';
                $scope.disabled = true;
            }
        };
        $scope.add = function () {
            $http.post(path.api + 'contests/' + $scope.contest.id + '/problems.json', [{id: newProblem.id}]).then(function () {
                load();
                newProblem.id = newProblem.title = '';
            });
        };
        $scope.remove = function (p) {
            $http['delete'](path.api + 'contests/' + $scope.contest.id + '/problems/' + p + '.json').then(function () {
                load();
                newProblem.id = newProblem.title = '';
            });
        };
        load();
    });

    function initContestEdit($scope, reset, save) {
        function change() {
            var t = /^(?:(\d+)\D+)?(\d+):(\d+):(\d+)$/.exec(form.length);
            var start = +form.start;
            if (t && start === start) {
                var day = t[1] || 0;
                var end = start + (day * 86400 + t[2] * 3600 + t[3] * 60 + +t[4]) * 1000;
                form.end = new Date(end);
            } else {
                form.end = null;
            }
        }
        var form;
        form = $scope.form = {};
        $scope.reset = reset;
        $scope.$watch('form.start', change);
        $scope.$watch('form.length', change);
        $scope.save = function () {
            change();
            save({
                id: form.id,
                title: form.title,
                description: form.description,
                startTime: +form.start / 1000,
                endTime: +form.end / 1000
            });
        };
        reset(form);
        change();
    }

    app.controller('contest-add', function ($scope, $state, contestApi) {
        initContestEdit($scope, function (form) {
            var now = new Date();
            now.setDate(now.getDate() + 1);
            now.setHours(12);
            now.setMinutes(0);
            now.setSeconds(0);
            now.setMilliseconds(0);
            now = now.getTime();
            $.extend(form, {
                start: new Date(now),
                length: '5:00:00',
                min: new Date(),
                title: '',
                description: ''
            });
        }, function (form) {
            contestApi.save(form, function (contest) {
                $state.go('contest-view', {id: contest.id});
            });
        });
    });
    app.run(function ($rootScope, $state, $interpolate, title) {
        $rootScope.$on('$stateChangeSuccess', function (event, state) {
            var currentState = $state.$current;
            var tt = currentState.title || currentState.data && currentState.data.pageTitle || $rootScope.pageTitle || 'Untitled Page';
            title($interpolate(tt)(currentState));
        });
        function getStatus(contest) {
            var now = +new Date() / 1000;
            var disabled = !!contest.disabled;
            var started = !contest.startTime || contest.startTime < now;
            var ended = !contest.endTime || contest.endTime < now;
            var error = contest.startTime && contest.startTime > contest.endTime;
            return disabled ? 4 : error ? 3 : started && !ended ? 1 : ended ? 2 : 0;
        }
        var text = ['Pending', 'RUNNING', 'ENDED', 'ERROR', 'DISABLED'];
        var style = [{color: 'red'}, {color: 'blue'}, {color: 'green'}, , {color: 'darkred'}];
        $rootScope.contestStatus = function (contest) {
            return text[getStatus(contest)];
        };
        $rootScope.statusColor = function (contest) {
            return style[getStatus(contest)];
        };
    });

    $(function ($) {
        var offset = 220;
        var duration = 500;
        var selector = '.back-to-top';
        var element = $(selector);

        // element.length || (element = $("<span class='back-to-top'></span>").appendTo('body'));

        $(window).scroll(function () {
            element[$(this).scrollTop() > offset ? 'fadeIn' : 'fadeOut'](duration);
        });

        element.css('opacity', .2).click(function () {
            $('html, body').animate({scrollTop: 0}, duration);
            return false;
        });
        $('script').remove();
    });
}(typeof window !== 'undefined' ? window : this);
