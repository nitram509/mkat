
function UebersichtController($scope, $http) {

  $http.get('rest/uebersicht').success(function(data) {
    $scope.uebersicht = data;
  });

}
