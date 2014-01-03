mkat.factory('$diskContentService', ["$http", function ($http) {

  var diskContent = {
    "files": [],
    "filesLength": 0,
    "diskId": ""
  };

  function enrichResult(diskContentHttpResult) {
    var files = diskContentHttpResult.files;
    for (var i = 0; i < files.length; i++) {
      var file = files[i];
      var dt = new Date(file['datetime']);
      file.localeDateString = dt.toLocaleDateString();
      file.localeString = dt.toLocaleString();
      file.localeSize = parseInt(file['size']).toLocaleString();
      if ('true' == file['isdir']) {
        file.iconClass = 'icon-folder-open'
      } else {
        file.iconClass = 'icon-file'
      }
      file.identClass = "ident" + (file['name'].replace( /[^/]+/g, '' ).length); // number of Slashes
      file.baseName = (/[^/]+$/).exec(file['name'])[0]
    }
  }

  return {

    getDiskContentById: function (diskId, successCallback) {
      var getHttpConfig = {
        method: "GET",
        url: 'disk/' + diskId
      };
      $http(getHttpConfig).success(function (data) {
        enrichResult(data);
        diskContent = data;
        successCallback(data);
      });
    }

  }

}]);
