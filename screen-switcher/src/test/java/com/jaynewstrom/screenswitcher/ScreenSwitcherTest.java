package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class ScreenSwitcherTest {

    @Test public void transitionIsNotCalledWhenCreatingScreenSwitcher() {
        Screen screen = mock(Screen.class);
        ScreenTestUtils.mockCreateView(screen);
        Activity activity = mock(Activity.class);
        ScreenTestUtils.testScreenSwitcher(activity, ScreenTestUtils.defaultState(screen));
        verify(screen).createView(any(Context.class), any(ViewGroup.class));
        verify(screen, never()).transition();
    }

    @Test public void allButTopScreenIsGoneWhenCreatingScreenSwitcher() {
        Activity activity = mock(Activity.class);
        Screen bottomScreen = mock(Screen.class);
        View bottomView = mock(View.class);
        when(bottomScreen.createView(any(Context.class), any(ViewGroup.class))).thenReturn(bottomView);
        Screen topScreen = mock(Screen.class);
        View topView = mock(View.class);
        when(topScreen.createView(any(Context.class), any(ViewGroup.class))).thenReturn(topView);
        ScreenTestUtils.testScreenSwitcher(activity, ScreenTestUtils.defaultState(Arrays.asList(bottomScreen, topScreen)));
        verify(bottomScreen).createView(any(Context.class), any(ViewGroup.class));
        verify(topScreen).createView(any(Context.class), any(ViewGroup.class));
        verify(bottomView).setVisibility(View.GONE);
    }
}
