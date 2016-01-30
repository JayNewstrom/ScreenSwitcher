Bootstrap
=========

Add screen switcher to your activity.
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

Use the presenter pattern to delegate calls to a `ScreenSwitcher`.
```java
public final class ScreenManager {

    private ScreenSwitcher screenSwitcher;

    private final ScreenSwitcherState screenSwitcherState;

    ScreenManager(ScreenSwitcherState screenSwitcherState) {
        this.screenSwitcherState = screenSwitcherState;
    }

    boolean isSameImplementation(ScreenSwitcher screenSwitcher) {
        return this.screenSwitcher == screenSwitcher;
    }

    void take(ScreenSwitcher screenSwitcher) {
        this.screenSwitcher = screenSwitcher;
    }

    void drop(ScreenSwitcher screenSwitcher) {
        if (isSameImplementation(screenSwitcher)) {
            this.screenSwitcher = null;
        }
    }

    public void pop(@IntRange(from = 1) int numberToPop) {
        if (screenSwitcher != null) {
            screenSwitcher.pop(numberToPop);
        }
    }

    public void push(Screen screen) {
        if (screenSwitcher != null) {
            screenSwitcher.push(screen);
        }
    }
}
```

Change screens by calling a screen switcher method (through the `ScreenManager` delegate).
```java
final class FirstView extends LinearLayout {

    @Inject ScreenManager screenManager;

    FirstView(Context context) {
        super(context);
        setOrientation(VERTICAL);
        setBackgroundResource(android.R.color.white);
        
        // Initialize dependencies using dependency injection.
        
        LayoutInflater.from(context).inflate(R.layout.first_view, this, true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_second) void onSecondScreenButtonClicked() {
        screenManager.push(new SecondScreen());
    }
}
```

In order to prevent overdraw, your activity should have a null background, and each screen should specify a background.
An example application theme is given below:
```xml 
<resources>

    <style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="android:windowBackground">@null</item>
    </style>
</resources>
```
