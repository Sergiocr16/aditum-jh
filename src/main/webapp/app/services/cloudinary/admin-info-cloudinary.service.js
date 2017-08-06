(function() {
    'use strict';


    angular
        .module('aditumApp')
        .factory('SaveImageCloudinary', SaveImageCloudinary);

    SaveImageCloudinary.$inject = ['CloudinaryService'];

    function SaveImageCloudinary (CloudinaryService) {
        var USER_TAG = "USER";
        var service = {
            save: save
        };
        return service;

        function save (file, image) {
            var TAGS = [USER_TAG, "User: " + image.user];
            return CloudinaryService.uploadFile(file, TAGS)
                .then(onSuccess);

            function onSuccess(response) {
                var height        = response.data.height;
                var width         = response.data.width;
                image.imageUrl    = response.data.public_id;
                image.aspectRatio = height / width;

                return image;
            }
        }


    }
})();
