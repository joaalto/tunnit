(ns tunnit.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-time.core :as time]
            [om-tools.dom :as ot :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            ))

(enable-console-print!)

(defonce app-state (atom {:text "Viikko"}))

(def week-map
  {:mon [] :tue [] :wed [] :thu [] :fri [] :sat [] :sun []})

(defcomponent week-view [app owner]
              (render [this]
                      (ot/div
                        (map (fn [day]
                               (ot/output (str day)))
                             (keys week-map))
                        ))
              )

(defcomponent app-view [app owner]
              (render [this]
                      (ot/div {:class "column-main"}
                              (dom/h1 nil (:text app))
                              (om/build week-view app)
                              )
                      ))

(defn main []
  (om/root
    app-view
    app-state
    {:target (. js/document (getElementById "app"))}))