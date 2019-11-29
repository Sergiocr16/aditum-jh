var ColorPicker = function() {

    var handleColorPicker = function() {
        if (!jQuery().colorpicker) {
            return;
        }
        $('.colorpicker-default').colorpicker({
            format: 'hex'
        });
        $('.colorpicker-rgba').colorpicker();
    }


    return {
        //main function to initiate the module
        init: function() {
            handleColorPicker();
        }
    };

}();
