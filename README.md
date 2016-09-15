# Android Material Intro Screen
Material intro screen is inspired by [Material Intro] and developed with love from scratch. I decided to rewrite completely almost all features in order to make Android intro screen easy to use for everyone and extensible as possible.
## Features
  - Easily add new slides
  - Custom slides
  - Parallax slides
  - Easy extensible api
  - Material design at it's best!!!

| Simple slide | Custom slide | Permission slide | Finish slide
|:-:|:-:|:-:|:-:|
| ![Simple slide] | ![Customslide] | ![Permission slide] | ![Finish slide] |

## Usage
### Step 1:
 [ ![Download](https://api.bintray.com/packages/tangoagency/maven/material-intro-screen/images/download.svg) ](https://bintray.com/tangoagency/maven/material-intro-screen/_latestVersion)

#### Add gradle dependecy
```compile 'agency.tango.android:material-intro-screen:0.0.1'```

### Step 2:
First, your intro activity class needs to extend MaterialIntroActivity:
```java
public class IntroActivity extends MaterialIntroActivity
```
### Step 3:
Add activity to manifest with defined theme:
```xml
        <activity
            android:name=".IntroActivity"
            android:theme="@style/Theme.Intro" />
```
### Step 4: 
Add slides:
```java
 @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorAccent)
                .possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                .neededPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
                .image(agency.tango.materialintroscreen.R.drawable.ic_next)
                .title("title 3")
                .description("Description 3")
                .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(IntroActivity.this, "We provide solutions to make you love your work", Toast.LENGTH_SHORT).show();
                    }
                }, "Work with love"));
}
```
Explanation of SlideFragment usage:
  - ```possiblePermissions``` &#8702; permissions which are not necessary to be granted
  - ```neededPersmissions``` &#8702; permission which are needed to be granted to move further from that slide
  - ```MessageButtonBehaviour``` &#8702; create a new instance only if you want to have a custom action or text on a message button

### Step 5: 
Customize Intro Activity:
  - ```setSkipButtonVisible()``` &#8702; show skip button instead of back button on the left bottom of screen
  - ```hideBackButton()``` &#8702; hides any button on the left bottom of screen
  - ```enableLastSlideAlphaExitTransition()``` &#8702; set if the last slide should disapear with alpha hiding effect

Customizing view animations: 

You can set enter, default and exit translation for every view in intro activity. To achive this you need to get translation wrapper for chosen view (for example: ```getNextButtonTranslationWrapper()```) and set there new class which will implement ```IViewTranslation```
```java
getNextButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });
```
Available translation wrappers:
- ```getNextButtonTranslationWrapper()```
- ```getBackButtonTranslationWrapper()```
- ```getPageIndicatorTranslationWrapper()```
- ```getViewPagerTranslationWrapper()``` 
- ```getSkipButtonTranslationWrapper()``` 

## Custom slides
Of course you are able to implement completely custom slides. You only need to extend SlideFragment and override following functions:
 - ```backgroundColor()```
 - ```buttonsColor()```
 - ```canMoveFurther()``` (only if you want to stop user from being able to move further before he will do some action)
 - ```cantMoveFurtherErrorMessage()``` (as above)
 
If you want to use parallax in a fragment please use one of the below views:
  - ```ParallaxFrameLayout```
  - ```ParallaxLinearLayout```
  - ```ParallaxRelativeLayout```

And set there the app:layout_parallaxFactor attribute:
```xml
<agency.tango.materialintroscreen.parallax.ParallaxLinearLayout
xmlns:android="http://schemas.android.com/apk/res/android">

    <ImageView
        android:id="@+id/image_slide"
        app:layout_parallaxFactor="0.6"/>
```

All features which are not available in simple Slide Fragment are shown here: [Custom Slide]

## Things I have used to create this
 - For parallax I have used files from [Material Intro] by [@HeinrichReimer]
 - [InkPageIndicator.java] by [@NickButcher]
 - Images used to create sample app are from [freepik]
 
[Custom Slide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/app/src/main/java/agency/tango/materialintro/CustomSlide.java>
[Material Intro]: <https://github.com/HeinrichReimer/material-intro/tree/master/library/src/main/java/com/heinrichreimersoftware/materialintro/view/parallax>
[@HeinrichReimer]: <https://github.com/HeinrichReimer>
[InkPageIndicator.java]: <https://github.com/nickbutcher/plaid/blob/master/app/src/main/java/io/plaidapp/ui/widget/InkPageIndicator.java>
[@NickButcher]: <https://github.com/nickbutcher>
[freepik]: <http://www.freepik.com/>
[Simple slide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/images/simple_slide.gif>
[Customslide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/images/custom_slide.gif>
[Permission slide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/images/permissions_slide.gif>
[Finish slide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/images/finish_slide.gif>
