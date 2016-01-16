Screen Switcher
=========

Screen Switcher is in its infancy. 
It's still being actively worked on, but the basic features are complete and working.

Bootstrap
-------
```java
public final class MainActivity extends Activity {

    // Use dependency injection to keep the state of the ScreenSwitcher as a Singleton.
    @Inject ScreenSwitcherState screenSwitcherState;
    // Use the presenter pattern for calling screen switcher methods throughout the app.
    @Inject ScreenManager screenManager;

    private ScreenSwitcher screenSwitcher;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize dependency injection here.
        
        screenSwitcher = ScreenSwitcherFactory.activityScreenSwitcher(this, screenSwitcherState);
        screenManager.take(screenSwitcher);
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        screenManager.drop(screenSwitcher);
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        // Disable touches while transitioning between screens.
        return screenSwitcher.isTransitioning() || super.dispatchTouchEvent(ev);
    }

    @Override public void onBackPressed() {
        // Only one transition can be executed at a time, swallow the back button if a transition
        // is already in progress.
        if (!screenSwitcher.isTransitioning()) {
            screenManager.pop();
        }
    }
}
```

License
-------

    Copyright 2016 Jay Newstrom

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

