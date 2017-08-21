/*!
 * jQuery TextChange Plugin
 * http://www.zurb.com/playground/jquery-text-change-custom-event
 *
 * Copyright 2010, ZURB
 * Released under the MIT License
 */
(function ($) {

	$.event.special.textchange = {

		setup: function (data, namespaces) {
			$(this).bind('keyup.textchange', $.event.special.textchange.handler);
			$(this).bind('cut.textchange paste.textchange input.textchange', $.event.special.textchange.delayedHandler);
		},

		teardown: function (namespaces) {
			$(this).unbind('.textchange');
		},

		handler: function (event) {
			var element = $(event.target || this);
			$.event.special.textchange.triggerIfChanged(element);
		},

		delayedHandler: function (event) {
			var element = $(event.target || this);
			setTimeout(function () {
				$.event.special.textchange.triggerIfChanged(element);
			});
		},

		triggerIfChanged: function (element) {
			var current = element[0].contentEditable === 'true' ? element.html() : element.val(), lastValue = element.data('lastValue');
			if (current !== lastValue) {
				element.trigger('textchange', [lastValue]);
				element.data('lastValue', current);
			}
		}
	};

	$.event.special.hastext = {

		setup: function (data, namespaces) {
			$(this).bind('textchange', $.event.special.hastext.handler);
		},

		teardown: function (namespaces) {
			$(this).unbind('textchange', $.event.special.hastext.handler);
		},

		handler: function (event, lastValue) {
			if ((lastValue === '') && lastValue !== $(this).val()) {
				$(this).trigger('hastext');
			}
		}
	};

	$.event.special.notext = {

		setup: function (data, namespaces) {
			$(this).bind('textchange', $.event.special.notext.handler);
		},

		teardown: function (namespaces) {
			$(this).unbind('textchange', $.event.special.notext.handler);
		},

		handler: function (event, lastValue) {
			if ($(this).val() === '' && $(this).val() !== lastValue) {
				$(this).trigger('notext');
			}
		}
	};

})(jQuery);
