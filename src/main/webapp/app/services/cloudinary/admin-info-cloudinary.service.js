(function () {
    'use strict';


    angular
        .module('aditumApp')
        .factory('SaveImageCloudinary', SaveImageCloudinary);

    SaveImageCloudinary.$inject = ['CloudinaryService'];

    function SaveImageCloudinary(CloudinaryService) {
        var USER_TAG = "USER";
        var ANNOUNCEMENT_TAG = "ANNOUNCEMENT_TAG";

        var service = {
            save: save,
            saveAnnouncement: saveAnnouncement
        };
        return service;

        function save(file, image) {
            if (image != undefined) {
                var TAGS = [USER_TAG, "User: " + image.user];
            } else {
                var TAGS = [USER_TAG, "User: "];
            }

            return CloudinaryService.uploadFile(file, TAGS)
                .then(onSuccess);

            function onSuccess(response) {
                var height = response.data.height;
                var width = response.data.width;
                var image = {};
                image.imageUrl = response.data.public_id;
                // image.aspectRatio = height / width;
                return image;
            }
        }

        function saveAnnouncement(file, image) {
            var TAGS = [ANNOUNCEMENT_TAG, "Announcement: " + image.title];
            return CloudinaryService.uploadFile(file, TAGS)
                .then(onSuccess);

            function onSuccess(response) {
                var height = response.data.height;
                var width = response.data.width;
                image.imageUrl = response.data.public_id;
                image.aspectRatio = height / width;

                return image;
            }
        }


    }
})();
