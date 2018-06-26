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

    ColorPickerDialog.showColorDialog(Context c, String title, new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
              // color - The selected color
            }

            @Override
            public void onCancelled() {

            }
        });
        
   Title is the title of the Color picker dialog.
   
## Few customization:
   
    ColorPickerDialog.showColorDialog(Context c, String title, int[] presets, , boolean supportTransparency ,int currentColor, new ColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
              // color - the selected color
            }

            @Override
            public void onCancelled() {

            }
        });
        
`title` is the title of the color picker dialog

`presets` is an int array of custom pre-defined colors available to the user.

`supportTransparency`- If set false, takes presets without alpha component ( 0xffffff over 0xffffffff). If set as true, the preset provided must be in 0xaarrggbb format.
      in most cases, false will work.
      
`currentColor` is the currently used color. This color is shown by default in the custom color picker.

Take a look at the demo app's code for more info
        
## More customization:
For complete customization, the individual views are available and they can be tweaked to create custom color pickers.

# Bonus:
This also includes the whole set of material colors in the resources. To use just use R.color.red_500, R.color.pinkA700 etc.
In Xml, @color/red_500, @color/pinkA700.
