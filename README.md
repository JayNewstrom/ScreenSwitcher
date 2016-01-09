Screen Switcher
=========

Screen Switcher is still a major work in progress.

Here is the start of the TODO list:
- ViewScreenSwitcher
- ScreenPopListener (state held in ScreenSwitcher State)
- more screen switcher methods
	- void replaceCurrentScreenWith(Screen screen);
    - void replaceCurrentScreenWith(List<Screen> screens);
    - void replaceTo(Screen screen);
- Thoughts on how to do split screen
- ScreenAnimationConfiguration needs javadoc on viewToAnimate.getParent won't be null, so that you can do animations, etc.
- Screen could use a method createScreen, for being symmetric with destroyScreen
- Ideas on how to handle dialogs
- Global onTransition method so the client can be notified when transitions occur

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

