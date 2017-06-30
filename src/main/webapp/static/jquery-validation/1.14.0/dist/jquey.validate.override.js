(function( factory ) {
	if ( typeof define === "function" && define.amd ) {
		define( ["jquery", "../jquery.validate"], factory );
	} else {
		factory( jQuery );
	}
}(function( $ ) {

jQuery.validator.setDefaults({
	    highlight: function(element) {
	        jQuery(element).closest('.form-group').addClass('has-error');
	    },
	    unhighlight: function(element) {
	        jQuery(element).closest('.form-group').removeClass('has-error');
	    },
	    errorElement: 'span',
	    errorClass: 'label label-danger',
	    errorPlacement: function(error, element) {
		
			if (element.parent('.input-group').length || element.prop('type') === 'checkbox' || element.prop('type') === 'radio') {
	            error.insertAfter(element.parent());
	        } else {
	            error.insertAfter(element);
	        }
	       
	    }
	});

}));