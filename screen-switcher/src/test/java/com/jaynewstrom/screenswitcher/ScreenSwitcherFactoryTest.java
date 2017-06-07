package com.jaynewstrom.screenswitcher;

import android.app.Activity;
import android.view.ViewGroup;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class ScreenSwitcherFactoryTest {

    private Activity activity;
    private ViewGroup view;
    private ScreenSwitcherState state;
    private ScreenSwitcherPopHandler popHandler;

    @Before public void setup() {
        activity = mock(Activity.class);
        when(activity.findViewById(android.R.id.content)).thenReturn(mock(ViewGroup.class));
        view = mock(ViewGroup.class);
        when(view.getContext()).thenReturn(activity);
        Screen screen = mock(Screen.class);
        ScreenTestUtils.mockCreateView(screen);
        state = new ScreenSwitcherState(Collections.singletonList(screen));
        popHandler = mock(ScreenSwitcherPopHandler.class);
    }

    @Test public void activityScreenSwitcherRejectsNullActivity() {
        try {
            //noinspection ConstantConditions
            ScreenSwitcherFactory.activityScreenSwitcher(null, state, popHandler);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("activity == null");
        }
    }

    @Test public void activityScreenSwitcherRejectsNullState() {
        try {
            //noinspection ConstantConditions
            ScreenSwitcherFactory.activityScreenSwitcher(activity, null, popHandler);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("state == null");
        }
    }

    @Test public void activityScreenSwitcherRejectsStateWithNoScreens() {
        try {
            state.getScreens().remove(0);
            ScreenSwitcherFactory.activityScreenSwitcher(activity, state, popHandler);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("state needs screens in order to initialize a ScreenSwitcher");
        }
    }

    @Test public void activityScreenSwitcherRejectsNullPopHandler() {
        try {
            //noinspection ConstantConditions
            ScreenSwitcherFactory.activityScreenSwitcher(activity, state, null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("popHandler == null");
        }
    }

    @Test public void activityScreenSwitcherIsCreated() {
        ScreenSwitcher screenSwitcher = ScreenSwitcherFactory.activityScreenSwitcher(activity, state, popHandler);
        assertThat(screenSwitcher).isNotNull();
    }

    @Test public void viewScreenSwitcherRejectsNullView() {
        try {
            //noinspection ConstantConditions
            ScreenSwitcherFactory.viewScreenSwitcher(null, state, popHandler);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("viewGroup == null");
        }
    }

    @Test public void viewScreenSwitcherRejectsNullState() {
        try {
            //noinspection ConstantConditions
            ScreenSwitcherFactory.viewScreenSwitcher(view, null, popHandler);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("state == null");
        }
    }

    @Test public void viewScreenSwitcherRejectsStateWithNoScreens() {
        try {
            state.getScreens().remove(0);
            ScreenSwitcherFactory.viewScreenSwitcher(view, state, popHandler);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("state needs screens in order to initialize a ScreenSwitcher");
        }
    }

    @Test public void viewScreenSwitcherRejectsNullPopHandler() {
        try {
            //noinspection ConstantConditions
            ScreenSwitcherFactory.viewScreenSwitcher(view, state, null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("popHandler == null");
        }
    }

    @Test public void viewScreenSwitcherIsCreated() {
        ScreenSwitcher screenSwitcher = ScreenSwitcherFactory.viewScreenSwitcher(view, state, popHandler);
        assertThat(screenSwitcher).isNotNull();
    }
}
