function DiskContentController($scope, $diskContentService, $routeParams) {

  $scope.diskId = $routeParams.diskId;
  $scope.diskContent = {
    "files": [],
    "filesLength": 0,
    "diskId" : ""
  };

  $scope.getDiskContentById = function() {
    $diskContentService.getDiskContentById($scope.diskId, function(diskContent) {
      $scope.diskContent = diskContent;
    });
  }

  $scope.getDiskContentById();
}
