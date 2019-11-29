(function() {
    'use strict';

    angular
        .module('aditumApp')
        .config(CloudinaryConfig);

    CloudinaryConfig.$inject = ['cloudinaryProvider'];

    function CloudinaryConfig(cloudinaryProvider) {
        cloudinaryProvider
            .set("cloud_name", "aditum")
            .set("upload_preset", "ozhexlcp")
            .set("base_url", "	https://api.cloudinary.com/v1_1/")
            .set("image_url", "http://res.cloudinary.com/");
    }
})();
