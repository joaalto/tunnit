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
  {1 {:name "Ma" :date ""}
   2 {:name "Ti" :date ""}
   3 {:name "Ke" :date ""}
   4 {:name "To" :date ""}
   5 {:name "Pe" :date ""}
   6 {:name "La" :date ""}
   7 {:name "Su" :date ""}})

(def date-format
  (format/formatter "dd.MM.yyyy"))

(defn today []
  (format/unparse date-format (time/now)))

(defn week-day [date]
  (time/day-of-week date))

(def week-dates ;[]
  (assoc-in week-map [(week-day (time/now))
                      :date] (today)))

(defcomponent week-view [app owner]
              (render [this]
                      (ot/div
                        (map (fn [day]
                               (ot/output {:class "day-label"}
                                          (str (:name day) " " (:date day))))
                              (vals week-dates))
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

