(ns tunnit.mock-data)

(defn project-map []
  {
   111 {:id 111
        :name "Projekti 1"
        :entries {
                  1 {:hours 4.5}
                  2 {:hours 7.5}
                  7 {:hours 2.5}
                  }
        }
   222 {:id 222
        :name "Projekti 2"
        :entries {
                  1 {:hours 3.5}
                  2 {:hours 0}
                  3 {:hours 7.5}
                  7 {:hours 1.5}
                  }
        }
   333 {:id 333
        :name "Projekti 3"
        :entries {
                  1 {:hours 3.5}
                  2 {:hours 0}
                  3 {:hours 7.5}
                  4 {:hours 7.5}
                  5 {:hours 7.5}
                  6 {:hours 7.5}
                  7 {:hours 5.5}
                  }
        }
   })

