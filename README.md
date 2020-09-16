Screen Switcher
=========

A small and configurable library that focuses on screen navigation.

The following decisions and features make managing the backstack trivial:

- Transition animations are performed on views
- State is designed to survive configuration changes
- State is kept separate from the transitions
- Overriding back button pressed behavior at the screen level
- Only one transition can occur at a time
- Navigation between screens happens in a type safe way, ensuring all required dependencies and data are passed to every screen
- Design favors composition, and global callbacks allow custom behavior across an app

Screen Switcher encourages the use of the following, however, none of the following are required:

- Single Activity
- Dependency Injection
- Proper handling of configuration changes

Usage
------------

Use the `ScreenSwitcher` to perform operations such as `#push(Screen)`, `#pop(numberOfScreensToPop)`.

Bootstrap
------------

There is a fair amount of work to get ScreenSwitcher integrated in your app. 
Please clone the sample for a full example.

Dialogs
-------

In order to ensure dialogs are restored properly, there is an example `DialogManager`. The `DialogManager` is a pattern that makes it possible to save/restore dialogs without leaking a context.
You can see the code in the `dialog-manager` module. You can see example usages in the `screen-switcher-sample` module.

Setup
------------
```groovy
dependencies {
    implementation 'com.jaynewstrom:screen-switcher:1.0.0'
}
```

Alternatives
------------
- [Fragments](http://developer.android.com/guide/components/fragments.html)
- [Flow](https://github.com/square/flow)
- [Paginize](https://github.com/neevek/Paginize)
- [Pancakes](https://github.com/mattlogan/Pancakes)
- [Pilot](https://github.com/doridori/Pilot)
- [Scoop](https://github.com/lyft/scoop)
- [SimpleFragment](https://github.com/evant/simplefragment)
- [StackLayout](https://github.com/konmik/StackLayout)

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
