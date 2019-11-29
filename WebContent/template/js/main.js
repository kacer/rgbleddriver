$(document).ready(function(){
	$('#colorpicker').ColorPicker({
	
		flat: true,
		onChange: function(hsb, hex, rgb) {
			switch(option) {
				case "single":
					setColorToLed(actualLed, new Rgb(rgb.r, rgb.g, rgb.b));
					break;
				case "all":
					let leds = $('#led-strip div');
					for(var i = 0; i < leds.length; i++) {
						let led = leds.eq(i);
						setColorToLed(led, new Rgb(rgb.r, rgb.g, rgb.b));
					}
					break;
				case "interpolate":
					let ledStrip = $('#led-strip div');
					setColorToLed(actualLed, new Rgb(rgb.r, rgb.g, rgb.b));
					let first = intToRgb(ledStrip.eq(0).children().val());
					let last = intToRgb(ledStrip.eq(ledStrip.length - 1).children().val());
					for(var i = 0; i <= ledStrip.length - 1; i++) {
						let t = i / (ledStrip.length - 1);
						let led = ledStrip.eq(i);
						let color = first.lerp(last, t);
						console.log(color.toString());
						setColorToLed(led, color);
					}
					break;
			}
		},
		onSubmit: function() {
			$('#colorpicker').fadeOut();
		}
	
	});
	
	$('#led-strip div').click(function() {
		actualLed = $(this);
		$('#colorpicker').ColorPickerSetColor(intToRgb(actualLed.children().val()));
		$('#colorpicker').fadeIn();
	});
	
	$('#btn-set').click(function() {
		let colors = [];
		let leds;
		let inputs = $('#led-strip div input');
		for(var i = 0; i < inputs.length; i++) {
			colors.push(new Led(inputs.eq(i).val()));
		}
	
		leds = new LedStrip(colors);
		
		$.ajax({
			type: "POST",
			url: "RequestServlet",
			contentType: "application/json",
			data: JSON.stringify(leds)
		});
	});
	
	$('input[name="color-mode"]').change(function() {
		option = $(this).val();
	});
	
	option = $('input[name="color-mode"]').val();
	
});

function rgbToInt(rgb) {
	return rgb.r << 16 | rgb.g << 8 | rgb.b;
}

function intToRgb(int) {
	var r = (int & 16711680) >> 16;
	var g = (int & 65280) >> 8;
	var b = int & 255;
	
	return new Rgb(r, g, b);
}

function setColorToLed(led, rgb) {
	led.children().val(rgbToInt(rgb));
	led.css({'background-color' : rgb.toString});
}

var actualLed;
var option;

var LedStrip = function(leds) {
	this.leds = leds;
}

var Led = function(color) {
	this.color = color;
}

var Rgb = function(r, g, b) {
	this.r = r;
	this.g = g;
	this.b = b;
	
	this.lerp = function(rgb, t) {
		return new Rgb(	r + (rgb.r - r) * t,
						g + (rgb.g - g) * t,
						b + (rgb.b - b) * t);
	}
	
	this.toString = function() {
		return "rgb(" + r + ", " + g + ", " + b + ")";
	}
};