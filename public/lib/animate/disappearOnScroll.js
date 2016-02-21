var app = angular.module('autoDisplay')
app.directive('disappearOnScroll', function() {
	return {
		restrict: 'A',
		scope: {

		},
		link: {
			post: function(scope, elem, attrs) {
				var initialHeight = parseInt(elem.css('height'));
				setTimeout(function() {
					initialHeight = parseInt(elem.css('height'));
				}, 500)
				var hide = attrs.hide==="true";

				var scrollPosition = $(window).scrollTop();
				var hidden = false;
				var factor = parseFloat(attrs.factor) ;
				elem.css('z-index', '-1')
				elem.css('overflow', 'hidden')
				elem.css('position', 'relative')
				$(window).on('scroll', function() {
					var newScrollPosition = $(window).scrollTop();
					if (!hidden) {
						var deltaScroll = newScrollPosition - scrollPosition;
						scrollPosition = newScrollPosition;
						if (initialHeight < newScrollPosition * factor && hide) {
							elem.css('display', 'none')
							hidden = true;
						} else {
							var newHeight = Math.min(initialHeight - newScrollPosition * factor + (newScrollPosition / 2) * factor, initialHeight);
							elem.css('height',  newHeight + 'px');
							//elem.css('padding-left',  initialHeight-newHeight + 'px');
							//elem.css('padding-right',  initialHeight-newHeight + 'px');
							elem.css('margin-top', (-newScrollPosition / 2) * factor + 'px');
						}
					}
				})
			}
		}
	}
})