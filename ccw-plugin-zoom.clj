(ns ccw-plugin-zoom
  "This Counterclockwise plugin installs keybindings to Eclipse to facilitate
   zoom/unzoom of editor/repl fonts.
   Use 'Alt+U Z' for increasing the fonts size (Zooming)
   Use 'Alt+U U' for decreasing the fonts size (Unzooming)"
  (:require
    [ccw.e4.dsl :refer :all]
    [ccw.eclipse :as e]
    [ccw.leiningen.launch :as l]
    [ccw.e4.model :as m]
    [clojure.string :as str]))

(def font-increment-step
  "Select the increment step by which the font size must be increased or decreased"
  2)

(defn update-font
  "Fonts are serialized as:
   1|.Helvetica Neue DeskInterface|12.0|0|COCOA|1|.HelveticaNeueDeskInterface-Regular;
   Given a serialized font s, then apply function f to the part at index idx
   (starting at 0).
   Example: (update-font
              \"1|.Helvetica Neue DeskInterface|12.0|0|COCOA|1|.HelveticaNeueDeskInterface-Regular;\"
              2
              (constantly \"16.0\"))
            would return \"1|.Helvetica Neue DeskInterface|16.0|0|COCOA|1|.HelveticaNeueDeskInterface-Regular;\""
  [s idx f]
  (let [parts (vec (str/split s #"\|"))
        new-parts (update-in parts [idx] f)]
    (str/join "|" new-parts)))

(defn update-font-size
  "Updates the font size by adding increment to it"
  [s increment]
  (update-font s 2
    (comp #(+ % increment) #(java.lang.Double/parseDouble %))))

(def font-prefs-keys
  "Eclipse font preferences keys we want to increment/decrement altogether"
  ["org.eclipse.jface.textfont"
   "org.eclipse.debug.ui.VariableTextFont"
   "org.eclipse.jface.dialogfont"
   "org.eclipse.ui.workbench.TAB_TEXT_FONT"
   "org.eclipse.ui.workbench.VIEW_MESSAGE_TEXT_FONT"
   "org.eclipse.ui.workbench.texteditor.blockSelectionModeFont"])

(defn zoomer
  "Adds increment to font sizes"
  [increment context]
  (doseq [pref font-prefs-keys]
    (let [before (e/preference "org.eclipse.ui.workbench" pref nil)
          after (update-font-size before increment)]
      (e/preference! "org.eclipse.ui.workbench" pref after))))

(defn zoom-in-hdl [context] (zoomer font-increment-step context))
(defn zoom-out-hdl [context] (zoomer (- font-increment-step) context))

(defcommand zoom-in "Zoom: increase the fonts size (editors, repls, etc.)")
(defhandler zoom-in zoom-in-hdl)
(defkeybinding zoom-in "Alt+U Z")

(defcommand zoom-out "Unzoom: decrease the fonts size (editors, repls, etc.)")
(defhandler zoom-out zoom-out-hdl)
(defkeybinding zoom-out "Alt+U U")


