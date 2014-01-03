mkat.factory('$searchService', ["$http", function ($http) {

  var searchResult = {"filme": []};

  return {

    createDefaultParameter: function () {
      return {
        query: "",
        searchParameterId: false,
        searchParameterName: false,
        searchParameterLabel: false,
        searchParameterDescription: false,
        searchParameterLogic: "OR",
        searchParameterSort: "NAME"
      };
    },

    getMovieById: function (movieId, successCallback) {
      function findInCurrentSearchResult() {
        for (var i = 0; i < searchResult.filme.length; i++) {
          var film = searchResult.filme[i];
          if (film.id == movieId) {
            return film;
          }
        }
        return undefined;
      }

      var foundMovie = findInCurrentSearchResult();
      if (foundMovie) {
        successCallback(foundMovie);
        return;
      }

      var params = this.createDefaultParameter();
      params.query = movieId;
      params.searchParameterId = true;
      this.doSearch(params, function (result) {
        var foundMovie = findInCurrentSearchResult();
        if (foundMovie) {
          successCallback(foundMovie);
        }
      });
    },

    doSearch: function (searchServiceParameter, successCallback) {

      var defaultParams = this.createDefaultParameter();
      var params = searchServiceParameter ? searchServiceParameter : defaultParams;

      var fields = "";
      fields += params.searchParameterId ? "ID," : "";
      fields += params.searchParameterName ? "NAME," : "";
      fields += params.searchParameterLabel ? "LABEL," : "";
      fields += params.searchParameterDescription ? "DESCRIPTION," : "";

      var getHttpConfig = {
        method: "GET",
        url: 'search/' + params.query,
        params: {
          "kw": params.searchParameterLogic,
          "order": params.searchParameterSort,
          "fields": fields
        }
      };
      $http(getHttpConfig).success(function (data) {
        searchResult = data;
        successCallback(data);
      });
    },

    getSearchResult: function () {
      return searchResult;
    }
  }

}])
;
