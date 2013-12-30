var mkat = angular.module('mkat', []).
   config(['$routeProvider', function ($routeProvider) {
     $routeProvider.
        when('/uebersicht', {templateUrl: 'partials/uebersicht.html', controller: "UebersichtController"}).
        when('/search/:query', {templateUrl: 'partials/search.html', controller: "SearchController"}).
        when('/description/:movieId', {templateUrl: 'partials/description.html', controller: "DescriptionController"}).
        when('/disk/:diskId', {templateUrl: 'partials/diskcontent.html', controller: "DiskContentController"}).
        otherwise({redirectTo: '/uebersicht'});
   }]);

mkat.directive("hrefCondition", function factory() {
  var directiveDefinitionObject = {
    restrict: 'A',
    link: function (scope, element, attrs) {
      var evaluatedCondition = scope.$eval(element.attr('href-condition'));
      if (!(typeof evaluatedCondition == 'undefined' || evaluatedCondition.length == 0)) {
        attrs.$set('href', scope.$eval(element.attr('href-value')));
      } else {
        attrs.$set('rel', 'nofollow');
      }
    }
  };
  return directiveDefinitionObject;
});
