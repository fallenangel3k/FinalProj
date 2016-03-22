(ns cljwallet.core
  (:require [clojure.data.json :as json])
  (:require [org.httpkit.client :as http]))

(defn -main [& args]
  (def addressInfo @(http/get "https://api.blockcypher.com/v1/btc/main/addrs/1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2"))
  (let [{a :body} addressInfo]
    (print a)))
