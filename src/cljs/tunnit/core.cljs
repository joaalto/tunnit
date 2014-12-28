(ns tunnit.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-time.core :as t]
            [cljs-time.format :as f]
            [om-tools.dom :as ot :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [clojure.string :as str]
            ))

(enable-console-print!)

(defn week-nr []
  (subs (get (str/split
               (f/unparse (f/formatters :week-date) (t/now))
               #"-")
             1) 1))

(def app-state (atom {:text (str "Viikko " (week-nr)) :date ""}))

(def day-map
  {1 {:name "Ma" :date ""}
   2 {:name "Ti" :date ""}
   3 {:name "Ke" :date ""}
   4 {:name "To" :date ""}
   5 {:name "Pe" :date ""}
   6 {:name "La" :date ""}
   7 {:name "Su" :date ""}
   })

(def date-format
  (f/formatter "dd.MM."))

(defn unparse-date [date]
  (f/unparse date-format date))

(defn week-day-nr [date]
  (t/day-of-week date))

(defn map-kv
  "Given a map and a function of two arguments, returns the map
  resulting from applying the function to each of its entries. The
  provided function must return a pair (a two-element sequence.)"
  [m f]
  (into {} (map (fn [[k v]] (f k v)) m)))

(defn count-date [k v]
  (let [today-nr (week-day-nr (t/now))
        date (t/minus (t/now)
                      (t/days (- today-nr k)))]
    [k (assoc v :date (unparse-date date))]
    ))

(defn week-dates []
  (map-kv day-map count-date))

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
                      (print (f/show-formatters))
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

