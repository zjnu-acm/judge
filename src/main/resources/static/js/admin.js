!function (window, undefined) {
    'use strict';
    var $ = window.jQuery, angular = window.angular;
    var isLocal = window.location.protocol === 'file:';
    var problemListCache, accountListCache;
    var localStorage = window.localStorage;

    var hasOwnProperty = Object.prototype.hasOwnProperty, keys = Object.keys || (function () {
        var hasDontEnumBug = !({toString: null}).propertyIsEnumerable('toString'),
                dontEnums = ['toString', 'toLocaleString', 'valueOf', 'hasOwnProperty', 'isPrototypeOf', 'propertyIsEnumerable', 'constructor'],
                dontEnumsLength = dontEnums.length;
        return function (obj) {
            if (typeof obj !== 'function' && (typeof obj !== 'object' || obj === null)) {
                throw new TypeError('Object.keys called on non-object');
            }
            var result = [], prop, i;
            for (prop in obj) {
                hasOwnProperty.call(obj, prop) && result.push(prop);
            }
            if (hasDontEnumBug) {
                for (i = 0; i < dontEnumsLength; i++) {
                    hasOwnProperty.call(obj, dontEnums[i]) && result.push(dontEnums[i]);
                }
            }
            return result;
        };
    }());

    var app = angular.module('adminApp', ['ngResource', 'ui.router', 'ngSanitize', 'angular-loading-bar', 'ui.bootstrap', 'ngCkeditor', 'datetime', 'ngFileUpload', 'isteven-multi-select']);
    app.factory('reload', function ($state) {
        return function (view, params, options) {
            if (typeof view !== 'string') {
                options = params;
                params = view;
                view = $state.current.name;
            }
            return $state.go(view, $.extend({}, $state.params || {}, params || {}), options || {location: 'replace', reload: true});
        };
    });
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
    app.factory('pageIndexApi', function ($resource, path) {
        return $resource(path.api + 'system/index.json', {}, {
            update: {method: 'PUT'}
        });
    });
    app.factory('accountApi', function ($resource, path) {
        return $resource(path.api + 'accounts/:id.json', {id: '@id'}, {
            query: {method: 'GET', isArray: false},
            update: {method: 'PATCH'},
            updatePassword: {method: 'PATCH', url: path.api + 'accounts/:id/password.json'},
            saveAll: {method: 'POST', url: path.api + 'accounts/import.json'}
        });
    });
    app.factory('miscApi', function ($resource, path) {
        return $resource(path.api + 'misc/:action.json', null, {
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
    app.factory('currentTime', function ($http, path) {
        var timeDiff = 0;
        $http.get(path.api + 'system/time.json').then(function (resp) {
            timeDiff = new Date - resp.data;
        });
        return function () {
            return new Date - timeDiff;
        };
    });
    app.factory('returnIt', function () {
        return function (x) {
            return function () {
                return x;
            };
        };
    });
    app.factory('Comparator', function () {
        function cmp(a, b) {
            return a < b ? -1 : a > b ? 1 : a === b ? a === 0 ? cmp(1 / a, 1 / b) : 0 : 0;
        }
        return {
            naturalOrder: function () {
                return cmp;
            }, reverseOrder: function (origin) {
                origin = typeof origin === 'function' ? origin : cmp;
                return function (a, b) {
                    return origin(b, a);
                };
            }, nullFirst: function (origin) {
                return function (a, b) {
                    return a === b ? a === 0 ? cmp(1 / a, 1 / b) : 0 : a === null ? -1 : b === null ? 1 : a === undefined ? -1 : b === undefined ? 1 : origin(a, b);
                };
            }
        };
    });
    app.factory('Set', function () {
        return typeof window.Set === 'function' ? window.Set : function () {
            var keys = {};
            return {
                add: function (value) {
                    keys[' ' + value] = !0;
                    return this;
                }, clear: function () {
                    keys = {};
                    return this;
                }, has: function (x) {
                    return keys[' ' + x] || false;
                }
            };
        };
    });
    app.factory('Sort', function (Set, Comparator) {
        var a = [], unshift = a.unshift, splice = a.splice, slice = a.slice, push = a.push, concat = a.concat;
        function Sort(param) {
            return new Sort.fn.init(param);
        }
        function remove(arr, keys) {
            var indexes = [];
            keys = keys || new Set();
            for (var i = 0, len = arr.length; i < len; ++i) {
                var t = arr[i].split(',')[0];
                keys.has(t) ? indexes.push(i) : keys.add(t);
            }
            while (indexes.length) {
                splice.call(arr, indexes.pop(), 1);
                --len;
            }
            return len;
        }
        function by(name, cmp) {
            return function (a, b) {
                return cmp(a[name], b[name]);
            };
        }
        function map(x) {
            return x.slice(-5) === ',desc' ? by(x.slice(0, -5), Comparator.nullFirst(Comparator.reverseOrder())) : by(x, Comparator.nullFirst(Comparator.naturalOrder()));
        }
        function parse(args) {
            var parsed = [];
            (function add(args) {
                $.each(args, function (_, arg) {
                    arg && (typeof arg === 'string' ? push.apply(parsed, Sort.parse(arg)) : add(arg));
                });
            })(args);
            return parsed;
        }
        Sort.fn = Sort.prototype = {
            init: function (param) {
                this.push(param);
            }, unshift: function () {
                var parsed = parse(arguments);
                if (parsed.length === 1 && parsed[0] === this[0]) {
                    parsed = [parsed[0].slice(-5) === ',desc' ? parsed[0].slice(0, -5) : parsed[0] + ',desc'];
                }
                unshift.apply(this, parsed);
                return remove(this);
            }, toArray: function () {
                return slice.call(this);
            }, remove: function (e) {
                var toRemove = new Set();
                $.each(Sort.parse(e), function (i, e) {
                    toRemove.add(e.split(',')[0]);
                });
                remove(this, toRemove);
                return this;
            }, toComparator: function () {
                var c = $.map(this, map);
                return function (a, b) {
                    for (var i = 0, len = c.length, t; i < len; ++i) {
                        t = c[i](a, b);
                        if (t) {
                            return t;
                        }
                    }
                    return 0;
                };
            }, push: function () {
                var parsed = parse(arguments);
                push.apply(this, parsed);
                return remove(this);
            }, concat: function () {
                return concat.apply(this.toArray(), arguments);
            },
            sort: a.sort,
            splice: splice,
            slice: slice,
            shift: a.shift,
            pop: a.pop,
            length: 0
        };
        Sort.fn.init.prototype = Sort.fn;
        Sort.parse = function (res) {
            res = res ? res.split(',') : [];
            var last = res.length > 1 && res[res.length - 1];
            if (last === 'asc') {
                res.pop();
            } else if (last === 'desc') {
                return res.pop(), $.map(res, function (x) {
                    return x + ',desc';
                });
            }
            return res;
        };
        return Sort;
    });
    app.factory('PaginationService', function ($filter) {
        var filter = $filter('filter');
        return function () {
            function doSet($this, list, size, page, filter) {
                data = $.isArray(list) ? list : data;
                currentPage = $.isNumeric(page) && ~page || currentPage;
                pageSize = $.isNumeric(size) && size > 0 && (size >>> 0) || pageSize;
                if (typeof filter === 'object')
                    oFilter = filter;
                return $this;
            }
            function filtered() {
                return filter(data, oFilter);
            }
            var data = [], currentPage = -1, pageSize = 20, oFilter, view = {
                setData: function (data) {
                    return doSet(this, data);
                }, setPage: function (page, size) {
                    return doSet(this, undefined, size, page);
                }, number: function () {
                    return ~currentPage;
                }, pageSize: function () {
                    return pageSize;
                }, isEmpty: function () {
                    return !data.length;
                }, totalPages: function () {
                    var data = filtered();
                    return ((data.length + pageSize - 1) / pageSize) >>> 0;
                }, totalElements: function () {
                    return data.length;
                }, filteredElements: function () {
                    var data = filtered();
                    return data.length;
                }, isPage: function (i) {
                    return isNumeric(i) && ~i === currentPage;
                }, offset: function (offset) {
                    return ~currentPage * pageSize + (offset >> 0);
                }, list: function () {
                    return filtered().slice(~currentPage * pageSize, -currentPage * pageSize);
                }, remove: function (item) {
                    var index = $.inArray(item, data);
                    if (index >= 0) {
                        data.splice(index, 1);
                    }
                    return this;
                }
            };
            return $.proxy(doSet, undefined, view).apply(undefined, arguments);
        };
    });

    app.directive('currentTime', function (currentTime, $filter) {
        return {
            restrict: 'A',
            scope: {
                format: '@currentTime',
                interval: '@'
            },
            link: function (scope, element) {
                function apply() {
                    element[isInput ? 'val' : 'text']($filter('date')(currentTime(), format));
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
        // Completely disable SCE. For demonstration purposes only!
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
        }).state('contest-edit', {
            title: '比赛编辑',
            url: '/contests/:id/edit.html',
            views: {
                '': {
                    templateUrl: 'contest-edit.html',
                    controller: 'contest-edit'
                }
            }
        }).state('account-list', {
            title: '用户列表',
            url: '/accounts.html?userId&nick&query&disabled&page&size&sort',
            views: {
                '': {
                    templateUrl: 'account-list.html',
                    controller: 'account-list'
                }
            }
        }).state('upload-account', {
            title: '导入用户',
            url: '/accounts/import.html?id&nick&school&email&page&sort&size',
            views: {
                '': {
                    templateUrl: 'account-import.html',
                    controller: 'account-import'
                }
            }
        }).state('page-index-changer', {
            title: '修改首页内容',
            url: '/system/index.html',
            views: {
                '': {
                    templateUrl: 'page-index-changer.html',
                    controller: 'page-index-changer'
                }
            }
        });
        isLocal || $urlRouterProvider.when(/^#?\/?$/, '/').otherwise('404.html');
    });
    app.run(function ($rootScope, $state, $interpolate, title) {
        $rootScope.$on('$stateChangeSuccess', function () {
            var currentState = $state.$current;
            var tt = currentState.title || currentState.data && currentState.data.pageTitle || $rootScope.pageTitle || 'Untitled Page';
            title($interpolate(tt)(currentState));
        });
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
                        var code = response.status, series = code / 100 >> 0;
                        if (series === 4 && code !== 404) {
                            alert(response.data && response.data.message || response.data);
                        }
                        if (series === 5) {
                            alert(response.data && (response.data.exception || response.data.message) || response.data || '服务器出错了');
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
            deferred: true,
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
        ckeditor.ckfinder = path.base + 'webjars/ckfinder/2.6.2.1/';
    });
    app.controller('index', function ($scope, contestApi, miscApi) {
        $scope.systemInfo = miscApi.getSystemInfo();
        $scope.setSystemInfo = function () {
            miscApi.setSystemInfo($scope.systemInfo, function () {
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
        veryslow = $sniffer.msie < 9;
    });

    app.controller('problem-list', function ($stateParams, $scope, $timeout, reload, problemApi, Sort) {
        if (veryslow && !$stateParams.size && !$stateParams.page) {
            reload({size: 20});
            return;
        }
        var sort = Sort($stateParams.sort);
        $scope.push = function (order) {
            sort.unshift(order);
            reload({sort: sort.toArray(), page: null});
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
            title(problem.title);
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
        function getId(x) {
            return x.id;
        }
        $scope.editor = {
            hint: 'Add New problem',
            hint2: 'Add a Problem'
        };
        $scope.problem = {};
        var slice = [].slice;
        contestApi.query({include: 'PENDING,RUNNING'}, function (contests) {
            $scope.contests = [{id: 0, title: '全局练习', selected: true}].concat(slice.call(contests));
        });
        $scope.save = function () {
            var problem = $scope.problem;
            problemApi.save($.extend({}, problem, {contests: $.map(problem.contests || [], getId)}), function (problem) {
                $state.go('problem-view-current', {id: problem.id});
            });
        };
    });
    app.controller('contest-list', function ($scope, contestApi) {
        function toParam(obj) {
            var include = [], exclude = [];
            for (var i in obj) {
                hasOwnProperty.call(obj, i) && (obj[i] ? include : exclude).push(i);
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
    app.controller('contest-view', function ($stateParams, $scope, contestApi, title, $state, currentTime) {
        function load() {
            contestApi.get($stateParams, function (data) {
                var contest = $scope.contest = data;
                $scope.problems = data.problems;
                title(contest.title);
            });
        }
        $scope.fromCharCode = String.fromCharCode;
        load();
        $scope['delete'] = function (contest) {
            confirm('确认删除这场比赛？删除后将无法恢复。') && contestApi['delete']({id: contest.id}, function () {
                $state.go('contest-list');
            });
        };
        $scope.isStarted = function (contest) {
            return contest && (!contest.startTime || contest.startTime < currentTime());
        };
    });

    app.service('initContestEdit', function (problemApi) {
        function p(t) {
            return t < 10 ? '0' + t : t;
        }
        function toLength(contest) {
            var t = (contest.endTime - contest.startTime) / 1000 >> 0, s = t % 60, m = ((t /= 60) >> 0) % 60,
                    h = ((t /= 60) >> 0) % 24, d = (t / 24) >> 0;
            return (d ? d + ' ' : '') + h + ':' + p(m) + ':' + p(s);
        }
        return function ($scope, contest, min, save) {
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
            function getProblems(problems) {
                for (var res = [], i = 0, len = problems && problems.length; i < len; ++i) {
                    var p = problems[i];
                    res[i] = {origin: p.origin, title: p.title};
                }
                return res;
            }
            function reset() {
                $.extend(form, {
                    id: contest.id,
                    start: new Date(contest.startTime),
                    length: toLength(contest),
                    min: new Date(+min),
                    title: contest.title,
                    description: contest.description,
                    problems: getProblems(contest.problems)
                });
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
                    startTime: +form.start,
                    endTime: +form.end,
                    problems: form.problems
                });
            };
            $scope.change = function (problem, innerForm) {
                var code = {'404': 'Not found.'};
                var val = problem.origin;
                var validator = $.proxy(innerForm, '$setValidity', 'origin');
                if (/^[1-9][0-9]*$/.test(val)) {
                    validator(undefined);
                    problemApi.get({id: problem.origin}, function (p) {
                        validator(true);
                        problem.title = p.title;
                        problem.$message = '';
                    }).$promise['catch'](function (resp) {
                        validator(false);
                        problem.title = '';
                        problem.$message = code[resp.status] || resp.data && resp.data.error || 'Error';
                    });
                } else {
                    validator(false);
                    problem.title = '';
                    problem.$message = '';
                }
            };
            $scope.add = function () {
                $scope.form.problems.push({});
            };
            $scope.moveUp = function (i) {
                if (i > 0) {
                    var p = $scope.form.problems, t = p[i];
                    p[i] = p[i - 1];
                    p[i - 1] = t;
                }
            };
            $scope.moveDown = function (i) {
                var p = $scope.form.problems, t = p[i];
                if (i + 1 < p.length) {
                    p[i] = p[i + 1];
                    p[i + 1] = t;
                }
            };
            $scope.remove = function (i) {
                $scope.form.problems.splice(i, 1);
            };
            $scope.toProblemIndex = function (x) {
                return String.fromCharCode(x + 65);
            };
            reset();
            change();
        };
    });

    app.controller('contest-add', function ($scope, $state, contestApi, initContestEdit, currentTime) {
        var startTime = function () {
            var now = new Date(currentTime());
            now.setDate(now.getDate() + 1);
            now.setHours(12);
            now.setMinutes(0);
            now.setSeconds(0);
            return (now.getTime() / 1000 >> 0) * 1000;
        }();
        var contest = {
            startTime: startTime,
            endTime: startTime + 5 * 60 * 60 * 1000,
            title: '',
            description: '',
            contests: []
        };
        initContestEdit($scope, contest, new Date(currentTime()), function (form) {
            contestApi.save(form, function (contest) {
                $state.go('contest-view', {id: contest.id});
            });
        });
    });
    app.controller('contest-edit', function ($scope, $state, $stateParams, contestApi, title, initContestEdit, currentTime) {
        contestApi.get({id: $stateParams.id}, function (contest) {
            initContestEdit($scope, contest, Math.min(contest.startTime, currentTime()), function (form) {
                contestApi.update(form, function (contest) {
                    $state.go('contest-view', {id: contest.id});
                });
            });
            contest.title && title('编辑 - ' + contest.id + (contest.title ? ': ' + contest.title : ''));
        });
    });
    app.factory('uploadAccounts', function () {
        function set(t) {
            storage = $.isArray(t) ? t : [];
            localStorage.setItem('uploadAccounts', JSON.stringify(storage));
            $.each(storage, function (i, user) {
                user.$index = i + 1;
            });
        }
        var storage;
        set(function () {
            try {
                return JSON.parse(localStorage.getItem('uploadAccounts'));
            } catch (e) {
                return [];
            }
        }());
        return function (t) {
            return arguments.length ? set(t) : storage;
        };
    });
    app.controller('account-list', function ($scope, $stateParams, $timeout, reload, accountApi, $modal, returnIt, path, Upload, $state, Sort, uploadAccounts) {
        $scope.params = $.extend({}, $stateParams);
        function toReqParam(params) {
            return $.extend({}, params, {sort: sort.concat(EXTERN_ORDERS)});
        }
        function exportUrl() {
            var url = path.api + 'accounts.xlsx', p = $stateParams, parts = [];
            var kks = keys(p);
            kks.sort();
            for (var i = 0, klen = kks.length; i < klen; ++i) {
                var key = kks[i], v = p[key];
                if (v === null || typeof v === 'undefined')
                    continue;
                key = encodeURIComponent(key);
                v = [].concat(v);
                for (var j = 0, vlen = v.length; j < vlen; ++j) {
                    parts.push(key + '=' + encodeURIComponent(v[j]));
                }
            }
            return parts.length ? url + ((url.indexOf('?') === -1) ? '?' : '&') + parts.join('&') : url;
        }
        var EXTERN_ORDERS = ['solved,desc', 'submit'];
        var sort = Sort($stateParams.sort);
        $scope.push = function (order) {
            sort.unshift(order);
            reload({sort: sort.toArray(), page: null});
        };
        $timeout(function () {
            $scope.pageChanged = function () {
                reload({page: $scope.currentPage - 1});
            };
        });
        $scope.page = accountListCache;
        accountApi.query(toReqParam($stateParams), function (page) {
            $scope.page = accountListCache = page;
            $scope.currentPage = page.number + 1;
        });
        $scope.currentPage = (+$stateParams.page || 0) + 1;
        $scope.disable = function (user) {
            accountApi.update({id: user.id, disabled: true}, function () {
                user.disabled = true;
            });
        };
        $scope.enable = function (user) {
            accountApi.update({id: user.id, disabled: false}, function () {
                user.disabled = false;
            });
        };
        $scope.submit = function () {
            reload($.extend({}, $scope.params, {page: null}));
        };
        $scope.resetPassword = function (u) {
            $modal.open({
                templateUrl: 'account-password.html',
                controller: 'account-password',
                resolve: {
                    user: returnIt(u)
                }
            });
        };
        $scope.exportUrl = exportUrl();
        $scope.fileChange = function () {
            var files = $scope.files;
            files = files ? [].concat(files) : [];
            files.length && Upload.upload({
                url: path.api + 'accounts.json',
                data: {file: files},
                arrayKey: '',
                filename: files[0].name
            }).then(function (response) {
                uploadAccounts(response.data);
                $state.go('upload-account');
            });
            $scope.files = undefined;
        };
    });
    app.controller('account-password', function ($scope, accountApi, user, $modalInstance) {
        var id = user.id;
        $scope.user = {id: id, password: ''};
        $scope.submit = function () {
            $scope.user.password && accountApi.updatePassword($scope.user, function () {
                alert('重置成功');
                $modalInstance.dismiss();
            });
        };
        $scope.dismiss = function (reason) {
            $modalInstance.dismiss(reason || 'cancel');
        };
    });
    app.controller('account-import', function ($scope, PaginationService, accountApi, $state, Sort, reload, $stateParams, $timeout, uploadAccounts) {
        function getQueryParams(param) {
            var pas = 'page,sort,size'.split(','), q = {};
            for (var i in param) {
                if (hasOwnProperty.call(param, i)) {
                    $.inArray(i, pas) >= 0 || (q[i] = param[i]);
                }
            }
            return q;
        }
        if (veryslow && !$stateParams.size && !$stateParams.page) {
            reload({size: 20});
            return;
        }
        var count = $scope.count = (function () {
            var data = {}, originCase = {}, dataDuplicate = {};
            function toKey(x) {
                return ('' + x).toUpperCase();
            }
            return $.extend(function (x, y) {
                var key = toKey(x);
                x = originCase[key] = originCase[key] || x;
                switch (arguments.length) {
                    case 0:
                        return $.extend({}, data);
                    case 1:
                        return data[x] || 0;
                    default:
                        data[x] = +y || 0;
                }
            }, {
                add: function (x) {
                    var key = toKey(x);
                    x = originCase[key] = originCase[key] || x;
                    data[x] = (+data[x] || 0) >> 0;
                    if (++data[x] === 2) {
                        dataDuplicate[x] = data[x];
                    }
                }, remove: function (x) {
                    var key = toKey(x);
                    x = originCase[key] = originCase[key] || x;
                    if (!--data[x]) {
                        delete data[x];
                        delete originCase[key];
                        delete dataDuplicate[x];
                    } else if (data[x] === 1) {
                        delete dataDuplicate[x];
                    } else {
                        --dataDuplicate[x];
                    }
                }, duplicate: function () {
                    return keys(dataDuplicate).length;
                }
            });
        })();
        var users = $scope.users = uploadAccounts();
        var params = $scope.params = getQueryParams($stateParams);
        var page = $scope.page = PaginationService(users, $stateParams.size || 50, $stateParams.page, params);
        var sort = Sort($stateParams.sort);
        $scope.list = page.list();
        $scope.filteredElements = page.filteredElements();
        $scope.push = function (order) {
            sort.unshift(order);
            users.sort(sort.toComparator());
            page.setPage();
            reload({page: null, sort: sort.toArray()});
        };
        $timeout(function () {
            $scope.pageChanged = function (page, params) {
                reload($.extend(params || $stateParams, {page: page || null}));
            };
        });
        $scope.exists = 0;
        $.each(users, function (i, user) {
            user.exists && ++$scope.exists;
            count.add(user.id);
        });
        $scope.remove = function (user) {
            page.remove(user);
            $scope.list = page.list();
            $scope.filteredElements = page.filteredElements();
            count.remove(user.id);
            if (user.exists) {
                --$scope.exists;
            }
        };
        $scope['import'] = function () {
            var existsPolicy = $.map($scope.outputPolicy, function (e) {
                return e.type;
            });
            accountApi.saveAll({content: users, existsPolicy: existsPolicy}, function () {
                uploadAccounts([]);
                $state.go('account-list');
            });
        };
        $scope.existPolicy = [
            {name: '启用用户', type: 'ENABLE', selected: true},
            {name: '更新密码', type: 'RESET_PASSWORD'},
            {name: '重置用户信息', type: 'RESET_USERINFO'}
        ];
    });
    app.controller('page-index-changer', function ($scope, pageIndexApi, $sce) {
        $scope.pageIndex = pageIndexApi.get(function (pageIndex) {
            $('.page-index-content').html($sce.trustAsHtml(pageIndex.value));
            $scope.submit = function () {
                pageIndex.$update(function () {
                    alert('保存成功');
                });
            };
        });
    });
    app.run(function ($rootScope, currentTime) {
        function getStatus(contest) {
            var now = +currentTime();
            var disabled = contest.disabled;
            var started = !contest.startTime || contest.startTime < now;
            var ended = !contest.endTime || contest.endTime < now;
            var error = contest.startTime && contest.startTime > contest.endTime;
            return disabled ? 4 : error ? 3 : started && !ended ? 1 : ended ? 2 : 0;
        }
        var text = ['Pending', 'RUNNING', 'ENDED', 'ERROR', 'DISABLED'];
        var style = [{color: 'red'}, {color: 'blue'}, {color: 'green'}, {color: 'red'}, {color: 'darkred'}];
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

        // element.length || (element = $('<span class="back-to-top"></span>').appendTo('body'));

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
