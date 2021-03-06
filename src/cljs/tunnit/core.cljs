(ns tunnit.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs-time.core :as t]
            [cljs-time.format :as f]
            [om-tools.dom :as ot :include-macros true]
            [om-tools.core :refer-macros [defcomponent]]
            [clojure.string :as str]
            [tunnit.mock-data :as data]
            ))

(enable-console-print!)

(defn week-nr []
  (subs (get (str/split
               (f/unparse (f/formatters :week-date) (t/now))
               #"-")
             1) 1))

(def day-map
  {1 {:name "Ma" :date nil}
   2 {:name "Ti" :date ""}
   3 {:name "Ke" :date ""}
   4 {:name "To" :date ""}
   5 {:name "Pe" :date ""}
   6 {:name "La" :date ""}
   7 {:name "Su" :date ""}
   })

(defonce app-state (atom {
                          :text (str "Viikko " (week-nr))
                          :date ""
                          :projects (data/project-map)}))

(def date-format (f/formatter "dd.MM."))

(defn unparse-date [date] (f/unparse date-format date))

(defn week-day-nr [date] (t/day-of-week date))

(defn map-kv [m f]
  (into {} (map (fn [[k v]] (f k v)) m)))

(defn count-date [k v]
  (let [today-nr (week-day-nr (t/now))
        date (t/minus (t/now)
                      (t/days (- today-nr k)))]
    [k (assoc v :date date)]))

(defn week-dates []
  (map-kv day-map count-date))

(defn handle-change [owner e project day]
  (print (str "value: " (.. e -target -value)
              ", project: " (:name @project)
              ", day: " day))

  (om/set-state! owner; app-state
                 [:projects (:id @project) :entries day]
                 (.. e -target -value))
  (print (om/get-state owner))
  )

(defcomponent project-entries [app owner]
  (render [this]
    ;(print (om/get-state owner))
    (ot/div
      (map (fn [project]
             (ot/div {:id "project-row" :class "project-row"}
                     (ot/input {
                                :class "project-name"
                                :type "text"
                                :value (:name project)})

                     (for [day (range 1 8)]
                       (let [hours
                             (get-in project [:entries day :hours])]
                         (ot/input {
                                    :class "hour-entry"
                                    :value hours
                                    :on-change #(handle-change owner % project day)}
                                   )))))
           (vals (:projects app))))))

(defcomponent week-view [app]
  (render [this]
    (ot/div {:class "week-row"}
            (ot/div {:class "day-row"}
                    (map (fn [[key day]]
                           (ot/div {:class "day-col"}
                                   (ot/output {:class "day-label"}
                                              (str (:name day) " " (unparse-date (:date day))))))
                         (week-dates))))))

(defcomponent app-view [app owner]
  (render [this]
    ;(print (f/show-formatters))
    (ot/div {:class "column-main"}
            (dom/h1 nil (str (:text app) " " (:date app)))
            (om/build week-view app)
            (om/build project-entries app))))

(defn main []
  (om/root
    app-view
    app-state
    {:target (. js/document (getElementById "app"))}))

