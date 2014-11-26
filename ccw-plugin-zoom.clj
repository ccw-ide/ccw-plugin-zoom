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
  2.0)

(def font-prefs-keys
  "Eclipse font preferences keys we want to increment/decrement altogether"
  [
   "org.eclipse.jface.textfont"
   "org.eclipse.debug.ui.VariableTextFont"
   "org.eclipse.jface.dialogfont"
   ; "org.eclipse.ui.workbench.TAB_TEXT_FONT"
   ; "org.eclipse.ui.workbench.VIEW_MESSAGE_TEXT_FONT"
   "org.eclipse.ui.workbench.texteditor.blockSelectionModeFont"
  ])

(defn zoomer
  "Adds increment to font sizes"
  [increment context]
  (let [font-registry (-> context
                        (m/context-key org.eclipse.ui.IWorkbench)
                        .getThemeManager
                        .getCurrentTheme
                        .getFontRegistry)]
  (doseq [pref-key font-prefs-keys]
    (when-let [font-data (first (.getFontData font-registry pref-key))] ;; FIXME what if font-data count > 1 ? What does that mean?
      (let [new-font-data (org.eclipse.swt.graphics.FontData. (str font-data))
            fds (str (doto new-font-data
                       (.setHeight (float (+ increment (.getHeight new-font-data)))))
                  ";")] ;; ";" part of the encoding
        (e/preference! "org.eclipse.ui.workbench" pref-key fds))))))

(defn zoom-in-hdl [context] (zoomer font-increment-step context))
(defn zoom-out-hdl [context] (zoomer (- font-increment-step) context))

(defcommand zoom-in "Zoom: increase the fonts size (editors, repls, etc.)")
(defhandler zoom-in zoom-in-hdl)
(defkeybinding zoom-in "Alt+U Z")

(defcommand zoom-out "Unzoom: decrease the fonts size (editors, repls, etc.)")
(defhandler zoom-out zoom-out-hdl)
(defkeybinding zoom-out "Alt+U U")
