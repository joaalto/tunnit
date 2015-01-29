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

(defn new-project [id name]
  {:id id :name name
   :entries {
             1 {:hours 0}
             2 {:hours 0}
             3 {:hours 0}
             4 {:hours 0}
             5 {:hours 0}
             6 {:hours 0}
             7 {:hours 0}
             }
   })

(def day-map
  {1 {:name "Ma" :date nil}
   2 {:name "Ti" :date ""}
   3 {:name "Ke" :date ""}
   4 {:name "To" :date ""}
   5 {:name "Pe" :date ""}
   6 {:name "La" :date ""}
   7 {:name "Su" :date ""}
   })

(def project-seq
  [{:id 111
    :name "Projekti 1"
    :entries {
              1 {:hours 4.5}
              2 {:hours 7.5}
              }
    }
   {:id 222
    :name "Projekti 2"
    :entries {
              1 {:hours 3.5}
              2 {:hours 0}
              3 {:hours 7.5}
              }
    }
   {:id 333
    :name "Projekti 3"
    :entries {
              1 {:hours 3.5}
              2 {:hours 0}
              3 {:hours 7.5}
              }
    }
   ])

(def app-state (atom {
                      :text (str "Viikko " (week-nr))
                      :date ""
                      :projects project-seq}))

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

(defcomponent project-row [app owner m]
              (render [this]
                      (print (str "day: " (:day m)))
                      (print (str "proj: " (get (first (:projects app)) :id)))
                      ;(print (str "proj: " (:projects app)))
                      (.log js/console app)
                      (ot/div {:class "hour-input"} nil
                              (ot/input {:class "hour-entry" :value 7.5})
                              )))

(defcomponent week-view [app]
              (render [this]
                      (ot/div {:class "week-row"}
                              (map (fn [[key day]]
                                     (ot/div {:class "day-col"}
                                             (ot/output {:class "day-label"}
                                                        (str (:name day) " " (unparse-date (:date day))))

                                             (om/build-all project-row (:projects app) {:opts {:day key}})
                                             ))
                                   (week-dates))
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

