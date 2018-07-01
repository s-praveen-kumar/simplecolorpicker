# simplecolorpicker
A simple and customizable color picker library for Android

# To add to project
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.s-praveen-kumar:simplecolorpicker:-SNAPSHOT'
	}
  
# Usage
## Basic use:
	new ColorPickerDialog.Builder(context).setPresets(presets).setCurrentColor(currentColor).setTitle(title).setShowAlphaBar(shouldShowAlphaBar).setListener(new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
	    	//Use picked color
            }

            @Override
            public void onCancelled() {

            }
        }).show();
   
`presets` int[] - Int array containing the preset colors. (Optional) If unspecified, a default preset is used.
`currentColor` int - The presently chosen color(if any). This will be selected by default in the custom color picker
`title` String  - Title for the ColorPicker dialog
`shouldShowAlphaBar` boolean - Whether or not, the colorPicker should allow transparency (defualt : true)

It is not required to use all these setters. If some are not used, the default values are used. And don't forget to call `show()` at the end.

## More customization:
For complete customization, the individual views are available and they can be tweaked to create custom color pickers.

# Bonus:
This also includes the whole set of material colors in the resources. To use just use R.color.red_500, R.color.pinkA700 etc.
In Xml, @color/red_500, @color/pinkA700.
