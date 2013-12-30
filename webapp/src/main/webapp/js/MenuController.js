function MenuController($scope, $location) {

  $scope.isActiveMenu = function(menuName) {
    var path = $location.path();
    while (path[0] == "/") path = path.substr(1);
    path = path.substr(0, Math.min(menuName.length, path.length));
    if (path == menuName) return 'active';
    return '';
  }
};