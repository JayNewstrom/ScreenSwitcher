package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.view.View;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class ActivityScreenSwitcherTest {

    @Test public void transitionIsNotCalledWhenCreatingScreenSwitcher() {
        Screen screen = mock(Screen.class);
        Activity activity = mock(Activity.class);
        new ActivityScreenSwitcher(activity, new ScreenSwitcherState(Collections.singletonList(screen)));
        verify(screen).createView(activity);
        verify(screen, never()).transition();
    }

    @Test public void allButTopScreenIsGoneWhenCreatingScreenSwitcher() {
        Activity activity = mock(Activity.class);
        Screen bottomScreen = mock(Screen.class);
        View bottomView = mock(View.class);
        when(bottomScreen.createView(activity)).thenReturn(bottomView);
        Screen topScreen = mock(Screen.class);
        View topView = mock(View.class);
        when(topScreen.createView(activity)).thenReturn(topView);
        new ActivityScreenSwitcher(activity, new ScreenSwitcherState(Arrays.asList(bottomScreen, topScreen)));
        verify(bottomScreen).createView(activity);
        verify(topScreen).createView(activity);
        verify(bottomView).setVisibility(View.GONE);
    }
}
