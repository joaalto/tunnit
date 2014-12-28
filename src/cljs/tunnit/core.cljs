(ns tunnit.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-time.core :as t]
            [cljs-time.format :as format]
            [om-tools.dom :as ot :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [clojure.string :as str]
            ))

(enable-console-print!)

(defn week []
  (subs
    (get
      (str/split
        (format/unparse (format/formatters :week-date) (t/now))
        #"-")
      1) 1))

(def app-state (atom {:text (str "Viikko " (week)) :date ""}))

(def day-map
  {1 {:name "Ma" :date ""}
   2 {:name "Ti" :date ""}
   3 {:name "Ke" :date ""}
   4 {:name "To" :date ""}
   5 {:name "Pe" :date ""}
   6 {:name "La" :date ""}
   7 {:name "Su" :date ""}})

(def date-format
  (format/formatter "dd.MM."))

(defn update-vals [m f & args]
  (reduce
    (fn [r [k v]] (assoc r k (apply f v args)))
    {} m))

(defn today []
  (format/unparse date-format (t/now)))

(defn week-day [date]
  (t/day-of-week date))

(defn week-dates []
  (assoc-in day-map [(week-day (t/now))
                      :date] (today)))

(defcomponent week-view [app owner]
              (render [this]
                      (ot/div
                        (map (fn [day]
                               (ot/output {:class "day-label"}
                                          (str (:name day) " " (:date day))))
                              (vals (week-dates)))
                        )))

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

