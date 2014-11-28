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
                       (.setHeight (float (+ increment (.getHeight font-data)))))
                  ";")] ;; ";" part of the encoding
        (e/preference! "org.eclipse.ui.workbench" pref-key fds))))))

;; NOTE concerning the handlers:
;; we do not use the default "org.eclipse.ui.context.window" keybinding context
;; because we would conflict with pre-existing keybindings of the Eclipse Platform.
;; Instead we define the keybinding for more specific contexts, which means
;; that in editors or repls, our keybinding will win and our zoom/unzoom handlers will
;; be triggered.
;;
;; ALSO, we overload the binding both for azerty and qwerty keyboards, that's why
;; zoom-in is both bound to "Ctrl++" (qwerty keyboards) and "Ctrl+=" (azerty keyboards)

(defcommand zoom-in "Zoom: increase the fonts size (editors, repls, etc.)")
(defhandler zoom-in (partial zoomer font-increment-step))
(defkeybinding zoom-in "Cmd++" :context :text-editor)
(defkeybinding zoom-in "Cmd++" :context :repl)
(defkeybinding zoom-in "Cmd+=" :context :text-editor)
(defkeybinding zoom-in "Cmd+=" :context :repl)


(defcommand zoom-out "Unzoom: decrease the fonts size (editors, repls, etc.)")
(defhandler zoom-out (partial zoomer (- font-increment-step)))
(defkeybinding zoom-out "Cmd+-" :context :text-editor)
(defkeybinding zoom-out "Cmd+-" :context :repl)

