
function UebersichtController($scope, $http) {

  $http.get('uebersicht').success(function(data) {
    $scope.uebersicht = data;
  });

}
