function SearchController($scope, $routeParams, $http, $searchService, $location) {

  $scope.query = "" + $routeParams.query;

  $scope.searchParameterLogic = "or"; // and, or

  $scope.searchParameterId = true;
  $scope.searchParameterName = true;
  $scope.searchParameterLabel = false;
  $scope.searchParameterDescription = false;

  $scope.searchParameterSort = "NAME";

  /*
   * ***********************************************************
   */
  $scope.advancedSearchHeight = 0;
  $scope.advancedSearchIconClass = "icon-circle-arrow-down";

  $scope.getAdvancedSearchStyle = function () {
    return {
      height: $scope.advancedSearchHeight + "px"
    };
  }

  $scope.toggleAdvancedSearch = function () {
    if ($scope.advancedSearchHeight == 0) {
      $scope.advancedSearchHeight = 80;
      $scope.advancedSearchIconClass = "icon-circle-arrow-up";
    } else {
      $scope.advancedSearchHeight = 0;
      $scope.advancedSearchIconClass = "icon-circle-arrow-down";
    }
  }

  /*
   * ***********************************************************
   */

  $scope.doSearch = function () {
    $location.path("/search/" + $scope.query);
    var params = $searchService.createDefaultParameter();
    params.query = $scope.query;
    params.searchParameterDescription = $scope.searchParameterDescription;
    params.searchParameterId = $scope.searchParameterId;
    params.searchParameterLabel = $scope.searchParameterLabel;
    params.searchParameterLogic = $scope.searchParameterLogic;
    params.searchParameterName = $scope.searchParameterName;
    params.searchParameterSort = $scope.searchParameterSort;
    $searchService.doSearch(params, function(result) {
      $scope.searchResult = result;
    });

  }

  var placeFocusOnInputElement = function () {
    var inputElement = document.getElementById("input-search-query");
    if (inputElement) {
      inputElement.focus();
    }
  };
  $scope.$on("$viewContentLoaded", placeFocusOnInputElement);

  if ($scope.query.length > 0) {
    $scope.doSearch();
  }
}
