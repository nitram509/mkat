function DescriptionController($scope, $searchService, $routeParams) {

  $scope.movieId = $routeParams.movieId;
  $scope.movie = {
    "name" : "",
    "id" : "",
    "description" : ""
  };

  $scope.getMovieDescription = function() {
    $searchService.getMovieById($scope.movieId, function(movie) {
      $scope.movie = movie;
    });
  }

  $scope.getMovieDescription();
}
