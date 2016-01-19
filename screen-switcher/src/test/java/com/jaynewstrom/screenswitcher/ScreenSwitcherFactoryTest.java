package com.jaynewstrom.screenswitcher;

import android.app.Activity;

import org.junit.Test;

import java.util.Collections;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

public final class ScreenSwitcherFactoryTest {

    @Test public void activityScreenSwitcherRejectsNullActivity() {
        try {
            //noinspection ConstantConditions
            ScreenSwitcherFactory.activityScreenSwitcher(null, new ScreenSwitcherState(Collections.singletonList(mock(Screen.class))));
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("activity == null");
        }
    }

    @Test public void activityScreenSwitcherRejectsNullState() {
        try {
            //noinspection ConstantConditions
            ScreenSwitcherFactory.activityScreenSwitcher(mock(Activity.class), null);
            fail();
        } catch (NullPointerException expected) {
            assertThat(expected).hasMessage("state == null");
        }
    }

    @Test public void activityScreenSwitcherRejectsStateWithNoScreens() {
        try {
            ScreenSwitcherState state = new ScreenSwitcherState(Collections.singletonList(mock(Screen.class)));
            state.getScreens().remove(0);
            ScreenSwitcherFactory.activityScreenSwitcher(mock(Activity.class), state);
            fail();
        } catch (IllegalArgumentException expected) {
            assertThat(expected).hasMessage("state needs screens in order to initialize a ScreenSwitcher");
        }
    }
}
