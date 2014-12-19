(ns tunnit.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-time.core :as time]
            [cljs-time.format :as format]
            [om-tools.dom :as ot :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            ))

(enable-console-print!)

(def app-state (atom {:text "Viikko" :date ""}))

(def week-map
  {:1 {:name "Ma"}
   :2 {:name "Ti"}
   :3 {:name "Ke"}
   :4 {:name "To"}
   :5 {:name "Pe"}
   :6 {:name "La"}
   :7 {:name "Su"}})

(def date-format
  (format/formatter "dd.MM.yyyy"))

(defn today []
  (format/unparse date-format (time/now)))

(defn week-dates []
  (assoc-in week-map [time/day-of-week :name] "Tänään")
  )

(defcomponent week-view [app owner]
              (render [this]
                      (om/update! app :date (today))

                      (ot/div
                        (map (fn [day]
                               (ot/output {:class "day-label"} (str (:name day))))
                               (vals week-map))
                        ))
              )

(defcomponent app-view [app owner]
              (render [this]
                      (print (format/show-formatters))
                      (ot/div {:class "column-main"}
                              (dom/h1 nil (str (:text app) " " (:date app)))
                              (om/build week-view app)
                              )
                      ))

(defn main []
  (om/root
    app-view
    app-state
    {:target (. js/document (getElementById "app"))}))
