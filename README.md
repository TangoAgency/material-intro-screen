# Android Material Intro Screen
 [ ![Download](https://api.bintray.com/packages/tangoagency/maven/material-intro-screen/images/download.svg) ](https://bintray.com/tangoagency/maven/material-intro-screen/_latestVersion)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/753f46972d8740d1984f8beb7d04fb9d)](https://www.codacy.com/app/TangoAgency/material-intro-screen?utm_source=github.com&utm_medium=referral&utm_content=TangoAgency/material-intro-screen&utm_campaign=badger)
[![Build Status](https://travis-ci.org/TangoAgency/material-intro-screen.svg?branch=master)](https://travis-ci.org/TangoAgency/material-intro-screen)
[![Android Arsenal Material Intro Screen](https://img.shields.io/badge/Android%20Arsenal-Material--Intro--Screen-green.svg?style=true)](http://android-arsenal.com/details/1/4368)

Material intro screen is inspired by [Material Intro] and developed with love from scratch. I decided to rewrite completely almost all features in order to make Android intro screen easy to use for everyone and extensible as possible.
## Features
  - [Easily add new slides][Intro Activity]
  - [Custom slides][Custom Slide]
  - [Parallax slides][Parallax Slide]
  - Easy and extensible api
  - Android TV support!
  - Material design at it's best!!!

| [Simple slide][SimpleSlide] | [Custom slide][Custom Slide] | [Permission slide][PermissionSlide] | [Finish slide][FinishSlide]
|:-:|:-:|:-:|:-:|
| ![Simple slide] | ![Customslide] | ![Permission slide] | ![Finish slide] |

## Usage
### Step 1:
#### Add gradle dependecy
```
dependencies {
  compile 'agency.tango.android:material-intro-screen:{latest_release}'
}
```
### Step 2:
#### First, your [intro activity][Intro Activity] class needs to extend MaterialIntroActivity:
```java
public class IntroActivity extends MaterialIntroActivity
```
### Step 3:
#### Add activity to [manifest][Manifest] with defined theme:
```xml
        <activity
            android:name=".IntroActivity"
            android:theme="@style/Theme.MaterialIntro" />
```
### Step 4: 
#### [Add slides:][Intro Activity]
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
                .grantPermissionMessage(R.string.txt_pls_grant_permission)
                .grantPermissionError(R.string.txt_grant_permission_error)
                .messageButtonColor(R.color.third_slide_buttons)
                .messageButtonTextColor(R.color.white)
                .title("title 3")
                .description("Description 3")
                .build(),
              new MessageButtonBehaviour(new MessageButtonClickListener() {
                  @Override
                  public void onClick(Button messageButton) {
                      messageButton.setText("Click me once again!");
                      showMessage("We provide solutions to make you love your work");
                  }
              }, "Work with love"));
}
```
#### Explanation of SlideFragment usage:
  - ```possiblePermissions``` &#8702; permissions which are not necessary to be granted
  - ```neededPersmissions``` &#8702; permission which are needed to be granted to move further from that slide
  - ```MessageButtonBehaviour``` &#8702; create a new instance only if you want to have a custom action or text on a message button

### Step 5: 
#### Customize Intro Activity:
  - ```setSkipButtonVisible()``` &#8702; show skip button instead of back button on the left bottom of screen
  - ```hideBackButton()``` &#8702; hides any button on the left bottom of screen
  - ```enableLastSlideAlphaExitTransition()``` &#8702; set if the last slide should disapear with alpha hiding effect
  - ```onLastSlidePassed``` &#8702; Override in order to perform some action after passing last slide

#### Customizing view animations: 

You can set enter, default and exit translation for every view in intro activity. To achive this you need to get translation wrapper for chosen view (for example: ```getNextButtonTranslationWrapper()```) and set there new class which will implement ```IViewTranslation```
```java
getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });
```
#### Available [translation wrappers][TranslationWrapper]:
- ```getNextButtonTranslationWrapper()```
- ```getBackButtonTranslationWrapper()```
- ```getPageIndicatorTranslationWrapper()```
- ```getViewPagerTranslationWrapper()``` 
- ```getSkipButtonTranslationWrapper()``` 

## Custom slides
#### Of course you are able to implement completely custom slides. You only need to extend SlideFragmentBase and override all needed by you functions.
 - ```backgroundColor()```
 - ```buttonsColor()```
 - ```canMoveFurther()``` (only if you want to stop user from being able to move further before he will do some action)
 - ```cantMoveFurtherErrorMessage()``` (as above)
 - ```neededPermissions()```
 - ```possiblePermissions()```
 - ```grantPermissionStringRes()```
 - ```grantPermissionErrorStringRes()```
 - ```messageButtonColor()```
 - ```messageButtonTextColor()```
   
#### If you want to use parallax in a fragment please use one of the below views:
  - [```ParallaxFrameLayout```][ParallaxFrame]
  - [```ParallaxLinearLayout```][ParallaxLinear]
  - [```ParallaxRelativeLayout```][ParallaxRelative]

#### And set there the [app:mis_layout_parallaxFactor][ParallaxFactor] attribute:
```xml
<agency.tango.materialintroscreen.parallax.ParallaxLinearLayout
xmlns:android="http://schemas.android.com/apk/res/android">

    <ImageView
        android:id="@+id/image_slide"
        app:mis_layout_parallaxFactor="0.6"/>
```

All features which are not available in simple Slide Fragment are shown here: [Custom Slide]

## Things I have used to create this
 - For parallax I have used files from [Material Intro] by [@HeinrichReimer]
 - [InkPageIndicator.java] by [@NickButcher]
 - Images used to create sample app are from [freepik]
 - For over scroll effect on last slide I have partially used [Android-Overscroll-ViewPager]
 
## Getting Help

To report a specific problem or feature request, [open a new issue on Github](https://github.com/TangoAgency/material-intro-screen/issues/new).

## Company
 
[![Facebook](https://github.com/TangoAgency/material-intro-screen/blob/master/images/facebook.png)](https://www.facebook.com/TangoDigitalAgency)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[![Twitter](https://github.com/TangoAgency/material-intro-screen/blob/master/images/twitter.png)](https://twitter.com/Tango_Agency)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[![LinkedIn](https://github.com/TangoAgency/material-intro-screen/blob/master/images/linkedin.png)](https://www.linkedin.com/company/tango-digital-agency)

[Here](https://github.com/TangoAgency/) you can see open source work developed by Tango Agency.
 
Whether you're searching for a new partner or trusted team for creating your new great product we are always ready to start work with you. 

You can contact us via contact@tango.agency.
Thanks in advance.
 
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
[Intro Activity]: <https://github.com/TangoAgency/material-intro-screen/blob/master/app/src/main/java/agency/tango/materialintro/IntroActivity.java>
[Parallax Slide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/app/src/main/res/layout/fragment_custom_slide.xml>
[PermissionSlide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/app/src/main/java/agency/tango/materialintro/IntroActivity.java#L52>
[FinishSlide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/app/src/main/java/agency/tango/materialintro/IntroActivity.java#L19>
[SimpleSlide]: <https://github.com/TangoAgency/material-intro-screen/blob/master/app/src/main/java/agency/tango/materialintro/IntroActivity.java#L43>
[ParallaxFrame]: <https://github.com/TangoAgency/material-intro-screen/blob/master/material-intro-screen/src/main/java/agency/tango/materialintroscreen/parallax/ParallaxFrameLayout.java>
[ParallaxLinear]: <https://github.com/TangoAgency/material-intro-screen/blob/master/material-intro-screen/src/main/java/agency/tango/materialintroscreen/parallax/ParallaxLinearLayout.java>
[ParallaxRelative]: <https://github.com/TangoAgency/material-intro-screen/blob/master/material-intro-screen/src/main/java/agency/tango/materialintroscreen/parallax/ParallaxRelativeLayout.java>
[ParallaxFactor]: <https://github.com/TangoAgency/material-intro-screen/blob/master/material-intro-screen/src/main/res/layout/fragment_slide.xml#L29>
[Manifest]: <https://github.com/TangoAgency/material-intro-screen/blob/master/app/src/main/AndroidManifest.xml#L28>
[TranslationWrapper]: <https://github.com/TangoAgency/material-intro-screen/blob/master/material-intro-screen/src/main/java/agency/tango/materialintroscreen/animations/ViewTranslationWrapper.java>
[Android-Overscroll-ViewPager]: <https://github.com/iamjiex/Android-Overscroll-ViewPager>