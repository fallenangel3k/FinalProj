(ns cljwallet.core
  (:require [clojure.data.json :as json]
            [org.httpkit.client :as http]
            [org.httpkit.server :as server])
  (:import  (org.bitcoinj.params MainNetParams)
            (org.bitcoinj.core Address AbstractWalletEventListener)
            (org.bitcoinj.kits WalletAppKit)
            (java.io File)))

;(defn -main [& args]
;  (def addressInfo @(http/get "https://api.blockcypher.com/v1/btc/main/addrs/1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2"))
;  (let [{a :body} addressInfo]
;    (print a)))

;(defn -main [& args]
;  (def params (. MainNetParams get))
;  (def filePrefix "forwarding-service")
;;  (def forwardingAddress (new Address params "1BvBMSEYstWetqTFn5Au4m4GFg7xJaNVN2"))
;  (def kit (new WalletAppKit params (new File ".") filePrefix))
;  (. kit startAsync)
;  (. kit awaitRunning)
;  (def wallet (. kit wallet))
;  (println (. (. wallet getBalance) getValue))
;  
;  (def listener (proxy [org.bitcoinj.core.AbstractWalletEventListener] []
;                  (onCoinsReveived [w tx prevBal newBal]
;                    (println newBal))))
;  (. wallet addEventListener listener)
;  (read-line))

(defn -main [& args]
  (defn app [req]
    {:status 200
     :header {"Content-Type" "text/html"}
     :body "Hello HTTP!"})
  (server/run-server app {:port 8080}))
