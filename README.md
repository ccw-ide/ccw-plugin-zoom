# ccw-plugin-zoom

This Counterclockwise plugin installs keybindings to Eclipse to facilitate zoom/unzoom of editor/repl fonts.

This plugin's state is stable.

## Install

The `~/.ccw/` folder is where Counterclockwise searches for User Plugins.

It is recommended to layout User Plugins inside this folder by mirroring Github's namespacing. So if you clone laurentpetit/ccw-plugin-zoom, you should do the following:

- Create a folder named `~/.ccw/laurentpetit/`
- Clone this project from `~/.ccw/laurentpetit/`

        mkdir -p ~/.ccw/laurentpetit
        cd ~/.ccw/laurentpetit
        git clone https://github.com/laurentpetit/ccw-plugin-zoom.git

- If you have already installed ccw-plugin-manager (https://github.com/laurentpetit/ccw-plugin-manager.git), then type `Alt+U S` to re[S]tart User Plugins (and thus ccw-plugin-zoom will be found and loaded)
- If you have not already installed ccw-plugin-manager, restart your Eclipse / Counterclockwise/Standalone instance.

## Usage

- Use 'Cmd++' on OS X, 'Ctrl++' on Windows/Linux for increasing the fonts size (Zooming)
- Use 'Cmd+-' on OS X, 'Ctrl+-' on Windows/Linux for decreasing the fonts size (Unzooming)

## Uninstall

To uninstall a User plugin, simply remove its directory. At the next Eclipse/Counterclockwise restart, it'll be gone.

## License

Copyright © 2009-2015 Laurent Petit

Distributed under the Eclipse Public License, the same as Clojure.

